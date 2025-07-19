package dwd_eist;

import java.util.Optional;

public class KVStore {
    private Tree memory;

    public ReadWriteLockDWD rwLock;

    // normal constructor
    public KVStore() {
        rwLock = new ReadWriteLockDWD();
    }

    // constructor used in tests
    public KVStore(boolean debug) {
        rwLock = new ReadWriteLockDWD(debug);
    }

    public String toString() {
    	return this.memory.toString();
    }


    public void putTemperature(int stationId, double temperature) throws InterruptedException {
        // TODO 3.1.
        rwLock.lockWriteLock();
        try {
            unsafePutTemperature(stationId, temperature);
        } finally {
            rwLock.unlockWriteLock();
        }
    }

    public Optional<Double> getAverage(int stationId, int numberOfHours) throws InterruptedException {
    	// TODO part 3.2.
        rwLock.lockReadLock();
        try {
            return unsafeGetAverage(stationId, numberOfHours);
        } finally {
            rwLock.unlockReadLock();
        }
    }

    public Optional<Double> getAverageParent(int stationId, int numberOfHours) throws InterruptedException {
        // TODO part 3.3.
        rwLock.lockReadLock();
        try {
            return unsafeGetAverageParent(stationId, numberOfHours);
        } finally {
            rwLock.unlockReadLock();
        }
    }

    /**
     * * Puts a temperature value into the KVStore.
     * * If the stationId is already in the KVStore, the temperature value should be added to the existing station.
     * * If the stationId is not in the KVStore, a new station should be created.
     * <p>
     * * @param stationId
     * * @param temperature
     * * @throws InterruptedException
     *
     * Note: This method is unsafe, there is no protection against concurrent access from different threads
     */
    private void unsafePutTemperature(int stationId, double temperature) throws InterruptedException {
        if (memory == null) {
            memory = new Tree(stationId);
        }
        memory.putTemperature(stationId, temperature);
    }

    /**
     * * Gets the average temperature of a certain station over the last numberOfHours hours.
     * * If the station is not found, the method should return Optional.empty().
     * <p>
     * * @param stationId
     * * @param numberOfHours
     * * @return the average temperature of a certain station over the last x hours
     * * @throws InterruptedException
     *
     * Note: This method is unsafe, there is no protection against concurrent access from different threads
     */
    private Optional<Double> unsafeGetAverage(int stationId, int numberOfHours) throws InterruptedException {
    	if (memory != null) {
            Tree node = memory.getTreeNode(stationId);
            return node == null ? Optional.empty() : node.getEntry().calculateAverage(numberOfHours);
        }
        return Optional.empty();
    }
    /**
     *      * Gets the average over numberOfHours of the parent station.
     *      * If the station is not found, the method should return Optional.empty().
     *      * <p>
     *          * @param stationId
     *          * @param numberOfHours
     *          * @return the last value of the parent station.
     *          * @throws InterruptedException
     *          <p>
     * Note: This method is unsafe, there is no protection against concurrent access from different threads
     * */

    private Optional<Double> unsafeGetAverageParent(int stationId, int numberOfHours) throws InterruptedException {
        if (memory != null) {
            Tree parent = memory.getParentNode(stationId);
            return parent == null ? Optional.empty() : parent.getEntry().calculateAverage(numberOfHours);
        }
        return Optional.empty();
    }

    public void setMemory(Tree memory) {
        this.memory = memory;
    }
}
