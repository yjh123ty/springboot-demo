package com.yoga.demo.utils.token;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JwtUtils {
  
  private static final String API_KEY = "demo";
  
  private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
  
  private static final Key KEY;

  private static final String ISSUER = "demo";
  
  static {
    KEY = new SecretKeySpec(
            DatatypeConverter.parseBase64Binary(API_KEY), SIGNATURE_ALGORITHM.getJcaName()
            );
  }

  /** 根据用户的id创建token */
  public static String createToken(String uid, long ttlMillis) {
      long nowMillis = System.currentTimeMillis();
      Date now = new Date(nowMillis);
      JwtBuilder builder = Jwts.builder()
                                      .setIssuedAt(now)
                                      .setSubject(uid)
                                      .setIssuer(ISSUER)
                                      .signWith(SIGNATURE_ALGORITHM, KEY);
      if (ttlMillis >= 0) {
          long expMillis = nowMillis + ttlMillis;
          Date exp = new Date(expMillis);
          builder.setExpiration(exp);
      }
      return builder.compact();
  }

  /** 验证用户的token从里面获取token */
  public static String validateToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
    Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    String uid = claims.getSubject();
    Jwts.parser()
      .requireSubject(uid)
      .requireIssuer(ISSUER)
      .setSigningKey(KEY)
      .parseClaimsJws(token);
    return uid;
  }
  
  public static void main(String[] args) {
    System.out.println(createToken("1", 2000l));
  }
}
