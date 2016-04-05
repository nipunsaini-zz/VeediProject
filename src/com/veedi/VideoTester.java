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
		waitForElementToBeInvisible(By.id("preloader"), 10);
		List<WebElement> frames = driver.findElement(By.id("adContainer")).findElements(By.tagName("iframe"));
		for(WebElement frame : frames){
			driver.switchTo().frame(frame);
			if(driver.getPageSource().contains("Ad will close in")){
				System.out.println("found frame");
				waitForElementToPresent(By.xpath("//button[contains(text(),'Skip Ad')]"), 10).click();
				break;
			}			
		}		
		driver.switchTo().defaultContent();
		new WebDriverWait(driver, 20).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("Veediframe")));		
	}
	
	@Test
	public void testVideoPlay() throws TestException{		
		Assert.assertTrue(isPlaying());		
	}
	
	@Test 
	public void testProgressBar() throws TestException{
		String before = "";
		String after = "";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement pause = driver.findElement(By.id("control-pause"));
		new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(pause));
		if(pause.isDisplayed()){
			System.out.println("pause visible");
			sleep(2000);
			js.executeScript("document.getElementById(arguments[0]).pause()", "videoTag");
			sleep(2000);
			js.executeScript("document.getElementById(arguments[0]).play()", "videoTag");	
			before = driver.findElement(By.id("timeline-played")).getCssValue("width").split("px")[0].trim();
			sleep(3000);
			waitForElementToBeInvisible(By.id("preloader"), 10);
			after = driver.findElement(By.id("timeline-played")).getCssValue("width").split("px")[0].trim();
			System.out.println(before);
			System.out.println(after);
		}else{
			throw new TestException("video player not visible");
		}		
		Assert.assertTrue(!before.equals(after));
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
	
	private  boolean isPlaying() throws TestException {
		String beforeTime="",afterTime="";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement pause = driver.findElement(By.id("control-pause"));
		new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(pause));
		if(pause.isDisplayed()){
			System.out.println("pause visible");
			sleep(2000);
			js.executeScript("document.getElementById(arguments[0]).pause()", "videoTag");
			sleep(2000);
			js.executeScript("document.getElementById(arguments[0]).play()", "videoTag");	
			beforeTime = driver.findElement(By.id("current-time-indicator")).getText();
			sleep(3000);
			waitForElementToBeInvisible(By.id("preloader"), 10);
			afterTime = driver.findElement(By.id("current-time-indicator")).getText();
			System.out.println(beforeTime);
			System.out.println(afterTime);
		}else{
			throw new TestException("video player not visible");
		}		
		return !beforeTime.equals(afterTime);
		
	}
	
	private WebElement waitForElementToPresent(By by, long seconds){
		return new WebDriverWait(driver,seconds).until(ExpectedConditions.visibilityOfElementLocated(by));
	}
	
	private void waitForElementToBeInvisible(By by, long seconds){
		new WebDriverWait(driver,seconds).until(ExpectedConditions.invisibilityOfElementLocated(by));
	}

}
