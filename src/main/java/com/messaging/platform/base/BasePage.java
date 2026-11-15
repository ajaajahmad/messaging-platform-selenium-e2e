package com.messaging.platform.base;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BasePage {

	protected WebDriver driver;
	protected WebDriverWait wait;
	protected final Logger log = LogManager.getLogger(getClass());

	private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);
	private static final long HARD_SLEEP_MS = 300;
	private static final int CLICK_ATTEMPTS = 3;
	private static final By TOAST = By.cssSelector("mat-snack-bar-container");

	public BasePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
	}

	protected WebElement waitForVisible(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	protected List<WebElement> waitForVisibleElements(By locator) {
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	protected WebElement waitForClickable(By locator) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	protected boolean waitForInvisible(By locator) {
		return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	protected WebElement getElement(By locator) {
		return driver.findElement(locator);
	}

	protected List<WebElement> getElements(By locator) {
		return driver.findElements(locator);
	}

	protected boolean isElementPresent(By locator) {
		return !driver.findElements(locator).isEmpty();
	}

	protected boolean isElementDisplayed(By locator) {
		List<WebElement> elements = driver.findElements(locator);
		return !elements.isEmpty() && elements.get(0).isDisplayed();
	}

	protected void click(By locator) {
		sleep();
		for (int attempt = 1;; attempt++) {
			try {
				waitForClickable(locator).click();
				break;
			} catch (StaleElementReferenceException | ElementClickInterceptedException e) {
				if (attempt == CLICK_ATTEMPTS) {
					throw e;
				}
				waitForToastToClear();
				sleep();
			}
		}
		sleep();
	}

	// A toast can sit on top of the control being clicked; note what it said and wait for it to go.
	private void waitForToastToClear() {
		List<WebElement> toasts = driver.findElements(TOAST);
		if (toasts.isEmpty()) {
			return;
		}
		try {
			log.warn("Toast covering the page: " + toasts.get(0).getText());
		} catch (StaleElementReferenceException e) {
			// already gone
		}
		waitForInvisible(TOAST);
	}

	protected void type(By locator, String text) {
		sleep();
		WebElement element = waitForVisible(locator);
		element.clear();
		element.sendKeys(text);
		sleep();
	}

	protected void typeAndEnter(By locator, String text) {
		WebElement element = waitForVisible(locator);
		element.clear();
		element.sendKeys(text, Keys.ENTER);
	}

	protected String getText(By locator) {
		return waitForVisible(locator).getText();
	}

	protected void sleep() {
		try {
			Thread.sleep(HARD_SLEEP_MS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}