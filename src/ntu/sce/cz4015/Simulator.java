package ntu.sce.cz4015;

import java.io.*;
import java.util.*;


public class Simulator {
	private final int initiationEventCount = 8000;
	private PriorityQueue<Event> eventList;
	private double simulationClock;
	private BaseStation[] stationList;
	private int blockedCallCount;
	private int droppedCallCount;
	private int handoverCount;
	private static int countUpToNow;

	// anonymous class for priority queue
	private static Comparator<Event> eventComparator = new Comparator<Event>(){
		@Override
		public int compare(Event e1, Event e2) {
			if (e1.getEventTime() < e2.getEventTime())
				return -1;
			if (e1.getEventTime() > e2.getEventTime())
				return 1;
			else
				return 0;
		}
	};
		
	public void init(){
		eventList = new PriorityQueue<Event>(10, eventComparator);
		simulationClock = 0;
		stationList = new BaseStation[20];
		blockedCallCount = 0;
		droppedCallCount = 0;
		handoverCount = 0;
		Simulator.countUpToNow = 0;

		for (int i = 0; i < 20; ++i)
			stationList[i] = new BaseStation(i);
	}
	
	public void readInEvents(boolean isDeterministic) {
		if (isDeterministic) {
			// deterministic mode
			CsvReader in = null;
			try {
				in = new CsvReader(
						"D:/EdveNTUre & Data/Year 3 sem 2/CZ4015/Homework/PCS_TEST_DETERMINSTIC_S21314.csv");
			} catch (Exception e) { }
			
			for (int i = 0; i < this.initiationEventCount; ++i) {
				String[] value = in.readOneLine();
				CallInitiationEvent event = new CallInitiationEvent(i+1,
						Double.parseDouble(value[0]),
						Double.parseDouble(value[3]),
						stationList[Integer.parseInt(value[1]) - 1], 0,
						Double.parseDouble(value[2]));
				//System.out.println(event.toString());
				eventList.add(event);
			}
		} else {
			// stochastic mode
			// generate all initiation event
			RandomNumberGenerator rng = new RandomNumberGenerator();
			for (int i = 0; i < this.initiationEventCount; ++i) {
				double interArrival = rng.carInterArrival();
				CallInitiationEvent event = new CallInitiationEvent(i+1,
						simulationClock + interArrival, rng.velocity(),
						stationList[rng.baseStation() - 1],
						rng.positionInBaseStation(), rng.callDuration());
				//System.out.println(event.toString());
				eventList.add(event);
				simulationClock += interArrival;
			}
			simulationClock = 0;
		}
	}
	
	public void startSimulation(){
		while (!eventList.isEmpty()){
			Event e = eventList.peek();
			eventList.remove(e);
			handleEvent(e);
		}
	}
	
	private void handleEvent(Event e){
		this.simulationClock = e.eventTime; // advance simclock
		BaseStation currentStation = e.getBaseStation();
		//System.out.println(e.toString());
		if (e instanceof CallInitiationEvent){
			if (currentStation.hasFreeChannelForCallInitiation()){
				currentStation.occupyOneChannel();
				generateNextEvent((CallInitiationEvent)e);
			} else {
				++this.blockedCallCount;
			}
			++Simulator.countUpToNow;
			if (Simulator.countUpToNow == 4000){
				this.handoverCount= 0;
				this.blockedCallCount = 0;
				this.droppedCallCount = 0;
			}
			
		} else if (e instanceof CallTerminationEvent){
			currentStation.releaseOneChannel();
		} else if (e instanceof CallHandoverEvent){
			BaseStation nextStation = stationList[currentStation.getId() + 1];
			currentStation.releaseOneChannel();
			if(nextStation.hasFreeChannelForHandover()){
				nextStation.occupyOneChannel();
				generateNextEvent((CallHandoverEvent)e);
			} else {
				++this.droppedCallCount;
			}
			++this.handoverCount;
		}
	}
	
	private void generateNextEvent(CallInitiationEvent e){
		Event event;
		// calculate time to reach the next base station
		double remainingDistance = 2000.0 - e.getPosition();
		double speedInMeterPerSecond = e.getSpeed() * 1000.0 / 3600.0;
		double remainingTimeInThisStation = Math.min(remainingDistance / speedInMeterPerSecond,e.getDuration());
		if (e.getDuration() > remainingTimeInThisStation && e.getBaseStation().getId() != 19){  
			// call will be handed over to next station
			// handover event will be generated
			event = new CallHandoverEvent(e.id, this.simulationClock + remainingTimeInThisStation,
							e.getSpeed(), e.getBaseStation(), e.getDuration() - remainingTimeInThisStation, 1);
		} else {
			// call will be terminated in this station
			event = new CallTerminationEvent(e.id, this.simulationClock + remainingTimeInThisStation,
					e.getBaseStation(), 0);
		}
		eventList.add(event);
	}
	
	private void generateNextEvent(CallHandoverEvent e){
		Event event;
		// calculate time to reach the next base station
		double speedInMeterPerSecond = e.getSpeed() * 1000.0 / 3600.0;
		double remainingTimeInThisStation = Math.min(2000.0 / speedInMeterPerSecond, e.getDuration());
		if (e.getDuration() > remainingTimeInThisStation && e.getBaseStation().getId() < 18) // prevent overflow
			{ 
			// call will be handed over to next station
			// handover event will be generated
			event = new CallHandoverEvent(e.id, this.simulationClock + remainingTimeInThisStation,
							e.getSpeed(), stationList[e.getBaseStation().getId() + 1], e.getDuration() - remainingTimeInThisStation, e.handoverCount+1);
		} else {
			// call will be terminated in this station
			event = new CallTerminationEvent(e.id, this.simulationClock + remainingTimeInThisStation,
					stationList[e.getBaseStation().getId() + 1], e.handoverCount);
		}
		eventList.add(event);
	}
	
	public void printStatistics(){
		System.out.println("Blocked Call Count:" + this.blockedCallCount);
		System.out.println("Blocked Call Percetage:" + (double)this.blockedCallCount/(this.initiationEventCount-4000));
		System.out.println("Dropped Call Count:" + this.droppedCallCount);
		System.out.println("Handover Count:" + this.handoverCount);
		System.out.println("Dropped Call Percetage:" + (double)this.droppedCallCount/(this.initiationEventCount-4000));
	}
	
	public static void main(String[] args) {
		Simulator sim = new Simulator();
		double totalBlockRate = 0;
		double  totalDropRate = 0;
		for (int i = 0; i < 100; ++i)
		{
			sim.init();
			sim.readInEvents(false);
			sim.startSimulation();
			//sim.printStatistics();
			System.out.print((double)sim.blockedCallCount/(sim.initiationEventCount-4000) + ",");
			System.out.println((double)sim.droppedCallCount/(sim.initiationEventCount-4000));
			totalBlockRate += (double)sim.blockedCallCount/(sim.initiationEventCount-4000);
			totalDropRate += (double)sim.droppedCallCount/(sim.initiationEventCount-4000);
		}
		System.out.println("AVE Blocked:" + totalBlockRate / 100);
		System.out.println("AVE Dropped:" + totalDropRate / 100);
	}

}
