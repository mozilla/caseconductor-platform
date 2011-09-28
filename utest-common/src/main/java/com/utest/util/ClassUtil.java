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
package com.utest.util;

import java.lang.reflect.Method;

public class ClassUtil
{
	/*
	 * This method use with proxy. Instead passing class object of targeted
	 * class pass methods instead.
	 */
	public static Method findMethod(Method[] methods, String methodName, Class[] parameterTypes)
	{
		Method method = null;
		for (Method methodCandidate : methods)
		{
			if (!methodCandidate.getName().equals(methodName))
			{
				continue;
			}
			Class[] params = methodCandidate.getParameterTypes();
			if (params.length != parameterTypes.length)
			{
				continue;
			}
			boolean isOk = true;
			for (int i = 0; i < params.length; i++)
			{
				if (parameterTypes[i].isPrimitive())
				{
					if (parameterTypes[i].equals(int.class) && params[i].equals(Integer.class))
					{
						continue;
					}
					if (parameterTypes[i].equals(double.class) && params[i].equals(Double.class))
					{
						continue;
					}
					if (parameterTypes[i].equals(float.class) && params[i].equals(Float.class))
					{
						continue;
					}
					if (parameterTypes[i].equals(long.class) && params[i].equals(Long.class))
					{
						continue;
					}
					if (parameterTypes[i].equals(boolean.class) && params[i].equals(Boolean.class))
					{
						continue;
					}
					if (parameterTypes[i].equals(byte.class) && params[i].equals(Byte.class))
					{
						continue;
					}
					if (parameterTypes[i].equals(short.class) && params[i].equals(Short.class))
					{
						continue;
					}
				}
				else if (!params[i].isAssignableFrom(parameterTypes[i]))
				{
					isOk = false;
					break;
				}
			}
			if (isOk)
			{
				method = methodCandidate;
				break;
			}
		}
		return method;

	}

	public static Method findMethod(Method[] methods, String methodName, Object[] parameters)
	{
		return findMethod(methods, methodName, fromObjectsToClasses(parameters));
	}

	public static Class[] fromObjectsToClasses(Object[] parameters)
	{
		Class[] parameterTypes = new Class[parameters.length];
		for (int i = 0; i < parameters.length; i++)
		{
			if (parameters[i] != null)
			{
				parameterTypes[i] = parameters[i].getClass();
			}
			else
			{
				parameterTypes[i] = Object.class;
			}
		}
		return parameterTypes;
	}

	public static Method getMethod(Class target, String methodName, Class[] parameterTypes)
	{
		return findMethod(target.getClass().getMethods(), methodName, parameterTypes);
	}

	public static Method getMethod(Class target, String methodName, Object[] parameters)
	{
		return getMethod(target, methodName, fromObjectsToClasses(parameters));
	}

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
		return false;
	}

	public static Object convertStringToPrimitive(Object object, Class toType)
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

}