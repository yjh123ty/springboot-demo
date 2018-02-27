package com.yoga.demo.utils;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

public final class Tools {
	
	public static final String ENCRYPT_KEY = "123456";

	public static final char UNDERLINE = '_';

	public static final String createGUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}

	/**
	 * 生成单号（订单号、发货单号等）
	 * 
	 * @return
	 */
	public static final String createSerialID() {
		SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmm");
		final int maxNum = 10;
		int i;
		int count = 0;
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer sb = new StringBuffer(format.format(new Date()));
		Random r = new Random();
		int len = 5;
		while (count < len) {
			i = Math.abs(r.nextInt(maxNum));
			if (i >= 0 && i < str.length) {
				sb.append(str[i]);
				count++;
			}
		}
		return sb.toString();
	}

	public static final boolean isEmail(String input) {
		Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
		Matcher m = p.matcher(input);
		boolean b = m.matches();
		return b;
	}

	public static final boolean isMobile(String input) {
		return Pattern.matches("^((13[0-9])|(15[^4,\\D])|(14[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$", input);
	}

	public static final Map<String, String> sortFormat(Map<String, String> sort) {
		Map<String, String> sortMap = new HashMap<String, String>();
		if (null != sort && !sort.isEmpty()) {
			// 排序字段名统一转换成下划线+小写字母
			for (Map.Entry<String, String> entry : sort.entrySet()) {
				sortMap.put(nameFormat(entry.getKey()), entry.getValue());
			}
		}
		return sortMap;
	}

	public static String nameFormat(String name) {
		StringBuilder result = new StringBuilder();
		if (name != null && name.length() > 0) {
			// 将第一个字符处理成大写
			result.append(name.substring(0, 1).toLowerCase());
			// 循环处理其余字符
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				// 在大写字母前添加下划线
				if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
					result.append("_");
				}
				// 其他字符直接转成大写
				result.append(s.toLowerCase());
			}
		}
		return result.toString();
	}

	/**
	 * 生成随即密码
	 * 
	 * @param pwd_len
	 *            生成的密码的总长度
	 * @return 密码的字符串
	 */
	public static String genRandomString(int len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
				't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer sb = new StringBuffer("");
		Random r = new Random();
		while (count < len) {
			// 生成随机数，取绝对值，防止生成负数，

			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1

			if (i >= 0 && i < str.length) {
				sb.append(str[i]);
				count++;
			}
		}

		return sb.toString();
	}

	public static final String urlEncode(String s) {
		try {
			return URLEncoder.encode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static final String urlDecode(String s) {
		try {
			return URLDecoder.decode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static final Date dateTimeAdd(Date date, int field, int amount) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	public static final boolean dateEquals(Date date1, Date date2) {
		Calendar calendar1 = new GregorianCalendar();
		calendar1.setTime(date1);
		Calendar calendar2 = new GregorianCalendar();
		calendar2.setTime(date2);
		long diff = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
		int days = new Long(diff / (24 * 60 * 60 * 1000)).intValue();
		int day1OfYear = calendar1.get(Calendar.DAY_OF_YEAR);
		int day2OfYear = calendar2.get(Calendar.DAY_OF_YEAR);
		return days == 0 && (day2OfYear - day1OfYear) == 0;
	}

	public static final String defaultDateFormat(Date date) {
		return dateFormat(date, "yyyy-MM-dd");
	}

	public static final String dateFormat(Date date, String foramt) {
		SimpleDateFormat sdf = new SimpleDateFormat(foramt);
		return sdf.format(date);
	}

	public static final String getRandomNumber(int len) {
		final int maxNum = 10;
		int i;
		int count = 0;
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		while (count < len) {
			i = Math.abs(r.nextInt(maxNum));
			if (i >= 0 && i < str.length) {
				sb.append(str[i]);
				count++;
			}
		}
		return sb.toString();
	}

	/**
	 * 将emoji表情替换成*
	 * 
	 * @param source
	 * @return 过滤后的字符串
	 */
	public static String filterEmoji(String source) {
		if (StringUtils.isNotBlank(source)) {
			return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
		} else {
			return source;
		}
	}

	/**
	 * 处理含sql注入风险的字符
	 * @param str
	 * @return
	 */
	public static String replaceBadSql(String str) {
		if(StringUtils.isEmpty(str))return str;
		str = str.toLowerCase();// 统一转为小写
		// 过滤掉的sql关键字，可以手动添加
		String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|"
				+ "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|"
				+ "table|from|grant|use|group_concat|column_name|"
				+ "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|"
				+ "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";
		String[] badStrs = badStr.split("\\|");
		for (int i = 0; i < badStrs.length; i++) {
			if (str.indexOf(badStrs[i]) >= 0) {
				str = str.replace(badStrs[i], "");
			}
		}
		return str;
	}

	/**
	 * 获取当前网络ip
	 * 
	 * @param request
	 * @return
	 */
	public static final String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
					if (null != inet)
						ipAddress = inet.getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	/**
	 * 设置cookie
	 * 
	 * @param cookies
	 * @param request
	 */
	public static final void setCookies(Map<String, Object> cookies, String domain, Integer expiry,
			HttpServletResponse response) {
		if (null == cookies || cookies.isEmpty())
			return;
		for (Map.Entry<String, Object> entry : cookies.entrySet()) {
			Cookie cookie = new Cookie(entry.getKey(), entry.getValue().toString());
			cookie.setDomain(domain == null ? "" : domain);
			cookie.setPath("/");
			cookie.setMaxAge(expiry == null ? Integer.MAX_VALUE : expiry);
			response.addCookie(cookie);
		}
	}

	/**
	 * base 64 decode
	 * 
	 * @param base64Code
	 *            待解码的base 64 code
	 * @return 解码后的byte[]
	 * @throws Exception
	 */
	public static byte[] base64Decode(String base64Code) throws Exception {
		return StringUtils.isEmpty(base64Code) ? null : Base64.decodeBase64(base64Code);
	}

	/**
	 * AES加密
	 * 
	 * @param content
	 *            待加密的内容
	 * @param encryptKey
	 *            加密密钥
	 * @return 加密后的byte[]
	 * @throws Exception
	 */
	public static String aesEncrypt(String content, String encryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(encryptKey.getBytes());
		kgen.init(128, random);

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

		return parseByte2HexStr(cipher.doFinal(content.getBytes("utf-8")));
	}

	/**
	 * AES解密
	 * 
	 * @param encryptString
	 *            待解密的string
	 * @param decryptKey
	 *            解密密钥
	 * @return 解密后的String
	 * @throws Exception
	 */
	public static String aesDecrypt(String encryptString, String decryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(decryptKey.getBytes());
		kgen.init(128, random);

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
		byte[] decryptBytes = cipher.doFinal(parseHexStr2Byte(encryptString));

		return new String(decryptBytes);
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @method parseByte2HexStr
	 * @param buf
	 * @return
	 * @throws @since
	 *             v1.0
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @method parseHexStr2Byte
	 * @param hexStr
	 * @return
	 * @throws @since
	 *             v1.0
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	public static String camelToUnderline(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(UNDERLINE);
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	

	public static void main(String[] args) throws Exception {
		System.out.println(isMobile("19600022222"));
		String content = "123";
		System.out.println("加密前：" + content);

		String key = "123456";
		System.out.println("加密密钥和解密密钥：" + key);

		String encrypt = aesEncrypt(content, key);
		System.out.println("加密后：" + encrypt);

		String decrypt = aesDecrypt(encrypt, key);
		System.out.println("解密后：" + decrypt);
	}
	
}
