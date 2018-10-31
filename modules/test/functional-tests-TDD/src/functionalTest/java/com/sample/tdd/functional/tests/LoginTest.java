package com.sample.tdd.functional.tests;

import static com.liferay.gs.testFramework.SeleniumReadPropertyKeys.DRIVER;
import static com.liferay.gs.testFramework.SeleniumReadPropertyKeys.getDefaultPassword;
import static com.liferay.gs.testFramework.SeleniumReadPropertyKeys.getDefaultUsername;
import static com.liferay.gs.testFramework.SeleniumReadPropertyKeys.getLinkToLogOut;
import static com.liferay.gs.testFramework.SeleniumReadPropertyKeys.getUrlToHome;

import static org.junit.Assert.assertTrue;

import com.sample.tdd.functional.pages.LoginPage;
import com.sample.tdd.functional.pages.WelcomePage;
import com.sample.tdd.functional.utils.FunctionalTest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Manoel Cyreno
 */
public class LoginTest extends FunctionalTest {

	@BeforeClass
	public static void setup() {
	}

	@AfterClass
	public static void tearDown() {
	}

	@After
	public void afterTestMethod() throws Exception {
		DRIVER.get(getUrlToHome() + getLinkToLogOut());
	}

	@Before
	public void beforeTestMethod() throws Exception {
		DRIVER.get(getUrlToHome());
	}

	@Test
	public void testLoginWithFail_validateErrorMessageAppearWhenTheBlankPasswordIsUsed_returnTrue() {
		_loginPage.clickOnSignIn();
		_loginPage.signInOnPortal(_liferayPortalUser, " ");
		assertTrue(_loginPage.passwordHelperIsDisplayed());
	}

	@Test
	public void testLoginWithFail_validateErrorMessageAppearWhenTheBlankUserIsUsed_returnTrue() {
		_loginPage.clickOnSignIn();
		_loginPage.signInOnPortal(" ", _liferayPortalPassword);
		assertTrue(_loginPage.loginHelperIsDisplayed());
	}

	@Test
	public void testLoginWithFail_validateErrorMessageAppearWhenTheWrongUserAndWrongPasswordIsUsed_returnTrue() {
		_loginPage.clickOnSignIn();
		_loginPage.signInOnPortal("invalidUser@liferay.com", "invalidPassword");
		assertTrue(_loginPage.alertErrorIsDisplayed());
	}

	@Test
	public void testLoginWithSuccess_validateLoginCanBeMadeWithSuccess_returnTrue() {
		_loginPage.clickOnSignIn();
		_loginPage.signInOnPortal(_liferayPortalUser, _liferayPortalPassword);
		assertTrue(
			_welcomePage.usernameIsDisplayed(_liferayPortalUsernameAcronym));
	}

	private final String _liferayPortalPassword = getDefaultPassword();
	private final String _liferayPortalUser = getDefaultUsername();
	private final String _liferayPortalUsernameAcronym = "TT";
	private final LoginPage _loginPage = new LoginPage();
	private final WelcomePage _welcomePage = new WelcomePage();

}