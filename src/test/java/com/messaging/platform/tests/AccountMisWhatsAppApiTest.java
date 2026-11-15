package com.messaging.platform.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.messaging.platform.base.BaseTest;
import com.messaging.platform.dataprovider.TestDataProvider;
import com.messaging.platform.utils.DateTimeManager;

public class AccountMisWhatsAppApiTest extends BaseTest {

	private String fileTitle;

	@BeforeTest
	public void generateScreenshotText() {
		fileTitle = DateTimeManager.appendLocalDateTime("account_mis_whatsapp_api");
	}

	@Test(groups = "user_login", dataProvider = "excelUserData", dataProviderClass = TestDataProvider.class)
	public void userLogin(String username, String password) throws IOException {
		try {
			loginPage.userLogin(username, password);
		} catch (IllegalArgumentException e) {
			Assert.fail("Login failed: " + e.getMessage());
		}
	}

	@Test()
	public void verifyAccountMisWhatsAppApi() throws InterruptedException, IOException {

		userAccountMisWhatsAppApiPage.openUserAccountMisWhatsAppApi("2026-01-01", "2026-01-15");
		userAccountMisWhatsAppApiPage.applyAllViewByOptions(fileTitle);

	}
}