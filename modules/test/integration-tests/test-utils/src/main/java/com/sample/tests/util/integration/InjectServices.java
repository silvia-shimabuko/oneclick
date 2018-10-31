package com.sample.tests.util.integration;

import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.test.rule.BaseTestRule;
import com.liferay.portal.kernel.test.rule.callback.BaseTestCallback;

import java.lang.reflect.Field;

import java.util.stream.Stream;

import javax.inject.Inject;

import org.junit.rules.TestRule;
import org.junit.runner.Description;

import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Nathan Ferracini
 */
public class InjectServices extends BaseTestCallback<Object, Object> {

	public static final TestRule instance = new BaseTestRule<>(
		new InjectServices());

	@Override
	public Object beforeMethod(Description description, Object target)
		throws Throwable {

		Field[] fields = target.getClass().getDeclaredFields();

		Stream.of(fields).filter(
			f -> f.isAnnotationPresent(Inject.class)
		).forEach(
			f -> _inject(target, f)
		);

		return target;
	}

	@SuppressWarnings("unchecked")
	private <T> T _getBean(Class<T> klass) throws Exception {
		return (T)PortalBeanLocatorUtil.locate(klass.getName());
	}

	private Object _getInjection(Field f) {
		Class<?> type = f.getType();
		Object injection = null;

		try {
			injection = _getBean(type);
		}
		catch (Exception ex) {
		}

		try {
			injection = _getService(type);
		}
		catch (Exception ex) {
		}

		if (injection == null) {
			return _throwInjectionError(f);
		}

		return injection;
	}

	private <T> T _getService(Class<T> klass) {
		ServiceTracker<T, T> serviceTracker = ServiceTrackerFactory.open(klass);

		return serviceTracker.getService();
	}

	private void _inject(Object target, Field f) {
		try {
			f.setAccessible(true);
			f.set(target, _getInjection(f));
		}
		catch (IllegalAccessException iae) {
			_throwInjectionError(f);
		}
		finally {
			f.setAccessible(false);
		}
	}

	private Object _throwInjectionError(Field f) {
		throw new IllegalStateException(
			">>> Unable to inject " + f.getName() + " of type " + f.getType());
	}

}