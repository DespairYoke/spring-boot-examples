package com.example.demo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() throws Exception {
		DemoApplicationTests util=   new DemoApplicationTests();
		String ab=util.createJWT("jwt", "{id:100,name:zhuweidong}", 60000);
		System.out.println(ab);
		//eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiJEU1NGQVdEV0FEQVMuLi4iLCJzdWIiOiJ7aWQ6MTAwLG5hbWU6eGlhb2hvbmd9IiwidXNlcl9uYW1lIjoiYWRtaW4iLCJuaWNrX25hbWUiOiJEQVNEQTEyMSIsImV4cCI6MTUxNzgzNTE0NiwiaWF0IjoxNTE3ODM1MDg2LCJqdGkiOiJqd3QifQ.ncVrqdXeiCfrB9v6BulDRWUDDdROB7f-_Hg5N0po980
		String jwt="eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiJEU1NGQVdEV0FEQVMuLi4iLCJzdWIiOiJ7aWQ6MTAwLG5hbWU6eGlhb2hvbmd9IiwidXNlcl9uYW1lIjoiYWRtaW4iLCJuaWNrX25hbWUiOiJEQVNEQTEyMSIsImV4cCI6MTUxNzgzNTEwOSwiaWF0IjoxNTE3ODM1MDQ5LCJqdGkiOiJqd3QifQ.G_ovXAVTlB4WcyD693VxRRjOxa4W5Z-fklOp_iHj3Fg";
		Claims c=util.parseJWT(ab);//注意：如果jwt已经过期了，这里会抛出jwt过期异常。
		System.out.println(c.getId());//jwt
		System.out.println(c.getIssuedAt());//Mon Feb 05 20:50:49 CST 2018
		System.out.println(c.getSubject());//{id:100,name:xiaohong}
		System.out.println(c.getIssuer());//null
		System.out.println(c.get("uid", String.class));//DSSFAWDWADAS...
	}

	public String createJWT(String id, String subject, long ttlMillis) throws Exception {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256 ;
		long nowMillis = System. currentTimeMillis();
		Date now = new Date( nowMillis);
		SecretKey key = generalKey();
		JwtBuilder builder = Jwts. builder()
				.setId(id)
				.setIssuedAt(now)
				.setSubject(subject)
				.signWith(signatureAlgorithm, key);
		if (ttlMillis >= 0){
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date( expMillis);
			builder.setExpiration( exp);
		}
		return builder.compact();
	}

	public Claims parseJWT(String jwt) throws Exception{
		SecretKey key = generalKey();
		Claims claims = Jwts. parser()
				.setSigningKey( key)
				.parseClaimsJws( jwt).getBody();
		return claims;
	}
	/**
	 * 由字符串生成加密key
	 * @return
	 */
	public SecretKey generalKey(){
		String stringKey = "7786df7fc3a34e26a61c034d5ec8245d";//本地配置文件中加密的密文7786df7fc3a34e26a61c034d5ec8245d
		byte[] encodedKey = Base64.decodeBase64(stringKey);//本地的密码解码[B@152f6e2
		//System.out.println(encodedKey);//[B@152f6e2
		System.out.println(Base64.encodeBase64URLSafeString(encodedKey));//7786df7fc3a34e26a61c034d5ec8245d
		SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");// 根据给定的字节数组使用AES加密算法构造一个密钥，使用 encodedKey中的始于且包含 0 到前 leng 个字节这是当然是所有。（后面的文章中马上回推出讲解Java加密和解密的一些算法）
		return key;
	}
}
