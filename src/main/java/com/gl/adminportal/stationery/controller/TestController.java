package com.gl.adminportal.stationery.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gl.adminportal.stationery.model.Test;
import com.gl.adminportal.stationery.service.TestService;

@RestController
public class TestController {
	
	@Autowired
	TestService testService;
	
	@RequestMapping(value = "/addTest", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<Void> addTest(@RequestBody Test test) {	
		if(test.getId()== null)
		{
			testService.save(test);
		}
		else
		{	
			testService.update(test);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/getTest", method = RequestMethod.GET)
	public ResponseEntity<String> getTest() {	
		return new ResponseEntity<String>("Sample",
				HttpStatus.OK);
	}
/*
	@RequestMapping(value = "/updateCountry/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public String updateCountry(@PathVariable("id") int id,Model model) {
		 model.addAttribute("country", this.testService.getCountry(id));
	        model.addAttribute("listOfCountries", this.testService.getAllCountries());
	        return "countryDetails";
	}

	@RequestMapping(value = "/deleteCountry/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public String deleteCountry(@PathVariable("id") int id) {
		testService.deleteCountry(id);
		 return "redirect:/getAllCountries";

	}	*/
}


