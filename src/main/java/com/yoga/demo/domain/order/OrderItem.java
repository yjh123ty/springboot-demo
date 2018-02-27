package com.yoga.demo.domain.order;

import com.yoga.demo.domain.BaseDO;

/**
 * 订单子项
 * 
 * @author yoga
 */
public class OrderItem extends BaseDO{

	private static final long serialVersionUID = 1410019978992170242L;

	private Integer orderId;

	private Integer productId;

	private Integer number;

	private Integer amount;

	private String note;

	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", orderId=" + orderId + ", productId=" + productId + ", number=" + number
				+ ", amount=" + amount + ", note=" + note + "]";
	}
	
	
	
	
	
}
