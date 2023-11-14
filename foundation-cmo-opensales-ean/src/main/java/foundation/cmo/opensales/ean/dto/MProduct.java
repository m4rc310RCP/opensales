package foundation.cmo.opensales.ean.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MProduct {
	@JsonAlias("Status")
	private Long status;
	
	@JsonAlias("Ean")
	private Long ean;
	
	@JsonAlias("Ncm")
	private Long ncm;
	
	@JsonAlias("Nome")
	private String name;
	
	@JsonAlias("Marca")
	private String brand;
}
