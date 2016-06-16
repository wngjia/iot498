package iot498.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.jersey.caching.CacheControl;
import io.vertx.core.Vertx;
import iot498.api.DeviceSim;
import iot498.dbi.DeviceSimMemDB;
import iot498.ts.DeviceSimVerticle;

@Path("/device-sims")
@Produces(MediaType.APPLICATION_JSON)
public class DeviceSimsResource
{
	public DeviceSimsResource(DeviceSimMemDB dbi, Vertx vertx)
	{
		this.dbi = dbi;
		this.vertx = vertx;
	}
	
	@GET
	@CacheControl(noCache=true)
	public Collection<DeviceSim> get()
	{
		ArrayList<DeviceSim> ret = dbi.get();
		
		ret.sort((l, r) -> l.id.compareTo(r.id));
		
		return ret;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add(
		@FormParam("id") @NotEmpty String id,
		@FormParam("hz") int hz,
		@FormParam("min") double min,
		@FormParam("max") double max,
		@FormParam("steps") int steps)
	{
		logger.info("add {}: {}, [{},{}], {}",
			id, hz, min, max, steps);
		
		vertx.deployVerticle(new DeviceSimVerticle(
			dbi.add(id, hz, min, max, steps)));
		
		URI self = uri.getAbsolutePathBuilder().path(id).build();
		
		return Response.created(self).build();
	}
	
	@Context
	UriInfo uri;

	private final DeviceSimMemDB dbi;
	private final Vertx vertx;
    
	private static final Logger logger
		= LoggerFactory.getLogger(DeviceSimsResource.class);
}
