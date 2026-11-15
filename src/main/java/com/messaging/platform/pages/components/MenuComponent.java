package com.messaging.platform.pages.components;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.messaging.platform.base.BasePage;

public class MenuComponent extends BasePage {

	public MenuComponent(WebDriver driver) {
		super(driver);
	}

	private By sidebar = By.xpath("//div[contains(@class,'custom-new-menu-wrapper')]");

	private WebElement root() {
		sleep(5000);
		return driver.findElement(sidebar);
	}

	private WebElement expand(WebElement scope, String text) {

		WebElement li = scope.findElement(By.xpath(".//span[normalize-space()='" + text + "']/ancestor::li[1]"));

		WebElement toggle = li
				.findElement(By.xpath("./div[contains(@class,'menu-event') or contains(@class,'child-menu-event')]"));

		WebElement collapse = li.findElement(By.xpath("./div[contains(@class,'collapse')]"));

		if (!collapse.isDisplayed()) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", toggle);
			sleep(800);
			clickMenuItem(toggle);
			sleep(1000);
		}

		return collapse;
	}

	private void clickFinal(WebElement scope, String text) {

		WebElement target = scope.findElement(By.xpath(".//span[normalize-space()='" + text + "']"));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", target);

		sleep(800);
		clickMenuItem(target);
		sleep(1000);
	}

	public void navigate(String... menuPath) {

		WebElement currentScope = root();

		for (int i = 0; i < menuPath.length - 1; i++) {
			currentScope = expand(currentScope, menuPath[i]);
		}

		String urlBefore = driver.getCurrentUrl();
		String target = menuPath[menuPath.length - 1];

		clickFinal(currentScope, target);

		// The click is sometimes swallowed while the sidebar is still settling; try once more.
		if (!waitForUrlChange(urlBefore)) {
			clickFinal(currentScope, target);
			waitForUrlChange(urlBefore);
		}
	}

	private boolean waitForUrlChange(String urlBefore) {
		try {
			return new WebDriverWait(driver, Duration.ofSeconds(5))
					.until(d -> !d.getCurrentUrl().equals(urlBefore));
		} catch (TimeoutException e) {
			return false;
		}
	}

	// The sidebar animates open, so an item can be in the DOM but not yet clickable.
	private void clickMenuItem(WebElement item) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(item)).click();
		} catch (ElementNotInteractableException | TimeoutException e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", item);
		}
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}