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
		
		Reporter.log("Starting MD-3993 on ["+env.browser+"] under ["+env.platform+"]");
		Reporter.log("targetIP: "+env.targetIP);
		Reporter.log("hubIP: "+env.hubIP);
		Reporter.log("browser: "+env.browser);
		ITestResult result = Reporter.getCurrentTestResult();
		
		//Start driver and auth into target
		WebDriver driver = env.startDriver();
		WebElement el;
		
		Reporter.log("Log in as: "+env.targetUser+":"+env.targetPass);
		driver.get("http://"+env.targetUser+":"+env.targetPass+"@"+env.targetIP+"/admin/infocfg");
		
		//Go to channel config
	    el = driver.findElement(By.linkText("Add channel"));
	    Assert.assertNotNull(el, "Could not add channel. Button missing?");
	    el.click();
	    	    
	    //Set up channel
	    Reporter.log("Setting up channel");
	    driver.findElement(By.id("channelname")).click();
	    driver.findElement(By.name("value")).clear();
	    driver.findElement(By.name("value")).sendKeys("MD-3993\n");
	    driver.findElement(By.cssSelector("[data-ember-action=\"376\"]")).click();
	    driver.findElement(By.cssSelector(".ember-view.new-source-button")).click();
	    driver.findElement(By.cssSelector("a.select2-choice b[role=\"presentation\"]")).click();
	    driver.findElement(By.cssSelector("ul.select2-results li:first-child")).click();
	    driver.findElement(By.cssSelector(".button.save-button")).click();
	    Reporter.log("Channel successfully created");
	    
	    //Visit preview page
	    Reporter.log("Checking preview");
	    String channel = StringUtils.split(driver.getCurrentUrl(),'/')[3];
	    String channum = channel.substring(channel.length()-1);
	    driver.findElement(By.linkText("Status")).click();
	    driver.findElement(By.linkText("http://"+env.targetIP+"/preview.cgi?channel="+channum)).click();
	    Assert.assertTrue(StringUtils.endsWith(driver.getTitle(), ":: MD-3993"));
	    
	    //Delete channel
	    Reporter.log("Deleting channel");
		driver.get("http://"+env.targetUser+":"+env.targetPass+"@"+env.targetIP+"/admin/infocfg");
	    driver.findElement(By.id("menu_channel_"+channum)).click();
	    driver.findElement(By.cssSelector("#fn_delete > input[type=\"submit\"]")).click();
	    driver.switchTo().alert().accept();
	    
	    //Cleanup
	    driver.quit();
	    Reporter.log("Test Successful. Exiting");
	}
}
