package foundation.cmo.opensales.ean.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Instantiates a new m product.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MProduct {
	
	/** The status. */
	@JsonAlias("Status")
	private Long status;
	
	/** The ean. */
	@JsonAlias("Ean")
	private Long ean;
	
	/** The ncm. */
	@JsonAlias("Ncm")
	private Long ncm;
	
	/** The name. */
	@JsonAlias("Nome")
	private String name;
	
	/** The brand. */
	@JsonAlias("Marca")
	private String brand;
}
