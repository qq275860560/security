登录认证授权模板

# 适用场景
此项目为登录认证授权模板，适用于传统session模式和微服务jwt认证模式，如果项目为spring，可以模仿该项目进行安全控制

包括但不限于以下功能

## 登录
com.github.qq275860560.security.MyUsernamePasswordAuthenticationFilter
## 登录失败
com.github.qq275860560.security.MySimpleUrlAuthenticationFailureHandler
## 登录成功
com.github.qq275860560.security.MySimpleUrlAuthenticationSuccessHandler

## 认证
com.github.qq275860560.security.MyAuthenticationFilter
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
## 退出成功
com.github.qq275860560.security.MyLogoutSuccessHandler
## 退出失败

# 使用方式
在对应的类中修改代码,比如在认证失败的时候，修改MyAuthenticationEntryPoint的commence方法即可
```
/**
 * @author jiangyuanlin@163.com
 *  
 */
 
@Component
@Slf4j         
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

        log.info("认证失败",authException);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<String ,Object>() {{
        	put("code", HttpStatus.UNAUTHORIZED.value());
        	put("msg", "认证失败");
        	put("data", authException.getMessage());
        }}));
    }
}
```

# 喜欢请留下star,给我一点前进的动力,谢谢