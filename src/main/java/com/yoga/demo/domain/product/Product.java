package com.yoga.demo.domain.product;

import java.io.Serializable;
import java.math.BigDecimal;

import com.yoga.demo.domain.BaseDO;
import java.util.Date;

/**
 * product
 */
public class Product extends BaseDO{

	private static final long serialVersionUID = 2000184758655893323L;

	public interface BARGAIN_FLAG{
		/**非特价*/
		int NOT_ON_SELL = 0;
		/**特价*/
		int ON_SELL = 1;
	}
	
    private String name;
    private String description;
    private int quantity;
    private BigDecimal price;
    private int bargainFlag;//是否特价 
    private Boolean delete;//是否删除
    private String image;

    public Product() {
    }

    public Product(Integer id, String name, String description, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getBargainFlag() {
		return bargainFlag;
	}

	public void setBargainFlag(int bargainFlag) {
		this.bargainFlag = bargainFlag;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", description=" + description + ", quantity=" + quantity + ", price=" + price
				+ ", bargainFlag=" + bargainFlag + ", delete=" + delete + "]";
	}

    
    
}
