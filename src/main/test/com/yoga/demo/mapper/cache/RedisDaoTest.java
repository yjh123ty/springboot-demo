package com.yoga.demo.mapper.cache;

import static org.junit.Assert.*;

import org.apache.catalina.core.ApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yoga.demo.BaseTest;
import com.yoga.demo.domain.product.Product;
import com.yoga.demo.mapper.product.ProductMapper;

public class RedisDaoTest extends BaseTest{
	
	private int id = 1;
	
	@Autowired
	private ProductMapper productMapper;
	

	@Test
	public void testGet() {
		RedisDao redisDao = new RedisDao();
		Product product = (Product) redisDao.getEntity(Product.class, "Product:".concat(String.valueOf(id)));
		if(null == product){
			product = productMapper.selectByPrimaryKey(id);
			if(product != null){
				String result = redisDao.putEntity(product, "Product:".concat(product.getId().toString()));
				System.err.println("result : " + result);
				product = (Product) redisDao.getEntity(Product.class, "Product:".concat(String.valueOf(id)));
				System.err.println("get from cache : " + product);
			}
		}
	}

}
