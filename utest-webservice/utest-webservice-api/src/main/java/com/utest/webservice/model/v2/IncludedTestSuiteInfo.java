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
package com.utest.webservice.model.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "includedtestsuite")
public class IncludedTestSuiteInfo extends BaseInfo
{
	@XmlElement(required = false)
	private Integer			testPlanId;
	@XmlElement(type = ResourceLocator.class, name = "testPlanLocator")
	private ResourceLocator	testPlanLocator;
	@XmlElement(required = false)
	private Integer			testSuiteId;
	@XmlElement(type = ResourceLocator.class, name = "testSuiteLocator")
	private ResourceLocator	testSuiteLocator;
	@XmlElement(required = false)
	private Integer			runOrder	= 0;

	public Integer getTestSuiteId()
	{
		return testSuiteId;
	}

	public void setTestSuiteId(Integer testSuiteId)
	{
		this.testSuiteId = testSuiteId;
	}

	public Integer getRunOrder()
	{
		return runOrder;
	}

	public void setRunOrder(Integer runOrder)
	{
		this.runOrder = runOrder;
	}

	public Integer getTestPlanId()
	{
		return testPlanId;
	}

	public void setTestPlanId(Integer testPlanId)
	{
		this.testPlanId = testPlanId;
	}

	public ResourceLocator getTestPlanLocator()
	{
		return testPlanLocator;
	}

	public void setTestPlanLocator(ResourceLocator testPlanLocator)
	{
		this.testPlanLocator = testPlanLocator;
	}

	public ResourceLocator getTestSuiteLocator()
	{
		return testSuiteLocator;
	}

	public void setTestSuiteLocator(ResourceLocator testSuiteLocator)
	{
		this.testSuiteLocator = testSuiteLocator;
	}
}
