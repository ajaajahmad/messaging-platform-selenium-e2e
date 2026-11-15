package com.messaging.platform.listeners;

import java.io.File;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
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

	// Selenium messages run to many lines of session detail; the first line says what went wrong.
	private static String firstLine(Throwable throwable) {
		if (throwable == null || throwable.getMessage() == null) {
			return String.valueOf(throwable);
		}
		return throwable.getMessage().lines().findFirst().orElse("");
	}

	// Read from the RunLog appender so the folder name, which includes the run's start time, is set
	// only in log4j2.xml.
	private static Path runLogDir() {
		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		FileAppender runLog = context.getConfiguration().getAppender("RunLog");
		return Path.of(runLog.getFileName()).getParent();
	}

	@Override
	public void onStart(ITestContext context) {
		log.info("Running '{}'", context.getName());
	}

	@Override
	public void onFinish(ITestContext context) {
		log.info("Logs: {}", runLogDir().toAbsolutePath());
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
		log.warn("SKIP  {}: {}", name(result), firstLine(result.getThrowable()));
	}

	// Saves what the browser showed when a test failed, so the cause can be seen afterwards.
	@Override
	public void onTestFailure(ITestResult result) {
		log.error("FAIL  {} ({} s): {}", name(result), seconds(result), firstLine(result.getThrowable()));

		// At debug, so the full trace reaches the log files without flooding the console.
		log.debug("Stack trace for {}", name(result), result.getThrowable());

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
