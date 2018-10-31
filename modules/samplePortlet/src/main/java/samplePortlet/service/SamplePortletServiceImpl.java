package samplePortlet.service;

import org.osgi.service.component.annotations.Component;

/**
 * @author Manoel Cyreno
 */
@Component(immediate = true, service = SamplePortletService.class)
public class SamplePortletServiceImpl implements SamplePortletService {

	@Override
	public String getText(String someText) {
		String text = someText;

		if (text != null && text != "") {
			text = "Sample Portlet - Hello World!";
		} else {
			text = "Sample Portlet - Error!";
		}

		return text;
	}

}