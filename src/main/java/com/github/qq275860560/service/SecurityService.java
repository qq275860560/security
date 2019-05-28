package com.github.qq275860560.service;

import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

/**
 * @author jiangyuanlin@163.com
 *
 */
public abstract class SecurityService {
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	/**用户密码加密策略
	 *  用户注册阶段,用户的密码保存到数据库前通常要进行加密，此接口定义解密策略
	 *  传统使用md5进行加密入库容易猜解，比如所有人的123456进行md5后保存到数据库都是一样的，建议使用BCryptPasswordEncoder
	 *  如果使用spring默认的BCryptPasswordEncoder,不需要重写该方法
	 * @param rawPassword 用户登录时输入的明文密码
	 * @return 数据库中的密码
	 */
	public String encode(CharSequence rawPassword) {
		return passwordEncoder.encode(rawPassword);// spring推荐使用该方式加密
		//return org.springframework.util.DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes());
	}

	

	/**用户密码匹配策略
	 *   用户登录阶段,需要校验密码准确性
	  *  如果使用spring默认的BCryptPasswordEncoder,不需要重写该方法
	 * @param rawPassword 用户登录时输入的明文密码
	 * @param encodedPassword 数据库中加密后的密码
	 * @return 如果匹配返回真，否则返回假
	 */
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);// spring推荐使用该方式匹配
		//return org.springframework.util.DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes()).equals(encodedPassword);
	}

	

	/**根据登录用户查询密码
	 * 在登录阶段时，要调用此接口获取到用户密码，之后跟加密后的登录密码比较
	 * 根据登录账号查询密码，此密码非明文密码，而是PasswordEncoder对明文加密后的密码，因为
	 * spring security框架中数据库默认保存的是PasswordEncoder对明文加密后的密码
	 * 用户发送的密码加密后会跟这个函数返回的密码相匹配，如果成功，则认证成功，并保存到session中，程序任何地方可以通过以下代码获取当前的username
	 * String username=(String)SecurityContextHolder.getContext().getAuthentication().getName();  
	 * 再根据用户名称查询数据库获得其他个人信息
	 * @param username 登录用户名称
	 * @return 返回加密后的用户密码字符串
	 */
	public String getPasswordByUserName(String username) {
		return passwordEncoder.encode("123456");// 数据库查出来的密码，默认每个用户都是123456的加密
	}
	/**
	 * 根据登录用户查询对应的角色名称集合
	 * 在认证阶段时，要调用此接口初始化用户权限
	 * 如果返回null或空集合，代表该用户没有权限，这类用户其实跟匿名用户没有什么区别
	 * 如果username隶属于某高层次的角色或组织，应当把高层次的角色或组织对应的角色也返回，比如username的角色为ROLE_1, ROLE_1继承ROLE_2角色，并且username属于A部门，A部门拥有角色ROLE_3；所以应当返回ROLE_1,ROLE_2,ROLE_3
	 * @param username 登录用户名称
	 * @return 角色名称集合
	 */
	public Set<String> getRoleNamesByUsername(String username){// ROLE_开头
		return Collections.EMPTY_SET;// 数据库查出来的用户角色权限，默认此用户没有特殊权限，跟未登录的用户（匿名用户，游客）相同
		
	}
	/**
	 * 根据登录用户查询客户端的信息
	 * 此接口是为了和oauth2的密码模式相互兼容，使用/login获取的token也可以访问开放接口
	 * 默认返回空信息
	 * 如果业务没有开放接口，或者该用户没有权限访问开放接口，返回空，
	 * 如果需要访问某个接口，其接口所需的SCOPE为SCOPE_USER,SCOPE_ADMIN,那么请返回包含USER,ADMIN的SCOPE集合,不需要前缀,
	 * @param username 用户名称
	 * @return 客户端信息(至少包括一个clientId字段和一个scope字段)
	 */
	public Map<String,Object> getClientByUsername(String username){
		return Collections.EMPTY_MAP;// return new HashMap<String,Object>() {{put("clientId":username);put("scope","ADMIN,USER");}	
	}

	/**
	 * 根据请求路径查询对应的角色名称集合
	 * 在授权阶段时，要调用此接口获取权限，之后跟登录用户的权限比较
	 * 登录用户至少拥有一个角色，才能访问
	 * 如果返回null或空集合或包含ROLE_ANONYMOUS，代表该url不需要权限控制，任何用户(包括匿名)用户都可以访问
	 * 如果url符合某个正则表达式，应当把正则表达式的角色也返回，比如/api/a的角色为ROLE_1,ROLE_2, 而数据库中还存在/api/*的角色为ROLE_3,ROLE_4；由于/api/a属于正则表达式/api/*,所以应当返回ROLE_1,ROLE_2,ROLE_3,ROLE_4
	 * @param url 请求路径（ip端口之后的路径）
	 * @return 权限集合
	 */
	public Set<String> getRoleNamesByUrI(String url){//ROLE_开头
		return Collections.EMPTY_SET;  // 数据库查出来的url角色权限，默认只要具有ROLE_ANONYMOUS角色的用户即可访问
	 
	}

	


	
	
	/**token的过期时长(单位为秒)
	 * @return 秒
	 */
	public int getAccessTokenValiditySeconds() {
		return 10*365*24*3600;	 
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