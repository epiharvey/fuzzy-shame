package com.epiphan.qa.tests.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
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
	public String platform;
	
	public TestEnvironment(String hip, String tip, String tun, String tpw, String brw, String s){
		hubIP = hip;
		targetIP = tip;
		targetUser = tun;
		targetPass = tpw;
		browser = brw;
		platform = s;
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
		switch (platform){
		case "linux":
			capabilities.setPlatform(Platform.LINUX);
			break;
		case "windows":
			capabilities.setPlatform(Platform.WINDOWS);
			break;
		case "mac":
			capabilities.setPlatform(Platform.MAC);
			break;
		default:
			capabilities.setPlatform(Platform.LINUX);
			break;
				
		}
		System.out.println("Connecting to hub at "+hubIP);
		driver = new RemoteWebDriver(hubUrl, capabilities);
		System.out.println("Connected");
		this.driver = driver;
		return driver;
	}
}
