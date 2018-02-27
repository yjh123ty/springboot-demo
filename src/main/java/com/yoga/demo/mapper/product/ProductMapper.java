package com.yoga.demo.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yoga.demo.domain.product.Product;
import com.yoga.demo.domain.product.dto.SearchProductDTO;
import com.yoga.demo.mapper.BaseMapper;

public interface ProductMapper extends BaseMapper<Product>{

	List<Product> list(SearchProductDTO searchProductDTO);

	int countList(SearchProductDTO searchProductDTO);

	void saveImages(@Param("id")Integer id, @Param("images")List<String> images);
	
}
