package com.epiphan.qa.tests;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

public class SeleniumTest {
	
	//TestEnvironment is a semi-flexible way to pass information to each @Test method 
	class TestEnvironment{
		WebDriver driver;
		String hubIP;
		String targetIP;
		String targetUser;
		String targetPass;
		String browser;
		
		public TestEnvironment(String hip, String tip, String tun, String tpw, String brw){
			hubIP = hip;
			targetIP = tip;
			targetUser = tun;
			targetPass = tpw;
			browser = brw;
			driver = null;
		}
		
		//for starting a new WebDriver with this environments specs
		WebDriver startDriver(){
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
	
	/* This should be the only important thing in this class.
	 * Builds the TestEnvironments to package data for the @Test methods in other test classes which extend
	 * SeleniumTest.
	 * Also starts the WebDrivers.
	 * TODO Think about moving the WebDriver init into a util function in TestEnvironment or SeleniumTest.
	 * TODO Make this optionally handle auth cridentials on a per-target basis.
	 */
	@DataProvider(name="env", parallel=true)
	public Object[][] env(ITestContext context){
		
		ArrayList<Object[]> envs = new ArrayList<Object[]>();
		
		String hubIP = context.getCurrentXmlTest().getParameter("hubIP");
		String[] targetIP = StringUtils.split(context.getCurrentXmlTest().getParameter("targetIP"), ' ');
		String targetUser = context.getCurrentXmlTest().getParameter("targetUser");
		String targetPass = context.getCurrentXmlTest().getParameter("targetPass");
		String[] browser = StringUtils.split(context.getCurrentXmlTest().getParameter("browser"), ' ');
		
		for(String ip : targetIP){
			for(String bsr : browser){
				Object[] o = {new TestEnvironment(hubIP, ip, targetUser, targetPass, bsr)};
				envs.add(o);
				//TestEnvironment env = new TestEnvironment(hubIP, targetIP, targetUser, targetPass, browser);
			}
		}
		
		Object[][] ret = new Object[envs.size()][];
		envs.toArray(ret);
		return ret;
	}
	
	
}
