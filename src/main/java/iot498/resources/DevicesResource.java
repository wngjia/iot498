package iot498.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.jersey.caching.CacheControl;
import iot498.api.Device;
import iot498.dbi.DeviceMemDB;

@Path("/devices")
@Produces(MediaType.APPLICATION_JSON)
public class DevicesResource
{
	public DevicesResource(DeviceMemDB dbi)
	{
		this.dbi = dbi;
	}
	
	@GET
	@CacheControl(noCache=true)
	public Collection<String> getOwners()
	{
		ArrayList<String> ret = dbi.getOwners();
		
		ret.sort((l, r) -> l.compareToIgnoreCase(r));
		
		return ret;
	}
	
	@GET @Path("/{owner}")
	@CacheControl(noCache=true)
	public Collection<Device> getChannels(
		@PathParam("owner") @NotEmpty String owner)
	{
		ArrayList<Device> ret = dbi.getDevices(owner);
		
		ret.sort((l, r) -> l.name.compareToIgnoreCase(r.name));
		
		return ret;
	}

	@POST @Path("/{owner}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add(
		@PathParam("owner") @NotEmpty String owner,
		@FormParam("name") @NotEmpty String name,
		@FormParam("description") @NotEmpty String description)
	{
		String id = UUID.randomUUID().toString();

		logger.info("add {}/{}: {},{}",
			owner, id, name, description);
		
		dbi.add(owner, id, name, description);
		
		URI self = uri.getAbsolutePathBuilder().path(id).build();
		
		return Response.created(self).build();
	}
	
	@DELETE @Path("/{owner}/{id}")
	public Response delete(
		@PathParam("owner") @NotEmpty String owner,
		@PathParam("id") @NotEmpty String id)
	{
		logger.info("delete {}/{}", owner, id);
		
		dbi.remove(owner, id);
		
		return Response.ok().build();
	}
	
	@Context
	UriInfo uri;

	private final DeviceMemDB dbi;
    
	private static final Logger logger
		= LoggerFactory.getLogger(DevicesResource.class);
}
