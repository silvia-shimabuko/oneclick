package com.sample.bdd.functional.pages;

import static com.liferay.gs.testFramework.SeleniumReadPropertyKeys.DRIVER;
import static com.liferay.gs.testFramework.SeleniumWaitMethods.getWaitDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * @author Manoel Cyreno
 */
public class WelcomePage {

	public boolean usernameIsDisplayed(String usernameAcronym) {
		By usernameAcronymLocator = By.xpath(
			".//*[contains(@id, " +
				"'p_p_id_com_liferay_product_navigation_user_personal" +
					"_bar_web_portlet_" +
						"ProductNavigationUserPersonalBarPortlet_')]//*[" +
							"contains(text(), '" + usernameAcronym + "')]");

		getWaitDriver().until(
			ExpectedConditions.visibilityOfElementLocated(
				usernameAcronymLocator));

		return DRIVER.findElement(usernameAcronymLocator).isDisplayed();
	}

}