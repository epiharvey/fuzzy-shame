package com.epiphan.qa.tests;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.*;

public class MD3993 {
	
	@Test(dataProviderClass=DataProviders.class,dataProvider="env")
	public void doTest(TestEnvironment env){
		
		//Start driver and auth into target
		WebDriver driver = env.startDriver();
		driver.get("http://"+env.targetUser+":"+env.targetPass+"@"+env.targetIP+"/admin/infocfg");
		
		//Go to channel config
	    driver.findElement(By.linkText("Add channel")).click();
	    
	    //Set up channel
	    driver.findElement(By.id("channelname")).click();
	    driver.findElement(By.name("value")).clear();
	    driver.findElement(By.name("value")).sendKeys("MD-3993\n");
	    new Select(driver.findElement(By.id("videosource"))).selectByVisibleText("HDMI-A");
	    driver.findElement(By.name("audio_D2P280290.analog-a")).click();
	    driver.findElement(By.cssSelector("input.apply_button")).click();
	    
	    //Visit preview page
	    WebElement e = driver.findElement(By.linkText("MD-3993"));
	    String channel = StringUtils.split(e.getAttribute("id"),'_')[2];
	    driver.findElement(By.linkText("Status")).click();
	    driver.findElement(By.linkText("http://192.168.114.117/preview.cgi?channel="+channel)).click();
	    Assert.assertTrue(StringUtils.endsWith(driver.getTitle(), ":: MD-3993"));
	    
	    //Delete channel
		driver.get("http://"+env.targetUser+":"+env.targetPass+"@"+env.targetIP+"/admin/infocfg");
	    driver.findElement(By.id("menu_channel_"+channel)).click();
	    driver.findElement(By.cssSelector("#fn_delete > input[type=\"submit\"]")).click();
	    driver.switchTo().alert().accept();
	    
	    //Cleanup
	    driver.quit();
	}
}
