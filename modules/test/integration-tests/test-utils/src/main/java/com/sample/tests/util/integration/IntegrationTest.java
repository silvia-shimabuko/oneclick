package com.sample.tests.util.integration;

import org.junit.Rule;
import org.junit.rules.TestRule;

/**
 * @author Nathan Ferracini
 */
public class IntegrationTest {

	@Rule
	public TestRule injectServices = InjectServices.instance;

}