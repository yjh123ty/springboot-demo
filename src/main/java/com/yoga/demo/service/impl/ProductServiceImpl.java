package com.yoga.demo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yoga.demo.common.Page;
import com.yoga.demo.domain.product.Product;
import com.yoga.demo.domain.product.dto.SearchProductDTO;
import com.yoga.demo.mapper.product.ProductMapper;
import com.yoga.demo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductMapper productMapper;

	@Override
	public Page<Product> listProducts(SearchProductDTO searchProductDTO) {
		Page<Product> page = new Page<Product>(searchProductDTO.getPageNumber(), searchProductDTO.getPageSize(), productMapper.countList(searchProductDTO));
        if(page.getTotal() > 0) {
            page.setRows(productMapper.list(searchProductDTO));
        } else {
            page.setRows(new ArrayList<Product>());
        }
		return page;
	}

	@Override
	public Product selectByPrimaryKey(Integer id) {
		return productMapper.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(Product product) {
		productMapper.updateByPrimaryKeySelective(product);
	}

	@Override
	public void insert(Product product, List<String> images) {
		product.setDelete(Boolean.FALSE);
		product.setCreateTime(new Date());
		productMapper.insert(product);
		if(CollectionUtils.isNotEmpty(images))
			//保存商品图片
			productMapper.saveImages(product.getId(), images);
	}

	
}
