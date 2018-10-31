package com.sample.bdd.functional.steps;

import com.liferay.gs.testFramework.SeleniumReadPropertyKeys;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * This class is responsable to
 * initialize and ending the structure
 * of the Cucumber Tests, in our case, the RunTest.java
 *
 * @author Manoel Cyreno
 */
public class SettingsStartingEndingSteps {

	@After
	public void afterScenario(Scenario scenario) {
		if (scenario.isFailed()) {
			byte[] screenshot =
				((TakesScreenshot)SeleniumReadPropertyKeys.DRIVER).getScreenshotAs(OutputType.BYTES);
			scenario.embed(screenshot, "image/png");
		}

		SeleniumReadPropertyKeys.DRIVER.navigate().to(SeleniumReadPropertyKeys.getUrlToHome() + SeleniumReadPropertyKeys.getLinkToLogOut());
	}

	@Before
	public void beforeScenario() {
		SeleniumReadPropertyKeys.DRIVER.get(
			SeleniumReadPropertyKeys.getUrlToHome());
		SeleniumReadPropertyKeys.DRIVER.manage().window().maximize();
		SeleniumReadPropertyKeys.DRIVER.manage().timeouts().implicitlyWait(SeleniumReadPropertyKeys.getTimeOut(), TimeUnit.SECONDS);
	}

}