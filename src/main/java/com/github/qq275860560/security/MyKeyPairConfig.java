package com.github.qq275860560.security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.util.Base64Utils;

import com.github.qq275860560.service.SecurityService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Configuration
@Slf4j
public class MyKeyPairConfig {

	@Autowired
	private SecurityService securityService;

	@Bean
	public PublicKey getPublicKey() throws Exception {
		log.debug("公钥配置");
		/*
		 * String jwtJksFileName = "jwt.jks"; String jwtJksPassword = "123456"; String
		 * jwtJksAlias = "jwt"; InputStream inputStream =
		 * Thread.currentThread().getContextClassLoader().getResourceAsStream(
		 * jwtJksFileName); KeyStore keyStore = KeyStore.getInstance("JKS");
		 * keyStore.load(inputStream, jwtJksPassword.toCharArray()); return
		 * keyStore.getCertificate(jwtJksAlias).getPublicKey();
		 */
		String publicKeyBase64EncodeString = securityService.getPublicKeyBase64EncodeString();
		byte[] keyBytes = Base64Utils.decode(publicKeyBase64EncodeString.getBytes());
		X509EncodedKeySpec keySpec_publicKey = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory_publicKey = KeyFactory.getInstance("RSA");
		return keyFactory_publicKey.generatePublic(keySpec_publicKey);

	}
	
	@Bean
	public PrivateKey getPrivateKey() throws Exception {
		log.debug("私钥配置");
		/*
		 * String jwtJksFileName = "jwt.jks"; String jwtJksPassword = "123456"; String
		 * jwtJksAlias = "jwt"; InputStream inputStream =
		 * Thread.currentThread().getContextClassLoader().getResourceAsStream(
		 * jwtJksFileName); KeyStore keyStore = KeyStore.getInstance("JKS");
		 * keyStore.load(inputStream, jwtJksPassword.toCharArray()); return (PrivateKey)
		 * keyStore.getKey(jwtJksAlias, jwtJksPassword.toCharArray());
		 */
		String privateKeyBase64EncodeString = securityService.getPrivateKeyBase64EncodeString();
		byte[] keyBytes = Base64Utils.decode(privateKeyBase64EncodeString.getBytes());
		PKCS8EncodedKeySpec keySpec_privateKey = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory_privateKey = KeyFactory.getInstance("RSA");
		return keyFactory_privateKey.generatePrivate(keySpec_privateKey);

	}
	
 
	@Bean
	public KeyPair getKeyPair() throws Exception {
		return new KeyPair(getPublicKey(),getPrivateKey());
	}
	
	@Bean
	public RsaVerifier getRsaVerifier() throws Exception {
		return new RsaVerifier((RSAPublicKey)getPublicKey() );
	}
	 
	@Bean
	public RsaSigner getRsaSigner() throws Exception {
		return new RsaSigner((RSAPrivateKey)getPrivateKey() );
	}

}
