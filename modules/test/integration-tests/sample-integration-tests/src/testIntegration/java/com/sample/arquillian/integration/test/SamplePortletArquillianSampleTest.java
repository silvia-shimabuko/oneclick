package com.sample.arquillian.integration.test;

import static org.junit.Assert.assertTrue;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;

import com.sample.tests.util.integration.IntegrationTest;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;

import samplePortlet.service.SamplePortletService;

/**
 * @author Manoel Cyreno
 */
@RunWith(Arquillian.class)
public class SamplePortletArquillianSampleTest extends IntegrationTest {

	@Test
	public void testGetTextWithBlankValue_returnError() {
		String result = _samplePortletService.getText("");

		assertTrue(result.contains("Error"));
	}

	@Test
	public void testGetTextWithNull_returnError() {
		String result = _samplePortletService.getText(null);

		assertTrue(result.contains("Error"));
	}

	@Test
	public void testGetTextWithValidValue_returnHelloWorld() {
		String result = _samplePortletService.getText("something");

		assertTrue(result.contains("Hello World"));
	}

	@Inject
	private SamplePortletService _samplePortletService;

}