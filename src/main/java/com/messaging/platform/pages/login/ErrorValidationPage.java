package com.messaging.platform.pages.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.messaging.platform.base.BasePage;

public class ErrorValidationPage extends BasePage {

	public ErrorValidationPage(WebDriver driver) {
		super(driver);
	}

	private By errorTextUsername = By.xpath("//span[contains(text(),'Username is required')]");

	private By errorTextPassword = By.xpath("//span[contains(text(),'Password is required')]");

	private By errorMessageLogin = By.xpath("//div[contains(@class, 'snack-bar-label')]");

	public void verifyWithEmptyUsername() {
		Assert.assertEquals(getText(errorTextUsername), "Username is required");
	}

	public void verifyWithEmptyPassword() {
		Assert.assertEquals(getText(errorTextPassword), "Password is required");
	}

	public void verifyWithInvalidCred() {
		Assert.assertEquals(getText(errorMessageLogin), "Combination of username and password is incorrect.");
	}
}
