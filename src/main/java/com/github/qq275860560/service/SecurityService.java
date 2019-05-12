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
	 * @param rawPassword
	 * @return
	 */
	public String encode(CharSequence rawPassword) {
		return passwordEncoder.encode(rawPassword);// spring推荐使用该方法加密
	}

	/**用户密码核对策略(如果使用spring默认的springBCryptPasswordEncoder,不需要重写该方法)
	 * @param rawPassword
	 * @param encodedPassword
	 * @return
	 */
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	};

	/**根据登录账号查询密码
	  * 根据登录账号查询密码，此密码非明文密码，而是PasswordEncoder对明文加密后的密码，因为
	 * spring security框架中数据库默认保存的是PasswordEncoder对明文加密后的密码
	 * 
	 * @param username 登录账号名称
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

	/**设置token的过期时间(单位为秒)
	 * @return
	 */
	public long getExpirationSeconds() {
		long expirationSeconds = 86400L;
		return expirationSeconds;
	}

	/**私钥字符串(百度搜索公钥私钥在线生成，生成后的私钥可作为此函数的返回值)
	 * @return
	 * @throws Exception
	 * @throws CertificateException
	 * @throws UnrecoverableKeyException
	 */
	public String getPrivateKeyString() throws Exception, CertificateException, UnrecoverableKeyException {
		String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCL/rwpW1aOL3C3fI1kly12CrS6Uaz8His8WgtgucCpkFes5LqL5SLYICXmEbXiY+ahTIduWmBNrDaje3AwPVl0pEsbBJ7BfAfz35PUPShYMJOymfYJ1GLsZL9X3bdv32afFS4mNAh8h8saYLUCO0H/TGL2hrcqbuMsXOHQ+Htw9xuuw9HF6K1XAhckR0UhP0bJ2CaVMoy7L9gtOxjrVJ+neyAdyPISgMYgleYABdlzhahlMMeUQKWPjySAHPGmdDZjU+4DwZ+VcsrADLYgwsEsYylHNJ+ISYxBBlJsDUU8y64OmDqKomlqcv/VAlZKMKvP/UCC3n7dJhMFjQpkqDZVAgMBAAECggEAWvbynIzU+R6qRw+PcEUrLQBX3pkjpc6UmWqI6hjIr2UzEwysiiohMf3xokTvwmLXgQeRGItw+AdmmWOjyjSS48+9XZjq7x4ArN4h2E7E4EjwL6UK0ehHPwNXsWhHwVGQBN5mVjyJJyG1PzaHZyPKBnFD/JwceF4FDtZrhLVwwD1Xo0Y35CWlani7LT8O7Q4SH2UOT8koARcBotFxs5oFvK2bvnsU7FDFTj7RFOKc3MFOYbxZ8q6/9N1i4u3H9q4D9dKSv3+z33Pz721b66CN5M6Q199MTBz9Mh0gFY6bNHZO8121pm3xW1rL2kChC/0SKgJFlSL1U3fsP1ydzJnkIQKBgQDdPutS8Ykw/N9OO+6vqKGxs3VYQTAKSJ7YVFPmgkjbIWwe/rwkmCo5L0tVEnykbqYXjcw8BxsOvAlO3luBfsOi3anMuTA4Dr0j9ll8P7f8jYjb0mfjpB34lne5yINQlMfWYFRuiNRXm6amsn0AWwHOazq7CFh82K8+/DCupSVCowKBgQCh/G9YPqApZAGRCRWEU0PkvrePWFdlMYCVT1aNtX3iflf1jelvBsi2yh5fXqEAsox9cQi2jW5i6XghIuG9RQnq4mWkVS6Neg/m7IMEP86L1p0wtLb4hCsi2FPoy0WQBuTNSFPOSmdwo8corLa1IYxF24X5e6BZH6dCgsp185kqpwKBgQCC7VwXToZwbgS76G2cl/9wCJI1swX53/XYcTbhX1I8EzBHu1mdkkrSYnGDG5iVOkGiCLDHCTFy68XaXW3rWRfvBpwYYbLuSZKWeI+GHrMDisJly8LdDN8LoAej8sv64MDN1V3Bt8lpOtxJI7Ejh7eu7vfnfM3Yu+YhMN6iS2qcXwKBgGYY8KkjF9newaoDmr9DqAhWOOYtnTAX6l6xmfFJkmWekpwf7SDgmsOzUz9zKnGBGFG8W+yL6iaH1wKztKqSCDU2qy/PzL65T6qSKeYUvX+gLoVTcfvjejjFNuYEsPydi7rjuobMmLQDVnUJn0M9OOeS/LeJt0BVVhvyyd9cQY8DAoGBALWEeojr3YYNC4f+HCTn80FSFEBmNxEPclUteYM0N0O3osBI/MxKTQkAQ4c74FEOZrpnZc7Kpnd1levXJW384qqRa39BuB/xpyXoHMCdoYZbDN4kqfi0JHoJTJ6E0ad5/2baRchVErEQRRAEAxD+77YOhoGdcwGIMCLBW4SU2Qut";
		return privateKey;
	}

	/**公钥字符串(百度搜索公钥私钥在线生成，生成后的公钥可作为此函数的返回值)
	 * @return
	 * @throws Exception
	 */
	public String getPublicKeyString() throws Exception {
		String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi/68KVtWji9wt3yNZJctdgq0ulGs/B4rPFoLYLnAqZBXrOS6i+Ui2CAl5hG14mPmoUyHblpgTaw2o3twMD1ZdKRLGwSewXwH89+T1D0oWDCTspn2CdRi7GS/V923b99mnxUuJjQIfIfLGmC1AjtB/0xi9oa3Km7jLFzh0Ph7cPcbrsPRxeitVwIXJEdFIT9GydgmlTKMuy/YLTsY61Sfp3sgHcjyEoDGIJXmAAXZc4WoZTDHlEClj48kgBzxpnQ2Y1PuA8GflXLKwAy2IMLBLGMpRzSfiEmMQQZSbA1FPMuuDpg6iqJpanL/1QJWSjCrz/1Agt5+3SYTBY0KZKg2VQIDAQAB";
		return publicKey;
	}
}