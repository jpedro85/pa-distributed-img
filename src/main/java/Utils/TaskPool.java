package Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * A task pool that executes Runnable objects in separate threads.
 */
public class TaskPool {

    private final int waitingTime;

    private final VarSync<Boolean> isRunningSate;

    private final VarSync<Integer> tasksRunning;

    private final VarSync< ArrayList<TaskExecutor> > taskExecutors;

    private final VarSync< Queue<Runnable> > waitingTasks;
    private final Condition awaitTasks;

    /**
    * Initializes a task pool with the specified size. Also creates its executors in the paused state.
    *
    * @param size The size of the task pool (number of executors threads).
    */
    public TaskPool(int size,int waitingTime)
    {
        this.waitingTime = waitingTime;

        this.isRunningSate = new VarSync<Boolean>(false);

        this.tasksRunning = new VarSync<Integer>(0);

        this.taskExecutors = new VarSync<ArrayList<TaskExecutor>>( new ArrayList<TaskExecutor>(size) );

        this.waitingTasks = new VarSync<  Queue<Runnable> >( new LinkedList<Runnable>() ) ;
        this.awaitTasks = this.waitingTasks.getLock().newCondition();


        this.initializeTaskExecutors(size);
    }

    /**
     *
     * @param size The size of the task pool (number of executors threads).
     */
    public TaskPool(int size)
    {
        this(size,100);
    }

    /**
     * Initializes the executor threads.
     */
    private void initializeTaskExecutors(int numberOfExecutors)
    {
        for (int i = 0; i < numberOfExecutors ; i++)
        {
            this.taskExecutors.asyncGet().add( new TaskExecutor(this.waitingTime));
        }
    }

    /**
     *  Starts all the pool executor threads.
     */
    public void start()
    {
        this.isRunningSate.lock();

        if( !this.isRunningSate.asyncGet() )
        {
            this.taskExecutors.lock();
            for (int i = 0; i < this.taskExecutors.asyncGet().size() ; i++)
            {
                this.taskExecutors.asyncGet().get(i).start();
            }

            this.taskExecutors.unlock();
            this.isRunningSate.asyncSet(true);
        }

        this.isRunningSate.unlock();
    }

    /**
     *  Pauses all the pool executor threads. The executors will only pause after finished their current task.
     */
    public void pause()
    {
        this.isRunningSate.lock();

        if( this.isRunningSate.asyncGet() )
        {
            this.taskExecutors.lock();
            for (int i = 0; i < this.taskExecutors.asyncGet().size() ; i++)
            {
                this.taskExecutors.asyncGet().get(i).pause();
            }

            this.taskExecutors.unlock();
            this.isRunningSate.asyncSet(false);
        }

        this.isRunningSate.unlock();
    }

    /**
     * @return <b>True</b> if the internal state is <b>paused</b> and all executors are <b>not executing</b>.
     */
    public boolean isPaused(){
        return !isRunningSate.syncGet() && tasksRunning.syncGet() == 0 ;
    }

    /**
     * Adds a task to the pool only if <b>task</b> does <b>not</b> already exists in the poll
     * <p>
     * @param task , task to be executed in the pool;
     */
    public void addTask(Runnable task)
    {
        this.waitingTasks.lock();

        if ( !this.waitingTasks.asyncGet().contains(task) )
        {
            this.waitingTasks.asyncGet().add(task);
            this.awaitTasks.signal();
        }

        this.waitingTasks.unlock();
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

        this.waitingTasks.lock();

        if ( !executing && this.waitingTasks.asyncGet().contains(taskToRemove) )
            removed = this.waitingTasks.asyncGet().remove(taskToRemove);
        else
            removed = false;

        this.waitingTasks.unlock();

        return removed;
    }

    /**
     * Check if a task is currently running in a pool
     *
     * @param task , to check if it is running;
     * @return <b>True</b> if the task is currently running.
     */
    public boolean isTaskRunning(Runnable task){

        this.taskExecutors.lock();
        for( int iTaskExecutor = 0; iTaskExecutor < this.taskExecutors.asyncGet().size(); iTaskExecutor++)
        {
            Runnable currentTask = this.taskExecutors.asyncGet().get(iTaskExecutor).getCurrentTask();
            if( currentTask != null && currentTask.equals( task ))
            {
                this.taskExecutors.unlock();
                return true;
            }

        }
        this.taskExecutors.unlock();
        return false;
    }


    /**
     * Adds n new Executor to the task pool. if the curren state is running also starts the executor.
     * <p>
     * @param numberOfExecutors number of executors to add
     */
    public void addExecutors(int numberOfExecutors)
    {
        this.isRunningSate.lock();
        this.taskExecutors.lock();

        if( this.isRunningSate.asyncGet() )
        {
            TaskExecutor executor;
            for (int i = 0; i < numberOfExecutors; i++)
            {
                executor = new TaskExecutor(this.waitingTime);
                this.taskExecutors.asyncGet().add( executor );
                executor.start();
            }
        }
        else
        {
            for (int i = 0; i < numberOfExecutors; i++)
            {
                this.taskExecutors.asyncGet().add( new TaskExecutor(this.waitingTime));
            }
        }

        this.taskExecutors.unlock();
        this.isRunningSate.unlock();
    }

    /**
     * Removes n Executors. The removed executors will finish their current task.
     * <p>
     * @param numberOfExecutors number of executors to remove. The minimum number oof executors is 1;
     * @return <b>true</b> if successfully removed
     */
    public boolean removeExecutors(int numberOfExecutors)
    {

        this.taskExecutors.lock();

        if ( this.taskExecutors.asyncGet().size() - numberOfExecutors == 0 )
        {
            this.taskExecutors.unlock();
            return false;
        }

        TaskExecutor executer;
        for (int i = 0; i < numberOfExecutors; i++)
        {
            executer = this.taskExecutors.asyncGet().remove( this.taskExecutors.asyncGet().size() - 1);
            executer.pause();
        }

        this.taskExecutors.unlock();
        return true;
    }

    /**
     * @return the <b>size</b> of the pool.
     */
    public int getSize(){
        return this.taskExecutors.syncGet().size();
    }

    /**
     * @return the actual number of running tasks of the pool.
     */
    public int getNumberOfRunningTasks()
    {
        return this.tasksRunning.syncGet();
    }

    /**
     *
     * @return the number of tasks waiting to be executed
     */
    public int getNumberOfWaitingTasks()
    {
        return this.waitingTasks.syncGet().size();
    }






    /**
     * TaskExecutor class
     */
    private class TaskExecutor extends Thread{

        private Runnable currentTask;
        private final int waitingTime;
        private VarSync<Boolean> isPaused;

        /**
         * Initializes TaskExecutor
         * @param waitingTime limit time to wait for a task. Time to recheck if a task exists and a signal was missed
         */
        public TaskExecutor(int waitingTime)
        {
            super();
            isPaused = new VarSync<Boolean>(true);
            this.waitingTime = waitingTime;
        }

        @Override
        public void start()
        {
            this.isPaused.lock();
            if ( this.isPaused.asyncGet() )
            {
                this.isPaused.asyncSet(false);
                this.isPaused.unlock();

                super.start();
            } else
                this.isPaused.unlock();
        }

        /**
         * Set the TaskExecuter to pause execution, will wait until the current task is finished;
         */
        public void pause()
        {
            this.isPaused.lock();
            if( !this.isPaused.asyncGet())
            {
                this.isPaused.asyncSet(true);
            }
            this.isPaused.unlock();
        }

        @Override
        public void run()
        {
            while (this.isRunning())
            {
                this.currentTask = this.pickNextTask();

                if ( this.isPaused.syncGet() )
                    break;

                this.startTask();
                this.currentTask.run();
                this.endTask();
            }
        }

        /**
         * @return Return true if thread is not paused, not pauses includes waiting for tasks (blocked) and Executing
         */
        public boolean isRunning()
        {
            return !isPaused.syncGet();
        }

        /**
         * Pick a task.If TaskPoll is empty waits until for signal that a new task is available, periodically checks if a signal was missed.
         * if TaskPoll is paused stops waiting and <b>return null</b>.
         * @return Runnable the task to be executed
         */
        private Runnable pickNextTask()
        {
            Runnable task = null;
            try
            {
                waitingTasks.lock();

                while( waitingTasks.asyncGet().isEmpty() && this.isRunning())
                {
                    awaitTasks.await( this.waitingTime,TimeUnit.MILLISECONDS );
                }

                if (!waitingTasks.asyncGet().isEmpty()){
                    task = waitingTasks.asyncGet().poll();
                }

                waitingTasks.unlock();

            }
            catch (InterruptedException e)
            {
                System.out.println("A Thread has been Interrupted while waiting for tasks!");
                e.printStackTrace();
            }

            return task;
        }

        private void startTask()
        {
            tasksRunning.lock();
            tasksRunning.asyncSet(tasksRunning.asyncGet() + 1);
            System.out.println(tasksRunning.asyncGet());
            tasksRunning.unlock();
        }

        private void endTask()
        {
            tasksRunning.lock();
            tasksRunning.asyncSet(tasksRunning.asyncGet() - 1);
            tasksRunning.unlock();

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
