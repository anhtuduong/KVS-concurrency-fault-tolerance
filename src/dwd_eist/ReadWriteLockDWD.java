package dwd_eist;

import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * The following class is a wrapper class for ReentrantReadWriteLock.
 * It also has a CountDownLatch object. We use the class to better test the locking mechanism in KVStore.
 */

public class ReadWriteLockDWD {
    private ReentrantReadWriteLock rwLock;
    private boolean debug = false;


    // Constructor used in tests.
    public ReadWriteLockDWD(boolean debug) {
        this.rwLock = new ReentrantReadWriteLock();
        this.debug = debug;
    }


    // Standard constructor.
    public ReadWriteLockDWD() {
        this.rwLock = new ReentrantReadWriteLock();
    }


    public void lockWriteLock() {
        rwLock.writeLock().lock();
        if(this.debug) {
        	System.out.println("W L "+Thread.currentThread().getId());
        }
    }

    public void unlockWriteLock() {
        if(this.debug) {
        	System.out.println("W U "+Thread.currentThread().getId());
        }
        rwLock.writeLock().unlock();
    }

    public void lockReadLock() {
        rwLock.readLock().lock();
        if(this.debug) {
        	System.out.println("R L "+Thread.currentThread().getId());
        }
    }

    public void unlockReadLock() {
        if(this.debug) {
        	System.out.println("R U "+Thread.currentThread().getId());
        }
        rwLock.readLock().unlock();
    }
    
    public boolean isWriteLocked() {
        return rwLock.isWriteLocked();
    }

    public int getReadLockCount() {
        return rwLock.getReadLockCount();
    }

}
