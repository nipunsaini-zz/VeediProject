package com.veedi;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VideoTester {
	WebDriver driver;
	
	@Before
	public void setup(){
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.get("http://www.veedi.com/player_test/tests/qa/main.html");
		sleep(5000);
		new WebDriverWait(driver, 20).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("Veediframe")));
		waitForElementToPresent(By.id("adContainer"),10);
		waitForElementToPresent(By.id("preloader"), 10);
		List<WebElement> frames = driver.findElement(By.id("adContainer")).findElements(By.tagName("iframe"));
		for(WebElement frame : frames){
			driver.switchTo().frame(frame);
			if(driver.getPageSource().contains("Ads by Google")){
				System.out.println("found frame");
				waitForElementToBeInvisible(By.className("fullslot-attribution-button"),20);	
				break;
			}			
		}
		
		driver.switchTo().defaultContent();
		new WebDriverWait(driver, 20).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("Veediframe")));
		
	}
	
	@Test 
	public void testVideoPlay(){		
		Assert.assertTrue(isPlaying());		
	}
	
	@Test @Ignore
	public void testTimeline(){
		
	}
	
	@After
	public void tearDown(){
		driver.close();
		driver.quit();
	}
	
	private void sleep(long ms){
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private  boolean isPlaying() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.getElementById(arguments[0]).pause()", "videoTag");
		sleep(3000);
		js.executeScript("document.getElementById(arguments[0]).play()", "videoTag");	
		double beforeTimelineWidth = Double.parseDouble(driver.findElement(By.id("timeline-played")).getCssValue("width").split("px")[0].trim());
		sleep(3000);
		double afterTimelineWidth = Double.parseDouble(driver.findElement(By.id("timeline-played")).getCssValue("width").split("px")[0].trim());
		System.out.println(beforeTimelineWidth);
		System.out.println(afterTimelineWidth);
		return beforeTimelineWidth != afterTimelineWidth;
		
	}
	
	private void waitForElementToPresent(By by, long seconds){
		new WebDriverWait(driver,seconds).until(ExpectedConditions.visibilityOfElementLocated(by));
	}
	
	private void waitForElementToBeInvisible(By by, long seconds){
		new WebDriverWait(driver,seconds).until(ExpectedConditions.invisibilityOfElementLocated(by));
	}

}
