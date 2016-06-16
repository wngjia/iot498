package iot498.api;

/**
 * A simulation setup for a device.
 */
public class DeviceSim
{
	public DeviceSim(String id, int hz,
		double min, double max, int steps)
	{
		this.id = id;
		this.hz = hz;
		this.min = min;
		this.max = max;
		this.steps = steps;
	}

	public final String id;
	public final int hz;
	public final double min;
	public final double max;
	public final int steps;
}
