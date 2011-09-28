/*
Case Conductor is a Test Case Management system.
Copyright (C) 2011 uTest Inc.

This file is part of Case Conductor.

Case Conductor is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Case Conductor is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Case Conductor.  If not, see <http://www.gnu.org/licenses/>.

*/
package com.utest.domain.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UtestSearchResult implements Serializable
{

	private static final long	serialVersionUID	= 1L;

	private int					totalRecords		= 0;
	@SuppressWarnings("unchecked")
	private List<?>				results				= new ArrayList();

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
