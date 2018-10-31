package samplePortlet.portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import samplePortlet.constants.SamplePortletKeys;

import samplePortlet.service.SamplePortletService;

/**
 * @author Manoel Cyreno
 */
@Component(immediate = true, property = {"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=ManoelApp Portlet",
		"javax.portlet.name=" + SamplePortletKeys.SamplePortlet,
		"javax.portlet.security-role-ref=power-user,user"}, service = Portlet.class)
public class SamplePortlet extends GenericPortlet {

	@Override
	protected void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		PrintWriter printWriter = renderResponse.getWriter();

		printWriter.print(_SamplePortletService.getText("text"));
	}

	@Reference
	private SamplePortletService _SamplePortletService;

}