
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.callback.Callback;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ParallelProcessing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String arr[] = {""};
		seriesExecution(arr);
		
		try {
			//parallelExecution(arr);
		}
		catch (Exception e) {
			System.out.println("execution completed ");
		}
		
	}
	static ConcurrentHashMap<Integer,Integer> hm=new ConcurrentHashMap<>();
	
	static void prallelExecutionDifference(long mills) {
		System.out.println(System.currentTimeMillis()-mills);
		throw new ArrayIndexOutOfBoundsException();
	}
	static void parallelExecution(String arr[]) {
		ConcurrentHashMap<Integer,Integer> hm=new ConcurrentHashMap<>();
		long beforExecution=System.currentTimeMillis();
		hm.put(1,0);
		int cont=0;
		for(String url:arr) {
			//if(ind==5)break;ind++;
			
			cont++;
			Thread t1=new Thread(new Runnable() {
				
				@Override
				public void run(){
					// TODO Auto-generated method stub
					try {

						ChromeOptions options = new ChromeOptions();
						options.addArguments("--no-sandbox");
						options.addArguments("--disable-dev-shm-usage");
						RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),
								options);
						driver.get(url);
						LocalDateTime now2 = LocalDateTime.now();
						System.out.println(driver.getTitle());
						hm.put(1,hm.get(1)+1);
						
						if(hm.get(1)==30) {
							prallelExecutionDifference(beforExecution);
						}
						driver.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});t1.start();
			
		}
		
	}
	static long seriesExecution(String arr[]) {
		long beforExecution=System.currentTimeMillis();
		for(String url:arr) {
			try {

				ChromeOptions options = new ChromeOptions();
				options.addArguments("--no-sandbox");
				options.addArguments("--disable-dev-shm-usage");
				RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),
						options);
				driver.get(url);
				LocalDateTime now2 = LocalDateTime.now();
				System.out.println(driver.getTitle());
				driver.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		
		return System.currentTimeMillis()-beforExecution;
	}
	

}

/*
 * 
 
 System.out.println(arr.length);
		List<Callable<Void>> callablesList=new ArrayList<>();
		List<String> timeStamps=Collections.synchronizedList(new ArrayList<String>());
		int ind=0;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now2 = LocalDateTime.now();
		System.out.println("Before Execution "+dtf.format(now2));
		String afterReString[]=new String[1];
		hm.put(1,0);
		for(String url:arr) {
			//if(ind==5)break;ind++;
			Thread t1=new Thread(new Runnable() {
				
				@Override
				public void run(){
					// TODO Auto-generated method stub
					try {

						ChromeOptions options = new ChromeOptions();
						options.addArguments("--no-sandbox");
						options.addArguments("--disable-dev-shm-usage");
						RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),
								options);
						driver.get(url);
						//timeStamps.add(driver.getTitle());
						LocalDateTime now2 = LocalDateTime.now();
						afterReString[0]=dtf.format(now2);
						System.out.println(driver.getTitle());
						hm.put(1,hm.get(1)+1);
						if(hm.get(1)==30) {
							System.out.println("After execution "+afterReString[0]);
						}
						driver.close();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});t1.start();
			
		}
		
		
		
		
 Callable<Void> callable=new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					// TODO Auto-generated method stub
					ChromeOptions options = new ChromeOptions();
					options.addArguments("--no-sandbox");
					options.addArguments("--disable-dev-shm-usage");
					RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),
							options);
					driver.get(url);
					timeStamps.add(driver.getTitle());
					System.out.println(driver.getTitle());
					driver.close();
					return null;
				}
			
			};
			callablesList.add(callable);
			
			ExecutorService executor = Executors.newFixedThreadPool(arr.length);
		try {
			executor.invokeAll(callablesList);
			System.out.println("completed");
			System.out.println(timeStamps.size());
		}
		catch (Exception e) {
			// TODO: handle exception
		}
 */
