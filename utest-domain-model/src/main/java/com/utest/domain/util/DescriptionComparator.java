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
