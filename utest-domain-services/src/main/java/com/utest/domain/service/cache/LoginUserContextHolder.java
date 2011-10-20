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
 * @author Miguel Bautista
 *
 * copyright 2010 by uTest 
 */
package com.utest.domain.service.cache;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.utest.domain.User;
import com.utest.domain.cache.LoginUserContext;

// TODO: This should be changed to some third-party caching that will 
// work across multiple servers - Miguel
public final class LoginUserContextHolder
{
	private static LoginUserContextHolder					instance;

	private final ConcurrentMap<Integer, LoginUserContext>	users;

	private LoginUserContextHolder()
	{
		users = new ConcurrentHashMap<Integer, LoginUserContext>();
	}

	public static LoginUserContextHolder getInstance()
	{
		if (instance == null)
		{
			instance = new LoginUserContextHolder();
		}
		return instance;
	}

	public User getUser(final Integer userId)
	{
		final LoginUserContext context = users.get(userId);
		if (context != null)
		{
			return context.getUser();
		}
		return null;
	}

	public void addUser(final User user)
	{
		users.putIfAbsent(user.getId(), new LoginUserContext(user));
	}

	public void removeUser(final Integer userId)
	{
		users.remove(userId);
	}

	public boolean updateUser(final User user)
	{
		final LoginUserContext context = users.get(user.getId());
		if (context != null)
		{
			context.setUser(user);
			return true;
		}
		return false;
	}

	public Set<Integer> getUserIds()
	{
		return Collections.unmodifiableSet(users.keySet());
	}

	public List<Integer> getTestCyclesForUser(final Integer userId)
	{
		final LoginUserContext context = users.get(userId);
		if (context != null)
		{
			return context.getTestcycles();
		}
		return null;
	}

	public boolean updateUserTestCycles(final Integer userId, final List<Integer> testcycles)
	{
		final LoginUserContext context = users.get(userId);
		if (context != null)
		{
			context.setTestcycles(testcycles);
			return true;
		}
		return false;
	}

}
