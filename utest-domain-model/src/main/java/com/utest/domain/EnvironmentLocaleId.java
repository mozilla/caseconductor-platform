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

public class EnvironmentLocaleId implements java.io.Serializable
{

	private Integer	environmentId;
	private String	localeCode;

	public EnvironmentLocaleId()
	{
	}

	public EnvironmentLocaleId(final Integer environmentId, final String localeCode)
	{
		this.setEnvironmentId(environmentId);
		this.localeCode = localeCode;
	}

	public String getLocaleCode()
	{
		return this.localeCode;
	}

	public void setLocaleCode(final String localeCode)
	{
		this.localeCode = localeCode;
	}

	public void setEnvironmentId(final Integer environmentId)
	{
		this.environmentId = environmentId;
	}

	public Integer getEnvironmentId()
	{
		return environmentId;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((environmentId == null) ? 0 : environmentId.hashCode());
		result = prime * result + ((localeCode == null) ? 0 : localeCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final EnvironmentLocaleId other = (EnvironmentLocaleId) obj;
		if (environmentId == null)
		{
			if (other.environmentId != null)
			{
				return false;
			}
		}
		else if (!environmentId.equals(other.environmentId))
		{
			return false;
		}
		if (localeCode == null)
		{
			if (other.localeCode != null)
			{
				return false;
			}
		}
		else if (!localeCode.equals(other.localeCode))
		{
			return false;
		}
		return true;
	}

}
