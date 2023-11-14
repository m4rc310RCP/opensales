package foundation.cmo.opensales.cups.services;

import java.util.List;

import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MCupsService {

	@Value("${cmo.foundation.cups.server.port}")
	private int port;
	@Value("${cmo.foundation.cups.server.ip}")
	private String serverIp;

	public List<CupsPrinter> listPrinters() throws Exception {
		CupsClient cupsClient = new CupsClient(serverIp, port);
		return cupsClient.getPrinters();
	}
	
	public CupsPrinter getPrinterDefaut() throws Exception {
		CupsClient cupsClient = new CupsClient(serverIp, port);
		return cupsClient.getDefaultPrinter();
	}
	
}
