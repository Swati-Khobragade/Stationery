package com.gl.adminportal.stationery.controller;

import java.util.Calendar;
import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("demo")
public class DemoController {

	
	public static void main(String args[]) {
		System.out.println("Hello World");
		
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Date result = cal.getTime();
		
		System.out.println(result);
		
		
		
		
		
		
	}
	
	
	@RequestMapping(value ="/new")
	public String demoTest() {
		return "Hello World";
		
	}
}
