package com.gmail.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class emitter {
	@GetMapping("/test")
	public String test() {
		return "how/";
	}
	
	@GetMapping("/emitter")
	@CrossOrigin(origins ="http://localhost:10")
	public SseEmitter eventEmitter() {
	
	   SseEmitter emitter = new SseEmitter(); //12000 here is the timeout and it is optional   
	
	   
	
	   //create a single thread for sending messages asynchronously
	
	   ExecutorService executor = Executors.newSingleThreadExecutor();
	
	   executor.execute(() -> {
	
	       try {
	
	           for (int i = 0; i < 4; i++) {
	
	              emitter.send("message" + i);           
	
	           }       
	
	       } catch(Exception e) {
	
	            emitter.completeWithError(e);       
	
	       } finally {
	
	            emitter.complete();       
	
	       }   
	
	   });
	
	   executor.shutdown();
	
	   return emitter;
	
	}
}
