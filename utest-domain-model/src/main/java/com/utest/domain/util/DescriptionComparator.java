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
package com.utest.domain.util;

import java.util.Comparator;

import com.utest.domain.CodeValueEntity;

@SuppressWarnings("unchecked")
public class DescriptionComparator implements Comparator
{

	@Override
	public int compare(final Object o1, final Object o2)
	{
		if (!((o1 instanceof CodeValueEntity) && (o2 instanceof CodeValueEntity)))
		{
			return 0;
		}
		final CodeValueEntity cv1 = (CodeValueEntity) o1;
		final CodeValueEntity cv2 = (CodeValueEntity) o2;
		if (cv1.getSortOrder().equals(cv2.getSortOrder()))
		{
			return (cv1.getDescription().compareTo(cv2.getDescription()));
		}
		else
		{
			return cv1.getSortOrder() - cv2.getSortOrder();
		}
	}
}
