package dwd_eist;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class Station {
    private int nbToInsert = 10000;
    private int inserted;
    AtomicBoolean finished;


    public Station() {
        finished = new AtomicBoolean(false);
        inserted = 0;
    }

    private double generateMeasurement() {
        return ThreadLocalRandom.current().nextDouble() * 255.0;
    }

    private boolean isFailing() {
        return ThreadLocalRandom.current().nextDouble() < App.probabilityFailure;
    }

    /* 
    Gets the next temperature value
    */
    public double getNextMeasurement() {
        double measurement;
        if (finished.get()) {
            return -1;
        }
        if (isFailing()) {
            measurement = -1;
        } else {
            measurement = generateMeasurement();
        }
        if (inserted == nbToInsert) {
            finished.set(true);
        }
        return measurement;
    }
}
