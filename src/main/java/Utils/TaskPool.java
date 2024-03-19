package Utils;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.Arrays;

/**
 * A task pool that executes Runnable objects in separate threads.
 */
public class TaskPool {

    private final int size;

    private int running;

    private final TaskExecutor[] taskExecutors;
    private final Queue<Runnable> waitingTasks;
    private final ReentrantLock waitingTasksLock;
    private final ReentrantLock runningLock;

    private final Condition awaitTasks;

    /**
    * Constructs a task pool with the specified size. Also creates its executors in the paused state.
    *
    * @param size The size of the task pool (number of executors threads).
    */
    public TaskPool(int size)
    {
        this.size = size;
        this.running = 0;

        this.taskExecutors = new TaskExecutor[this.size];

        this.waitingTasks = new LinkedList<Runnable>();
        this.waitingTasksLock = new ReentrantLock();
        this.awaitTasks = this.waitingTasksLock.newCondition();

        this.runningLock = new ReentrantLock();

        this.initializeTaskExecutors();
    }

    /**
     * Initializes the executor threads.
     */
    private void initializeTaskExecutors()
    {
        for (int i = 0; i < this.taskExecutors.length ; i++)
        {
            this.taskExecutors[i] = new TaskExecutor();
        }

    }

    /**
     *  Starts all the pool executor threads.
     */
    public void start()
    {
        for (int i = 0; i < this.taskExecutors.length ; i++)
        {
            this.taskExecutors[i].start();
        }
    }

    /**
     *  Pauses all the pool executor threads. The executor will only pause after finished the current task.
     */
    public void pause()
    {
        for (int i = 0; i < this.taskExecutors.length ; i++)
        {
            this.taskExecutors[i].pause();
        }
    }

    /**
     * Adds a task to the pool only if <b>task</b> does <b>not</b> already exists in the poll
     * <p>
     * @param task , task to be executed in the pool;
     */
    public void addTask(Runnable task) {
        this.waitingTasksLock.lock();

        if (!this.waitingTasks.contains(task))
        {
            this.waitingTasks.add(task);
            this.awaitTasks.signal();
        }

        this.waitingTasksLock.unlock();
    }

    /**
     * Removes a task from the pool if task is not being executed. A task that is executing will not be removed
     * <p>
     * @param taskToRemove , task to be removed executed in the pool;
     * @return <b>True</b> if the task was successfully removed.
     */
    public boolean removeTask(Runnable taskToRemove)
    {
        boolean executing = this.isTaskRunning(taskToRemove);
        boolean removed;

        this.waitingTasksLock.lock();

        if ( !executing && this.waitingTasks.contains(taskToRemove) )
            removed = this.waitingTasks.remove(taskToRemove);
        else
            removed = false;

        this.waitingTasksLock.unlock();

        return removed;
    }

    /**
     * Check if a task is currently running in a pool
     *
     * @param task , to check if it is running;
     * @return <b>True</b> if the task is currently running.
     */
    public boolean isTaskRunning(Runnable task){

        for( int iTask = 0; iTask < this.taskExecutors.length; iTask++)
        {
            Runnable currentTask = this.taskExecutors[iTask].getCurrentTask();
            if( currentTask != null && currentTask.equals( task ))
                return true;

        }
        return false;
    }

    /**
     * @return the <b>size</b> of the pool.
     */
    public int getSize(){
        return this.size;
    }

    /**
     * @return the actual number of running tasks of the pool.
     */
    public int getNumberOfRunningTasks()
    {
        int actualRunning;
        runningLock.lock();
        actualRunning = this.running;
        runningLock.unlock();

        return actualRunning;
    }

    /**
     *
     * @return the number of tasks waiting to be executed
     */
    public int getNumberOfWaitingTasks()
    {
        int tasks;
        this.waitingTasksLock.lock();
        tasks = waitingTasks.size();
        this.waitingTasksLock.unlock();
        return tasks;
    }

    private class TaskExecutor extends Thread{

        private Runnable currentTask;
        private boolean paused;
        private final ReentrantLock stateLock;

        public TaskExecutor()
        {
            super();
            stateLock = new ReentrantLock();
            paused = true;
        }

        @Override
        public void start()
        {
            stateLock.lock();
            if ( this.paused )
            {
                this.paused = false;
                stateLock.unlock();

                super.start();
            } else
                stateLock.unlock();
        }

        /**
         * Set the TaskExecuter to pause execution, will wait until the current task is finished;
         */
        public void pause()
        {
            stateLock.lock();
            if( !this.paused )
            {
                this.paused = true;
            }
            stateLock.unlock();
        }

        @Override
        public void run()
        {
            while (this.isRunning())
            {
                this.waitForTask();
                this.currentTask = this.pickNextTask();

                this.starTask();
                this.currentTask.run();
                this.endTask();
            }
        }

        /**
         * @return Return true if thread is not paused, not pauses includes waiting for tasks (blocked) and Executing
         */
        public boolean isRunning() {

            boolean running;
            stateLock.lock();
            running = !this.paused ;
            stateLock.unlock();
            return running;
        }

        private Runnable pickNextTask()
        {
            Runnable task;
            waitingTasksLock.lock();
            task = waitingTasks.poll();
            waitingTasksLock.unlock();

            return task;
        }

        private void waitForTask()
        {
            try
            {
                waitingTasksLock.lock();

                while( waitingTasks.isEmpty() )
                    awaitTasks.await();

                waitingTasksLock.unlock();
            }
            catch (InterruptedException e)
            {
                System.out.println("A Thread has been Interrupted while waiting for tasks!");
                e.printStackTrace();
            }
        }

        private void starTask()
        {
            runningLock.lock();
            running++;
            runningLock.unlock();
        }

        private void endTask()
        {
            runningLock.lock();
            running--;
            runningLock.unlock();

            this.currentTask = null;
        }

        /**
         * @return the current tasks, null if there is no current task
         */
        public Runnable getCurrentTask(){
            return this.currentTask;
        }

    }
}
