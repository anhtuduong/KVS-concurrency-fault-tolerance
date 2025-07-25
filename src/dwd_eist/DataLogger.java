package dwd_eist;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataLogger extends Thread {

    AtomicBoolean keepRunning;
    private Station station;
    int stationID;
    private static ArrayList<DataLogger> threads = new ArrayList<>();
    public static int nbStations;

    private AtomicInteger numberOfMeasurements;

    private KVStore store;

    public DataLogger(int stationID, KVStore store) {
        this.store = store;
        this.keepRunning = new AtomicBoolean(true);
        this.numberOfMeasurements = new AtomicInteger(0);
        this.station = new Station();
        this.stationID = stationID;
    }

    public static void addStation(int stationID, KVStore store) {
        DataLogger newStation = new DataLogger(stationID, store);
        threads.add(newStation);
    }

    public static void startThreads() {
        threads.forEach(DataLogger::start);
    }

    public static void joinThreads() {
        threads.forEach(dl -> {
            try {
                dl.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(DataLogger.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public static void stopAllDataLoggers() {
        threads.forEach(dl -> dl.keepRunning.set(false));
    }

    public static void cleanup() {
        threads.clear();
    }


    @Override
    public void run() {
        while (keepRunning.get() && numberOfMeasurements.incrementAndGet() <= App.TOTAL_NUMBER_OF_MEASUREMENTS) {
            try {
                double measurement = this.station.getNextMeasurement(); 
                // A negative measurement indicates a station failure.
                if (measurement < 0) { // this branch is only taken when the station fails (part 4)
                    // TODO part 4
                    // In case of a failure, we implement a fault-tolerance mechanism.
                    // We try to get the latest value from the parent station in the tree.
                    // We ask for the average of the last 1 value, which is just the latest value.
                    store.getAverageParent(stationID, 1).ifPresent(value -> {
                        try {
                            // If a parent value is present, we use it for the current station.
                            store.putTemperature(stationID, value);
                        } catch (InterruptedException e) {
                            // Restore the interrupted status.
                            Thread.currentThread().interrupt();
                        }
                    });
                } else {
                    // TODO: Insert value into KVStore store.
                    // If the measurement is valid, store it in the KVStore.
                    store.putTemperature(stationID, measurement);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static ArrayList<DataLogger> getThreads() {
        return threads;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Station getStation() {
        return station;
    }
}
