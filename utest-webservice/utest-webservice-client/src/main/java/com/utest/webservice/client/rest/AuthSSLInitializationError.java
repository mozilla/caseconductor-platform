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
 * copyright 2010 by uTest 
 */
package com.utest.webservice.client.rest;

public class AuthSSLInitializationError extends Error
{
	private static final long	serialVersionUID	= -5472381805564831828L;

	public AuthSSLInitializationError()
	{
		super();
	}

	public AuthSSLInitializationError(String message)
	{
		super(message);
	}
}
