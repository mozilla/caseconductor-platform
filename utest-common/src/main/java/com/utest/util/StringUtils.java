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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringUtils
{
	public final static String	SEARCH_CRITERIA_SEPARATOR	= ",";

	public static List<String> toList(final String values, final String separator)
	{
		final List<String> list = new ArrayList<String>();
		if ((values == null) || (values.length() == 0))
		{
			return list;
		}
		for (final String value : values.split(separator))
		{
			list.add(value);
		}
		return list;
	}

	public static String[] toArray(final String values, final String separator)
	{
		return toList(values, separator).toArray(new String[0]);
	}

	public static Map<String, String> toMap(final String values, final String groupSeparator, final String keyValueSeparator)
	{
		final Map<String, String> map = new HashMap<String, String>();
		if ((values == null) || (values.length() == 0))
		{
			return map;
		}

		for (final String group : toList(values, groupSeparator))
		{
			final int index = group.indexOf(keyValueSeparator);
			if (index < 0)
			{
				map.put(group, null);
				continue;
			}
			if (index == 0)
			{
				throw new IllegalArgumentException();
			}
			map.put(group.substring(0, index), group.substring(index + 1));
		}
		return map;
	}

}
