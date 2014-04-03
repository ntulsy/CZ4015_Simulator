package ntu.sce.cz4015;

public class CallTerminationEvent extends Event {
	
	public int handoverCount;
	
	public CallTerminationEvent(int id, double time, BaseStation station, int handoverCount) {
		super();
		this.id = id;
		this.baseStation = station;
		this.eventTime = time;
		this.handoverCount = handoverCount;
	}
	
	public String toString(){
		return "CallTermination: Time: " + eventTime + " Station: " + baseStation.getId();
	}
}
