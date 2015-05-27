package com.epiphan.qa.tests.util;

import java.util.ArrayList;
import java.util.Properties;

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
	public static Object[][] env(){

		ArrayList<Object[]> envs = new ArrayList<Object[]>();

		Properties p = System.getProperties();
		//System.out.println(p.toString());
		String hubIP = p.getProperty("hubIP");
		String[] targetIP = StringUtils.split(p.getProperty("targetIP"), ',');
		String targetUser = p.getProperty("targetUser");
		String targetPass = p.getProperty("targetPass");
		String[] browser = StringUtils.split(p.getProperty("targetBrowser"), ',');
		String[] os	= StringUtils.split(p.getProperty("targetOS"), ',');

		for(String ip : targetIP){
			for(String bsr : browser){
				for(String s : os){
					Object[] o = {new TestEnvironment(hubIP, ip, targetUser, targetPass, bsr, s)};
					envs.add(o);
				}
			}
		}

		Object[][] ret = new Object[envs.size()][];
		envs.toArray(ret);
		return ret;
	}

	@Test(dataProvider="env")
	public void selfTest(TestEnvironment e){
		DataProviders.env();
	}


}
