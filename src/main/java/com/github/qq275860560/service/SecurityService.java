package com.github.qq275860560.service;

import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author jiangyuanlin@163.com
 *
 */
public abstract class SecurityService {
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	/**用户密码加密策略(如果使用spring默认的springBCryptPasswordEncoder,不需要重写该方法)
	 * @param rawPassword 用户登录时输入的明文密码
	 * @return 数据库中的密码
	 */
	public String encode(CharSequence rawPassword) {
		return passwordEncoder.encode(rawPassword);// spring推荐使用该方式加密
	}

	/**用户密码核对策略(如果使用spring默认的springBCryptPasswordEncoder,不需要重写该方法)
	 * @param rawPassword 用户登录时输入的明文密码
	 * @param encodedPassword 数据库中加密后的密码
	 * @return
	 */
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	};

	/**根据登录账号查询密码
	  * 根据登录账号查询密码，此密码非明文密码，而是PasswordEncoder对明文加密后的密码，因为
	 * spring security框架中数据库默认保存的是PasswordEncoder对明文加密后的密码
	 * 
	 * @param username 登录用户名称
	 * @return 返回字符串
	 */
	public String getPasswordByUserName(String username) {
		return passwordEncoder.encode("123456");// 数据库查出来的密码，默认每个用户都是123456的加密
	}

	/**
	 *   根据请求路径查询对应的角色名称列表，
	 *   登录用户至少拥有一个角色，才能访问
	 *   如果返回null或空集合或包含ROLE_ANONYMOUS，代表该url不需要权限控制，任何用户(包括匿名)用户都可以访问
	 *   如果url符合某个正则表达式，应当把正则表达式的角色也返回，比如/api/a的角色为ROLE_1,ROLE_2, 而数据库中还存在/api/*的角色为ROLE_3,ROLE_4；由于/api/a属于正则表达式/api/*,所以应当返回ROLE_1,ROLE_2,ROLE_3,ROLE_4
	 * @param url 请求路径（ip端口之后的路径）
	 * @return
	 */
	public Set<String> getRoleNameSetByUrI(String url){
		return new HashSet<String>() {{
			add("ROLE_ANONYMOUS");// 数据库查出来的url角色权限，默认只要具有ROLE_ANONYMOUS角色的用户即可访问
		}};
	}

	/**
	 *   根据登录用户查询对应的角色名称列表，
	 *   如果返回null或空集合，代表该用户没有权限，这类用户其实跟匿名用户没有什么区别
	 *   如果username隶属于某高层次的角色或组织，应当把高层次的角色或组织对应的角色也返回，比如username的角色为ROLE_1, ROLE_1继承ROLE_2角色，并且username属于A部门，A部门拥有角色ROLE_3；所以应当返回ROLE_1,ROLE_2,ROLE_3
	 * @param username 登录用户名称
	 * @return
	 */
	public Set<String> getRoleNameSetByUsername(String username){
		return new HashSet<String>() {{
			add("ROLE_ANONYMOUS");// 数据库查出来的用户角色权限，默认此用户可以访问ROLE_ANONYMOUS角色的url
		}};
	}

	/**token的过期时间(单位为秒)
	 * @return
	 */
	public long getExpirationSeconds() {
		long expirationSeconds = 10*365*24*3600L;
		return expirationSeconds;
	}

	/**私钥字符串(参考https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/util/RsaUtil.java)
	 * @return
	 * @throws Exception
	 * @throws CertificateException
	 * @throws UnrecoverableKeyException
	 */
	public String getPrivateKeyBase64EncodeString() throws Exception, CertificateException, UnrecoverableKeyException {
		String privateKey = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAhoMJ703MADFT4Lf5MUQDQiG4qz7wqArKvzMhPdOmK8FM2GXKY57RTn4vXIrudYC7kl6Fdfuyedvv1wXYiMkqDwIDAQABAkBq2uIjhmvOo2D8nWmKJ3tnJ56p+x/2fkw9w4JeuSnCi2vvfcUN4Sb2FRR5Ckgw+4DExvC8W5Fjr5EGg6MedjvxAiEA2O+6sjn3zvljzREYHc8Pc3dlmaSW2zmCo/nwyCO9EUUCIQCeu7n4oBtnv7K++8461grqlB1Afu5Es89k/XvES6DhQwIhANCO+PArpsBHJtmZm5Pc4z/hA76Ia7frPFulCQWAxl35AiEAlH9tQPKQEORfFZq+2X4q4j/EifT1dWJ+cK1Pn1ldXb8CIQDUD6VYAC/nR+nIYUiU12kn2uBKe1bg2fwnUOJotFc6Kw==";
		return privateKey;
	}

	/**公钥字符串(参考https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/util/RsaUtil.java)
	 * @return
	 * @throws Exception
	 */
	public String getPublicKeyBase64EncodeString() throws Exception {
		String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIaDCe9NzAAxU+C3+TFEA0IhuKs+8KgKyr8zIT3TpivBTNhlymOe0U5+L1yK7nWAu5JehXX7snnb79cF2IjJKg8CAwEAAQ==";
		return publicKey;
	}
}