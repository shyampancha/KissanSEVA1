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

import com.farmsystem.backend.entity.Farmer;
import com.farmsystem.backend.entity.Order;
import com.farmsystem.backend.entity.Product;
import com.farmsystem.backend.repository.FarmerRepo;
import com.farmsystem.backend.repository.OrderRepo;
import com.farmsystem.backend.repository.ProductRepo;

@CrossOrigin
@RestController
@RequestMapping("/farmer")
public class FarmerController 
{
	@Autowired
	FarmerRepo farmerRepo;
	
	@Autowired
	OrderRepo orderRepo;
	
	@Autowired
	ProductRepo productRepo;
	
	@GetMapping("/profile/{username}")
	public Optional<Farmer> getFarmer(@PathVariable String username) {

		
		int fid = farmerRepo.findByName(username);
			          
		return farmerRepo.findById(fid);
		    
	}
	
	@PostMapping("/forgot-password")
	public String forgotPassword(@RequestBody Farmer farmer)
	{
		String username = farmer.getUser_name();
		String newpassword = farmer.getPassword();
		
		farmerRepo.updatePassword(newpassword,username);
		
		return "updated";
	}
	
	@PostMapping("/Registration")
	public String regFarmer(@RequestBody Farmer farmer) {

		System.out.println(farmer.toString());
//		    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();	
//		    String encodedPassword = passwordEncoder.encode(user.getPassword());
//		    user.setPassword(encodedPassword);
		     
		    farmerRepo.save(farmer);
		     
		    return "register_success";
		    
	}
	
	
	@PostMapping("/login")
	public String loginUser(@RequestBody Farmer farmer) {
	        
			System.out.println(farmer.getUser_name());
		
			List<Farmer> farmerList = farmerRepo.findAll();              
			
			String passMsg = "pass" ;
			String failMsg = "fail" ;
			
			for(Farmer farmerobj : farmerList )
			{
			if(farmerobj.getUser_name().equals(farmer.getUser_name()) && farmerobj.getPassword().equals(farmer.getPassword()))
				{
					
					return passMsg ;
				}
			}
		
		return failMsg;
	}
	
	
	@PostMapping("/orders")
	public List<Order> getDetails(@RequestBody Farmer farmer)
	{
		String uname = farmer.getUser_name();
		
		int fid = farmerRepo.findByName(uname);
		
		List<Order> orderList = orderRepo.findById(fid);  
		
		return orderList;
		
	}
	
	@PostMapping("/add-product")
	public String getDetails(@RequestBody Product product)
	{
		System.out.println(product.getCrop());
		String uname = product.getFarmer().getUser_name();
		
		int fid = farmerRepo.findByName(uname);
		
		product.getFarmer().setFid(fid);
		
		productRepo.save(product);
	     
	    return "register_success";
		
	}
	
	@PostMapping("/orders/change-status")
	public String getDetails(@RequestBody Order order)
	{
		System.out.println(order.getOid());
		
		int oid = order.getOid();
		
		int fid = order.getFarmer().getFid();
		
		String crop = order.getCrop_category();
		
		double quantityAvailable = productRepo.getQuantity(fid,crop);
		
		double quatitytOrdered = order.getQuantity();
		
		double quantityRemains = (quantityAvailable)-(quatitytOrdered);
		
		if(quantityRemains == 0)
		{
			productRepo.deleteQuantityCompletly(fid,crop);
		}
		else
		{
			productRepo.deductQuantity(fid,quantityRemains,crop);
		}
				
		orderRepo.changeStatus(oid);
	     
	    return "approved successfully";
		
	}
	
}
