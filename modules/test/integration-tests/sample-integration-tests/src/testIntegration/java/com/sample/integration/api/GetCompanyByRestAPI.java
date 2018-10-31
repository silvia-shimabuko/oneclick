package com.sample.integration.api;

import com.sample.integration.utils.CommonMethods;

/**
 * @author Leonardo Ozaki
 */
public class GetCompanyByRestAPI {

	public String getCompanyByVirtualHost() throws Exception {
		String result = _commonMethods.executeGet(
			"/api/jsonws/company/get-company-by-virtual-host/virtual-host" +
				"/localhost");

		return result;
	}

	private CommonMethods _commonMethods = new CommonMethods();

}