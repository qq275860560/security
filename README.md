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

命令行切换到项目根目录下，执行
```
mvn spring-boot:run
```
此时，本地会默认开启8080端口，命令行执行登录
```
curl -i -H "Content-Type:application/json;charset=UTF-8" \
	  -X POST   http://localhost:8080/login \
	  -d '{"username":"username1","password":"password1"}'

```
响应结果
```
HTTP/1.1 200
Authorization: Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE1NTczNjcwMDl9.H1JcMvQQInYx8IM2iTkW-WQIN8eUQw-mBwRbbT0SBoXKeyXijNM3jvDpHLBisWNIobwPDIUqMIGEdk3MRC6TBAeTHxmZZacZbNenrsn9mB8qJU_P1zG7Hi6mRjrPBtdg0cEW44VGl6z_cUNgh1wjR7aMMhNzF7EQ0JxQkBl4P5g
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json;charset=UTF-8
Content-Length: 45
Date: Wed, 08 May 2019 01:56:49 GMT

{"msg":"登录成功","code":200,"data":null}

```

响应头部Authorization对应的值就是token，以后带着token就可以高高兴兴的访问系统了
```
curl -i -H "Content-Type:application/json;charset=UTF-8" \
-H "Authorization:Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE1NTczNjcwMDl9.H1JcMvQQInYx8IM2iTkW-WQIN8eUQw-mBwRbbT0SBoXKeyXijNM3jvDpHLBisWNIobwPDIUqMIGEdk3MRC6TBAeTHxmZZacZbNenrsn9mB8qJU_P1zG7Hi6mRjrPBtdg0cEW44VGl6z_cUNgh1wjR7aMMhNzF7EQ0JxQkBl4P5g" \
-X POST http://localhost:8080/listUser
```
响应结果
```
HTTP/1.1 200
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Wed, 08 May 2019 02:00:52 GMT

{"msg":"查询成功","code":"OK","data":[{"roles":"ROLE_ADMIN","userId":1,"username":"admin"},{"roles":"ROLE_ADMIN","userId":2,"username":"admin2"}]}

```
# 喜欢请留下star,给我一点前进的动力,谢谢