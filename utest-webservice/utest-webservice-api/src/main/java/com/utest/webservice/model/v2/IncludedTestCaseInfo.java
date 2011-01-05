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
package com.utest.webservice.model.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "includedtestcase")
public class IncludedTestCaseInfo extends TestCaseVersionInfo
{
	@XmlElement(required = false)
	private Integer			containerId;
	@XmlElement(required = false)
	private final Integer	priorityId	= 0;
	@XmlElement(required = false)
	private final Integer	runOrder	= 0;
	@XmlElement(required = false)
	private final boolean	blocking	= false;

	public Integer getContainerId()
	{
		return containerId;
	}

	public void setContainerId(Integer containerId)
	{
		this.containerId = containerId;
	}

	public Integer getPriorityId()
	{
		return priorityId;
	}

	public Integer getRunOrder()
	{
		return runOrder;
	}

	public boolean isBlocking()
	{
		return blocking;
	}
}
