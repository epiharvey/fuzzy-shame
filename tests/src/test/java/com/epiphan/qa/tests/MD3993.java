package com.epiphan.qa.tests;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import com.epiphan.qa.tests.util.*;

public class MD3993 {
	
	@Test(dataProviderClass=com.epiphan.qa.tests.util.DataProviders.class,dataProvider="env")
	public void doTest(TestEnvironment env){
		
		
		ITestResult result = Reporter.getCurrentTestResult();
		Reporter.log("Starting MD-3993");
		//Start driver and auth into target
		WebDriver driver = env.startDriver();
		
		Reporter.log("Started WebDriver. Acquiring target");
		driver.get("http://"+env.targetUser+":"+env.targetPass+"@"+env.targetIP+"/admin/infocfg");
		
		//Go to channel config
	    driver.findElement(By.linkText("Add channel")).click();
	    
	    //Set up channel
	    Reporter.log("Setting up channel");
	    driver.findElement(By.id("channelname")).click();
	    driver.findElement(By.name("value")).clear();
	    driver.findElement(By.name("value")).sendKeys("MD-3993\n");
	    new Select(driver.findElement(By.id("videosource"))).selectByVisibleText("HDMI-A");
	    driver.findElement(By.name("audio_D2P280290.analog-a")).click();
	    driver.findElement(By.cssSelector("input.apply_button")).click();
	    
	    //Visit preview page
	    Reporter.log("Checking preview");
	    WebElement e = driver.findElement(By.linkText("MD-3993"));
	    String channel = StringUtils.split(e.getAttribute("id"),'_')[2];
	    driver.findElement(By.linkText("Status")).click();
	    driver.findElement(By.linkText("http://192.168.114.117/preview.cgi?channel="+channel)).click();
	    Assert.assertTrue(StringUtils.endsWith(driver.getTitle(), ":: MD-3993"));
	    
	    //Delete channel
	    Reporter.log("Deleting channel");
		driver.get("http://"+env.targetUser+":"+env.targetPass+"@"+env.targetIP+"/admin/infocfg");
	    driver.findElement(By.id("menu_channel_"+channel)).click();
	    driver.findElement(By.cssSelector("#fn_delete > input[type=\"submit\"]")).click();
	    driver.switchTo().alert().accept();
	    
	    //Cleanup
	    driver.quit();
	    Reporter.log("Test Successful. Exiting");
	}
}
