/**
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 
 * @author Vadim Kisen
 *
 * copyright 2010 by uTest 
 */
package com.utest.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.testng.annotations.BeforeClass;

import com.utest.domain.AuthenticatedUserInfo;
import com.utest.domain.User;
import com.utest.persistence.AbstractDatabaseTestCase;

public class BaseDomainServiceIntegrationTest extends AbstractDatabaseTestCase
{
	@Autowired
	protected UserService	userService;

	@BeforeClass(alwaysRun = true)
	public void cleanup()
	{
		logout();
	}

	protected void loginUser(final User user) throws Exception
	{
		final List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		final AuthenticatedUserInfo authUser = new AuthenticatedUserInfo(user.getId(), user.getFullName());
		final Authentication auth = new UsernamePasswordAuthenticationToken(authUser, null, grantedAuthorities);
		final SecurityContext ctx = new SecurityContextImpl();
		ctx.setAuthentication(auth);
		SecurityContextHolder.setContext(ctx);
	}

	protected void logout()
	{
		if (SecurityContextHolder.getContext() != null)
		{
			SecurityContextHolder.getContext().setAuthentication(null);
		}
	}
}
