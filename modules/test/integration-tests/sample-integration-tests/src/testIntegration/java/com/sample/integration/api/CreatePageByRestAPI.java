package com.sample.integration.api;

import com.sample.integration.utils.CommonMethods;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author Manoel Cyreno
 */
public class CreatePageByRestAPI {

	public String createPage(IPageParametersByRestAPI pageParameters)
		throws Exception {

		List<NameValuePair> params = _commonMethods.commonApiParams(
			pageParameters.getGroupID(), pageParameters.getIsPrivatePage());

		String pageName = pageParameters.getPageName();

		String newPageName = pageName.replace(" ", "-");

		params.add(new BasicNameValuePair("parentLayoutId", "0"));
		params.add(new BasicNameValuePair("name", pageName.toLowerCase()));
		params.add(new BasicNameValuePair("title", pageName.toLowerCase()));
		params.add(new BasicNameValuePair("description", "description"));
		params.add(new BasicNameValuePair("type", "portlet"));
		params.add(new BasicNameValuePair("hidden", "false"));
		params.add(
			new BasicNameValuePair(
				"friendlyURL", "/" + newPageName.toLowerCase()));

		_commonMethods.executePost("/api/jsonws/layout/add-layout", params);

		return _commonMethods.getMyObj().toString();
	}

	private CommonMethods _commonMethods = new CommonMethods();

}