package iot498.api;

/**
 * Time series data entry.
 */
public class DataEntry
{
	public DataEntry(long ts, double val)
	{
		this.ts = ts;
		this.val = val;
	}

	public final long ts; // time-stamp
	public final double val; // value
}
