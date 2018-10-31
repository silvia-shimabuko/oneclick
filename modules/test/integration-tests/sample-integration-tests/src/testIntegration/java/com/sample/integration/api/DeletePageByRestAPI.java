package com.sample.integration.api;

import com.sample.integration.utils.CommonMethods;

import java.util.List;

import org.apache.http.NameValuePair;

/**
 * @author Manoel Cyreno
 */
public class DeletePageByRestAPI {

	public String deletePage(IPageParametersByRestAPI pageParameters)
		throws Exception {

		List<NameValuePair> params = _commonMethods.commonApiParams(
			pageParameters.getGroupID(), pageParameters.getIsPrivatePage(),
			pageParameters.getLayoutId());

		_commonMethods.executePost("/api/jsonws/layout/delete-layout", params);

		return _commonMethods.getMyObj().toString();
	}

	private CommonMethods _commonMethods = new CommonMethods();

}