package iot498;

import java.util.UUID;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import iot498.resources.DevicesResource;

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
		JerseyEnvironment jersey = env.jersey();
		
		MyDatabase dbi = new MyDatabase();
		
		setup_demo(dbi);
		
		jersey.register(new DevicesResource(dbi));
	}

	public static void main(String[] args)
		throws Exception
	{
		new MyApp().run(args);
	}
	
	private void setup_demo(MyDatabase dbi)
	{
		dbi.add("Jia", UUID.randomUUID().toString(),
			"Office Temp", "Office temperature"); 

		dbi.add("Jia", UUID.randomUUID().toString(),
			"Office Power", "Office power usage"); 

		dbi.add("Alice", UUID.randomUUID().toString(),
			"Car GPS", "Car location"); 

		dbi.add("ECE", UUID.randomUUID().toString(),
			"Server Users", "Number of server users"); 
	}
}
