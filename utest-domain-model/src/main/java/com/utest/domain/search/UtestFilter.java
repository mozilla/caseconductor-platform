package com.utest.domain.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UtestFilter implements Serializable
{

	private static final long	serialVersionUID	= 1L;

	/**
	 * The name of the property to filter on. It may be nested. Examples:
	 * <code>"name", "dateOfBirth", "employee.age", "employee.spouse.job.title"</code>
	 */
	protected String			property;

	/**
	 * The value to compare the property with. Should be of a compatible type
	 * with the property. Note that <code>null</code> is also valid for "equal"
	 * and "not equal" comparisons. Examples:
	 * <code>"Fred", new Date(), 45</code>
	 */
	protected Object			value;

	/**
	 * The type of comparison to do between the property and the value. The
	 * options are limited to the integer constants on this class:
	 * 
	 * <code>OP_EQAUL, OP_NOT_EQUAL, OP_LESS_THAN, OP_GREATER_THAN, LESS_OR_EQUAL, OP_GREATER_OR_EQUAL, OP_IN, OP_NOT_IN, OP_LIKE, OP_ILIKE, OP_NULL, OP_NOT_NULL, OP_EMPTY, OP_NOT_EMPTY, OP_SOME, OP_ALL, OP_NONE, OP_AND, OP_OR, OP_NOT</code>
	 * .
	 */
	protected int				operator;

	public UtestFilter()
	{

	}

	public UtestFilter(final String property, final Object value, final int operator)
	{
		this.property = property;
		this.value = value;
		this.operator = operator;
	}

	public UtestFilter(final String property, final Object value)
	{
		this.property = property;
		this.value = value;
		this.operator = OP_EQUAL;
	}

	public static final int	OP_EQUAL	= 0, OP_NOT_EQUAL = 1, OP_LESS_THAN = 2, OP_GREATER_THAN = 3, OP_LESS_OR_EQUAL = 4, OP_GREATER_OR_EQUAL = 5, OP_LIKE = 6, OP_ILIKE = 7,
			OP_IN = 8, OP_NOT_IN = 9, OP_NULL = 10, OP_NOT_NULL = 11, OP_EMPTY = 12, OP_NOT_EMPTY = 13;
	public static final int	OP_AND		= 100, OP_OR = 101, OP_NOT = 102;
	public static final int	OP_SOME		= 200, OP_ALL = 201, OP_NONE = 202 /*
																			 * not
																			 * SOME
																			 */;

	/**
	 * Create a new Filter using the == operator.
	 */
	public static UtestFilter equal(final String property, final Object value)
	{
		return new UtestFilter(property, value, OP_EQUAL);
	}

	/**
	 * Create a new Filter using the < operator.
	 */
	public static UtestFilter lessThan(final String property, final Object value)
	{
		return new UtestFilter(property, value, OP_LESS_THAN);
	}

	/**
	 * Create a new Filter using the > operator.
	 */
	public static UtestFilter greaterThan(final String property, final Object value)
	{
		return new UtestFilter(property, value, OP_GREATER_THAN);
	}

	/**
	 * Create a new Filter using the <= operator.
	 */
	public static UtestFilter lessOrEqual(final String property, final Object value)
	{
		return new UtestFilter(property, value, OP_LESS_OR_EQUAL);
	}

	/**
	 * Create a new Filter using the >= operator.
	 */
	public static UtestFilter greaterOrEqual(final String property, final Object value)
	{
		return new UtestFilter(property, value, OP_GREATER_OR_EQUAL);
	}

	/**
	 * Create a new Filter using the IN operator.
	 * 
	 * <p>
	 * This takes a variable number of parameters. Any number of values can be
	 * specified.
	 */
	public static UtestFilter in(final String property, final Collection<?> value)
	{
		return new UtestFilter(property, value, OP_IN);
	}

	/**
	 * Create a new Filter using the IN operator.
	 * 
	 * <p>
	 * This takes a variable number of parameters. Any number of values can be
	 * specified.
	 */
	public static UtestFilter in(final String property, final Object... value)
	{
		return new UtestFilter(property, value, OP_IN);
	}

	/**
	 * Create a new Filter using the NOT IN operator.
	 * 
	 * <p>
	 * This takes a variable number of parameters. Any number of values can be
	 * specified.
	 */
	public static UtestFilter notIn(final String property, final Collection<?> value)
	{
		return new UtestFilter(property, value, OP_NOT_IN);
	}

	/**
	 * Create a new Filter using the NOT IN operator.
	 * 
	 * <p>
	 * This takes a variable number of parameters. Any number of values can be
	 * specified.
	 */
	public static UtestFilter notIn(final String property, final Object... value)
	{
		return new UtestFilter(property, value, OP_NOT_IN);
	}

	/**
	 * Create a new Filter using the LIKE operator.
	 */
	public static UtestFilter like(final String property, final String value)
	{
		return new UtestFilter(property, value, OP_LIKE);
	}

	/**
	 * Create a new Filter using the ILIKE operator.
	 */
	public static UtestFilter ilike(final String property, final String value)
	{
		return new UtestFilter(property, value, OP_ILIKE);
	}

	/**
	 * Create a new Filter using the != operator.
	 */
	public static UtestFilter notEqual(final String property, final Object value)
	{
		return new UtestFilter(property, value, OP_NOT_EQUAL);
	}

	/**
	 * Create a new Filter using the IS NULL operator.
	 */
	public static UtestFilter isNull(final String property)
	{
		return new UtestFilter(property, true, OP_NULL);
	}

	/**
	 * Create a new Filter using the IS NOT NULL operator.
	 */
	public static UtestFilter isNotNull(final String property)
	{
		return new UtestFilter(property, true, OP_NOT_NULL);
	}

	/**
	 * Create a new Filter using the IS EMPTY operator.
	 */
	public static UtestFilter isEmpty(final String property)
	{
		return new UtestFilter(property, true, OP_EMPTY);
	}

	/**
	 * Create a new Filter using the IS NOT EMPTY operator.
	 */
	public static UtestFilter isNotEmpty(final String property)
	{
		return new UtestFilter(property, true, OP_NOT_EMPTY);
	}

	/**
	 * Create a new Filter using the AND operator.
	 * 
	 * <p>
	 * This takes a variable number of parameters. Any number of
	 * <code>Filter</code>s can be specified.
	 */
	public static UtestFilter and(final UtestFilter... filters)
	{
		final UtestFilter filter = new UtestFilter("AND", null, OP_AND);
		for (final UtestFilter f : filters)
		{
			filter.add(f);
		}
		return filter;
	}

	/**
	 * Create a new Filter using the OR operator.
	 * 
	 * <p>
	 * This takes a variable number of parameters. Any number of
	 * <code>Filter</code>s can be specified.
	 */
	public static UtestFilter or(final UtestFilter... filters)
	{
		final UtestFilter filter = and(filters);
		filter.property = "OR";
		filter.operator = OP_OR;
		return filter;
	}
	
	/**
	 * Create a new Filter using the OR operator.
	 * 
	 * <p>
	 * This takes a variable number of parameters. Any number of
	 * <code>Filter</code>s can be specified.
	 */
	public static UtestFilter or(final List<UtestFilter> filters)
	{
		final UtestFilter filter = new UtestFilter("OR", null, OP_OR);
		for (final UtestFilter f : filters)
		{
			filter.add(f);
		}
		return filter;
	}
	
	/**
	 * Create a new Filter using the NOT operator.
	 */
	public static UtestFilter not(final UtestFilter filter)
	{
		return new UtestFilter("NOT", filter, OP_NOT);
	}

	/**
	 * Create a new Filter using the SOME operator.
	 */
	public static UtestFilter some(final String property, final UtestFilter filter)
	{
		return new UtestFilter(property, filter, OP_SOME);
	}

	/**
	 * Create a new Filter using the ALL operator.
	 */
	public static UtestFilter all(final String property, final UtestFilter filter)
	{
		return new UtestFilter(property, filter, OP_ALL);
	}

	/**
	 * Create a new Filter using the NONE operator.
	 */
	public static UtestFilter none(final String property, final UtestFilter filter)
	{
		return new UtestFilter(property, filter, OP_NONE);
	}

	/**
	 * Used with OP_OR and OP_AND filters. These filters take a collection of
	 * filters as their value. This method adds a filter to that list.
	 */
	@SuppressWarnings("unchecked")
	public void add(final UtestFilter filter)
	{
		if ((value == null) || !(value instanceof List))
		{
			value = new ArrayList();
		}
		((List) value).add(filter);
	}

	/**
	 * Used with OP_OR and OP_AND filters. These filters take a collection of
	 * filters as their value. This method removes a filter from that list.
	 */
	@SuppressWarnings("unchecked")
	public void remove(final UtestFilter filter)
	{
		if ((value == null) || !(value instanceof List))
		{
			return;
		}
		((List) value).remove(filter);
	}

	public String getProperty()
	{
		return property;
	}

	public void setProperty(final String property)
	{
		this.property = property;
	}

	public Object getValue()
	{
		return value;
	}

	public void setValue(final Object value)
	{
		this.value = value;
	}

	public int getOperator()
	{
		return operator;
	}

	public void setOperator(final int operator)
	{
		this.operator = operator;
	}

	/**
	 * @return true if the operator should have a single value specified.
	 * 
	 *         <p>
	 *         <code>EQUAL, NOT_EQUAL, LESS_THAN, LESS_OR_EQUAL, GREATER_THAN, GREATER_OR_EQUAL, LIKE, ILIKE</code>
	 */
	public boolean isTakesSingleValue()
	{
		return operator <= 7;
	}

	/**
	 * @return true if the operator should have a list of values specified.
	 * 
	 *         <p>
	 *         <code>IN, NOT_IN</code>
	 */
	public boolean isTakesListOfValues()
	{
		return (operator == OP_IN) || (operator == OP_NOT_IN);
	}

	/**
	 * @return true if the operator does not require a value to be specified.
	 * 
	 *         <p>
	 *         <code>NULL, NOT_NULL, EMPTY, NOT_EMPTY</code>
	 */
	public boolean isTakesNoValue()
	{
		return (operator >= 10) && (operator <= 13);
	}

	/**
	 * @return true if the operator should have a single Filter specified for
	 *         the value.
	 * 
	 *         <p>
	 *         <code>NOT, ALL, SOME, NONE</code>
	 */
	public boolean isTakesSingleSubFilter()
	{
		return (operator == OP_NOT) || (operator >= 200);
	}

	/**
	 * @return true if the operator should have a list of Filters specified for
	 *         the value.
	 * 
	 *         <p>
	 *         <code>AND, OR</code>
	 */
	public boolean isTakesListOfSubFilters()
	{
		return (operator == OP_AND) || (operator == OP_OR);
	}

	/**
	 * @return true if the operator does not require a property to be specified.
	 * 
	 *         <p>
	 *         <code>AND, OR, NOT</code>
	 */
	public boolean isTakesNoProperty()
	{
		return (operator >= 100) && (operator <= 102);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + operator;
		result = prime * result + ((property == null) ? 0 : property.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		final UtestFilter other = (UtestFilter) obj;
		if (operator != other.operator)
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
		if (value == null)
		{
			if (other.value != null)
			{
				return false;
			}
		}
		else if (!value.equals(other.value))
		{
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toString()
	{
		switch (operator)
		{
			case UtestFilter.OP_IN:
				return "`" + property + "` in (" + value + ")";
			case UtestFilter.OP_NOT_IN:
				return "`" + property + "` not in (" + value + ")";
			case UtestFilter.OP_EQUAL:
				return "`" + property + "` = " + value;
			case UtestFilter.OP_NOT_EQUAL:
				return "`" + property + "` != " + value;
			case UtestFilter.OP_GREATER_THAN:
				return "`" + property + "` > " + value;
			case UtestFilter.OP_LESS_THAN:
				return "`" + property + "` < " + value;
			case UtestFilter.OP_GREATER_OR_EQUAL:
				return "`" + property + "` >= " + value;
			case UtestFilter.OP_LESS_OR_EQUAL:
				return "`" + property + "` <= " + value;
			case UtestFilter.OP_LIKE:
				return "`" + property + "` LIKE " + value;
			case UtestFilter.OP_ILIKE:
				return "`" + property + "` ILIKE " + value;
			case UtestFilter.OP_AND:
			case UtestFilter.OP_OR:
				if (!(value instanceof List))
				{
					return (operator == UtestFilter.OP_AND ? "AND: " : "OR: ") + "**INVALID VALUE - NOT A LIST: (" + value + ") **";
				}

				final String op = operator == UtestFilter.OP_AND ? " and " : " or ";

				final StringBuilder sb = new StringBuilder("(");
				boolean first = true;
				for (final Object o : ((List) value))
				{
					if (first)
					{
						first = false;
					}
					else
					{
						sb.append(op);
					}
					if (o instanceof UtestFilter)
					{
						sb.append(o.toString());
					}
					else
					{
						sb.append("**INVALID VALUE - NOT A FILTER: (" + o + ") **");
					}
				}
				if (first)
				{
					return (operator == UtestFilter.OP_AND ? "AND: " : "OR: ") + "**EMPTY LIST**";
				}

				sb.append(")");
				return sb.toString();
			case UtestFilter.OP_NOT:
				if (!(value instanceof UtestFilter))
				{
					return "NOT: **INVALID VALUE - NOT A FILTER: (" + value + ") **";
				}
				return "not " + value.toString();
			case UtestFilter.OP_SOME:
				if (!(value instanceof UtestFilter))
				{
					return "SOME: **INVALID VALUE - NOT A FILTER: (" + value + ") **";
				}
				return "some `" + property + "` {" + value.toString() + "}";
			case UtestFilter.OP_ALL:
				if (!(value instanceof UtestFilter))
				{
					return "ALL: **INVALID VALUE - NOT A FILTER: (" + value + ") **";
				}
				return "all `" + property + "` {" + value.toString() + "}";
			case UtestFilter.OP_NONE:
				if (!(value instanceof UtestFilter))
				{
					return "NONE: **INVALID VALUE - NOT A FILTER: (" + value + ") **";
				}
				return "none `" + property + "` {" + value.toString() + "}";
			default:
				return "**INVALID OPERATOR: (" + operator + ") - VALUE: " + value + " **";
		}
	}

}
