package com.utest.client.test;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ClassUtil
{
	@SuppressWarnings("unchecked")
	public static boolean canConvert(Object object, Class toType)
	{
		if (object == null)
		{
			return true;
		}

		if (toType == Object.class)
		{
			return true;
		}

		Class fromType = object.getClass();
		if (fromType.equals(toType))
		{
			return true;
		}

		if (toType.isAssignableFrom(fromType))
		{
			return true;
		}

		if (toType == String.class)
		{
			return true;
		}

		if (object instanceof Boolean)
		{
			if (toType == boolean.class || Number.class.isAssignableFrom(toType))
			{
				return true;
			}
		}
		else if (object instanceof Number)
		{
			if (toType.isPrimitive() || Number.class.isAssignableFrom(toType))
			{
				return true;
			}
		}
		else if (object instanceof Character)
		{
			if (toType == char.class)
			{
				return true;
			}
		}
		else if (object instanceof String)
		{
			if (toType.isPrimitive())
			{
				return true;
			}
			if (toType == Boolean.class || toType == Character.class || toType == Byte.class || toType == Short.class || toType == Integer.class || toType == Long.class
					|| toType == Float.class || toType == Double.class)
			{
				return true;
			}
		}
		else if (fromType.isArray())
		{
			// Collection -> array
			if (toType.isArray())
			{
				Class cType = toType.getComponentType();
				int length = Array.getLength(object);
				for (int i = 0; i < length; i++)
				{
					Object value = Array.get(object, i);
					if (!canConvert(value, cType))
					{
						return false;
					}
				}
				return true;
			}
			else if (Collection.class.isAssignableFrom(toType))
			{
				return canCreateCollection(toType);
			}
			else
			{
				if (Array.getLength(object) > 0)
				{
					Object value = Array.get(object, 0);
					return canConvert(value, toType);
				}
				else
				{
					return canConvert("", toType);
				}
			}
		}
		else if (object instanceof Collection)
		{
			// Collection -> array
			if (toType.isArray())
			{
				Class cType = toType.getComponentType();
				Iterator it = ((Collection) object).iterator();
				while (it.hasNext())
				{
					Object value = it.next();
					if (!canConvert(value, cType))
					{
						return false;
					}
				}
				return true;
			}
			else if (Collection.class.isAssignableFrom(toType))
			{
				return canCreateCollection(toType);
			}
			else
			{
				if (((Collection) object).size() > 0)
				{
					Object value;
					if (object instanceof List)
					{
						value = ((List) object).get(0);
					}
					else
					{
						Iterator it = ((Collection) object).iterator();
						value = it.next();
					}
					return canConvert(value, toType);
				}
				else
				{
					return canConvert("", toType);
				}
			}
		}
		return false;
	}

	/**
	 * Converts the supplied object to the specified type. Throws a runtime
	 * exception if the conversion is not possible.
	 */
	@SuppressWarnings("unchecked")
	public static Object convert(Object object, Class toType)
	{
		if (object == null)
		{
			if (toType.isPrimitive())
			{
				return convertNullToPrimitive(toType);
			}
			return null;
		}

		if (toType == Object.class)
		{
			return object;
		}

		Class fromType = object.getClass();
		if (fromType.equals(toType) || toType.isAssignableFrom(fromType))
		{
			return object;
		}

		if (fromType.isArray())
		{
			int length = Array.getLength(object);
			if (toType.isArray())
			{
				Class cType = toType.getComponentType();

				Object array = Array.newInstance(cType, length);
				for (int i = 0; i < length; i++)
				{
					Object value = Array.get(object, i);
					Array.set(array, i, convert(value, cType));
				}
				return array;
			}
			else if (Collection.class.isAssignableFrom(toType))
			{
				Collection collection = allocateCollection(toType);
				for (int i = 0; i < length; i++)
				{
					collection.add(Array.get(object, i));
				}
				return unmodifiableCollection(collection);
			}
			else
			{
				if (length > 0)
				{
					Object value = Array.get(object, 0);
					return convert(value, toType);
				}
				else
				{
					return convert("", toType);
				}
			}
		}
		else if (object instanceof Collection)
		{
			int length = ((Collection) object).size();
			if (toType.isArray())
			{
				Class cType = toType.getComponentType();
				Object array = Array.newInstance(cType, length);
				Iterator it = ((Collection) object).iterator();
				for (int i = 0; i < length; i++)
				{
					Object value = it.next();
					Array.set(array, i, convert(value, cType));
				}
				return array;
			}
			else if (Collection.class.isAssignableFrom(toType))
			{
				Collection collection = allocateCollection(toType);
				collection.addAll((Collection) object);
				return unmodifiableCollection(collection);
			}
			else
			{
				if (length > 0)
				{
					Object value;
					if (object instanceof List)
					{
						value = ((List) object).get(0);
					}
					else
					{
						Iterator it = ((Collection) object).iterator();
						value = it.next();
					}
					return convert(value, toType);
				}
				else
				{
					return convert("", toType);
				}
			}
		}
		else if (toType == String.class)
		{
			return object.toString();
		}
		else if (object instanceof Boolean)
		{
			if (toType == boolean.class)
			{
				return object;
			}
			boolean value = ((Boolean) object).booleanValue();
			return allocateNumber(toType, value ? 1 : 0);
		}
		else if (object instanceof Number)
		{
			double value = ((Number) object).doubleValue();
			if (toType == boolean.class || toType == Boolean.class)
			{
				return value == 0.0 ? Boolean.FALSE : Boolean.TRUE;
			}
			if (toType.isPrimitive() || Number.class.isAssignableFrom(toType))
			{
				return allocateNumber(toType, value);
			}
		}
		else if (object instanceof Character)
		{
			if (toType == char.class)
			{
				return object;
			}
		}
		else if (object instanceof String)
		{
			Object value = convertStringToPrimitive(object, toType);
			if (value != null)
			{
				return value;
			}
		}
		throw new RuntimeException("Cannot convert " + object.getClass() + " to " + toType);
	}

	@SuppressWarnings("unchecked")
	protected static Object convertNullToPrimitive(Class toType)
	{
		if (toType == boolean.class)
		{
			return Boolean.FALSE;
		}
		if (toType == char.class)
		{
			return new Character('\0');
		}
		if (toType == byte.class)
		{
			return new Byte((byte) 0);
		}
		if (toType == short.class)
		{
			return new Short((short) 0);
		}
		if (toType == int.class)
		{
			return new Integer(0);
		}
		if (toType == long.class)
		{
			return new Long(0L);
		}
		if (toType == float.class)
		{
			return new Float(0.0f);
		}
		if (toType == double.class)
		{
			return new Double(0.0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected static Object convertStringToPrimitive(Object object, Class toType)
	{
		if (toType == boolean.class || toType == Boolean.class)
		{
			return Boolean.valueOf((String) object);
		}
		if (toType == char.class || toType == Character.class)
		{
			return new Character(((String) object).charAt(0));
		}
		if (toType == byte.class || toType == Byte.class)
		{
			return new Byte((String) object);
		}
		if (toType == short.class || toType == Short.class)
		{
			return new Short((String) object);
		}
		if (toType == int.class || toType == Integer.class)
		{
			return new Integer((String) object);
		}
		if (toType == long.class || toType == Long.class)
		{
			return new Long((String) object);
		}
		if (toType == float.class || toType == Float.class)
		{
			return new Float((String) object);
		}
		if (toType == double.class || toType == Double.class)
		{
			return new Double((String) object);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected static Number allocateNumber(Class type, double value)
	{
		if (type == Byte.class || type == byte.class)
		{
			return new Byte((byte) value);
		}
		if (type == Short.class || type == short.class)
		{
			return new Short((short) value);
		}
		if (type == Integer.class || type == int.class)
		{
			return new Integer((int) value);
		}
		if (type == Long.class || type == long.class)
		{
			return new Long((long) value);
		}
		if (type == Float.class || type == float.class)
		{
			return new Float((float) value);
		}
		if (type == Double.class || type == double.class)
		{
			return new Double(value);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected static boolean canCreateCollection(Class type)
	{
		if (!type.isInterface() && ((type.getModifiers() & Modifier.ABSTRACT) == 0))
		{
			return true;
		}

		if (type == List.class)
		{
			return true;
		}

		if (type == Set.class)
		{
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected static Collection allocateCollection(Class type)
	{
		if (!type.isInterface() && ((type.getModifiers() & Modifier.ABSTRACT) == 0))
		{
			try
			{
				return (Collection) type.newInstance();
			}
			catch (Exception ex)
			{
				throw new RuntimeException("Cannot create collection of type: " + type, ex);
			}
		}

		if (type == List.class)
		{
			return new ArrayList();
		}
		if (type == Set.class)
		{
			return new HashSet();
		}
		throw new RuntimeException("Cannot create collection of type: " + type);
	}

	@SuppressWarnings("unchecked")
	protected static Collection unmodifiableCollection(Collection collection)
	{
		if (collection instanceof List)
		{
			return Collections.unmodifiableList((List) collection);
		}
		else if (collection instanceof Set)
		{
			return Collections.unmodifiableSet((Set) collection);
		}
		// Cannot wrap it into a proper unmodifiable collection,
		// so we just return the original collection itself
		return collection;
	}
}
