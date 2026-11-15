package com.messaging.platform.pages.mis;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.messaging.platform.base.BasePage;
import com.messaging.platform.pages.components.DatePickerComponent;
import com.messaging.platform.pages.components.MenuComponent;
import com.messaging.platform.pages.components.ViewByComponent;
import com.messaging.platform.utils.HtmlReportUtil;
import com.messaging.platform.utils.ScreenshotUtils;

public class WhatsAppAdvancedPanelPage extends BasePage {

	private final ScreenshotUtils screenshot;
	private final MenuComponent menu;
	private final ViewByComponent viewBy;
	private final DatePickerComponent date;

	private final String[] viewOptions = { "Date & Campaign", "Date", "Campaign" };
	private final By summaryTable = By.className("data-table");

	public WhatsAppAdvancedPanelPage(WebDriver driver) {
		super(driver);
		this.screenshot = new ScreenshotUtils();
		this.menu = new MenuComponent(driver);
		this.viewBy = new ViewByComponent(driver);
		this.date = new DatePickerComponent(driver);
	}

	private By searchButton = By.xpath("//button[contains(text(),'Search')]");

	public void openWhatsAppAdvanced(String startValue, String endValue) {
		menu.navigate("Reports", "WhatsApp", "Advanced");
		date.chooseDateOnAdvancedPage(startValue, endValue);
		;
		click(searchButton);
		sleep();
	}

	public void applyAllViewByOptions(String fileTitle) throws IOException {

		for (String option : viewOptions) {
			viewBy.selectViewByTab(option);

			WebElement table = driver.findElement(summaryTable);

			HtmlReportUtil.createHtmlTable(table, fileTitle + " - " + option, "reports/whatsapp-advanced-panel",
					option.replaceAll("\\W+", "_"));

			screenshot.getScreenshot(driver, fileTitle + "_" + option);
		}

		for (int i = 0; i < 11; i++) {
			boolean selected = viewBy.selectMoreViewByOption(i);
			if (!selected)
				break;

			WebElement table = driver.findElement(summaryTable);

			HtmlReportUtil.createHtmlTable(table, fileTitle + " - More Option " + i, "reports/whatsapp-advanced-panel",
					"More_Option_" + i);

			screenshot.getScreenshot(driver, fileTitle + "_option_" + i);
		}
	}
}