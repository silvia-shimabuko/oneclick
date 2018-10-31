package com.sample.integration.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sample.integration.api.CreatePageByRestAPI;
import com.sample.integration.api.DeletePageByRestAPI;
import com.sample.integration.api.GetCompanyByRestAPI;
import com.sample.integration.api.GetGroupByRestAPI;
import com.sample.integration.api.IPageParametersByRestAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * @author Manoel Cyreno
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RunTest {

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Test
	public void testCreatePageAndDeletePage() throws Exception {
		when(_pageParameters.getGroupID()).thenReturn(_getGroupID());
		when(_pageParameters.getIsPrivatePage()).thenReturn("false");
		when(_pageParameters.getPageName()).thenReturn("test page");

		String createResult = _createPage.createPage(_pageParameters);

		Assert.assertThat(createResult, not(containsString("exception")));

		JSONObject findParameter = new JSONObject(createResult);

		when(
			_pageParameters.getLayoutId()
		).thenReturn(
			findParameter.getString("layoutId")
		);
		String deleteResult = _deletePage.deletePage(_pageParameters);

		Assert.assertThat(deleteResult, not(containsString("exception")));
	}

	@Test
	public void testCreatePageDuplicatedWillFail() throws Exception {
		when(_pageParameters.getGroupID()).thenReturn(_getGroupID());
		when(_pageParameters.getIsPrivatePage()).thenReturn("false");
		when(_pageParameters.getPageName()).thenReturn("test page");

		String result1 = _createPage.createPage(_pageParameters);

		Assert.assertThat(result1, not(containsString("exception")));

		String result2 = _createPage.createPage(_pageParameters);

		Assert.assertThat(result2, containsString("exception"));

		// Remove the page previously created

		JSONObject findParameter = new JSONObject(result1);

		when(
			_pageParameters.getLayoutId()
		).thenReturn(
			findParameter.getString("layoutId")
		);
		String deleteResult = _deletePage.deletePage(_pageParameters);

		Assert.assertThat(deleteResult, not(containsString("exception")));
	}

	private String _getGroupID() throws Exception {
		String companyJSon = _getCompany.getCompanyByVirtualHost();

		JSONObject companyObj = new JSONObject(companyJSon);

		String companyID = (String)companyObj.get("companyId");

		String sGroupArray = _getGroup.getGroupsByCompanyId(companyID);

		JSONArray groupArray = new JSONArray(sGroupArray);

		JSONObject group = groupArray.getJSONObject(0);

		return group.getString("groupId");
	}

	private final CreatePageByRestAPI _createPage = new CreatePageByRestAPI();
	private final DeletePageByRestAPI _deletePage = new DeletePageByRestAPI();
	private final GetCompanyByRestAPI _getCompany = new GetCompanyByRestAPI();
	private final GetGroupByRestAPI _getGroup = new GetGroupByRestAPI();
	private final IPageParametersByRestAPI _pageParameters = mock(
		IPageParametersByRestAPI.class);

}