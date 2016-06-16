package iot498.dbi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import iot498.api.DataEntry;

public class TimeSeriesMemDB
	implements AutoCloseable, TimeSeriesDBI
{
	@Override
	public Future<Void> insert(
		String id, long ts, double val)
	{
		return exe.submit(() -> {
			
			TreeMap<Long, Double> entries = db.get(id);
			if (entries == null)
			{
				entries = new TreeMap<>();
				db.put(id, entries);
			}
			
			entries.put(ts, val);
			
			return null;
		});
	}
	
	@Override
	public Future<ArrayList<DataEntry>> select(
		String id, long ts_begin)
	{
		return exe.submit(() -> {
			
			ArrayList<DataEntry> ret = new ArrayList<>();
			
			TreeMap<Long, Double> entries = db.get(id);
			if (entries != null)
			{
				entries.tailMap(ts_begin, true)
					.forEach((ts,val) -> {
						ret.add(new DataEntry(ts, val));
					});
			}
			
			return ret;
		});
	}
	
	@Override
	public void close() throws Exception
	{
		exe.shutdownNow();
	}
	
	private final ExecutorService
		exe = Executors.newSingleThreadExecutor();
	
	private final HashMap<String, TreeMap<Long, Double>>
		db = new HashMap<>();
}
