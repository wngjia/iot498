package iot498;

import java.util.UUID;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.vertx.core.Vertx;
import iot498.dbi.DeviceMemDB;
import iot498.dbi.DeviceSimMemDB;
import iot498.dbi.TimeSeriesDBI;
import iot498.dbi.TimeSeriesMemDB;
import iot498.resources.DataResource;
import iot498.resources.DevicesResource;
import iot498.ts.DeviceSimVerticle;
import iot498.ts.TSServerVerticle;
import iot498.resources.DeviceSimsResource;

public class MyApp extends Application<MyConfig>
{
	@Override
	public void initialize(Bootstrap<MyConfig> bootstrap)
	{
	    bootstrap.addBundle(
	    	new AssetsBundle("/assets/", "/", "index.html"));
	    
	    bootstrap.addBundle(
	    	new DBIExceptionsBundle());
	}
	
	@Override
	public void run(MyConfig config, Environment env)
		throws Exception
	{
		Vertx vertx = Vertx.vertx();
		
		DeviceMemDB device_dbi = new DeviceMemDB();
		DeviceSimMemDB sim_dbi = new DeviceSimMemDB();
		TimeSeriesDBI ts_dbi = new TimeSeriesMemDB();

		setup_demo(device_dbi, sim_dbi, vertx);

		vertx.deployVerticle(new TSServerVerticle(ts_dbi));
		
		JerseyEnvironment jersey = env.jersey();
		jersey.register(new DevicesResource(device_dbi));
		jersey.register(new DataResource(ts_dbi));
		jersey.register(new DeviceSimsResource(sim_dbi, vertx));
	}

	public static void main(String[] args)
		throws Exception
	{
		new MyApp().run(args);
	}
	
	private void setup_demo(
		DeviceMemDB device_dbi,
		DeviceSimMemDB sim_dbi,
		Vertx vertx)
	{
		String temp_id = UUID.randomUUID().toString();
		device_dbi.add("Jia", temp_id,
			"Office Temp", "Office temperature");
		
		vertx.deployVerticle(new DeviceSimVerticle(
			sim_dbi.add(temp_id, 100, 65, 85, 1000)));

		device_dbi.add("Jia", UUID.randomUUID().toString(),
			"Office Power", "Office power usage");

		device_dbi.add("Alice", UUID.randomUUID().toString(),
			"Car GPS", "Car location");

		device_dbi.add("ECE", UUID.randomUUID().toString(),
			"Server Users", "Number of server users"); 
	}
}
