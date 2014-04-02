package ntu.sce.cz4015;

public class CallTerminationEvent extends Event {
	
	public CallTerminationEvent(double time, BaseStation station) {
		super();
		this.baseStation = station;
		this.eventTime = time;
	}
	
	public String toString(){
		return "CallTermination: Time: " + eventTime + " Station: " + baseStation.getId();
	}
}
