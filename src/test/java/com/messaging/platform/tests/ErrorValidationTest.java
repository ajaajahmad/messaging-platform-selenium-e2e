package com.messaging.platform.tests;

import java.io.IOException;

import org.testng.annotations.Test;
import com.messaging.platform.base.BaseTest;
import com.messaging.platform.dataprovider.TestDataProvider;

public class ErrorValidationTest extends BaseTest {

	@Test(dataProvider = "invalidLoginData", dataProviderClass = TestDataProvider.class)
	public void verifyWithInvalidCredentials(String username, String password) throws IOException {

		loginPage.enterUsername(username);
		loginPage.enterPassword(password);
		loginPage.clickLoginButton();

		errorValidationPage.verifyWithInvalidCred();
	}
}