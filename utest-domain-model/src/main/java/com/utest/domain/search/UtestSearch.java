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
import java.util.Collection;
import java.util.List;

public class UtestSearch implements Serializable
{

	private static final long	serialVersionUID	= 1L;

	private int					firstResult			= -1;							// -1
	// stands
	// for
	// unspecified

	private int					maxResults			= -1;							// -1
	// stands
	// for
	// unspecified

	private int					page				= -1;							// -1
	// stands
	// for
	// unspecified

	private List<UtestFilter>	filters				= new ArrayList<UtestFilter>();

	private List<UtestSort>		sorts				= new ArrayList<UtestSort>();

	public UtestSearch()
	{

	}

	// Filters
	public UtestSearch addFilter(final UtestFilter filter)
	{
		filters.add(filter);
		return this;
	}

	/**
	 * Add a filter that uses the == operator.
	 */
	public UtestSearch addFilterEqual(final String property, final Object value)
	{
		filters.add(UtestFilter.equal(property, value));
		return this;
	}

	/**
	 * Add a filter that uses the >= operator.
	 */
	public UtestSearch addFilterGreaterOrEqual(final String property, final Object value)
	{
		filters.add(UtestFilter.greaterOrEqual(property, value));
		return this;
	}

	/**
	 * Add a filter that uses the > operator.
	 */
	public UtestSearch addFilterGreaterThan(final String property, final Object value)
	{
		filters.add(UtestFilter.greaterThan(property, value));
		return this;
	}

	/**
	 * Add a filter that uses the IN operator.
	 */
	public UtestSearch addFilterIn(final String property, final Collection<?> value)
	{
		filters.add(UtestFilter.in(property, value));
		return this;
	}

	/**
	 * Add a filter that uses the IN operator.
	 * 
	 * <p>
	 * This takes a variable number of parameters. Any number of values can be
	 * specified.
	 */
	public UtestSearch addFilterIn(final String property, final Object... value)
	{
		filters.add(UtestFilter.in(property, value));
		return this;
	}

	/**
	 * Add a filter that uses the NOT IN operator.
	 */
	public UtestSearch addFilterNotIn(final String property, final Collection<?> value)
	{
		filters.add(UtestFilter.notIn(property, value));
		return this;
	}

	/**
	 * Add a filter that uses the NOT IN operator.
	 * 
	 * <p>
	 * This takes a variable number of parameters. Any number of values can be
	 * specified.
	 */
	public UtestSearch addFilterNotIn(final String property, final Object... value)
	{
		filters.add(UtestFilter.notIn(property, value));
		return this;
	}

	/**
	 * Add a filter that uses the <= operator.
	 */
	public UtestSearch addFilterLessOrEqual(final String property, final Object value)
	{
		filters.add(UtestFilter.lessOrEqual(property, value));
		return this;
	}

	/**
	 * Add a filter that uses the < operator.
	 */
	public UtestSearch addFilterLessThan(final String property, final Object value)
	{
		filters.add(UtestFilter.lessThan(property, value));
		return this;
	}

	/**
	 * Add a filter that uses the LIKE operator.
	 */
	public UtestSearch addFilterLike(final String property, final String value)
	{
		filters.add(UtestFilter.like(property, value));
		return this;
	}

	/**
	 * Add a filter that uses the ILIKE operator.
	 */
	public UtestSearch addFilterILike(final String property, final String value)
	{
		filters.add(UtestFilter.ilike(property, value));
		return this;
	}

	/**
	 * Add a filter that uses the != operator.
	 */
	public UtestSearch addFilterNotEqual(final String property, final Object value)
	{
		filters.add(UtestFilter.notEqual(property, value));
		return this;
	}

	/**
	 * Add a filter that uses the IS NULL operator.
	 */
	public UtestSearch addFilterNull(final String property)
	{
		filters.add(UtestFilter.isNull(property));
		return this;
	}

	/**
	 * Add a filter that uses the IS NOT NULL operator.
	 */
	public UtestSearch addFilterNotNull(final String property)
	{
		filters.add(UtestFilter.isNotNull(property));
		return this;
	}

	/**
	 * Add a filter that uses the IS EMPTY operator.
	 */
	public UtestSearch addFilterEmpty(final String property)
	{
		filters.add(UtestFilter.isEmpty(property));
		return this;
	}

	/**
	 * Add a filter that uses the IS NOT EMPTY operator.
	 */
	public UtestSearch addFilterNotEmpty(final String property)
	{
		filters.add(UtestFilter.isNotEmpty(property));
		return this;
	}

	public void removeFilter(final UtestFilter filter)
	{
		filters.remove(filter);
	}

	public void clearFilters()
	{
		filters.clear();
	}

	// Sorts
	public UtestSearch addSort(final UtestSort sort)
	{
		sorts.add(sort);
		return this;
	}

	/**
	 * Add ascending sort by property
	 */
	public UtestSearch addSortAsc(final String property)
	{
		sorts.add(new UtestSort(property));
		return this;
	}

	/**
	 * Add ascending sort by property
	 */
	public UtestSearch addSortAsc(final String property, final boolean ignoreCase)
	{
		sorts.add(new UtestSort(property, false, ignoreCase));
		return this;
	}

	/**
	 * Add descending sort by property
	 */
	public UtestSearch addSortDesc(final String property)
	{
		sorts.add(new UtestSort(property, true));
		return this;
	}

	/**
	 * Add descending sort by property
	 */
	public UtestSearch addSortDesc(final String property, final boolean ignoreCase)
	{
		sorts.add(new UtestSort(property, true, ignoreCase));
		return this;
	}

	/**
	 * Add sort by property. Ascending if <code>desc == false</code>, descending
	 * if <code>desc == true</code>.
	 */
	public UtestSearch addSort(final String property, final boolean desc)
	{
		sorts.add(new UtestSort(property, desc));
		return this;
	}

	/**
	 * Add sort by property. Ascending if <code>desc == false</code>, descending
	 * if <code>desc == true</code>.
	 */
	public UtestSearch addSort(final String property, final boolean desc, final boolean ignoreCase)
	{
		sorts.add(new UtestSort(property, desc, ignoreCase));
		return this;
	}

	public void removeSort(final UtestSort sort)
	{
		sorts.remove(sort);
	}

	public void clearSorts()
	{
		sorts.clear();
	}

	public void clear()
	{
		clearFilters();
		clearSorts();
		clearPaging();
	}

	// Paging
	public int getFirstResult()
	{
		return firstResult;
	}

	public void setFirstResult(final int firstResult)
	{
		this.firstResult = firstResult;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(final int page)
	{
		this.page = page;
	}

	public int getMaxResults()
	{
		return maxResults;
	}

	public void setMaxResults(final int maxResults)
	{
		this.maxResults = maxResults;
	}

	public void clearPaging()
	{
		setFirstResult(-1);
		setPage(-1);
		setMaxResults(-1);
	}

	public List<UtestFilter> getFilters()
	{
		return filters;
	}

	public void setFilters(final List<UtestFilter> filters)
	{
		this.filters = filters;
	}

	public List<UtestSort> getSorts()
	{
		return sorts;
	}

	public void setSorts(final List<UtestSort> sorts)
	{
		this.sorts = sorts;
	}

}
