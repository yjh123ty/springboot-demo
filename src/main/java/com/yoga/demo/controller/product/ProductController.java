package com.yoga.demo.controller.product;

import com.yoga.demo.common.JsonMsgBean;
import com.yoga.demo.common.Page;
import com.yoga.demo.common.annotation.WebLog;
import com.yoga.demo.domain.product.Product;
import com.yoga.demo.domain.product.dto.SearchProductDTO;
import com.yoga.demo.service.ProductService;
import com.yoga.demo.utils.result.JsonMsgBeanUtils;
import com.yoga.demo.utils.upload.ImageUploadUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 商品
 * 
 * @author yoga
 */
@Controller
@RequestMapping("product")
public class ProductController {
	@Value("${img_host}")
	private String imgHost;
	
	@Autowired
	private ProductService productService;
	
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView modelAndView = new ModelAndView("/pages/product/index");
		return modelAndView;
	}
	
	@RequestMapping(value = "getOne",method = RequestMethod.GET)
	@ResponseBody
    public JsonMsgBean listProducts(@RequestParam Integer id){
		if (id != null){
			Product product = productService.selectByPrimaryKey(id);
			if (product != null){
				return JsonMsgBeanUtils.defaultSeccess(product);
			}else {
				return JsonMsgBeanUtils.fail("未找到该商品", 404);
			}
		}else{
			return JsonMsgBeanUtils.fail("请输入id", 401);
		}
    }

	@RequestMapping(value = "products",method = RequestMethod.GET)
	@ResponseBody
	public Page<Product> listProducts(SearchProductDTO searchProductDTO){
		Page<Product> page = productService.listProducts(searchProductDTO);
		return page;
	}
	
	@RequestMapping(value = "edit",method = RequestMethod.GET)
    public ModelAndView toEdit(Integer id, Model model){
		ModelAndView mav = new ModelAndView("pages/product/edit");
		if(null != id){
			Product product = productService.selectByPrimaryKey(id);
			if(product != null){
				mav.addObject("product", product);
			}
		}
        return mav;
    }
	
	
	@RequestMapping(value = "saveOrUpdate",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("product:edit")
	public JsonMsgBean upload(Product product, @RequestParam("images")MultipartFile[] images){
		List<String> imagesStr = new ArrayList<String>();
		if(StringUtils.isNotBlank(images[0].getOriginalFilename()) && images != null){
			String dirPath = "product".concat("/").concat(UUID.randomUUID().toString().replaceAll("-", StringUtils.EMPTY));
			String uploadImgUrl = ImageUploadUtils.uploadImgDefault(images[0], dirPath);
			//保存第一张图片为展示图
			product.setImage(imgHost.concat(uploadImgUrl));
			System.out.println("filename : " + images[0].getOriginalFilename());
			imagesStr.add(uploadImgUrl);
			for (int i = 1; i < images.length; i++) {
				uploadImgUrl = ImageUploadUtils.uploadImgDefault(images[i], dirPath);
				imagesStr.add(imgHost.concat(uploadImgUrl));
			}
		}
		if(product.getId() != null){
			productService.updateByPrimaryKeySelective(product);
		}else{
			productService.insert(product, imagesStr);
		}
		Product p = productService.selectByPrimaryKey(product.getId());
		String image = p.getImage();
		return JsonMsgBeanUtils.defaultSeccess(image);
	}
	
	@RequestMapping(value = "delete",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("product:del")
	@WebLog(desc = "删除商品")
	public JsonMsgBean delete(Integer id){
		productService.deleteByPrimaryKey(id);
		return JsonMsgBeanUtils.defaultSeccess();
	}
}
