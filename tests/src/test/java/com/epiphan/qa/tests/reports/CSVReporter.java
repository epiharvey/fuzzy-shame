package com.epiphan.qa.tests.reports;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import java.nio.file.Files;

public class CSVReporter implements IReporter{

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> iSuites,
			String outputDir) {
		
		Path dir = Paths.get(outputDir+"/Epiphan");
		Path reportFile = Paths.get(outputDir+"/Epiphan/report.csv");
		Path summaryFile = Paths.get(outputDir+"/Epiphan/summary.csv");
		BufferedWriter report;
		System.out.println("Creating \""+reportFile.toString()+"\"");
		
		try {
			Files.createDirectories(reportFile.getParent());
			Files.createFile(reportFile);
			report = Files.newBufferedWriter(reportFile, Charset.forName("US-ASCII"));		
			
		} catch (IOException e) {
			System.out.println("[EpiphanReporter] Unable to create output file");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return;
		}
		
		for (ISuite suite : iSuites) {
			
			System.out.println("Evaluating results for suite: "+suite.getName());
			String suiteName = suite.getName();
			try {
				report.write("SUITE: "+suite.getName()+", TEST NAME, RESULT, TIME (ms), INFO\n");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Map<String, ISuiteResult> suiteResults = suite.getResults();
			for (String testName : suiteResults.keySet()) {
				
				System.out.println("Test: "+testName);
				
				ISuiteResult suiteResult = suiteResults.get(testName);
				ITestContext testContext = suiteResult.getTestContext();
				IResultMap failResult = testContext.getFailedTests();
				Set<ITestResult> testsFailed = failResult.getAllResults();
				
				for (ITestResult testResult : testsFailed) {
					
					System.out.println("Failed test");
					try {
						
						String thrown = StringUtils.replace(testResult.getThrowable().getClass().getName(), "\"", "\"\"");
						report.write(",");
						report.write(testName);
						report.write(",");
						report.write("FAIL");
						report.write(",");
						report.write(String.valueOf(testResult.getEndMillis() - testResult.getStartMillis()));
						report.write(",");
						report.write("\""+thrown+"\"");
						report.write("\n");
						
						report.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				IResultMap passResult = testContext.getPassedTests();
				Set<ITestResult> testsPassed = passResult.getAllResults();
				for (ITestResult testResult : testsPassed) {
					
					try {
						report.write(",");
						report.write(testName);
						report.write(",");
						report.write("PASS");
						report.write(",");
						report.write(String.valueOf(testResult.getEndMillis() - testResult.getStartMillis()));
						report.write(",");
						report.write("NONE");
						report.write("\n");
						report.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("Passed test");
				}
				
				IResultMap skipResult = testContext.getSkippedTests();
				Set<ITestResult> testsSkipped = skipResult.getAllResults();
				for (ITestResult testResult : testsSkipped) {
					
					try {
					report.write(",");
					report.write(testName);
					report.write(",");
					report.write("SKIP");
					report.write(",");
					report.write(String.valueOf(testResult.getEndMillis() - testResult.getStartMillis()));
					report.write(",");
					report.write("NONE");
					report.write("\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("Skipped test");
				}
				
			}
		}
		try {
			report.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
