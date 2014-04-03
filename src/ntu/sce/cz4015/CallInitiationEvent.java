package ntu.sce.cz4015;

public class CallInitiationEvent extends Event {
	private double speed;
	private double position;
	private double duration;
	
	public CallInitiationEvent(int id, double time, double speed, BaseStation station, double position, double duration) {
		super();
		this.id = id;
		this.baseStation = station;
		this.eventTime = time;
		this.speed = speed;
		this.position = position;
		this.duration = duration;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getPosition() {
		return position;
	}

	public void setPosition(double position) {
		this.position = position;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}
	
	@Override
	public String toString(){
		return "CallInitiation: Time: " + eventTime + " Speed: " + speed + " Station: " + baseStation.getId() + " Position: " + position + " duration: " + duration;
	}
}
