package com.sample.bdd.functional.steps;

import static org.junit.Assert.assertEquals;

import com.liferay.gs.testFramework.SeleniumReadPropertyKeys;

import com.sample.bdd.functional.pages.LoginPage;
import com.sample.bdd.functional.pages.WelcomePage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * @author Manoel Cyreno
 */
public class LoginStepDefinitions {

	@Given("^I am on Home Page$")
	public void i_am_on_home_page() {
		SeleniumReadPropertyKeys.DRIVER.get(
			SeleniumReadPropertyKeys.getUrlToHome());
	}

	@When("^I fill the (-?[^\"]*) and (-?[^\"]*) I will be logged$")
	public void i_fill_the_login_and_password_i_will_be_logged(
		String emailAddress, String password) {

		_loginPage.clickOnSignIn();
		signInOnPortal(emailAddress, password);
	}

	public void signInOnPortal(String emailAddress, String password) {
		_loginPage.fillEmailAddressField(emailAddress);
		_loginPage.fillPasswordField(password);
		_loginPage.clickOnSignInOfTheModal();
	}

	@Then("^The error message will appear related with (-?[^\"]*)$")
	public void the_error_message_will_appear_related_with(String wrongField) {
		switch (wrongField.toLowerCase().toString()) {
			case "both":
				assertEquals(true, _loginPage.alertErrorIsDisplayed());
				break;
			case "user":
				assertEquals(true, _loginPage.loginHelperIsDisplayed());
				break;
			case "password":
				assertEquals(true, _loginPage.passwordHelperIsDisplayed());
				break;
			default:
				//This switch will catch the failure if the others cases weren't mapped
				assertEquals(true, false);
		}
	}

	@Then("^The (-?[^\"]*) is displayed$")
	public void the_username_is_displayed(String username) {
		assertEquals(true, _welcomePage.usernameIsDisplayed(username));
	}

	private LoginPage _loginPage = new LoginPage();
	private WelcomePage _welcomePage = new WelcomePage();

}