package iot498.ts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;
import iot498.dbi.TimeSeriesDBI;

/**
 * Record time series from devices into database. 
 */
public class TSServerVerticle extends AbstractVerticle
{
	public TSServerVerticle(TimeSeriesDBI dbi)
	{
		this.host = System.getProperty(
			"iot498.TSServer.host", "127.0.0.1");
		this.port = Integer.parseInt(System.getProperty(
			"iot498.TSServer.port", "8888"));
		this.dbi = dbi;
	}
	
	@Override
	public void start() throws Exception
	{
		NetServer server = vertx.createNetServer();
		
		server.connectHandler(sock -> {
			
			String sock_addr = sock.remoteAddress().toString();

			logger.info(
				"Client {}: connected",
				sock_addr);
			
			sock.handler(RecordParser.newDelimited(
				"\r\n", buf -> process_data(buf.toString())));
			
			sock.closeHandler(v -> {
				logger.info(
					"Client {}: disconnected",
					sock_addr);
			});
			
			sock.exceptionHandler(e -> {
				logger.info(
					"Client {}: exception {}",
					sock_addr, e.toString());
			});

		}).listen(port, host);
	}
	
	private void process_data(String msg)
	{
		String[] fields = msg.trim().split("\\,");
		
		if (fields.length < 3)
			return;
		
		try
		{
			String id = fields[0];
			long ts = Long.parseLong(fields[1]);
			double val = Double.parseDouble(fields[2]);
			
			dbi.insert(id, ts, val);
		}
		catch (NumberFormatException e)
		{
		}
	}

	private final String host;
	private final int port;
	private final TimeSeriesDBI dbi;
	
	private static final Logger logger
		= LoggerFactory.getLogger(TSServerVerticle.class);
}
