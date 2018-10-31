package com.liferay.samples2;

/**
 * @author Diego Furtado
 */
public class OrderChange {

	public OrderChange(OrderDao orderDao) {
		_orderDao = orderDao;
	}

	public void change(Long codeChange) throws Exception {
		Order order = _orderDao.obtain(codeChange);

		if (OrderStatus.CANCEL == order.getStatus()) {
			throw new Exception(
				"It is not allowed to exchange a canceled order");
		}
		else {
			System.out.println("It is allowed to change the order");
		}
	}

	private OrderDao _orderDao;

}