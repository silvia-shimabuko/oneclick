package com.sample.integration.utils;

import static com.liferay.gs.testFramework.SeleniumReadPropertyKeys.getDefaultPassword;
import static com.liferay.gs.testFramework.SeleniumReadPropertyKeys.getDefaultUsername;
import static com.liferay.gs.testFramework.SeleniumReadPropertyKeys.getUrlToHome;

import java.io.UnsupportedEncodingException;

import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import org.json.JSONObject;

/**
 * @author Manoel Cyreno
 */
public class CommonMethods {

	public List<NameValuePair> commonApiParams(
		String groupID, String isPrivatePage) {

		List<NameValuePair> params = new ArrayList<>();

		params.add(new BasicNameValuePair("groupId", groupID));

		if ("yes".equals(isPrivatePage) || "true".equals(isPrivatePage)) {
			params.add(new BasicNameValuePair("privateLayout", "true"));
		}
		else {
			params.add(new BasicNameValuePair("privateLayout", "false"));
		}

		return params;
	}

	public List<NameValuePair> commonApiParams(
		String groupID, String isPrivatePage, String layoutId) {

		List<NameValuePair> params = commonApiParams(groupID, isPrivatePage);

		params.add(new BasicNameValuePair("layoutId", layoutId));

		return params;
	}

	public String executeGet(String gettUrl) throws Exception {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

		try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
			URL url = new URL(getUrlToHome());

			HttpHost targetHost = new HttpHost(
				url.getHost(), url.getPort(), url.getProtocol());

			HttpGet get = new HttpGet(gettUrl);

			_setCredentials(get);

			HttpResponse resp = httpClient.execute(
				targetHost, get, new BasicHttpContext());

			HttpEntity entity = resp.getEntity();

			return EntityUtils.toString(entity);
		}
	}

	public void executePost(String postUrl, List<NameValuePair> params)
		throws Exception {

		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

		try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
			URL url = new URL(getUrlToHome());

			HttpHost targetHost = new HttpHost(
				url.getHost(), url.getPort(), url.getProtocol());

			HttpPost post = new HttpPost(postUrl);

			_setCredentials(post);

			_setParams(post, params);

			HttpResponse resp = httpClient.execute(
				targetHost, post, new BasicHttpContext());

			HttpEntity entity = resp.getEntity();

			_myObj = new JSONObject(EntityUtils.toString(entity));
		}
	}

	public JSONObject getMyObj() {
		return _myObj;
	}

	private void _setCredentials(HttpRequestBase post) {
		Base64 base = new Base64();

		String username = getDefaultUsername();
		String password = getDefaultPassword();

		String credentials = base.encodeAsString(
			(username + ":" + password).getBytes());

		post.setHeader("Authorization", "Basic " + credentials);
	}

	private void _setParams(HttpPost post, List<NameValuePair> params)
		throws UnsupportedEncodingException {

		UrlEncodedFormEntity encodeForm = new UrlEncodedFormEntity(
			params, "UTF-8");

		post.setEntity(encodeForm);
	}

	private JSONObject _myObj;

}