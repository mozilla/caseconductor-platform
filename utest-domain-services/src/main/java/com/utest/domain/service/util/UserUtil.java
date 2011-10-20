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
package com.utest.domain.service.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.utest.domain.AuthenticatedUserInfo;

public class UserUtil
{
	public static Integer getCurrentUserId()
	{
		final SecurityContext ctx = SecurityContextHolder.getContext();
		Authentication auth = null;
		if (ctx != null)
		{
			auth = ctx.getAuthentication();
		}
		return ((AuthenticatedUserInfo) auth.getPrincipal()).getLoggedInUserId();
	}

}
