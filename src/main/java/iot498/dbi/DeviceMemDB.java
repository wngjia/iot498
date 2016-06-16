package iot498.dbi;

import java.util.ArrayList;
import java.util.HashMap;

import iot498.api.Device;

/**
 * In-memory database for device information (owner etc.)
 */
public class DeviceMemDB
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
	Device add(String owner, String id,
		String name, String description)
	{
		HashMap<String, Device> devices = db.get(owner);
		
		if (devices == null)
		{
			devices = new HashMap<>();
			db.put(owner, devices);
		}
		
		assert !devices.containsKey(id);

		Device ret = new Device(owner, id, name, description);
		
		devices.put(id, ret);
		
		return ret;
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
