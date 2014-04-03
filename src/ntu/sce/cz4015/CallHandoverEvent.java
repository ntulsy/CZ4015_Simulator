package ntu.sce.cz4015;

public class CallHandoverEvent extends Event {
	private double speed;
	private double duration; // this duration is the remaining duration of the call
	public int handoverCount;
	public CallHandoverEvent(int id,double time, double speed, BaseStation station, double duration,int handoverCount) {
		super();
		this.id = id;
		this.baseStation = station;
		this.eventTime = time;
		this.speed = speed;
		this.duration = duration;
		this.handoverCount = handoverCount;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}
	
	@Override
	public String toString(){
		return "CallHandover: Time: " + eventTime + " Speed: " + speed + " From Station: " + baseStation.getId() + " duration: " + duration;
	}
}
