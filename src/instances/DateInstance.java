package instances;

import java.time.LocalDate;

/**
 *
 * Created by Artem Siatchinov on 8/5/2016.
 */
public abstract class DateInstance extends DataBaseInstance {

    private LocalDate date;
    private int startTime;
    private int endTime;


    @Override
    public String toString() {
        return super.toString() +
                ". Date: " + date +
                ". Start time: " + startTime +
                ". End time: " + endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
