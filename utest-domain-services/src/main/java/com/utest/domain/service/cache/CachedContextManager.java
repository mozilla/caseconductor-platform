/**
 *
 * Licensed under the GNU General Public License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/gpl.txt
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
