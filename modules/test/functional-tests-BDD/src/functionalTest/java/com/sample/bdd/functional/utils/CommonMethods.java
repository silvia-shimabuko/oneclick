package com.sample.bdd.functional.utils;

import static com.liferay.gs.testFramework.SeleniumReadPropertyKeys.DRIVER;
import static com.liferay.gs.testFramework.SeleniumWaitMethods.getWaitDriver;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * @author Manoel Cyreno
 */
public class CommonMethods {

	public static void click(By locator) {
		waitUntilVisible(locator);
		DRIVER.findElement(locator).click();
	}

	public static void input(By locator, String text) {
		waitElement(locator);
		DRIVER.findElement(locator).clear();
		waitElement(locator);
		DRIVER.findElement(locator).sendKeys(text);
	}

	public static boolean isDisplayed(By locator) {
		getWaitDriver().until(visibilityOfElementLocated(locator));

		return DRIVER.findElement(locator).isDisplayed();
	}

	public static void select(By locator, int index) {
		waitUntilVisible(locator);
		select(DRIVER.findElement(locator), index);
	}

	public static void select(By locator, String text) {
		waitElement(locator);
		select(DRIVER.findElement(locator), text);
		waitElement(locator);
	}

	public static void select(WebElement element, int index) {
		Select select = new Select(element);

		select.selectByIndex(index);
	}

	public static void select(WebElement element, String text) {
		Select select = new Select(element);

		select.selectByVisibleText(text);
	}

	public static void waitElement(By locator) {
		waitElementAppearOnScreen(locator);
		getWaitDriver().until(elementToBeClickable(locator));
	}

	public static void waitElementAppearOnScreen(By locator) {
		waitUntilPresent(locator);
		waitUntilVisible(locator);
	}

	public static Boolean waitUntilInvisible(By locator) {
		return getWaitDriver().until(ExpectedConditions.
			invisibilityOfElementLocated(locator));
	}

	public static WebElement waitUntilPresent(By element) {
		return getWaitDriver().until(presenceOfElementLocated(element));
	}

	public static WebElement waitUntilVisible(By locator) {
		return getWaitDriver().until(ExpectedConditions.
			visibilityOfElementLocated(locator));
	}

}