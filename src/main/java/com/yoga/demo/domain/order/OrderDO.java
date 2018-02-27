package com.yoga.demo.domain.order;

import java.math.BigDecimal;
import java.util.List;

import com.yoga.demo.domain.BaseDO;

/**
 * 订单
 * 
 * @author yoga
 */
public class OrderDO extends BaseDO{
	private static final long serialVersionUID = -479315892526407528L;

	private Integer id;
	
	private String orderNo;
	
	/**总金额*/
	private BigDecimal totalAmount;
	
	/**用户Id*/
	private Integer userId;
	
	/**订单状态:已取消，待支付，待配送，待收货，待评价，已完成*/
	private Integer status;
	
	private List<OrderItem> orderItems;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
