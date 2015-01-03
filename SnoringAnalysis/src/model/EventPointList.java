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
	
	public boolean remove(EventPoint eventPoint)
	{
		return list.remove(eventPoint);
	}
	
	public boolean isEmpty()
	{
		return list.isEmpty();
	}
	
	
	@Override
	public Iterator<EventPoint> iterator()
	{
		return list.iterator();
	}

}
