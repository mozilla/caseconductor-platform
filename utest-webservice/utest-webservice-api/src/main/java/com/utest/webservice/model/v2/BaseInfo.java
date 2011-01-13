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

import javax.xml.bind.annotation.XmlElement;

//@XmlRootElement()
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "BaseInfo")
public class BaseInfo
{
	@XmlElement(type = ResourceIdentity.class, name = "resourceIdentity", required = false)
	private ResourceIdentity	resourceIdentity;
	@XmlElement(type = Timeline.class, name = "timeline", required = false)
	private Timeline			timeline;

	public BaseInfo()
	{
	}

	public ResourceIdentity getResourceIdentity()
	{
		return resourceIdentity;
	}

	public void setResourceIdentity(final ResourceIdentity resourceIdentity)
	{
		this.resourceIdentity = resourceIdentity;
	}

	public Timeline getTimeline()
	{
		return timeline;
	}

	public void setTimeline(final Timeline timeline)
	{
		this.timeline = timeline;
	}
}
