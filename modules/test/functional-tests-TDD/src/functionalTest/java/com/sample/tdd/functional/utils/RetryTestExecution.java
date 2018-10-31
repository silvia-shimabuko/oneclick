package com.sample.tdd.functional.utils;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author Manoel Cyreno
 */
public class RetryTestExecution implements TestRule {

	public RetryTestExecution(int retryCount) {
		_retryCount = retryCount;
	}

	public Statement apply(Statement base, Description description) {
		return _statement(base, description);
	}

	private Statement _statement(
		final Statement base, final Description description) {

		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				Throwable caughtThrowable = null;

				for (int i = 0; i < _retryCount; i++) {
					try {
						base.evaluate();

						return;
					}
					catch (Throwable t) {
						caughtThrowable = t;
						System.err.println(
							description.getDisplayName() + ": run " + (i + 1) +
								" failed");
					}
				}

				System.err.println(
					description.getDisplayName() + ": giving up after " +
						_retryCount + " failures");

				throw caughtThrowable;
			}

		};
	}

	private int _retryCount;

}