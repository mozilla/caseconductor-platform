package com.utest.domain.search;

import java.io.Serializable;


public class UtestSort implements Serializable
{

	private static final long	serialVersionUID	= 1L;

	protected String			property;
	protected boolean			desc				= false;
	protected boolean			ignoreCase			= false;

	public UtestSort()
	{

	}

	public UtestSort(final String property, final boolean desc, final boolean ignoreCase)
	{
		this.property = property;
		this.desc = desc;
		this.ignoreCase = ignoreCase;
	}

	public UtestSort(final String property, final boolean desc)
	{
		this.property = property;
		this.desc = desc;
	}

	public UtestSort(final String property)
	{
		this.property = property;
	}

	public static UtestSort asc(final String property)
	{
		return new UtestSort(property);
	}

	public static UtestSort asc(final String property, final boolean ignoreCase)
	{
		return new UtestSort(property, ignoreCase);
	}

	public static UtestSort desc(final String property)
	{
		return new UtestSort(property, true);
	}

	public static UtestSort desc(final String property, final boolean ignoreCase)
	{
		return new UtestSort(property, true, ignoreCase);
	}

	public String getProperty()
	{
		return property;
	}

	public void setProperty(final String property)
	{
		this.property = property;
	}

	public boolean isDesc()
	{
		return desc;
	}

	public void setDesc(final boolean desc)
	{
		this.desc = desc;
	}

	public boolean isIgnoreCase()
	{
		return ignoreCase;
	}

	public void setIgnoreCase(final boolean ignoreCase)
	{
		this.ignoreCase = ignoreCase;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((property == null) ? 0 : property.hashCode());
		result = prime * result + (desc ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final UtestSort other = (UtestSort) obj;
		if (desc != other.desc)
		{
			return false;
		}
		if (property == null)
		{
			if (other.property != null)
			{
				return false;
			}
		}
		else if (!property.equals(other.property))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		if (property == null)
		{
			sb.append("null");
		}
		else
		{
			sb.append("`");
			sb.append(property);
			sb.append("`");
		}
		sb.append(desc ? " desc" : " asc");
		if (ignoreCase)
		{
			sb.append(" (ignore case)");
		}
		return sb.toString();
	}
}
