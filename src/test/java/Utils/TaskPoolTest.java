package Utils;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for TaskPool")
public class TaskPoolTest {

    TaskPool myTaskPool;

    @BeforeEach
    void beforeEachTest(){
        this.myTaskPool = new TaskPool(2);
    }

    @Nested
    class Task implements Runnable{

        public int[] array;
        public String name;

        Task(int size,String name){
            this.name = name;
            array = new int[size];
        }

        @Override
        public void run(){

            for (int i = 0; i < array.length; i++) {
                array[i] = i*2;
                System.out.println(name + " " + array[i]);
            }

            try {

                Thread.sleep(1000);

            }catch (InterruptedException e){
                e.printStackTrace();
            }

        }

    }

    @Test
    @DisplayName("TaskPool Creation")
    void testCreationTest(){

        // Create a TaskPool object with 2 threads
        TaskPool myTaskPool = new TaskPool(2);
        // Verify that the TaskPool object is not null
        assertNotNull(myTaskPool);
        // Verify that the size of the TaskPool is set correctly
        assertEquals(2, myTaskPool.getSize());
    }

    @Test
    @DisplayName("TaskPool AddTask")
    void testAddTask(){

        TaskPool myTaskPool = new TaskPool(2);

        myTaskPool.addTask(new Task(3,"t1"));
        assertEquals(myTaskPool.getNumberOfWaitingTasks(),1);

        myTaskPool.addTask(new Task(4,"t2"));
        assertEquals(myTaskPool.getNumberOfWaitingTasks(),2);
    }

    @Test
    @DisplayName("TaskPool RemoveTask NotRunningContext")
    void removeTaskNotRunningContext(){

        TaskPool myTaskPool = new TaskPool(2);

        Task t1 = new Task(3, "t1");
        myTaskPool.addTask(t1);
        Task t2 = new Task(3, "t2");
        myTaskPool.addTask(t2);
        myTaskPool.addTask(new Task(4, "t3"));

        int waitingTasks = myTaskPool.getNumberOfWaitingTasks();
        assertTrue(myTaskPool.removeTask(t1));
        assertEquals(myTaskPool.getNumberOfWaitingTasks(),waitingTasks-1);

    }

    @Test
    @DisplayName("TaskPool RemoveTask InRunningContext")
    void removeTaskInRunningContext(){

        TaskPool myTaskPool = new TaskPool(2);

        Task t1 = new Task(3, "t1");
        myTaskPool.addTask(t1);
        Task t2 = new Task(3, "t2");
        myTaskPool.addTask(t2);
        myTaskPool.addTask(new Task(4, "t3"));

        myTaskPool.start();
        // wait for start
        try { Thread.sleep(500); }catch (Exception e) { e.printStackTrace();}

        // t1 and t2 are running so can not be removed
        assertFalse( myTaskPool.removeTask(t2) );
        // execution time > 1 s so  waitingTasks need to be 1
        assertEquals(1, myTaskPool.getNumberOfWaitingTasks());

        try { Thread.sleep(2000); }catch (Exception e) { e.printStackTrace();}

        // t1 and t2 are running so can not be removed
        assertFalse( myTaskPool.removeTask(t2) );
        assertEquals(0,myTaskPool.getNumberOfWaitingTasks());

    }

    @Test
    @DisplayName("TaskPool awake after wait for tasks test")
    void awakeAfterWaitForTasksTest(){

        TaskPool myTaskPool = new TaskPool(2);
        myTaskPool.start();

        //wait for threads to sleep
        try { Thread.sleep(100); }catch (Exception e) { e.printStackTrace();}
        //awake one threads by adding one task
        myTaskPool.addTask(new Task(4, "t3"));
        //wait for threads to sleep
        try { Thread.sleep(100); }catch (Exception e) { e.printStackTrace();}
        //awake one threads by adding one task
        myTaskPool.addTask(new Task(4, "t4"));

        myTaskPool.pause();

    }

    @Test
    @DisplayName("Test numberOFRunningTasks")
    void testGetNumberOfRunningTasks(){

        this.myTaskPool.start();

        this.myTaskPool.addTask( new Task(5,"T1") );
        try { Thread.sleep(500); }catch (Exception e) { e.printStackTrace();}
        this.myTaskPool.addTask( new Task(10,"T2") );
        try { Thread.sleep(100); }catch (Exception e) { e.printStackTrace();}

        assertEquals(2,this.myTaskPool.getNumberOfRunningTasks());
        //wait for one to finish
        try { Thread.sleep(500); }catch (Exception e) { e.printStackTrace();}

        assertEquals(1,this.myTaskPool.getNumberOfRunningTasks());

    }

    @Test
    @DisplayName( "Test numberOFRunningTasks" )
    void testGetNumberOfWaitingTasks(){

        this.myTaskPool.start();

        this.myTaskPool.addTask(new Task(25,"T1"));
        this.myTaskPool.addTask(new Task(10,"T2"));
        this.myTaskPool.addTask(new Task(20,"T3"));

        //wait for start executing threads
        try { Thread.sleep(10); }catch (Exception e) { e.printStackTrace();}

        assertEquals(1,this.myTaskPool.getNumberOfWaitingTasks());

    }

    @Test
    @DisplayName( "Test getSize" )
    void testGetSize(){

        // before each test start myTaskPool with 2
        this.myTaskPool.start();
        assertEquals(2,this.myTaskPool.getSize());
    }

    @Test
    @DisplayName( "Test Is TaskRunning" )
    void testIsTaskRunning(){

        // before each test start myTaskPool with 2
        this.myTaskPool.start();
        Task t1 = new Task(10,"T1");
        this.myTaskPool.addTask( t1 );
        try { Thread.sleep(10); }catch (Exception e) { e.printStackTrace();}
        assertTrue( this.myTaskPool.isTaskRunning(t1) );
    }

    @Test
    @DisplayName( "Test is addExecuter" )
    void addExecutorTest(){

        int nExecutors = this.myTaskPool.getSize();
        this.myTaskPool.addExecutors(1);
        assertEquals(nExecutors + 1, this.myTaskPool.getSize());

        this.myTaskPool.start();

        try { Thread.sleep(100); }catch (Exception e) { e.printStackTrace(); }

        nExecutors = this.myTaskPool.getSize() ;
        this.myTaskPool.addExecutors(1);

        assertEquals(nExecutors + 1, this.myTaskPool.getSize());

    }

    @Test
    @DisplayName( "Test is removeExecuter" )
    void removeExecutorTest(){

        int nExecutors = this.myTaskPool.getSize();
        assertTrue( this.myTaskPool.removeExecutors(1) );
        assertFalse( this.myTaskPool.removeExecutors(1) );

        this.myTaskPool.addExecutors(1);

        this.myTaskPool.start();
        assertTrue( this.myTaskPool.removeExecutors(1) );
        assertFalse( this.myTaskPool.removeExecutors(1) );

    }

    @Test
    @DisplayName( "Test is isPaused" )
    void isPausedTest(){

        this.myTaskPool.start();
        this.myTaskPool.addTask( new Task(6,"t1"));
        try { Thread.sleep(100); }catch (Exception e) { e.printStackTrace(); }
        assertEquals(1 , this.myTaskPool.getNumberOfRunningTasks());
        this.myTaskPool.pause();

        try { Thread.sleep(1100); }catch (Exception e) { e.printStackTrace(); }

        assertEquals(0 , this.myTaskPool.getNumberOfRunningTasks());
    }


}
