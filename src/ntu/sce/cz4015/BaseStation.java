package ntu.sce.cz4015;

public class BaseStation {
	
	private final int reservedForHandover = 0;

	private int busyChannel;
	private int freeChannel;
	private int id;
	
	public BaseStation(int id) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.busyChannel = 0;
		this.freeChannel = 10;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void occupyOneChannel(){
		--this.freeChannel;
		++this.busyChannel;
	}
	public void releaseOneChannel(){
		++this.freeChannel;
		--this.busyChannel;
	}
	
	public boolean hasFreeChannelForCallInitiation(){
		return this.freeChannel - this.reservedForHandover > 0;
	}
	
	public boolean hasFreeChannelForHandover(){
		return this.freeChannel > 0;
	}
}
