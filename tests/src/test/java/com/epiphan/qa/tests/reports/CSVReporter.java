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
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

import java.nio.file.Files;

/**
 * Ouputs testng reports in a CSV file.
 * 
 * @author Ian Harvey {@literal <iharvey@epiphan.com>}
 *
 */
public class CSVReporter implements IReporter {

	/**
	 * The main logic for report generation. 
	 * <p>
	 * Each test result is printed to one line in the CSV file.
	 * The test results are sorted FAIL->PASS->SKIP->OTHER(not shown)
	 * 
	 * @param xmlSuites List of XmlSuite objects (I don't know what these are
	 * for)
	 * @param iSuites List of ISuite objects for generating the report
	 */
	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> iSuites,
			String outputDir) {

		Path dir = Paths.get(outputDir + "/Epiphan");
		Path reportFile = Paths.get(outputDir + "/Epiphan/report.csv");
		Path summaryFile = Paths.get(outputDir + "/Epiphan/summary.csv");
		BufferedWriter report;
		System.out.println("Creating \"" + reportFile.toString() + "\"");

		try {
			Files.createDirectories(reportFile.getParent());
			Files.createFile(reportFile);
			report = Files.newBufferedWriter(reportFile,
					Charset.forName("US-ASCII"));

		} catch (IOException e) {
			System.out
					.println("[EpiphanReporter] Unable to create output file");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return;
		}

		for (ISuite suite : iSuites) {

			System.out.println("Evaluating results for suite: "
					+ suite.getName());
			try {
				report.write("SUITE, TEST, METHOD, STATUS, TIME (ms), LOG, THROWABLE\n");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Map<String, ISuiteResult> suiteResults = suite.getResults();
			for (String testName : suiteResults.keySet()) {

				ISuiteResult suiteResult = suiteResults.get(testName);
				ITestContext testContext = suiteResult.getTestContext();

				IResultMap failResult = testContext.getFailedTests();
				Set<ITestResult> testsFailed = failResult.getAllResults();
				
				IResultMap passResult = testContext.getPassedTests();
				Set<ITestResult> testsPassed = passResult.getAllResults();
				
				IResultMap skipResult = testContext.getSkippedTests();
				Set<ITestResult> testsSkipped = skipResult.getAllResults();
				
				for (ITestResult testResult : testsFailed) {

					writeTestInfo(suite, testResult, report);
					writeTestLog(suite, testResult, report);
					writeTestThrowable(suite, testResult, report);
					nextRecord(report);
					

				}

				for (ITestResult testResult : testsPassed) {

					writeTestInfo(suite, testResult, report);
					writeTestLog(suite, testResult, report);
					writeTestThrowable(suite, testResult, report);
					nextRecord(report);

				}

				for (ITestResult testResult : testsSkipped) {

					writeTestInfo(suite, testResult, report);
					writeTestLog(suite, testResult, report);
					writeTestThrowable(suite, testResult, report);
					nextRecord(report);
					
				}

			}
		}
		try {
			report.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Reads in a test log, and formats it as a single String.
	 * <p>
	 * The test log is anything logged by <code>tr</code> using
	 * <code>Reporter.log(str)</code>
	 * <p>
	 * This input will be squished into one <code>String</code> object
	 * with newline characters between entries.
	 *
	 * @param tr The <code>ITestResult</code> who's log to read
	 * @return The test log of <code>tr</code> formatted as a string
	 * 
	 */
	private String readLog(ITestResult tr) {
		List<String> log = Reporter.getOutput(tr);
		StringBuilder logString = new StringBuilder();

		for (String s : log) {
			logString.append(s).append("\n");
		}

		return logString.toString();
	}

	/**
	 * Writes the basic test info (suite, class, method, status, time)
	 * @param s The <code>ISuite</code> object representing the current test
	 * suite
	 * @param r The <code>ITestResult</code> object representing the current
	 * test result
	 * @param w The <code>BufferedWriter</code> to print out the test info.
	 * This should be a BufferedWriter pointing to the report file
	 */
	private void writeTestInfo(ISuite s, ITestResult r, BufferedWriter w) {

		try {
			w.write(s.getName());
			w.write(",");
			w.write(r.getTestClass().getName());
			w.write(",");
			w.write(r.getName());
			w.write(",");
			if (r.getStatus() == ITestResult.FAILURE) {
				w.write("FAIL");
			}else if (r.getStatus() == ITestResult.SUCCESS) {
				w.write("PASS");
			}else if (r.getStatus() == ITestResult.SKIP) {
				w.write("SKIP");
			} else {
				w.write("OTHR");
			}
			w.write(",");
			w.write(String.valueOf(r.getEndMillis() - r.getStartMillis()));
			w.write(",");
			w.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Writes the log of a test into one cell in the CSV report file
	 * @param s The <code>ISuite</code> object representing the current test
	 * suite
	 * @param r The <code>ITestResult</code> object representing the current
	 * test result
	 * @param w The <code>BufferedWriter</code> to print out the test log.
	 * This should be a BufferedWriter pointing to the report file
	 */
	private void writeTestLog(ISuite s, ITestResult r, BufferedWriter w) {
		try {
			w.write("\""+StringUtils.replace(readLog(r), "\"", "\"\"")+"\"");
			w.write(",");
			w.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return;
		}
	}
	
	private void writeTestThrowable (ISuite s, ITestResult r, BufferedWriter w) {
		try{
			String thrown;
			
			if (r.getThrowable() != null)
				thrown = StringUtils.replace(r.getThrowable().getClass().getName(), "\"", "\"\"");
			else
				thrown = "";
			w.write("\""+thrown+"\"");
			w.write(",");
			w.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Unnecessary convenience method to print the newline character between
	 * records in the CSV report file
	 * @param w The <code>BufferedWriter</code> to print out the test info.
	 * This should be a BufferedWriter pointing to the report file
	 */
	private void nextRecord(BufferedWriter w) {
		try {
			w.write("\n");
			w.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return;
		}
	}
	
	private void writeHeader (List<ISuite>)

}
