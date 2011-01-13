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

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

//@XmlRootElement()
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "timeline")
public class Timeline
{
	// @XmlElement(required = false)
	private Integer	createdBy;
	// @XmlElement(required = false)
	private Date	createDate;
	// @XmlElement(required = false)
	private Integer	lastChangedBy;
	// @XmlElement(required = false)
	private Date	lastChangeDate;

	@XmlAttribute(name = "createdBy")
	public Integer getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(final Integer createdBy)
	{
		this.createdBy = createdBy;
	}

	@XmlAttribute(name = "createDate")
	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(final Date createDate)
	{
		this.createDate = createDate;
	}

	@XmlAttribute(name = "lastChangedBy")
	public Integer getLastChangedBy()
	{
		return lastChangedBy;
	}

	public void setLastChangedBy(final Integer lastChangedBy)
	{
		this.lastChangedBy = lastChangedBy;
	}

	@XmlAttribute(name = "lastChangeDate")
	public Date getLastChangeDate()
	{
		return lastChangeDate;
	}

	public void setLastChangeDate(final Date lastChangeDate)
	{
		this.lastChangeDate = lastChangeDate;
	}

}
