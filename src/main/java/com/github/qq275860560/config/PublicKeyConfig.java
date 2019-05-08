package com.github.qq275860560.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Configuration
@Slf4j
public class PublicKeyConfig {

	@Value("${jwtJksFileName}")
	private String jwtJksFileName;
	@Value("${jwtJksPassword}")
	private String jwtJksPassword;
	@Value("${jwtJksAlias}")
	private String jwtJksAlias;

	@Bean
	public PublicKey publicKey() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(jwtJksFileName);

		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(inputStream, jwtJksPassword.toCharArray());

		return keyStore.getCertificate(jwtJksAlias).getPublicKey();

	}

}
