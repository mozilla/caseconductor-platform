package com.utest.domain.view;

public class TestCaseExportSingleStepExtendedView extends TestCaseExportView
{
	private String	externalAuthorEmail;
	private String	bugList;
	private String	tagList;
	private String	testSuiteList;

	public TestCaseExportSingleStepExtendedView()
	{
		super();
	}

	public String getExternalAuthorEmail()
	{
		return externalAuthorEmail;
	}

	public void setExternalAuthorEmail(String externalAuthorEmail)
	{
		this.externalAuthorEmail = externalAuthorEmail;
	}

	public String getBugList()
	{
		return bugList;
	}

	public void setBugList(String bugList)
	{
		this.bugList = bugList;
	}

	public String getTagList()
	{
		return tagList;
	}

	public void setTagList(String tagList)
	{
		this.tagList = tagList;
	}

	public String getTestSuiteList()
	{
		return testSuiteList;
	}

	public void setTestSuiteList(String testSuiteList)
	{
		this.testSuiteList = testSuiteList;
	}

}
