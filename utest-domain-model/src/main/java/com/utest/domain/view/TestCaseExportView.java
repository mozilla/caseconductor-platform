package com.utest.domain.view;

import com.utest.domain.Entity;

public class TestCaseExportView extends Entity
{
	public final static String	HEADER_TYPE	= "Header";
	public final static String	STEP_TYPE	= "Step";

	private String				type;
	private String				productName;
	private String				testCaseName;
	private String				createdBy;
	private String				createDate;
	private String				description;
	private String				stepNumber;
	private String				instruction;
	private String				expectedResult;
	private String				testCaseVersionId;

	public TestCaseExportView()
	{
		super();
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getTestCaseName()
	{
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName)
	{
		this.testCaseName = testCaseName;
	}

	public String getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}

	public String getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(String createDate)
	{
		this.createDate = createDate;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getStepNumber()
	{
		return stepNumber;
	}

	public void setStepNumber(String stepNumber)
	{
		this.stepNumber = stepNumber;
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

	public void setTestCaseVersionId(String testCaseVersionId)
	{
		this.testCaseVersionId = testCaseVersionId;
	}

	public String getTestCaseVersionId()
	{
		return testCaseVersionId;
	}

}
