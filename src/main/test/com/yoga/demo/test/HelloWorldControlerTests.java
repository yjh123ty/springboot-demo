package com.yoga.demo.test;

import com.yoga.demo.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.yoga.demo.controller.test.HelloWorldController;
import com.yoga.demo.mapper.user.UserInfoMapper;

public class HelloWorldControlerTests extends BaseTest {
	private MockMvc mvc;
	
//	@Autowired
//    private User1Mapper User1Mapper;
//	@Autowired
//    private User2Mapper User2Mapper;
	
	@Autowired
	UserInfoMapper userInfoMapper;
	
    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new HelloWorldController()).build();
    }
    @Test
    public void getMVCResult() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/login").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    
//    @Test
//    public void testInsert() throws Exception {
//        User1Mapper.insert(new User("bbb"));
//        User1Mapper.insert(new User("ccc"));
//        User1Mapper.insert(new User("dddd"));
// 
//        Assert.assertEquals(4, User1Mapper.getAll().size());
//    }
//    
//    @Test
//    public void testDel() throws Exception {
//        User1Mapper.delete(9L);
// 
//        Assert.assertEquals(3, User1Mapper.getAll().size());
//    }
//    
//    @Test
//    public void testAll() throws Exception {
//        List<User> list = User1Mapper.getAll();
//        for (User user : list) {
//        	System.err.println(user.toString());
//		}
//    }
//    
//    @Test
//    public void testOne() throws Exception {
//        User user = User2Mapper.getOne(100L);
//    	System.err.println(user.toString());
//    }
}
