package foundation.cmo.opensales.cups.services;

import java.util.List;

import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The Class MCupsService.
 */
@Component
public class MCupsService {

	/** The port. */
	@Value("${cmo.foundation.cups.server.port}")
	private int port;
	
	/** The server ip. */
	@Value("${cmo.foundation.cups.server.ip}")
	private String serverIp;

	/**
	 * List printers.
	 *
	 * @return the list
	 * @throws Exception the exception
	 */
	public List<CupsPrinter> listPrinters() throws Exception {
		CupsClient cupsClient = new CupsClient(serverIp, port);
		return cupsClient.getPrinters();
	}
	
	/**
	 * Gets the printer defaut.
	 *
	 * @return the printer defaut
	 * @throws Exception the exception
	 */
	public CupsPrinter getPrinterDefaut() throws Exception {
		CupsClient cupsClient = new CupsClient(serverIp, port);
		return cupsClient.getDefaultPrinter();
	}
	
}
