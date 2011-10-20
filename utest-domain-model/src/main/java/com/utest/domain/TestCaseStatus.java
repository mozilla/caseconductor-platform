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
package com.utest.domain;

public class TestCaseStatus extends LocalizedEntity
{

	public static final Integer	PENDING		= new Integer(1);
	public static final Integer	ACTIVE		= new Integer(2);
	public static final Integer	LOCKED		= new Integer(3);
	public static final Integer	CLOSED		= new Integer(4);
	public static final Integer	DISCARDED	= new Integer(5);

	public TestCaseStatus()
	{
	}

}
