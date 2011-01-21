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
package com.utest.webservice.impl.v2;

import javax.ws.rs.core.EntityTag;

import com.utest.domain.TimelineEntity;
import com.utest.webservice.builders.ObjectBuilderFactory;

public class BaseWebServiceImpl
{
	protected ObjectBuilderFactory	objectBuilderFactory;

	public BaseWebServiceImpl()
	{
		super();
	}

	public BaseWebServiceImpl(final ObjectBuilderFactory objectBuilderFactory)
	{
		super();
		this.objectBuilderFactory = objectBuilderFactory;
	}

	protected EntityTag computeEtagForEntity(TimelineEntity timelineEntity_)
	{
		return new EntityTag(timelineEntity_.getId() + "-" + timelineEntity_.getVersion());
	}

}
