package com.epiphan.qa.tests;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.testng.ITestContext;
import org.testng.annotations.*;

public class DataProviders {
		
	/* This should be the only important thing in this class.
	 * Builds the TestEnvironments to package data for the @Test methods in other test classes which extend
	 * SeleniumTest.
	 * Also starts the WebDrivers.
	 * TODO Think about moving the WebDriver init into a util function in TestEnvironment or SeleniumTest.
	 * TODO Make this optionally handle auth cridentials on a per-target basis.
	 */
	@DataProvider(name="env", parallel=true)
	public static Object[][] env(ITestContext context){
		
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
