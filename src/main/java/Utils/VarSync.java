package Utils;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Generic Class that merges a var and a lock. Util when we need to sync a single variable.
 * @param <T> Type of the var
 */
public class VarSync<T>{

    private T value;
    private final ReentrantLock lock;

    /**
     * Creates an instance of VarSync
     * <p>
     * @param value variable to sync
     * @param fair if the lock is fair, that means the lock respect the order of lock calls.
     */
    public VarSync(T value, boolean fair ){
        this.value = value;
        this.lock = new ReentrantLock(fair);
    }

    /**
     * Creates an instance of VarSync with the default value of fair as true
     * <p>
     * @param value value to sync
     */
    public VarSync(T value){
        this.value = value;
        this.lock = new ReentrantLock(true);
    }

    /**
     * Locks the lock
     */
    public void lock(){
        this.lock.lock();
    }

    /**
     * unlocks the lock
     */
    public void unlock(){
        this.lock.unlock();
    }

    /**
     * Locks and get de value
     * @return actual value of the stored var
     */
    public T syncGet(){
        T var;
        this.lock.lock();
        var = this.value;
        this.lock.unlock();
        return var;
    }

    /**
     * Lock and set the value to newValue
     * @param newValue value to replace the current value.
     */
    public void syncSet(T newValue){
        this.lock.lock();
        this.value = newValue;
        this.lock.unlock();
    }

    /**
     * @return stored value without locking. Util in a multi statement context.
     */
    public T asyncGet(){
        return this.value;
    }

    /**
     * Replaces value with newValue without locking. Util in a multi statement context.
     * @param newValue value to replace the current value.
     */
    public void asyncSet( T newValue){
        this.value = newValue;
    }

    /**
     * @return Returns the lock.
     */
    public ReentrantLock getLock(){
        return this.lock;
    }

}
