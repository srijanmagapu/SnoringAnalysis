package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

public class EventPointList extends Observable implements Iterable<EventPoint>
{
	private List<EventPoint> list;
	
	public EventPointList()
	{
		this.list = new ArrayList<EventPoint>();
	}
	
	public void add(EventPoint eventPoint)
	{
		list.add(eventPoint);
		setChanged();
		notifyObservers();
	}
	
	public void add(EventPoint[] eventPoints)
	{
		for(EventPoint point : eventPoints)
			add(point);
	}
	
	public void add(EventPointList eventPoints)
	{
		for(EventPoint point : eventPoints)
			add(point);
	}
	
	public boolean remove(EventPoint eventPoint)
	{
		return list.remove(eventPoint);
	}
	
	public boolean isEmpty()
	{
		return list.isEmpty();
	}
	
	public int size()
	{
		return list.size();
	}
	
	public EventPoint get(int index)
	{
		return list.get(index);
	}
	
	public EventPoint[] getPointsArray()
	{
		return list.toArray(new EventPoint[0]);
	}
	
	public EventPoint[] getPointsFromCluster(int cluster)
	{
		EventPointList clusterLsit = new EventPointList();
		for(EventPoint point : list)
			if(point.getCluster() == cluster)
				clusterLsit.add(point);
		
		return clusterLsit.getPointsArray();
	}
	
	@Override
	public Iterator<EventPoint> iterator()
	{
		return list.iterator();
	}
	
}
