package iot498.ts;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.net.NetSocket;
import iot498.api.DeviceSim;

/**
 * Generate time series for device simulation.
 */
public class DeviceSimVerticle extends AbstractVerticle
{
	public DeviceSimVerticle(DeviceSim sim)
	{
		this.host = System.getProperty(
			"iot498.TSServer.host", "127.0.0.1");
		this.port = Integer.parseInt(System.getProperty(
			"iot498.TSServer.port", "8888"));
		this.sim = sim;
		this.state = (sim.min+sim.max)/2;
	}
	
	@Override
	public void start(Future<Void> success) throws Exception
	{
		vertx.createNetClient().connect(port, host, res -> {
			if (res.succeeded())
			{
				NetSocket sock = res.result();
				
				vertx.setPeriodic(1000/sim.hz, id -> {
					send_data(sock);
				});
			}
			else
			{
				success.fail(res.cause());
			}
		});
	}
	
	private void send_data(NetSocket sock)
	{
		sock.write(String.format("%s,%d,%f\r\n",
			sim.id, System.currentTimeMillis(), state));
		
		update_state();
	}
	
	private void update_state()
	{
		double step = (sim.max-sim.min)/sim.steps;
		
		if (Math.random() < 0.5)
			state -= step;
		else
			state += step;
		
		if (state < sim.min)
			state = sim.min;
		
		if (state > sim.max)
			state = sim.max;
	}

	private final String host;
	private final int port;
	private final DeviceSim sim;
	private double state;
}
