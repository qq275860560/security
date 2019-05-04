package com.github.qq275860560.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangyuanlin@163.com
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {

	private String username;
	private String password;

}
