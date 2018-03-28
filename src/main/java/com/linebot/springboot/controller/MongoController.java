package com.linebot.springboot.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.linebot.springboot.model.Customer;
import com.linebot.springboot.repository.CustomerRepository;

@RestController
public class MongoController {

	@Autowired
	private CustomerRepository repository;
	
    @RequestMapping(value = "/mongo/list")
    @ResponseBody
    public List<Customer> findList() {
    	List<Customer> result = new ArrayList<>();
    	for (Customer customer : repository.findAll()) {
			result.add(customer);
		}
        return result;
    }
    
    @RequestMapping(value = "/mongo/put")
    @ResponseBody
    public boolean put() {
    	repository.save(new Customer("Alice", "Smith"));
		repository.save(new Customer("Bob", "Smith"));
        return true;
    }

    @RequestMapping(value = "/mongo/deleteAll")
    @ResponseBody
    public boolean deleteAll() {
    	repository.deleteAll();
        return true;
    }
    
    @RequestMapping(value = "/mongo/findAll")
    @ResponseBody
    public List<Customer> findAll() {
        return repository.findAll();
    }
    
    @RequestMapping(value = "/mongo/findbyFirstName")
    @ResponseBody
    public Customer findbyFirstName(@RequestParam(name = "firstName") String firstName) {
        return repository.findByFirstName(firstName);
    }
    
    @RequestMapping(value = "/mongo/findbyLastName")
    @ResponseBody
    public Customer findbyLastName(@RequestParam(name = "lastName") String lastName) {
        return repository.findByFirstName(lastName);
    }
    
    
}
