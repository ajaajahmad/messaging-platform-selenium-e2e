package com.messaging.platform.listeners;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.messaging.platform.base.BaseTest;
import com.messaging.platform.utils.DateTimeManager;

public class TestListeners implements ITestListener {

	private static final Logger log = LogManager.getLogger(TestListeners.class);

	// Data-provider arguments are left out of every line: they include the login password.
	private static String name(ITestResult result) {
		return result.getTestClass().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName();
	}

	private static long seconds(ITestResult result) {
		return (result.getEndMillis() - result.getStartMillis()) / 1000;
	}

	@Override
	public void onTestStart(ITestResult result) {
		log.info("START {}", name(result));
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		log.info("PASS  {} ({} s)", name(result), seconds(result));
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		log.warn("SKIP  {}", name(result));
	}

	// Saves what the browser showed when a test failed, so the cause can be seen afterwards.
	@Override
	public void onTestFailure(ITestResult result) {
		Throwable cause = result.getThrowable();
		log.error("FAIL  {} ({} s): {}", name(result), seconds(result),
				cause == null ? "no exception" : String.valueOf(cause.getMessage()).split("\\n")[0]);

		if (!(result.getInstance() instanceof BaseTest)) {
			return;
		}

		WebDriver driver = ((BaseTest) result.getInstance()).getDriver();
		if (driver == null) {
			return;
		}

		String name = result.getTestClass().getRealClass().getSimpleName() + "_" + result.getMethod().getMethodName();

		try {
			File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(source, new File("./screenshots/failures/" + DateTimeManager.appendLocalDateTime(name) + ".png"));
		} catch (Exception e) {
			log.error("Failure screenshot not saved for {}: {}", name, e.getMessage());
		}
	}
}
