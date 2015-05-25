package com.epiphan.qa.tests.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TestEnvironment{
	WebDriver driver;
	public String hubIP;
	public String targetIP;
	public String targetUser;
	public String targetPass;
	public String browser;
	
	public TestEnvironment(String hip, String tip, String tun, String tpw, String brw){
		hubIP = hip;
		targetIP = tip;
		targetUser = tun;
		targetPass = tpw;
		browser = brw;
		driver = null;
	}
	
	//for starting a new WebDriver with this environments specs
	public WebDriver startDriver(){
		URL hubUrl;
		WebDriver driver;
		DesiredCapabilities capabilities;
		try {
			hubUrl = new URL("http://"+hubIP+"/wd/hub");
		} catch (MalformedURLException e) {
			System.out.println("Unable to create WebDriver: Bad hub address");
			e.printStackTrace();
			return null;
		}
		capabilities = new DesiredCapabilities();
		capabilities.setCapability("browserName", browser);
		System.out.println("Connecting to hub at "+hubIP);
		driver = new RemoteWebDriver(hubUrl, capabilities);
		System.out.println("Connected");
		this.driver = driver;
		return driver;
	}
}
