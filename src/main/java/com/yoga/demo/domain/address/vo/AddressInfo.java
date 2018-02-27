package com.yoga.demo.domain.address.vo;

public class AddressInfo {
	private String address;
	private String pointX;
	private String pointY;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPointX() {
		return pointX;
	}
	public void setPointX(String pointX) {
		this.pointX = pointX;
	}
	public String getPointY() {
		return pointY;
	}
	public void setPointY(String pointY) {
		this.pointY = pointY;
	}
	@Override
	public String toString() {
		return "AddressInfo [address=" + address + ", pointX=" + pointX + ", pointY=" + pointY + "]";
	}
	
	
}
