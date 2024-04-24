package dwd_eist;

import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private KVStore store;
    public static final int millisToWait = 1000;
    public static final int numberOfStations = 10;
    public static final int numberOfAgents = 10;
    public static final int numberToInsert = 1000;

    public static int TOTAL_NUMBER_OF_MEASUREMENTS = 500;
    public static double probabilityFailure = 0.4;


    public void cleanup() {
        DataLogger.cleanup();
        Agency.cleanup();
    }

    public void dataProcessingPart() {
    	Entry entry = new Entry(5);
        entry.addTemperature(10.0);
        entry.addTemperature(20.0);
        entry.addTemperature(30.0);
        entry.addTemperature(23.0);
        entry.addTemperature(15.0);
        System.out.println("Average of the last 2 measurements: " + entry.calculateAverage(2).get());
        System.out.println("Average of all measurements: " + entry.calculateAverage(5).get());
    }
    
    public void KVSPart() {
    	KVStore kvs = new KVStore();
    	try {
        kvs.putTemperature(1, 9.0);
        kvs.putTemperature(1, 10.0);
        kvs.putTemperature(4, 30.0);
        kvs.putTemperature(2, 15.0);
        kvs.putTemperature(7, 30.0);
    	} catch (InterruptedException ex) {
            Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    	System.out.println("The KVS contains:\n" + kvs.toString());
    }
    
    public void runScenario(boolean debug) {
        store = new KVStore(debug);
        for (int i = 0; i < numberOfStations; i++) {
            DataLogger.addStation(i, store);
        }

        for (int i = 0; i < numberOfAgents; i++) {
            Agency.addAgency(store);
        }
        // Start the treads:
        DataLogger.startThreads();
        Agency.startThreads();

        DataLogger.joinThreads();
        Agency.joinThreads();


        Agency.stopAllAgencies();
        if(!debug) System.out.println("All Agents stopped");
        DataLogger.stopAllDataLoggers();
        if(!debug) System.out.println("All DataLoggers stopped");
    }


    public static void main(String[] args) {
        App app = new App();
        
        app.dataProcessingPart(); // Part 1
        app.KVSPart(); // Part 2
        App.probabilityFailure = 0.0;
        app.runScenario(false); // Part 3 (no failure)
        App.probabilityFailure = 0.4;
        app.runScenario(false); // Part 4 (adding failure)
        app.cleanup();
    }


}
