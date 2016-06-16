package iot498.dbi;

import java.util.ArrayList;
import java.util.concurrent.Future;

import iot498.api.DataEntry;

/**
 * Database interface for time series. 
 */
public interface TimeSeriesDBI
{
	/**
	 * Insert a data entry into the database.
	 * 
	 * @param id Device ID.
	 * @param ts Timestamp.
	 * @param val Value.
	 * @return Completion wrapped in Future.
	 */
	Future<Void> insert(
		String id, long ts, double val);

	/**
	 * Retrieve all data entries after a certain time.
	 * 
	 * @param id Device ID.
	 * @param ts_begin Timestamp to start with. 
	 * @return Future Entries wrapped in Future.
	 */
	Future<ArrayList<DataEntry>> select(
		String id, long ts_begin);
}
