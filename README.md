登录认证授权组件

# 适用场景
如果项目采用spring框架，那么可以模仿该项目代码进行安全控制,支持微服务jwt认证模式

# 功能
## 私钥配置
com.github.qq275860560.security.MyPrivateKeyConfig
## 公钥配置
com.github.qq275860560.security.MyPublicKeyConfig
## 登录
com.github.qq275860560.security.MyUsernamePasswordAuthenticationFilter
## 登录失败
com.github.qq275860560.security.MyAuthenticationFailureHandler
## 登录成功
com.github.qq275860560.security.MyAuthenticationSuccessHandler

## 认证
com.github.qq275860560.security.MyRequestHeaderAuthenticationFilter
## 认证失败
com.github.qq275860560.security.MyAuthenticationEntryPoint
## 授权
com.github.qq275860560.security.MyFilterSecurityInterceptor
## 授权:获取用户对应的角色权限
com.github.qq275860560.security.MyUserDetailsService
## 授权:获取url对应的角色权限
com.github.qq275860560.security.MyFilterInvocationSecurityMetadataSource
## 授权:决策
com.github.qq275860560.security.MyAccessDecisionManager
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