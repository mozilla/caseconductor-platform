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

// Generated Sep 3, 2010 1:57:10 PM by Hibernate Tools 3.2.4.GA

/**
 * EnvironmentProfileEnvironmentGroup generated by hbm2java
 */
public class EnvironmentProfileEnvironmentGroup extends TimelineEntity implements ProfileDependable, EnvironmentDependable
{

	private Integer	environmentGroupId;
	private Integer	environmentProfileId;

	public EnvironmentProfileEnvironmentGroup()
	{
	}

	public EnvironmentProfileEnvironmentGroup(final Integer environmentGroupId, final Integer environmentProfileId)
	{
		super();
		this.environmentGroupId = environmentGroupId;
		this.environmentProfileId = environmentProfileId;
	}

	public Integer getEnvironmentGroupId()
	{
		return this.environmentGroupId;
	}

	public void setEnvironmentGroupId(final Integer environmentGroupId)
	{
		this.environmentGroupId = environmentGroupId;
	}

	public Integer getEnvironmentProfileId()
	{
		return this.environmentProfileId;
	}

	public void setEnvironmentProfileId(final Integer environmentProfileId)
	{
		this.environmentProfileId = environmentProfileId;
	}

	@Override
	public Integer getProfileId()
	{
		return getEnvironmentProfileId();
	}

	@Override
	public void setProfileId(final Integer profileId_)
	{
		setEnvironmentProfileId(profileId_);

	}

}
