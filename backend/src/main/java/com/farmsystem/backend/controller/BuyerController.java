package com.farmsystem.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmsystem.backend.entity.Buyer;
import com.farmsystem.backend.entity.BuyerCart;
import com.farmsystem.backend.entity.Farmer;
import com.farmsystem.backend.entity.Order;
import com.farmsystem.backend.entity.Product;
import com.farmsystem.backend.repository.BuyerCartRepo;
import com.farmsystem.backend.repository.BuyerRepo;
import com.farmsystem.backend.repository.FarmerRepo;
import com.farmsystem.backend.repository.OrderRepo;
import com.farmsystem.backend.repository.ProductRepo;

@CrossOrigin
@RestController
@RequestMapping("/buyer")
public class BuyerController {

	@Autowired
	BuyerRepo buyerRepo;
	
	@Autowired
	OrderRepo orderRepo;
	
	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	BuyerCartRepo buyercartRepo;
	
	@Autowired
	FarmerRepo farmerRepo;
	
	
	
	@PostMapping("/login")
	public String loginUser(@RequestBody Buyer buyer) {
	        
			System.out.println(buyer.getPassword());
			System.out.println(buyer.getUser_name());
			List<Buyer> buyerList = buyerRepo.findAll();              
			
			String passMsg = "pass" ;
			String failMsg = "fail" ;
			
			for(Buyer buyerobj : buyerList )
			{
			if(buyerobj.getUser_name().equals(buyer.getUser_name()) && buyerobj.getPassword().equals(buyer.getPassword()))
				{
					
					return passMsg ;
				}
			}
		
		return failMsg;
	}
	
	@PostMapping("/Registration")
	public String regFarmer(@RequestBody Buyer buyer) {

		     
		    buyerRepo.save(buyer);
		     
		    return "register_success";
		    
	}
	
	@PostMapping("/search")
	public List<Product> searchProduct(@RequestBody Product prod) {
	        
			String item = prod.getCrop();
			List<Product> productList = productRepo.findProduct(item);              
		
		return productList;
	}
	
	@PostMapping("/allsearch")
	public List<Product> searchProduct() {
	        
			
			List<Product> productList = productRepo.findAll();              
		
		return productList;
	}
	
	
	@PostMapping("/myCart")
	public List<BuyerCart> buyerCart() {
	        
			
		List<BuyerCart> cartlist = buyercartRepo.findAll();              
		
		return cartlist;
	}
	
	//http://localhost:9099/buyer/confirmed-orders
		
		@PostMapping("/confirmed-orders")
	public List<Order> buyerCart(@RequestBody Buyer buyer) {
	        
			System.out.println(buyer.getUser_name());
			String uname = buyer.getUser_name();
			
			int bid = buyerRepo.findByName(uname);
			
			List<Order> orderList = orderRepo.findByBId(bid);  
			
			return orderList;
		
	}
		
		@PostMapping("/addOrder")
		public String regFarmer(@RequestBody Order order) {
				
			String buyeruname = order.getBuyer().getUser_name();
			
			System.out.println(buyeruname);
			int bid = buyerRepo.findByName(buyeruname);
				order.getBuyer().setBid(bid);
				
				String farmername = order.getFarmer().getFirstname();
				System.out.println(farmername);
				int fid = farmerRepo.findByFid(farmername);
				order.getFarmer().setFid(fid);
			    orderRepo.save(order);
			     
			    return "added";
			    
		}
		
		@GetMapping("/profile/{username}")
		public Optional<Buyer> getBuyer(@PathVariable String username) {

			
			int bid = buyerRepo.findByName(username);
				          
			return buyerRepo.findById(bid);
			    
		}
		
}
