[TOC]
登录认证授权组件

# 适用场景
简化spring默认的安全框架使用，暴露出项目常用的几个接口[SecurityService](https://github.com/qq275860560/security/blob/master/src/main/java/com/github/qq275860560/service/SecurityService.java)

# 接口功能
## 根据登录用户查询密码
## 根据登录用户查询对应的角色名称集合
## 根据请求路径查询对应的角色名称集合


# 技术实现
## token私钥配置
com.github.qq275860560.security.MyPrivateKeyConfig
## token公钥配置
com.github.qq275860560.security.MyPublicKeyConfig
## 密码加密策略
com.github.qq275860560.security.MyPasswordEncoder
## 登录
com.github.qq275860560.security.MyUsernamePasswordAuthenticationFilter
## 登录失败
com.github.qq275860560.security.MyAuthenticationFailureHandler
## 登录成功
com.github.qq275860560.security.MyAuthenticationSuccessHandler

## 认证
com.github.qq275860560.security.MyRequestHeaderAuthenticationFilter
## 认证:获取用户对应的角色权限
com.github.qq275860560.security.MyUserDetailsService
## 认证失败
com.github.qq275860560.security.MyAuthenticationEntryPoint

## 授权:获取url对应的角色权限
com.github.qq275860560.security.MyRoleFilterInvocationSecurityMetadataSource
## 授权:角色投票决策
com.github.qq275860560.security.MyRoleAffirmativeBased
## 授权失败
com.github.qq275860560.security.MyAccessDeniedHandler

## 退出
com.github.qq275860560.security.MyLogoutHandler
## 退出成功
com.github.qq275860560.security.MyLogoutSuccessHandler
## 退出失败
com.github.qq275860560.security.MyLogoutHandler

# 使用方式
参考
[登录认证授权组件调用示例](https://github.com/qq275860560/security-demo)

# 温馨提醒

* 此项目将会长期维护，增加或改进实用的功能
* 右上角点击star，给我继续前进的动力,谢谢