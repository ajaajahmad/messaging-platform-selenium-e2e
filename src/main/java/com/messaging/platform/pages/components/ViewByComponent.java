package com.messaging.platform.pages.components;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.messaging.platform.base.BasePage;

public class ViewByComponent extends BasePage {

	public ViewByComponent(WebDriver driver) {
		super(driver);
	}

	private By moreViewByDropdown = By.id("more_con");

	private By loader = By.className("loader-wrapper");

	public void selectViewByTab(String viewByText) {
		waitForInvisible(loader);
		click(By.xpath("//li//span[normalize-space()='" + viewByText + "']"));
		waitForInvisible(loader);
	}

	public boolean selectMoreViewByOption(int index) {

		WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(10));

		explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loader-wrapper")));

		click(moreViewByDropdown);

		List<WebElement> options = explicitWait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("ul.bottom_drop li")));

		if (options.size() > index) {
			options.get(index).click();
			sleep();
			waitForInvisible(loader);
			return true;
		}

		return false;
	}
}