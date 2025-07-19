package dwd_eist;

import java.util.LinkedList;
import java.util.Optional;


public class Entry {

    int id;
    private LinkedList<Double> values;
    public static final Entry INVALID = new Entry(-1);
    public static final Entry NOT_AVAILABLE = new Entry(-2);

    public Entry(int id) {
        this.id = id;
        this.values = new LinkedList<>();
    }

    public Entry(int id, LinkedList<Double> values) {
        this.id = id;
        this.values = values;
    }

    /**
     * * Calculates the average temperature over the past numberOfHours hours.
     * * If the number of entries is smaller than numberOfHours, the average is calculated over the available entries.
     * * If the number of hours is 0, the method should return Optional.empty().
     * * If there are no entries, the method should return Optional.empty().
     *
     * @param numberOfHours
     * @return the average temperature over the past x hours
     */
    public Optional<Double> calculateAverage(int numberOfHours) {
        // TODO 1.1.
        if (numberOfHours <= 0 || values.isEmpty()) {
            return Optional.empty();
        }

        int count = Math.min(numberOfHours, values.size());
        double sum = 0;
        for (int i = 0; i < count; i++) {
            sum += values.get(i);
        }

        return Optional.of(sum / count);
    }

    /**
     * * Adds a new temperature value to the list of values.
     * * The new value is added at the beginning of the list.
     * * @param temperature
     */
    public void addTemperature(double temperature) {
        this.values.addFirst(temperature);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Entry other = (Entry) obj;
        if (this.values.size() != other.values.size()) {
            return false;
        }
        for (int i = 0; i < values.size(); i++) {
            if (!values.get(i).equals(other.values.get(i))) {
                return false;
            }
        }
        return this.id == other.id;
    }

    public int getId() {
        return id;
    }

    public LinkedList<Double> getValues() {
        return values;
    }


}
