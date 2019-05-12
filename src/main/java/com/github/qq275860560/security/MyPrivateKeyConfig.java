package com.github.qq275860560.security;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;

import com.github.qq275860560.service.SecurityService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Configuration
@Slf4j
public class MyPrivateKeyConfig {
	@Autowired
	private SecurityService securityService;

	@Bean
	public PrivateKey privateKey() throws Exception {
		log.trace("私钥配置");
		/*
		 * String jwtJksFileName = "jwt.jks"; String jwtJksPassword = "123456"; String
		 * jwtJksAlias = "jwt"; InputStream inputStream =
		 * Thread.currentThread().getContextClassLoader().getResourceAsStream(
		 * jwtJksFileName); KeyStore keyStore = KeyStore.getInstance("JKS");
		 * keyStore.load(inputStream, jwtJksPassword.toCharArray()); return (PrivateKey)
		 * keyStore.getKey(jwtJksAlias, jwtJksPassword.toCharArray());
		 */
		String privateKey = securityService.getPrivateKeyString();
		byte[] keyBytes = Base64Utils.decode(privateKey.getBytes());
		PKCS8EncodedKeySpec keySpec_privateKey = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory_privateKey = KeyFactory.getInstance("RSA");
		return keyFactory_privateKey.generatePrivate(keySpec_privateKey);

	}

}
