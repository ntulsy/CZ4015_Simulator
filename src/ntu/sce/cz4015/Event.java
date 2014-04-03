package ntu.sce.cz4015;

public class Event {
	protected BaseStation baseStation;
	protected double eventTime;
	public int id;
	
	public BaseStation getBaseStation() {
		return baseStation;
	}
	public void setBaseStation(BaseStation baseStation) {
		this.baseStation = baseStation;
	}
	public double getEventTime() {
		return eventTime;
	}
	public void setEventTime(double eventTime) {
		this.eventTime = eventTime;
	}
	
}
