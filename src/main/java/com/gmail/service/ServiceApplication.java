package com.gmail.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ServiceApplication {
	
	public static void main(String[] args) throws MalformedURLException {
		
		/*TreeSet<Domain> treeSet=new TreeSet<>();
		
		treeSet.add(new Domain("asd","asd"));
		treeSet.add(new Domain("aaa","asdjh"));
		for(Domain a:treeSet) {
			System.out.println(a.urlString+' '+a.titleString);
		}
		try {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
			RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),
					options);
			PriorityQueue<String> urlQueue=new PriorityQueue<>();
			driver.get("https://news.google.com/topstories?hl=en-IN&gl=IN&ceid=IN:en");
			//System.out.println(driver.findElement(By.tagName("html")).getText());
			
			List<WebElement> webElement=driver.findElements(By.tagName("a"));
			
			displayList(webElement);
		}
		catch (Exception e) {
			// TODO: handle exception
			
		} */
		SpringApplication.run(ServiceApplication.class, args);
	}
	
	
	static void displayList(List<WebElement> elementsList) {
		for(WebElement element:elementsList) {
			System.out.println(element.getAttribute("href"));
		}
	}
	
	static class Domain implements Comparable<Domain>{
		String urlString;String titleString;
		public Domain(String urlString,String titleString) {
			this.urlString=urlString;this.titleString=titleString;
		}
		@Override
		public int compareTo(Domain o) {
			if(this.urlString.equals(o.urlString))
				return 0;
			else if(this.urlString.compareTo(o.urlString)>0) 
				return 1;
			else 
				return -1;
		}
	}
}


/**
 
  	
		
		//
		 * 
  
 
 */

