package com.sample.integration.api;

import com.sample.integration.utils.CommonMethods;

/**
 * @author Leonardo Ozaki
 */
public class GetGroupByRestAPI {

	public String getGroupsByCompanyId(String companyId) throws Exception {
		String result = _commonMethods.executeGet(
			"/api/jsonws/group/get-groups/company-id/" + companyId +
				"/parent-group-id/0/site/true");

		return result;
	}

	private final CommonMethods _commonMethods = new CommonMethods();

}