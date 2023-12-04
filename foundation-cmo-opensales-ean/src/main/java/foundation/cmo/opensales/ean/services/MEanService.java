package foundation.cmo.opensales.ean.services;

import org.springframework.beans.factory.annotation.Autowired;

import foundation.cmo.opensales.ean.dto.MProduct;

/**
 * The Class MEanService.
 */
public class MEanService{
	
	/** The cache. */
	@Autowired
	private MEanCache cache;
	
	/**
	 * Gets the product.
	 *
	 * @param ean the ean
	 * @return the product
	 */
	public MProduct getProduct( Long ean) {
		return cache.getProductCache(ean);
	}
	
	/**
	 * Gets the product image.
	 *
	 * @param product the product
	 * @return the product image
	 */
	public String getProductImage(MProduct product) {
		return cache.getImage(product);
	}
}
