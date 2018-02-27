package com.yoga.demo.utils.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

public final class HttpUtils {
	
	public static String get(String url) {
		CloseableHttpClient httpClient = null;
		if(url.startsWith("https://")){
			enableSSL();
			RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
					.setExpectContinueEnabled(true).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
					.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", socketFactory).build();
			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(defaultRequestConfig).build();
		}else{
			httpClient =  HttpClients.createDefault();
		}
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		try{
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				httpClient.close();
				if(null != response)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static JSONObject getRemoteJSON(String url){
		String response = get(url);
		return JSONObject.fromObject(response);
	}
	
	public static String post(String uri, String requestBody, String contentType){
		return post(uri, requestBody, contentType, null);
	}

	public static String post(String uri, String requestBody, String contentType, Map<String, String> headerMap){
		String body = "";
		HttpClient httpClient =  HttpClients.createDefault();
		HttpPost post = new HttpPost(uri);
		StringEntity stringEntity = new StringEntity(requestBody, "utf-8");
		post.setEntity(stringEntity);
		post.setHeader("Content-Type", contentType);
		if(null != headerMap && !headerMap.isEmpty()){
			for(Map.Entry<String, String> entry : headerMap.entrySet()){
				post.setHeader(entry.getKey(), entry.getValue());
			}
		}
		try {
			HttpResponse response = httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			body = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			httpClient.getClass();
		}
		return body;
	}
	

	public static String post(String uri, Map<String, Object> requestBody, String contentType,  Map<String, String> headerMap){
		String body = "";
		HttpClient httpClient =  HttpClients.createDefault();
		HttpPost post = new HttpPost(uri);
		if(null != requestBody && !requestBody.isEmpty()){
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			for(Map.Entry<String, Object> entry : requestBody.entrySet()){
				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
			StringEntity entity = null;
			try {
				entity = new UrlEncodedFormEntity(parameters);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			post.setEntity(entity);
		}
		post.setHeader("Content-Type", contentType);
		if(null != headerMap && !headerMap.isEmpty()){
			for(Map.Entry<String, String> entry : headerMap.entrySet()){
				post.setHeader(entry.getKey(), entry.getValue());
			}
		}
		try {
			HttpResponse response = httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			body = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			httpClient.getClass();
		}
		return body;
	}
	
	public static String post(String uri, JSONObject requestBody){
		String body = "";
		HttpClient httpClient =  HttpClients.createDefault();
		HttpPost post = new HttpPost(uri);
		if(null != requestBody){
			post.setHeader("Accept", "application/json");
			post.addHeader("Content-Type", "application/json; Charset=utf-8");
			post.setEntity(new StringEntity(requestBody.toString(), Charset.forName("utf-8")));
		}
		try {
			HttpResponse response = httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			body = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			httpClient.getClass();
		}
		return body;
	}
	
	public static String download(String uri, String realPath, String uploadFolder){
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(uri);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				Calendar calendar = Calendar.getInstance();
				String filename = System.currentTimeMillis()+".jpg";
				String filePath = uploadFolder + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/"+filename;
				File dir = new File(realPath + uploadFolder + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/");
				if(!dir.exists())
					dir.mkdirs();
		        InputStream input = entity.getContent();
		        OutputStream output = new FileOutputStream(new File(realPath + filePath));
		        IOUtils.copy(input, output);
		        output.flush();
		        return filePath;
		      }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				response.close();
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static TrustManager manager = new X509TrustManager() {
		
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			// TODO Auto-generated method stub
			
		}
	};
	
	private static void enableSSL(){
		try {
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new TrustManager[]{manager}, null);
			socketFactory = new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}
	
	private static SSLConnectionSocketFactory socketFactory;
}
