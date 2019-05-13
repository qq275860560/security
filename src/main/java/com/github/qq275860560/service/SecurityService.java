package com.github.qq275860560.service;

import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
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
	 * @return
	 */
	public String encode(CharSequence rawPassword) {
		return passwordEncoder.encode(rawPassword);// spring推荐使用该方法加密
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
	public abstract String getPasswordByUserName(String username);

	/**
	 *   根据请求路径查询对应的角色名称列表，
	 *   登录用户至少拥有一个角色，才能访问
	 *   如果返回null或空集合或包含ROLE_ANONYMOUS，代表该url不需要权限控制，任何用户(包括匿名)用户都可以访问
	 *   如果url符合某个正则表达式，应当把正则表达式的角色也返回，比如/api/a的角色为ROLE_1,ROLE_2, 而数据库中还存在/api/*的角色为ROLE_3,ROLE_4；由于/api/a属于正则表达式/api/*,所以应当返回ROLE_1,ROLE_2,ROLE_3,ROLE_4
	 * @param url 请求路径（ip端口之后的路径）
	 * @return
	 */
	public abstract Set<String> getRoleNameSetByUrI(String url);

	/**
	 *   根据登录用户查询对应的角色名称列表，
	 *   如果返回null或空集合，代表该用户没有权限，这类用户其实跟匿名用户没有什么区别
	 *   如果username隶属于某高层次的角色或组织，应当把高层次的角色或组织对应的角色也返回，比如username的角色为ROLE_1, ROLE_1继承ROLE_2角色，并且username属于A部门，A部门拥有角色ROLE_3；所以应当返回ROLE_1,ROLE_2,ROLE_3
	 * @param username 登录用户名称
	 * @return
	 */
	public abstract Set<String> getRoleNameSetByUsername(String username);

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
		String privateKey = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAwMyeTv8ef7XSiS9v+vx/n4CP0xQMYlXt9Sw4Q1ic1pQKpvCWI5bGhnYJk+seHpf0mv1AyPWoRQCeGoiE8tdymwIDAQABAkBZnUJgOZj1h4/qkBAeu0qe2uT/Gax/8K0AbqNgOkn8OlilY6HzkyuonQee29KHXoV7a1nxW3HePTnahAKWB/NhAiEA7Edny/2XhXG7s0rPnSBdLfbfpj4XYxhQ29grAAN09qkCIQDQ5C3qTvcTQ8AZpNUZ3fdvx1rKHpSf9M7i2g6dNItdowIhAJZY4X12QkJRmqR9yBoti91BqBJ6lBskT990b/g0OurxAiAXOFYWRqLWZGCVOSprDq5zoXBpKU8SHM9mjiCzvuSDCwIhAMFVhazoOAP2DQHmo+DRJItHDYOQKSk2Q4tR9A9519nx";
		return privateKey;
	}

	/**公钥字符串(参考https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/util/RsaUtil.java)
	 * @return
	 * @throws Exception
	 */
	public String getPublicKeyBase64EncodeString() throws Exception {
		String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMDMnk7/Hn+10okvb/r8f5+Aj9MUDGJV7fUsOENYnNaUCqbwliOWxoZ2CZPrHh6X9Jr9QMj1qEUAnhqIhPLXcpsCAwEAAQ==";
		return publicKey;
	}
}