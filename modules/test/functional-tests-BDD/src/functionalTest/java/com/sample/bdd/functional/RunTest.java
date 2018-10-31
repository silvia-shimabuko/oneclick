package com.sample.bdd.functional;

import static com.liferay.gs.testFramework.SeleniumReadPropertyKeys.DRIVER;
import static com.liferay.gs.testFramework.SeleniumWaitMethods.waitMediumTime;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * @author Manoel Cyreno
 */
@CucumberOptions(
	features = "src/functionalTest/resources",
	glue = {"com/sample/bdd/functional/steps"},
	plugin = {"pretty", "html:reports/cucumber"}, tags = {}
)
@RunWith(Cucumber.class)
public class RunTest {

	public static Boolean _runnedFromAllTestsSuite = false;

	@AfterClass
	public static void afterClass() throws Exception {
		_runnedFromAllTestsSuite = false;
		closeDriver();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		_runnedFromAllTestsSuite = true;
	}

	public static void closeDriver() {
		if (_runnedFromAllTestsSuite == false) {
			if (DRIVER.toString().contains("chrome")) {
				DRIVER.close();
				waitMediumTime();
				waitMediumTime();
				waitMediumTime();
				DRIVER.quit();
			} else {
				DRIVER.quit();
			}
		}
	}

}