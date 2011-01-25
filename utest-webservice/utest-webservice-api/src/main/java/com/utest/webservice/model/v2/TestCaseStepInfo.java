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
@XmlType(name = "testcasestep")
public class TestCaseStepInfo extends BaseInfo
{
	@XmlElement(required = true)
	private Integer			stepNumber;
	@XmlElement(required = true)
	private String			name;
	@XmlElement(required = true)
	private String			instruction;
	@XmlElement(required = true)
	private String			expectedResult;
	@XmlElement(required = false)
	private Integer			estimatedTimeInMin;
	@XmlElement(required = true)
	private Integer			testCaseVersionId;
	@XmlElement(type = ResourceLocator.class, name = "testCaseVersionLocator")
	private ResourceLocator	testCaseVersionLocator;

	public Integer getStepNumber()
	{
		return stepNumber;
	}

	public void setStepNumber(Integer stepNumber)
	{
		this.stepNumber = stepNumber;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getInstruction()
	{
		return instruction;
	}

	public void setInstruction(String instruction)
	{
		this.instruction = instruction;
	}

	public String getExpectedResult()
	{
		return expectedResult;
	}

	public void setExpectedResult(String expectedResult)
	{
		this.expectedResult = expectedResult;
	}

	public Integer getEstimatedTimeInMin()
	{
		return estimatedTimeInMin;
	}

	public void setEstimatedTimeInMin(Integer estimatedTimeInMin)
	{
		this.estimatedTimeInMin = estimatedTimeInMin;
	}

	public Integer getTestCaseVersionId()
	{
		return testCaseVersionId;
	}

	public void setTestCaseVersionId(Integer testCaseVersionId)
	{
		this.testCaseVersionId = testCaseVersionId;
	}

	public ResourceLocator getTestCaseVersionLocator()
	{
		return testCaseVersionLocator;
	}

	public void setTestCaseVersionLocator(ResourceLocator testCaseVersionLocator)
	{
		this.testCaseVersionLocator = testCaseVersionLocator;
	}
}
