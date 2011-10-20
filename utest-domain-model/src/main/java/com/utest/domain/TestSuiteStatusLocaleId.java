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

/**
 * TestSuiteStatusLocaleId generated by hbm2java
 */

public class TestSuiteStatusLocaleId implements java.io.Serializable
{

	private Integer	testSuiteStatusId;
	private String	localeCode;

	public TestSuiteStatusLocaleId()
	{
	}

	public TestSuiteStatusLocaleId(Integer testSuiteStatusId, String localeCode)
	{
		this.testSuiteStatusId = testSuiteStatusId;
		this.localeCode = localeCode;
	}

	public Integer getTestSuiteStatusId()
	{
		return this.testSuiteStatusId;
	}

	public void setTestSuiteStatusId(Integer testSuiteStatusId)
	{
		this.testSuiteStatusId = testSuiteStatusId;
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
		if (!(other instanceof TestSuiteStatusLocaleId))
			return false;
		TestSuiteStatusLocaleId castOther = (TestSuiteStatusLocaleId) other;

		return ((this.getTestSuiteStatusId() == castOther.getTestSuiteStatusId()) || (this.getTestSuiteStatusId() != null && castOther.getTestSuiteStatusId() != null && this
				.getTestSuiteStatusId().equals(castOther.getTestSuiteStatusId())))
				&& ((this.getLocaleCode() == castOther.getLocaleCode()) || (this.getLocaleCode() != null && castOther.getLocaleCode() != null && this.getLocaleCode().equals(
						castOther.getLocaleCode())));
	}

	@Override
	public int hashCode()
	{
		int result = 17;

		result = 37 * result + (getTestSuiteStatusId() == null ? 0 : this.getTestSuiteStatusId().hashCode());
		result = 37 * result + (getLocaleCode() == null ? 0 : this.getLocaleCode().hashCode());
		return result;
	}

}
