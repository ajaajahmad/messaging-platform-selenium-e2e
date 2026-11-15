package com.messaging.platform.base;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TimeoutException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.messaging.platform.pages.campaign.SmsCampaignPage;
import com.messaging.platform.pages.campaign.WhatsAppCampaignPage;
import com.messaging.platform.pages.components.LogoutComponent;
import com.messaging.platform.pages.login.ErrorValidationPage;
import com.messaging.platform.pages.login.LoginPage;
import com.messaging.platform.pages.mis.RcsAdvancedApiPage;
import com.messaging.platform.pages.mis.RcsAdvancedPanelPage;
import com.messaging.platform.pages.mis.RcsSummaryApiPage;
import com.messaging.platform.pages.mis.RcsSummaryPanelPage;
import com.messaging.platform.pages.mis.SmsAdvancedApiPage;
import com.messaging.platform.pages.mis.SmsAdvancedPanelPage;
import com.messaging.platform.pages.mis.SmsSummaryApiPage;
import com.messaging.platform.pages.mis.SmsSummaryPanelPage;
import com.messaging.platform.pages.mis.TruecallerAdvancedApiPage;
import com.messaging.platform.pages.mis.TruecallerAdvancedPanelPage;
import com.messaging.platform.pages.mis.TruecallerSummaryApiPage;
import com.messaging.platform.pages.mis.TruecallerSummaryPanelPage;
import com.messaging.platform.pages.mis.WhatsAppAdvancedApiPage;
import com.messaging.platform.pages.mis.WhatsAppAdvancedPanelPage;
import com.messaging.platform.pages.mis.WhatsAppSummaryApiPage;
import com.messaging.platform.pages.mis.WhatsAppSummaryPanelPage;
import com.messaging.platform.pages.users.UserAccountMisRcsApiPage;
import com.messaging.platform.pages.users.UserAccountMisRcsPanelPage;
import com.messaging.platform.pages.users.UserAccountMisSmsApiPage;
import com.messaging.platform.pages.users.UserAccountMisSmsPanelPage;
import com.messaging.platform.pages.users.UserAccountMisTruecallerApiPage;
import com.messaging.platform.pages.users.UserAccountMisTruecallerPanelPage;
import com.messaging.platform.pages.users.UserAccountMisWhatsAppApiPage;
import com.messaging.platform.pages.users.UserAccountMisWhatsAppPanelPage;
import com.messaging.platform.utils.ConfigReader;

public class BaseTest {

	static {
		LogManager.getLogManager().reset();
		Logger.getLogger("").setLevel(Level.SEVERE);
	}

	protected WebDriver driver;
	protected LoginPage loginPage;
	protected ErrorValidationPage errorValidationPage;
	protected SmsCampaignPage smsCampaignPage;
	protected WhatsAppCampaignPage wabaCampaignPage;
	protected LogoutComponent logoutComponent;
	protected SmsSummaryPanelPage smsSummaryPanelPage;
	protected SmsSummaryApiPage smsSummaryApiPage;
	protected SmsAdvancedPanelPage smsAdvancedPanelPage;
	protected SmsAdvancedApiPage smsAdvancedApiPage;
	protected WhatsAppSummaryPanelPage whatsAppSummaryPanelPage;
	protected WhatsAppSummaryApiPage whatsAppSummaryApiPage;
	protected WhatsAppAdvancedPanelPage whatsAppAdvancedPanelPage;
	protected WhatsAppAdvancedApiPage whatsAppAdvancedApiPage;
	protected TruecallerSummaryPanelPage truecallerSummaryPanelPage;
	protected TruecallerSummaryApiPage truecallerSummaryApiPage;
	protected TruecallerAdvancedPanelPage truecallerAdvancedPanelPage;
	protected TruecallerAdvancedApiPage truecallerAdvancedApiPage;
	protected RcsSummaryPanelPage rcsSummaryPanelPage;
	protected RcsSummaryApiPage rcsSummaryApiPage;
	protected RcsAdvancedPanelPage rcsAdvancedPanelPage;
	protected RcsAdvancedApiPage rcsAdvancedApiPage;
	protected UserAccountMisSmsPanelPage userAccountMisSmsPanelPage;
	protected UserAccountMisSmsApiPage userAccountMisSmsApiPage;
	protected UserAccountMisWhatsAppPanelPage userAccountMisWhatsAppPanelPage;
	protected UserAccountMisWhatsAppApiPage userAccountMisWhatsAppApiPage;
	protected UserAccountMisRcsPanelPage userAccountMisRcsPanelPage;
	protected UserAccountMisRcsApiPage userAccountMisRcsApiPage;
	protected UserAccountMisTruecallerPanelPage userAccountMisTruecallerPanelPage;
	protected UserAccountMisTruecallerApiPage userAccountMisTruecallerApiPage;

	@BeforeSuite(alwaysRun = true)
	public void setUp() {

		String browserName = ConfigReader.get("browser");

		if (browserName.equalsIgnoreCase("chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.setPageLoadStrategy(PageLoadStrategy.EAGER);
			driver = new ChromeDriver(options);
		} else if (browserName.equalsIgnoreCase("firefox")) {
			FirefoxOptions options = new FirefoxOptions();
			options.setPageLoadStrategy(PageLoadStrategy.EAGER);
			driver = new FirefoxDriver(options);
		} else if (browserName.equalsIgnoreCase("edge")) {
			EdgeOptions options = new EdgeOptions();
			options.setPageLoadStrategy(PageLoadStrategy.EAGER);
			driver = new EdgeDriver(options);
		} else {
			throw new RuntimeException("Browser not supported: " + browserName);
		}

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts()
				.pageLoadTimeout(Duration.ofSeconds(Long.parseLong(ConfigReader.get("timeout"))));

		try {
			driver.get(ConfigReader.get("app_url"));
		} catch (TimeoutException e) {
			System.err.println("Page load timed out; continuing so the loaded application state can be checked: "
					+ e.getMessage());
		}

		loginPage = new LoginPage(driver);
		errorValidationPage = new ErrorValidationPage(driver);
		smsCampaignPage = new SmsCampaignPage(driver);
		wabaCampaignPage = new WhatsAppCampaignPage(driver);
		logoutComponent = new LogoutComponent(driver);
		smsSummaryPanelPage = new SmsSummaryPanelPage(driver);
		smsSummaryApiPage = new SmsSummaryApiPage(driver);
		smsAdvancedPanelPage = new SmsAdvancedPanelPage(driver);
		smsAdvancedApiPage = new SmsAdvancedApiPage(driver);
		whatsAppSummaryPanelPage = new WhatsAppSummaryPanelPage(driver);
		whatsAppSummaryApiPage = new WhatsAppSummaryApiPage(driver);
		whatsAppAdvancedPanelPage = new WhatsAppAdvancedPanelPage(driver);
		whatsAppAdvancedApiPage = new WhatsAppAdvancedApiPage(driver);
		truecallerSummaryPanelPage = new TruecallerSummaryPanelPage(driver);
		truecallerSummaryApiPage = new TruecallerSummaryApiPage(driver);
		truecallerAdvancedPanelPage = new TruecallerAdvancedPanelPage(driver);
		truecallerAdvancedApiPage = new TruecallerAdvancedApiPage(driver);
		rcsSummaryPanelPage = new RcsSummaryPanelPage(driver);
		rcsSummaryApiPage = new RcsSummaryApiPage(driver);
		rcsAdvancedPanelPage = new RcsAdvancedPanelPage(driver);
		rcsAdvancedApiPage = new RcsAdvancedApiPage(driver);
		userAccountMisSmsPanelPage = new UserAccountMisSmsPanelPage(driver);
		userAccountMisSmsApiPage = new UserAccountMisSmsApiPage(driver);
		userAccountMisWhatsAppPanelPage = new UserAccountMisWhatsAppPanelPage(driver);
		userAccountMisWhatsAppApiPage = new UserAccountMisWhatsAppApiPage(driver);
		userAccountMisRcsPanelPage = new UserAccountMisRcsPanelPage(driver);
		userAccountMisRcsApiPage = new UserAccountMisRcsApiPage(driver);
		userAccountMisTruecallerPanelPage = new UserAccountMisTruecallerPanelPage(driver);
		userAccountMisTruecallerApiPage = new UserAccountMisTruecallerApiPage(driver);

	}

	@AfterSuite(alwaysRun = true)
	public void tearDown() {
		try {
			if (logoutComponent != null) {
				logoutComponent.userLogout();
			}
		} finally {
			if (driver != null) {
				driver.quit();
			}
		}
	}
}