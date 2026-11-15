package com.messaging.platform.listeners;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.messaging.platform.base.BaseTest;
import com.messaging.platform.utils.DateTimeManager;

public class TestListeners implements ITestListener {

	// Saves what the browser showed when a test failed, so the cause can be seen afterwards.
	@Override
	public void onTestFailure(ITestResult result) {
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
			System.err.println("Failure screenshot not saved for " + name + ": " + e.getMessage());
		}
	}
}
