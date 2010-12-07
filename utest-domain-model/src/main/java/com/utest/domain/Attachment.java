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
package com.utest.domain;

import java.util.Date;

public class Attachment extends Entity
{
	private String	fileName;
	private String	storageUrl;
	private Double	fileSize;
	private String	fileType;
	private Integer	entityTypeId;
	private Integer	entityId;
	private Integer	attachmentTypeId;
	private String	bucketName;
	private String	objectKey;
	private Date	createDate;

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(final String fileName)
	{
		this.fileName = fileName;
	}

	public String getFileType()
	{
		return fileType;
	}

	public void setFileType(final String fileType)
	{
		this.fileType = fileType;
	}

	public Integer getEntityTypeId()
	{
		return entityTypeId;
	}

	public void setEntityTypeId(final Integer entityTypeId)
	{
		this.entityTypeId = entityTypeId;
	}

	public Integer getAttachmentTypeId()
	{
		return attachmentTypeId;
	}

	public void setAttachmentTypeId(final Integer attachmentTypeId)
	{
		this.attachmentTypeId = attachmentTypeId;
	}

	public Integer getEntityId()
	{
		return entityId;
	}

	public void setEntityId(final Integer entityId)
	{
		this.entityId = entityId;
	}

	public String getStorageUrl()
	{
		return storageUrl;
	}

	public void setStorageUrl(final String storageUrl)
	{
		this.storageUrl = storageUrl;
	}

	public Double getFileSize()
	{
		return fileSize;
	}

	public void setFileSize(final Double fileSize)
	{
		this.fileSize = fileSize;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(final Date createDate)
	{
		this.createDate = createDate;
	}

	public String getBucketName()
	{
		return bucketName;
	}

	public void setBucketName(final String bucketName)
	{
		this.bucketName = bucketName;
	}

	public String getObjectKey()
	{
		return objectKey;
	}

	public void setObjectKey(final String objectKey)
	{
		this.objectKey = objectKey;
	}

}
