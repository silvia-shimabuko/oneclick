package com.liferay.samples2.unit.test;

import com.liferay.samples2.Order;
import com.liferay.samples2.OrderChange;
import com.liferay.samples2.OrderDao;
import com.liferay.samples2.OrderStatus;

import org.junit.Assert;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Diego Furtado
 */
public class OrderTest {

	@Test
	public void anErrorOccursWhenAttemptingToChangeTheCanceledOrder()
		throws Exception {

		try {
			_order.setStatus(OrderStatus.CANCEL);

			Mockito.doReturn(_order
			).when(
				_orderDao
			).obtain(
				Long.valueOf(777)
			);

			OrderChange orderChange = new OrderChange(_orderDao);

			orderChange.change(Long.valueOf(777));
		}
		catch (Exception e) {
			Assert.assertTrue(
				e.getMessage().contains("It is not allowed to exchange a canceled order"));
		}
	}

	private Order _order = new Order();
	private OrderDao _orderDao = Mockito.mock(OrderDao.class);

}