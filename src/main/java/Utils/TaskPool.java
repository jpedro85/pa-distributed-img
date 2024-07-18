package Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * Represents a task pool that manages the execution of Runnable objects in multiple threads.
 * <p>
 * The TaskPool class provides a flexible and efficient way to distribute tasks across a pool
 * of executor threads, allowing for parallel execution of tasks. It manages a queue of tasks,
 * executes them using a pool of executor threads, and provides methods for controlling the
 * execution flow, such as starting, pausing, adding, and removing tasks and executors.
 *
 * <p>Features:
 * <ul>
 *   <li>Manages a pool of executor threads to execute Runnable tasks concurrently.</li>
 *   <li>Supports dynamic addition and removal of executor threads based on workload.</li>
 *   <li>Allows pausing and resuming the execution of tasks in the pool.</li>
 *   <li>Ensures thread safety using synchronization and locks.</li>
 *   <li>Provides methods to add, remove, and query tasks in the pool.</li>
 * </ul>
 *
 * <p>
 * The TaskPool class is designed to be highly customizable and suitable for various
 * multithreading task execution scenarios.
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
     * @param size        The size of the task pool (number of executor threads).
     * @param waitingTime The time limit to wait for a task in milliseconds,without checking for new tasks.
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
     * Initializes a task pool with the specified size and default waiting time of 100 milliseconds.
     *
     * @param size The size of the task pool ( number of executor threads ).
     */
    public TaskPool(int size)
    {
        this(size,100);
    }

    /**
     * Initializes the executor threads.
     *
     * @param numberOfExecutors The number of executor threads to initialize.
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
     * Checks if the task pool is paused. The TaskPool is paused if his state is paused and all his executors are not executing.
     *
     * @return <b>True</b> if the internal state is <b>paused</b> and all executors are <b>not executing</b>.
     */
    public boolean isPaused(){
        return !isRunningSate.syncGet() && tasksRunning.syncGet() == 0 ;
    }

    /**
     * Adds a task to the pool only if <b>task</b> does <b>not</b> already exists in the poll
     *
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
     * @param task The to check if it is running;
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
     * @param numberOfExecutors The number of executors to add
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
     *
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
     * Gets the current size of the pool.
     *
     * @return The size of the pool.
     */
    public int getSize(){
        return this.taskExecutors.syncGet().size();
    }

    /**
     * Gets the actual number of running tasks in the pool.
     *
     * @return The number of running tasks in the pool.
     */
    public int getNumberOfRunningTasks()
    {
        return this.tasksRunning.syncGet();
    }

    /**
     * Gets the number of tasks waiting to be executed.
     *
     * @return The number of tasks waiting to be executed.
     */
    public int getNumberOfWaitingTasks()
    {
        return this.waitingTasks.syncGet().size();
    }




    /**
     * Represents a worker thread responsible for executing tasks in the TaskPool.
     *
     * <p>The TaskExecutor class encapsulates the behavior of individual worker threads
     * within the TaskPool. Each TaskExecutor instance runs in its own thread and
     * continuously picks up tasks from the TaskPool's task queue for execution.
     *
     * <p>Key Features:
     * <ul>
     *   <li>Runs in its own thread and executes tasks asynchronously.</li>
     *   <li>Picks tasks from the task queue and executes them sequentially.</li>
     *   <li>Supports pausing and resuming the execution of tasks based on pool state.</li>
     *   <li>Manages its own state, including whether it is currently running or paused.</li>
     *   <li>Ensures thread safety when accessing shared resources using synchronization.</li>
     * </ul>
     * <p>
     * The TaskExecutor class is an integral part of the TaskPool infrastructure,
     * providing the concurrency mechanism for executing tasks concurrently in a
     * controlled manner.
     */
    private class TaskExecutor extends Thread{

        private Runnable currentTask;
        private final int waitingTime;
        private VarSync<Boolean> isPaused;

        /**
         * Initializes a new TaskExecutor with the specified waiting time.
         *
         * @param waitingTime The time to wait for a task in milliseconds when
         *                    checking for new tasks.
         */
        public TaskExecutor(int waitingTime)
        {
            super();
            isPaused = new VarSync<Boolean>(true);
            this.waitingTime = waitingTime;
        }

        /**
         * Starts the TaskExecutor thread for executing tasks.
         * If the TaskExecutor is currently paused, it will resume execution.
         */
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
         * Pauses the execution of tasks by the TaskExecutor.
         *
         * <p>The TaskExecutor only pause after the current task is finished.
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
         * Picks the next task from the task queue for execution.
         *
         * <p>If no tasks are available and the TaskExecutor is running, it will
         * wait for a signal that a new task is available.
         * <p>At the interval of <b>waitingTime</b> will check is a new task is available.
         *
         * @return The next task to be executed, or null if no tasks are available.
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

        /**
         * Starts the execution of a task.
         *
         * <p>Updates the count of running tasks in the TaskPool.
         */
        private void startTask()
        {
            tasksRunning.lock();
            tasksRunning.asyncSet(tasksRunning.asyncGet() + 1);
            System.out.println(tasksRunning.asyncGet());
            tasksRunning.unlock();
        }

        /**
         * Ends the execution of a task.
         *
         * <p>Updates the count of running tasks in the TaskPool.
         */
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
