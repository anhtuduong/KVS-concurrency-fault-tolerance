package dwd_eist;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Agency extends Thread {
    AtomicBoolean keepRunning = new AtomicBoolean();
    private static AtomicInteger numberOfMeasurements;
    static ArrayList<Agency> threads = new ArrayList<>();

    static int NUMBER_OF_HOURS = 10;

    private KVStore store;

    private int getRandInt() {
        return (int) (Math.random() * 10);
    }

    public static void stopAllAgencies() {
        threads.forEach(dl -> dl.keepRunning.set(false));
    }

    private Agency(KVStore store) {
        this.store = store;
        this.keepRunning.set(true);
        numberOfMeasurements = new AtomicInteger(0);
    }

    public static void addAgency(KVStore store) {
        Agency newAgency = new Agency(store);
        threads.add(newAgency);
    }

    public static void startThreads() {
        threads.forEach(Agency::start);
    }

    public static void joinThreads() {
        threads.forEach(dl -> {
            try {
                dl.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Agency.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public static void cleanup() {
        threads.clear();
    }

    @Override
    public void run() {
        while (keepRunning.get() && numberOfMeasurements.incrementAndGet() < App.TOTAL_NUMBER_OF_MEASUREMENTS) {
            try {
                int stationId = getRandInt();
                Optional<Double> measurement = store.getAverage(stationId, NUMBER_OF_HOURS);
                numberOfMeasurements.incrementAndGet();
                if (measurement.isPresent()) {
                    System.out.println("Average temperature for station " + stationId + " is " + measurement.get());
                } else {
                    System.out.println("Station " + stationId + " not found");
                    // If station with stationId is not found, retry. Do not end the thread!
                    continue;
                }
                System.out.println("Average temperature for station " + stationId + " is " + measurement.get());
                Thread.sleep(App.millisToWait); // thread waits 10ms. Should this be changed ?
            } catch (InterruptedException ex) {
                Logger.getLogger(Station.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
