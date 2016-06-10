package iot498;

import java.util.ArrayList;
import java.util.HashMap;

import iot498.api.Device;

/**
 * This is supposed to provide database access.
 * Since we don't have a database now,
 * it serves as a database as well.
 * 
 * @author wj
 *
 */
public class MyDatabase
{
	public synchronized
	ArrayList<String> getOwners()
	{
		return new ArrayList<>(db.keySet());
	}
	
	public synchronized
	ArrayList<Device> getDevices(String owner)
	{
		HashMap<String, Device> devices = db.get(owner);
		
		return (devices == null)?
			new ArrayList<>():
			new ArrayList<>(devices.values());
	}
	
	public synchronized
	void add(String owner, String id,
		String name, String description)
	{
		HashMap<String, Device> devices = db.get(owner);
		
		if (devices == null)
		{
			devices = new HashMap<>();
			db.put(owner, devices);
		}
		
		assert !devices.containsKey(id);

		devices.put(id, new Device(
			owner, id, name, description));
	}

	public synchronized
	boolean remove(String owner, String id)
	{
		HashMap<String, Device> devices = db.get(owner);
		
		if (devices == null)
			return false;

		return devices.remove(id) != null;
	}
	
	private final HashMap<String, HashMap<String, Device>>
		db = new HashMap<>();
}
