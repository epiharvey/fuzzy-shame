package com.epiphan.qa.tests;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import com.epiphan.qa.tests.util.*;


public class LoginTest {
	
	@Test(dataProviderClass=com.epiphan.qa.tests.util.DataProviders.class,dataProvider="env")
	public void logIn(TestEnvironment e){
		WebDriver d;
		
		System.out.println("Beginning Log In Test");
		d = e.startDriver();
		System.out.println("Target IP is \""+e.targetIP+"\"");
		//d.get("http://"+e.targetUser+":"+e.targetPass+"@"+e.targetIP+"/admin");
		d.get("http://google.ca");
		d.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Connected to target");
		System.out.println("Page title is \""+d.getTitle()+"\"");
		d.quit();
	}
	
}
