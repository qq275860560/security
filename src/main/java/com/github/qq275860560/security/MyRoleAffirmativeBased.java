package com.github.qq275860560.security;

import java.util.Arrays;

import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *  
 */

@Component
@Slf4j
public class MyRoleAffirmativeBased extends AffirmativeBased {
	public MyRoleAffirmativeBased() {
		super(Arrays.asList(new WebExpressionVoter(), new RoleVoter(), // 角色投票器,默认前缀为ROLE_,可以改成角色继承投票器
				new AuthenticatedVoter()));
	}
}