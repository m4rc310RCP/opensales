package foundation.cmo.opensales.ean.services;

import org.springframework.beans.factory.annotation.Autowired;

import foundation.cmo.opensales.ean.dto.MProduct;

public class MEanService{
	
	@Autowired
	private MEanCache cache;
	
	public MProduct getProduct( Long ean) {
		return cache.getProductCache(ean);
	}
	
	public String getProductImage(MProduct product) {
		return cache.getImage(product);
	}
}
