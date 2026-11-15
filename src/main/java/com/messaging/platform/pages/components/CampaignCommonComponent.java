package com.messaging.platform.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.messaging.platform.base.BasePage;

public class CampaignCommonComponent extends BasePage {

	public CampaignCommonComponent(WebDriver driver) {
		super(driver);
	}

	// ========== LOCATORS ==========

	private By createCampaignButton = By
			.xpath("//button[contains(@class,'btn-send')]//span[normalize-space()='Create Campaign']");

	private By campaignNameInputBox = By.xpath("//div[contains(@class,'campaign-name')]//input[@type='text']");

	private By categoryDropdown = By.cssSelector("app-custom-select-with-create ng-select .ng-select-container");

	private By categorySearchInputBox = By.cssSelector("ng-dropdown-panel input[placeholder='Search ...']");

	private By categoryOption(String categoryText) {
		return By.xpath("//ng-dropdown-panel//span[contains(@class,'custom-select-with-create-label') and @title='"
				+ categoryText + "']");
	}

	// ========== ACTIONS ==========

	public void clickOnCreateCampaignButton() {
		click(createCampaignButton);
	}

	public void enterCampaignName(String campaignName) {
		type(campaignNameInputBox, campaignName);
	}

	public void selectCampaignCategory(String categoryText) {
		click(categoryDropdown);
		type(categorySearchInputBox, categoryText);
		click(categoryOption(categoryText));
	}
}