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
@XmlType(name = "UtestSearchRequest")
public class UtestSearchRequest
{
	@XmlElement(required = false)
	Integer			pageNumber;
	@XmlElement(required = false)
	Integer			pageSize;
	@XmlElement(required = false)
	private String	sortField;
	@XmlElement(required = false)
	private String	sortDirection;

	@org.apache.cxf.aegis.type.java5.XmlElement(minOccurs = "0", nillable = true)
	public Integer getPageNumber()
	{
		return pageNumber;
	}

	public void setPageNumber(final Integer pageNumber)
	{
		this.pageNumber = pageNumber;
	}

	@org.apache.cxf.aegis.type.java5.XmlElement(minOccurs = "0", nillable = true)
	public Integer getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(final Integer pageSize)
	{
		this.pageSize = pageSize;
	}

	@org.apache.cxf.aegis.type.java5.XmlElement(minOccurs = "0", nillable = true)
	public String getSortField()
	{
		return sortField;
	}

	public void setSortField(final String sortField)
	{
		this.sortField = sortField;
	}

	@org.apache.cxf.aegis.type.java5.XmlElement(minOccurs = "0", nillable = true)
	public String getSortDirection()
	{
		return sortDirection;
	}

	public void setSortDirection(final String sortDirection)
	{
		this.sortDirection = sortDirection;
	}
}
