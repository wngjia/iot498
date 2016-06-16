package iot498.resources;

import java.util.Collection;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.jersey.caching.CacheControl;
import iot498.api.DataEntry;
import iot498.dbi.TimeSeriesDBI;

@Path("/data")
@Produces(MediaType.APPLICATION_JSON)
public class DataResource
{
	public DataResource(TimeSeriesDBI dbi)
	{
		this.dbi = dbi;
	}
	
	@GET @Path("/raw/{id}")
	@CacheControl(noCache=true)
	public Collection<DataEntry> getRawData(
		@PathParam("id") @NotEmpty String id,
		@QueryParam("ts_begin") Long ts_begin)
		throws Exception
	{
		if (ts_begin == null)
			ts_begin = new Long(0);
		
		logger.trace("get {}: {}", id, ts_begin);
		
		return dbi.select(id, ts_begin).get();
	}

	private final TimeSeriesDBI dbi;
    
	private static final Logger logger
		= LoggerFactory.getLogger(DataResource.class);
}
