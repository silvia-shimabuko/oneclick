package com.sample.tdd.functional.testsSuite;

import static com.liferay.gs.testFramework.SeleniumReadPropertyKeys.DRIVER;
import static com.liferay.gs.testFramework.SeleniumWaitMethods.waitMediumTime;

import com.sample.tdd.functional.tests.LoginTest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Manoel Cyreno
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(LoginTest.class)
public class RunAllTests {

	public static Boolean runnedFromAllTestsSuite = false;

	@AfterClass
	public static void afterClass() throws Exception {
		runnedFromAllTestsSuite = false;
		closeDriver();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		runnedFromAllTestsSuite = true;
	}

	public static void closeDriver() {
		if (RunAllTests.runnedFromAllTestsSuite == false) {
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