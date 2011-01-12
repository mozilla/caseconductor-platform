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
package com.utest.webservice.builders;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.apache.commons.beanutils.PropertyUtils;

import com.utest.domain.Entity;
import com.utest.domain.Locale;
import com.utest.domain.LocaleDescriptable;
import com.utest.domain.LocalizedEntity;
import com.utest.domain.TimelineVersionable;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.webservice.model.v2.BaseInfo;
import com.utest.webservice.model.v2.ResourceIdentity;
import com.utest.webservice.model.v2.ResourceLocator;
import com.utest.webservice.model.v2.Timeline;
import com.utest.webservice.model.v2.UtestResult;
import com.utest.webservice.model.v2.UtestResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public class Builder<Ti, To>
{

	protected final ObjectBuilderFactory		factory;
	protected Class<Ti>							infoClass;
	protected Class<? extends UtestResult<Ti>>	resultClass;

	Builder(final ObjectBuilderFactory factory, final Class<Ti> clazz, final Class<? extends UtestResult<Ti>> resultClass)
	{
		this.factory = factory;
		infoClass = clazz;
		this.resultClass = resultClass;
	}

	List<Ti> toInfo(final List<To> objects, final UriBuilder ub, final Object... uriBuilderArgs) throws Exception
	{
		final List<Ti> ret = new ArrayList<Ti>();
		if ((objects == null) || objects.isEmpty())
		{
			return ret;
		}
		for (final To obj : objects)
		{
			final Object[] args = new Object[uriBuilderArgs.length + 1];
			int i = 0;
			for (; i < uriBuilderArgs.length; i++)
			{
				args[i] = uriBuilderArgs[i];
			}
			if (obj instanceof Entity)
			{
				args[i] = ((Entity) obj).getId();
			}
			ret.add(toInfo(obj, ub.clone(), args));
		}
		return ret;

	}

	public Ti toInfo(final To object, final UriBuilder ub, Object... uriBuilderArgs) throws Exception
	{
		Ti result;
		final Constructor<Ti> constr = infoClass.getConstructor(new Class[] {});
		if (constr == null)
		{
			throw new IllegalArgumentException("No default constructor found for " + infoClass.getName());
		}
		result = constr.newInstance(new Object[] {});
		PropertyUtils.copyProperties(result, object);
		if (object instanceof LocalizedEntity)
		{
			LocalizedEntity localizedEntity = (LocalizedEntity) object;
			LocaleDescriptable localDescriptable = localizedEntity.getLocale(Locale.DEFAULT_LOCALE);
			PropertyUtils.copyProperties(result, localDescriptable);
		}
		Map<?, ?> resultProperties = PropertyUtils.describe(result);
		// don't return password field
		if (resultProperties.containsKey("password"))
		{
			PropertyUtils.setProperty(result, "password", null);
		}
		//
		populateLocators(result, ub);
		//
		if (result instanceof BaseInfo)
		{
			populateIdentityAndTimeline((BaseInfo) result, object, ub, uriBuilderArgs);
		}

		// special handling for descendands of Builder
		populateExtendedProperties(result, object, ub, uriBuilderArgs);

		return result;
	}

	protected void populateExtendedProperties(Ti result, To object, UriBuilder ub, Object[] uriBuilderArgs)
	{
		// do nothing.this method could be overitten by descendants
	}

	protected void populateLocators(Ti result, UriBuilder ub) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		Map<?, ?> resultProperties = PropertyUtils.describe(result);
		for (Object property : resultProperties.keySet())
		{
			if (((String) property).endsWith("Locator"))
			{
				String resourceName = ((String) property).substring(0, ((String) property).indexOf("Locator"));
				if (resultProperties.containsKey(resourceName + "Id"))
				{
					Integer resourceId = (Integer) PropertyUtils.getProperty(result, resourceName + "Id");
					if (resourceId != null)
					{
						String resourcePath = getResourcePath(resourceName);
						ResourceLocator resourceLocator = new ResourceLocator(resourceId, ub.path(resourcePath).build(resourceId).toString());
						PropertyUtils.setProperty(result, (String) property, resourceLocator);
					}
				}
				// represents a user who performed an operation
				else if (resourceName.endsWith("By"))
				{
					Integer resourceId = (Integer) PropertyUtils.getProperty(result, resourceName);
					if (resourceId != null)
					{
						String resourcePath = getResourcePath("user");
						ResourceLocator resourceLocator = new ResourceLocator(resourceId, ub.path(resourcePath).build(resourceId).toString());
						PropertyUtils.setProperty(result, (String) property, resourceLocator);
					}
				}
			}
		}
	}

	protected void populateIdentityAndTimeline(BaseInfo result, final To object, final UriBuilder ub, Object... uriBuilderArgs) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException
	{
		ResourceIdentity resourceIdentity = new ResourceIdentity();
		PropertyUtils.copyProperties(resourceIdentity, object);
		result.setResourceIdentity(resourceIdentity);

		if (object instanceof TimelineVersionable)
		{
			Timeline timeline = new Timeline();
			PropertyUtils.copyProperties(timeline, object);
			result.setTimeline(timeline);
		}
	}

	@SuppressWarnings("unchecked")
	public UtestResult<Ti> createResult(final UtestSearchRequest request, final UtestSearchResult result, final UriBuilder ub) throws Exception
	{
		UtestResult<Ti> info;
		if (resultClass != null)
		{
			info = resultClass.newInstance();
		}
		else
		{
			info = new UtestResultInfo<Ti>();
		}
		final List<To> list = (List<To>) result.getResults();
		if (list.isEmpty())
		{
			info.setRows((List<Ti>) Collections.emptyList());
		}
		else
		{
			info.setRows(toInfo(list, ub));
		}
		info.setTotalResults(result.getTotalRecords());
		return info;
	}

	public void updateSearch(final UtestSearchRequest request, final UtestSearch search)
	{
		search.setPage(request.getPageNumber());
		search.setMaxResults(request.getPageSize());
		return;
	}

	public To toObject(final Class<To> objectClass, final Ti info) throws Exception
	{
		To result;
		final Constructor<To> constr = objectClass.getConstructor(new Class[] {});
		if (constr == null)
		{
			throw new IllegalArgumentException("No default constructor found for " + infoClass.getName());
		}
		result = constr.newInstance(new Object[] {});
		PropertyUtils.copyProperties(result, info);
		return result;
	}

	public String toStringFromDescriptable(final Object object)
	{
		try
		{
			final Method getDescription = object.getClass().getMethod("getDescription", new Class[] {});
			return (String) getDescription.invoke(object, new Object[] {});
		}
		catch (final Exception e)
		{
			return null;
		}
	}

	public List<String> toStringListFromDescriptable(final List<?> objects)
	{
		if ((objects == null) || objects.isEmpty())
		{
			return Collections.emptyList();
		}
		final List<String> results = new ArrayList<String>();
		for (final Object object : objects)
		{
			final String value = toStringFromDescriptable(object);
			if (value != null)
			{
				results.add(value);
			}
		}
		return results;
	}

	private String getResourcePath(String resourceKey_)
	{
		return ResourceManager.getResourcePath(resourceKey_);
	}
}
