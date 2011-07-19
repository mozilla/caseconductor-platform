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

import javax.xml.bind.annotation.XmlAttribute;

public class ResourceLocator
{
	private final Integer	id;
	private final String	url;
	private final String	name;

	public ResourceLocator(Integer id, String url, String name)
	{
		super();
		this.id = id;
		this.url = url;
		this.name = name;
	}

	@XmlAttribute(name = "name")
	public String getName()
	{
		return name;
	}

	@XmlAttribute(name = "url")
	public String getUrl()
	{
		return url;
	}

	@XmlAttribute(name = "id")
	public Integer getId()
	{
		return id;
	}
}
