package com.liferay.samples2;

/**
 * @author Diego Furtado
 */
public class Order {

	public OrderStatus getStatus() {
		return _status;
	}

	public void setStatus(OrderStatus status) {
		_status = status;
	}

	private OrderStatus _status;

}