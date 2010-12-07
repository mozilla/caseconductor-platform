package com.utest.domain.search;

import java.io.Serializable;
import java.util.List;


public class UtestSearchResult implements Serializable
{

	private static final long	serialVersionUID	= 1L;

	private int					totalRecords;
	private List<?>				results;

	public UtestSearchResult()
	{

	}

	public int getTotalRecords()
	{
		return totalRecords;
	}

	public void setTotalRecords(final int totalRecords)
	{
		this.totalRecords = totalRecords;
	}

	public List<?> getResults()
	{
		return results;
	}

	public void setResults(final List<?> results)
	{
		this.results = results;
	}

}
