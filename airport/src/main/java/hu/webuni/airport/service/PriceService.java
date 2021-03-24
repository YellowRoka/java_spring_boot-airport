package hu.webuni.airport.service;

import org.springframework.stereotype.Service;

@Service
public class PriceService {
	
	private DiscountService discountService;
	
	
	public PriceService(DiscountService discountService) {
		//super();
		this.discountService = discountService;
	}


	//@Autowired
	public int getFinalPrice(int price) {
		return (int)(price/100.0 * (100 - discountService.getDiscountPercent(price)));
	}
}