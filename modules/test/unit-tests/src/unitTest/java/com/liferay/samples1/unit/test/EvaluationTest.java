package com.liferay.samples1.unit.test;

import com.liferay.samples1.Evaluation;
import com.liferay.samples1.IStudent;

import org.junit.Assert;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Diego Furtado
 */
public class EvaluationTest {

	@Test
	public void testStudentApproved() {
		Mockito.when(
			_student.getTestGrade()
		).thenReturn(
			7.0
		);

		Mockito.when(
			_student.getWorkGrade()
		).thenReturn(
			7.0
		);

		String result = _evaluation.evaluate(_student);

		Assert.assertEquals("Approved", result);
	}

	@Test
	public void testStudentProofOfRecovery() {
		Mockito.when(
			_student.getTestGrade()
		).thenReturn(
			6.9
		);

		Mockito.when(
			_student.getWorkGrade()
		).thenReturn(
			0.0
		);

		String result = _evaluation.evaluate(_student);

		Assert.assertEquals("You will have to take proof of recovery", result);
	}

	@Test
	public void testStudentResetTheJob() {
		Mockito.when(
			_student.getTestGrade()
		).thenReturn(
			7.0
		);

		Mockito.when(
			_student.getWorkGrade()
		).thenReturn(
			6.9
		);

		String result = _evaluation.evaluate(_student);

		Assert.assertEquals("You will need to reset the job", result);
	}

	private Evaluation _evaluation = new Evaluation();
	private IStudent _student = Mockito.mock(IStudent.class);

}