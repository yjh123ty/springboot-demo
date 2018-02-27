package com.yoga.demo.mapper.cache;

import java.lang.reflect.Field;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.yoga.demo.domain.BaseDO;
import com.yoga.demo.domain.product.Product;
import com.yoga.demo.utils.reflect.ReflectionUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 适用于高并发下的序列化对象，采用自定义序列化的方式
 * 
 * RuntimeSchema<Product>
 * 	用于描述对象，进行序列化与反序列化操作的 schema概要
 * 	通过反射获取对象类型的属性，并通过schema将 字节码byte中的数据赋值给这些属性
 * @author yoga
 */
public class RedisDao {
	
	private static final JedisPool jedisPool;
	
	static {
		jedisPool = new JedisPool(createJedisConfig(), "127.0.0.1", 6379, 0, "Thbl2016TEST");
	}
	
	
	public RedisDao(){
	}
	
	//用于描述对象，进行序列化与反序列化操作的 schema概要
	// 通过反射获取对象类型的属性，并通过schema将 字节码byte中的数据赋值给这些属性
//	private static RuntimeSchema<Product> schema = RuntimeSchema.createFrom(Product.class);
	
	
	/**
	 * 
	 * @param obj 对象
	 * @param key 存入的业务键
	 * @return
	 */
	public static <T> String putEntity(T obj, String key){
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				Class<? extends Object> clz = obj.getClass();
				//采用自定义的反序列化
				@SuppressWarnings("unchecked")
				//用于描述对象，进行序列化与反序列化操作的 schema概要
				// 通过反射获取对象类型的属性，并通过schema将 字节码byte中的数据赋值给这些属性
				RuntimeSchema<T> schema = (RuntimeSchema<T>) RuntimeSchema.createFrom(clz); 
				byte[] bytes = ProtostuffIOUtil.toByteArray(obj, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				//超时缓存
				int seconds = 60 * 60;
				String result = jedis.setex(key.getBytes(), seconds , bytes);
				return result;
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * 缓存中获取对象信息
	 * @param clz
	 * @param key
	 * @return
	 */
	public static <T> Object getEntity(Class<?> clz, String key){
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				//采用自定义的序列化， protostuff 用一个schema来描述对象
				byte[] bytes = jedis.get(key.getBytes());
				//缓存获取到数据
				if(null != bytes){
					//用于描述对象，进行序列化与反序列化操作的 schema概要
					// 通过反射获取对象类型的属性，并通过schema将 字节码byte中的数据赋值给这些属性
					RuntimeSchema<T> schema = (RuntimeSchema<T>) RuntimeSchema.createFrom(clz);
					//构造一个空对象
					T ojb = schema.newMessage();
					//使用该技术，可以将jdk控件压缩到原生jdk的十分之一 到 五分之一 ， 节省cpu
					ProtostuffIOUtil.mergeFrom(bytes, ojb, schema);
					return ojb;
				}else{
					return null;
				}
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	/**
	 * 从缓存中删除
	 * @param key
	 */
	public static void deleteProduct(String key){
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				jedis.del(key);
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 构建配置
	 * @return
	 */
	private static JedisPoolConfig createJedisConfig() {  
        JedisPoolConfig jedisConfig = new JedisPoolConfig();  
        jedisConfig.setMaxIdle(20);  
        jedisConfig.setMaxTotal(100);
        jedisConfig.setTestOnBorrow(true);
        jedisConfig.setMinIdle(8);
        jedisConfig.setMaxWaitMillis(10000);
        return jedisConfig;  
    }  
	
	public static void main(String[] args) {
		Product product = new Product();
		product.setId(10);
		product.setName("测试商品3");
		System.err.println(getEntity(Product.class, "Product:10"));
		
//		System.err.println(getEntity(Product.class, "Product:10"));
//		deleteProduct("Product:10");
//		System.err.println("删除商品。");
//		System.err.println("删除后：" + getProduct(10));
		
//		System.err.println(putEntity(product, "Product:".concat(product.getId().toString())));
		System.err.println(getEntity(Product.class, "Product:10"));
		
		
	}
	
}

