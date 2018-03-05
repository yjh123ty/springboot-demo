package com.yoga.demo.service;

import java.util.List;

import com.yoga.demo.common.Page;
import com.yoga.demo.domain.product.Product;
import com.yoga.demo.domain.product.dto.SearchProductDTO;

public interface ProductService {

	Page<Product> listProducts(SearchProductDTO searchProductDTO);

	Product selectByPrimaryKey(Integer id);

	void updateByPrimaryKeySelective(Product product);

	void insert(Product product, List<String> images);

	void deleteByPrimaryKey(Integer id);

}
