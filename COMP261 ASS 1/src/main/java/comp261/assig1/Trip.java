package comp261.assig1;

import java.util.ArrayList;

public class Trip {
    private String stop_pattern_id;
    private String tripId;
    private int stopSequence;
    private String timePoint;

    public Trip(){
    }
    public Trip(String stop_pattern_id,String tripId, int stopSequence,String timePoint){
        this.stop_pattern_id=stop_pattern_id;
        this.tripId=tripId;
        this.stopSequence=stopSequence;
        this.timePoint=timePoint;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTripId() {
        return tripId;
    }

    public int getStopSequence() {
        return stopSequence;
    }

    public String getStop_pattern_id() {
        return stop_pattern_id;
    }

    public String getTimePoint() {
        return timePoint;
    }

    public void setStop_pattern_id(String stop_pattern_id) {
        this.stop_pattern_id = stop_pattern_id;
    }

    public void setStopSequence(int stopSequence) {
        this.stopSequence = stopSequence;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint;
    }

}
