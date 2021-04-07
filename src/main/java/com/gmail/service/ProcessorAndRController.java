package com.gmail.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmail.service.ServiceApplication.Domain;

@RestController
public class ProcessorAndRController {
	int cont=0;
	int result=0;
	boolean isRunning=false;
	@GetMapping("/showStatus")
	public String status() {
		//this will emit
		if(errOccurred)
			return errOccurredMessage;
		else if(isCompleted)
			return "processing completed";
		
		return "processing "+result+" yet <br><br> current domain "+statusCurrentDomain+"<br><br> current url "+statusCurrentURL+""
				+ "<br><br> processed urls "+statusProcessedURLs+"<br><br> current link set "+statusLINKSet
				+" <br><br> linkset size "+statusLinkSetSize+" <br><br> processed size "+statusProcessedSetSize;
	}
	
	@GetMapping("/res")
	public String res() {
		try {
			Scanner scanner=new Scanner(new File("data.txt"));
			String reString="";
			while(scanner.hasNextLine())
				reString+=scanner.nextLine()+"<br>";
			return reString;
		}
		catch (Exception e) {
			return e.getMessage();
		}
		
	}
	
	@GetMapping("/start")
	public String start() {
		//this will emit
		if(!isRunning) {
			Thread thread=new Thread(new Runnable() {
				
				@Override
				public void run() {
					process();
				}
			});
			thread.start();
		}
		return result+"";
	}
	
	
	
	
	
	
	boolean errOccurred=false;
	String errOccurredMessage="";
	
	boolean isCompleted=false;
	
	String statusCurrentDomain="null";
	String statusCurrentURL="null";
	
	String statusProcessedURLs="null";
	String statusLINKSet="null";
	
	int statusLinkSetSize=0;
	int statusProcessedSetSize=0;
	
	void process() {
		try {
			isRunning=true;
			Stack<String> domains=LoadSources();
			System.out.println(domains.size());
			
			while(!domains.isEmpty()) {
				
				result++;
				String currentDomain=domains.pop();  statusCurrentDomain=currentDomain;
				Object response[]=extracthrefsAndTitle(currentDomain, currentDomain);
				TreeSet<String> LinksSet=(TreeSet<String>) response[1];
				String baseTitle=(String)response[0];
				
				TreeSet<Domain> urlAndTitleSet=new TreeSet<>();
				urlAndTitleSet.add(new Domain("http://"+currentDomain, baseTitle));
				
				TreeSet<String> processedURLs=new TreeSet<>();
				processedURLs.add("http://"+currentDomain);
				
				System.out.println("starting with current domain as "+currentDomain);
				System.out.println("stating link set is "); displayList(LinksSet);
				
				while(!LinksSet.isEmpty()) {
					String currentURL=LinksSet.pollFirst(); statusCurrentURL=currentURL;
					//System.out.println("to be processed "+currentURL);
					//System.out.println("After removal "+LinksSet);
					if(!processedURLs.add(currentURL)) {
						System.out.println("processed already");
						continue;
					}
						
					Object res[]=extracthrefsAndTitle(currentURL, currentDomain);
					String title=(String)res[0];
					urlAndTitleSet.add(new Domain(currentURL, title));
					//System.out.println("result after calling "+res[1]);
					Set<String> resSet=(Set<String>) res[1];
					for(String s:resSet) {
						if(processedURLs.contains(s)) {
							continue;
						}
						LinksSet.add(s);
					}
					//System.out.println("Links set after addition ");displayList(LinksSet);
					//System.out.println("processed ulrs ");displayList(processedURLs);
					statusLINKSet=LinksSet+"";
					statusProcessedURLs=processedURLs+"";
					statusLinkSetSize=LinksSet.size();
					
					statusProcessedSetSize=processedURLs.size();
					//break;
				}
				saveData(urlAndTitleSet);
				
				
			}
			isCompleted=true;
		}
		catch (Exception e) {
			errOccurred=true;
			errOccurredMessage=e.getMessage();
			e.printStackTrace();
		}
	}
	
	
	static Object[] extracthrefsAndTitle(String url,String currentDomain) throws MalformedURLException {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		RemoteWebDriver driver = new RemoteWebDriver(
				new URL("http://headlesschromet.azurewebsites.net/wd/hub"),
				options);
		if(url.contains("http://") || url.contains("https://"))
			driver.get(url);
		else
			driver.get("http://"+url);
			
		List<WebElement> webElements=driver.findElements(By.tagName("a"));
		String title=driver.getTitle();
		TreeSet<String> set=new TreeSet<>();
		for(WebElement element:webElements) {
			if(element.getAttribute("href")!=null && getCleanedURL(element.getAttribute("href")).equals(currentDomain))
				set.add(element.getAttribute("href"));
		}
		webElements.clear();
		driver.close();
		
		Object response[]=new Object[2];
		response[0]=title;
		response[1]=set;
		
		return response;
	}
	
	static void saveData(TreeSet<Domain> set) throws IOException {
		FileWriter fw=new FileWriter(new File("data.txt"),true);
		for(Domain domain:set) {
			String data=domain.urlString+"\\//$\\//"+domain.titleString;
			fw.append("\n"+data);
			
		}
		fw.close();
		
	}
	
	Stack<String> LoadSources() throws FileNotFoundException {
		Scanner sc=new Scanner(new File("src.txt"));
		Stack<String> stack=new Stack<>();
		while(sc.hasNextLine())
			stack.add(sc.nextLine());
		sc.close();
		return stack;
	}
	static void displayList(TreeSet<String> set) {
		for(String s:set) {
			System.out.println(s);
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

	static String getCleanedURL(String url) {
		int index = url.indexOf("//");
		url = url.substring(index + 2, url.length());

		if (url.substring(0, 3).equals("www")) {
			url = url.substring(4, url.length());
		}

		try {
			int index2 = url.indexOf("/");
			url = url.substring(0, index2);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return url;
	}
}
