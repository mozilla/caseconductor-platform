/*
Case Conductor is a Test Case Management system.
Copyright (C) 2011 uTest Inc.

This file is part of Case Conductor.

Case Conductor is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Case Conductor is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Case Conductor.  If not, see <http://www.gnu.org/licenses/>.

*/
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
package com.utest.domain.service.cache;

import java.util.List;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.utest.annotations.CacheEvent;
import com.utest.domain.User;

@Aspect
public class CachedContextManager
{
	private final LoginUserContextHolder	holder	= LoginUserContextHolder.getInstance();

	@Around(value = "@annotation(com.utest.annotations.CacheEvent) && @annotation(cacheEvent)")
	public Object processCacheEvent(final ProceedingJoinPoint joinPoint, final CacheEvent cacheEvent) throws Throwable
	{
		if (CacheEvent.CACHE_READ.equals(cacheEvent.operation()))
		{
			return checkCache(joinPoint, cacheEvent);
		}

		final Object ret = joinPoint.proceed(joinPoint.getArgs());
		if (CacheEvent.CACHE_REFRESH.equals(cacheEvent.operation()))
		{
			cleanCache(joinPoint, cacheEvent);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	private Object checkCache(final ProceedingJoinPoint joinPoint, final CacheEvent cacheEvent) throws Throwable
	{
		final Object[] args = joinPoint.getArgs();

		Object cachedValue = null;
		if (CacheEvent.USER_MATCHING.equals(cacheEvent.type()))
		{
			final Integer userId = getUserFromArgs(args);
			if (userId != null)
			{
				cachedValue = holder.getTestCyclesForUser(userId);
				if (cachedValue == null)
				{
					cachedValue = joinPoint.proceed(args);
					holder.updateUserTestCycles(userId, (List<Integer>) cachedValue);
				}
			}
		}
		return cachedValue;
	}

	private void cleanCache(final ProceedingJoinPoint joinPoint, final CacheEvent cacheEvent) throws Throwable
	{
		if (CacheEvent.USER_MATCHING.equals(cacheEvent.type()))
		{
			final Object[] args = joinPoint.getArgs();
			final Integer userId = getUserFromArgs(args);
			if (userId != null)
			{
				holder.updateUserTestCycles(userId, null);
			}
		}
		else if (CacheEvent.USER_CONTEXT.equals(cacheEvent.type()))
		{
			final Object[] args = joinPoint.getArgs();
			final Integer userId = getUserFromArgs(args);
			if (userId != null)
			{
				holder.removeUser(userId);
			}
		}
		else if (CacheEvent.TEST_CYCLE_MATCHING.equals(cacheEvent.type()))
		{
			final Set<Integer> loggedInUsers = holder.getUserIds();
			for (final Integer userId : loggedInUsers)
			{
				holder.updateUserTestCycles(userId, null);
			}
		}
	}

	private Integer getUserFromArgs(final Object[] args)
	{
		if (args != null)
		{
			for (final Object obj : args)
			{
				if (obj instanceof User)
				{
					return ((User) obj).getId();
				}
			}
		}
		return null;
	}

}
