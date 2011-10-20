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

// Generated Oct 7, 2009 11:18:35 AM by Hibernate Tools 3.2.4.GA

/**
 * TestCycleStatusLocaleId generated by hbm2java
 */

public class TestCycleStatusLocaleId implements java.io.Serializable
{

	private Integer	testCycleStatusId;
	private String	localeCode;

	public TestCycleStatusLocaleId()
	{
	}

	public TestCycleStatusLocaleId(Integer testCycleStatusId, String localeCode)
	{
		this.testCycleStatusId = testCycleStatusId;
		this.localeCode = localeCode;
	}

	public Integer getTestCycleStatusId()
	{
		return this.testCycleStatusId;
	}

	public void setTestCycleStatusId(Integer testCycleStatusId)
	{
		this.testCycleStatusId = testCycleStatusId;
	}

	public String getLocaleCode()
	{
		return this.localeCode;
	}

	public void setLocaleCode(String localeCode)
	{
		this.localeCode = localeCode;
	}

	@Override
	public boolean equals(Object other)
	{
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TestCycleStatusLocaleId))
			return false;
		TestCycleStatusLocaleId castOther = (TestCycleStatusLocaleId) other;

		return ((this.getTestCycleStatusId() == castOther.getTestCycleStatusId()) || (this.getTestCycleStatusId() != null && castOther.getTestCycleStatusId() != null && this
				.getTestCycleStatusId().equals(castOther.getTestCycleStatusId())))
				&& ((this.getLocaleCode() == castOther.getLocaleCode()) || (this.getLocaleCode() != null && castOther.getLocaleCode() != null && this.getLocaleCode().equals(
						castOther.getLocaleCode())));
	}

	@Override
	public int hashCode()
	{
		int result = 17;

		result = 37 * result + (getTestCycleStatusId() == null ? 0 : this.getTestCycleStatusId().hashCode());
		result = 37 * result + (getLocaleCode() == null ? 0 : this.getLocaleCode().hashCode());
		return result;
	}

}
