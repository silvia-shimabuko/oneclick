package com.sample.bdd.functional.steps;

import static com.liferay.gs.testFramework.SeleniumReadPropertyKeys.getDefaultPassword;
import static com.liferay.gs.testFramework.SeleniumReadPropertyKeys.getDefaultUsername;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

/**
 * @author Manoel Cyreno
 */
public class BackgroungStepDefinitions {

	@Then("^he are on welcome page$")
	public void he_are_on_welcome_page() {
		_lsd.the_username_is_displayed(_liferayPortalUsernameAcronym);
	}

	@Given("^the user is logged in liferay portal$")
	public void the_user_is_logged_in_liferay_portal() {
		_lsd.i_am_on_home_page();
		_lsd.i_fill_the_login_and_password_i_will_be_logged(
			getDefaultUsername(), getDefaultPassword());
	}

	private static final String _liferayPortalUsernameAcronym = "TT";

	private LoginStepDefinitions _lsd = new LoginStepDefinitions();

}