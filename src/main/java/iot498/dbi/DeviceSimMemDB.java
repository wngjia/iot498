package iot498.dbi;

import java.util.ArrayList;

import iot498.api.DeviceSim;

/**
 * In-memory database for device simulations.
 */
public class DeviceSimMemDB
{
	public synchronized
	ArrayList<DeviceSim> get()
	{
		return new ArrayList<>(db);
	}
	
	public synchronized
	DeviceSim add(String id, int hz,
		double min, double max, int steps)
	{
		DeviceSim ret = new DeviceSim(id, hz, min, max, steps);

		db.add(ret);
		
		return ret;
	}

	private final ArrayList<DeviceSim> db = new ArrayList<>();
}
