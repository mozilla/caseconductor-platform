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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.utest.domain.AccessRole;
import com.utest.domain.Company;
import com.utest.domain.Environment;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentType;
import com.utest.domain.Permission;
import com.utest.domain.Product;
import com.utest.domain.ProductComponent;
import com.utest.domain.Tag;
import com.utest.domain.Team;
import com.utest.domain.TestCase;
import com.utest.domain.TestCaseStep;
import com.utest.domain.TestCycle;
import com.utest.domain.TestPlan;
import com.utest.domain.TestRun;
import com.utest.domain.TestRunResult;
import com.utest.domain.TestRunTestCaseAssignment;
import com.utest.domain.TestSuite;
import com.utest.domain.User;
import com.utest.domain.search.UtestFilter;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.search.UtestSort;
import com.utest.domain.service.Initializable;
import com.utest.domain.view.EnvironmentTypeView;
import com.utest.domain.view.EnvironmentView;
import com.utest.domain.view.TestCaseVersionView;
import com.utest.domain.view.TestRunTestCaseView;
import com.utest.domain.view.TestSuiteTestCaseView;
import com.utest.webservice.model.v2.CompanyInfo;
import com.utest.webservice.model.v2.CompanySearchResultInfo;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.EnvironmentGroupSearchResultInfo;
import com.utest.webservice.model.v2.EnvironmentInfo;
import com.utest.webservice.model.v2.EnvironmentSearchResultInfo;
import com.utest.webservice.model.v2.EnvironmentTypeInfo;
import com.utest.webservice.model.v2.EnvironmentTypeSearchResultInfo;
import com.utest.webservice.model.v2.EnvironmentTypeViewInfo;
import com.utest.webservice.model.v2.EnvironmentTypeViewSearchResultInfo;
import com.utest.webservice.model.v2.EnvironmentViewInfo;
import com.utest.webservice.model.v2.EnvironmentViewSearchResultInfo;
import com.utest.webservice.model.v2.PermissionInfo;
import com.utest.webservice.model.v2.PermissionSearchResultInfo;
import com.utest.webservice.model.v2.ProductComponentInfo;
import com.utest.webservice.model.v2.ProductComponentSearchResultInfo;
import com.utest.webservice.model.v2.ProductInfo;
import com.utest.webservice.model.v2.ProductSearchResultInfo;
import com.utest.webservice.model.v2.RoleInfo;
import com.utest.webservice.model.v2.RoleSearchResultInfo;
import com.utest.webservice.model.v2.SearchResultInfo;
import com.utest.webservice.model.v2.TagInfo;
import com.utest.webservice.model.v2.TagSearchResultInfo;
import com.utest.webservice.model.v2.TeamInfo;
import com.utest.webservice.model.v2.TeamSearchResultInfo;
import com.utest.webservice.model.v2.TestCaseInfo;
import com.utest.webservice.model.v2.TestCaseSearchResultInfo;
import com.utest.webservice.model.v2.TestCaseStepInfo;
import com.utest.webservice.model.v2.TestCaseStepSearchResultInfo;
import com.utest.webservice.model.v2.TestCaseVersionInfo;
import com.utest.webservice.model.v2.TestCaseVersionSearchResultInfo;
import com.utest.webservice.model.v2.TestCycleInfo;
import com.utest.webservice.model.v2.TestCycleSearchResultInfo;
import com.utest.webservice.model.v2.TestPlanInfo;
import com.utest.webservice.model.v2.TestPlanSearchResultInfo;
import com.utest.webservice.model.v2.TestRunInfo;
import com.utest.webservice.model.v2.TestRunResultInfo;
import com.utest.webservice.model.v2.TestRunResultSearchResultInfo;
import com.utest.webservice.model.v2.TestRunSearchResultInfo;
import com.utest.webservice.model.v2.TestRunTestCaseAssignmentInfo;
import com.utest.webservice.model.v2.TestRunTestCaseAssignmentSearchResultInfo;
import com.utest.webservice.model.v2.TestRunTestCaseInfo;
import com.utest.webservice.model.v2.TestRunTestCaseSearchResultInfo;
import com.utest.webservice.model.v2.TestSuiteInfo;
import com.utest.webservice.model.v2.TestSuiteSearchResultInfo;
import com.utest.webservice.model.v2.TestSuiteTestCaseInfo;
import com.utest.webservice.model.v2.TestSuiteTestCaseSearchResultInfo;
import com.utest.webservice.model.v2.UserInfo;
import com.utest.webservice.model.v2.UserSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;
import com.utest.webservice.util.RestUtil;

public class ObjectBuilderFactoryImpl implements ObjectBuilderFactory, Initializable
{

	private static final String			FROM			= "From";
	private static final String			TO				= "To";
	private static final String			ID				= "id";
	private static final String			CREATE_DATE		= "createDate";
	private static final String			CHANGE_DATE		= "lastChangeDate";
	private static final String			CREATE_USER_ID	= "createdBy";
	private static final String			CHANGE_USER_ID	= "lastChangedBy";

	@SuppressWarnings("unchecked")
	private static Map<Class, Builder>	builders		= new HashMap<Class, Builder>();

	ObjectBuilderFactoryImpl()
	{
	}

	@Override
	public void initialize()
	{
		builders.put(TeamInfo.class, new Builder<TeamInfo, Team>(this, TeamInfo.class, TeamSearchResultInfo.class));
		builders.put(UserInfo.class, new Builder<UserInfo, User>(this, UserInfo.class, UserSearchResultInfo.class));
		builders.put(RoleInfo.class, new Builder<RoleInfo, AccessRole>(this, RoleInfo.class, RoleSearchResultInfo.class));
		builders.put(PermissionInfo.class, new Builder<PermissionInfo, Permission>(this, PermissionInfo.class, PermissionSearchResultInfo.class));
		builders.put(EnvironmentViewInfo.class, new Builder<EnvironmentViewInfo, EnvironmentView>(this, EnvironmentViewInfo.class, EnvironmentViewSearchResultInfo.class));

		builders.put(EnvironmentTypeViewInfo.class, new Builder<EnvironmentTypeViewInfo, EnvironmentTypeView>(this, EnvironmentTypeViewInfo.class,
				EnvironmentTypeViewSearchResultInfo.class));

		builders.put(EnvironmentInfo.class, new Builder<EnvironmentInfo, Environment>(this, EnvironmentInfo.class, EnvironmentSearchResultInfo.class));
		builders.put(EnvironmentTypeInfo.class, new Builder<EnvironmentTypeInfo, EnvironmentType>(this, EnvironmentTypeInfo.class, EnvironmentTypeSearchResultInfo.class));
		builders.put(EnvironmentGroupInfo.class, new Builder<EnvironmentGroupInfo, EnvironmentGroup>(this, EnvironmentGroupInfo.class, EnvironmentGroupSearchResultInfo.class));
		builders.put(TagInfo.class, new Builder<TagInfo, Tag>(this, TagInfo.class, TagSearchResultInfo.class));
		builders.put(CompanyInfo.class, new Builder<CompanyInfo, Company>(this, CompanyInfo.class, CompanySearchResultInfo.class));
		builders.put(ProductInfo.class, new Builder<ProductInfo, Product>(this, ProductInfo.class, ProductSearchResultInfo.class));
		builders.put(ProductComponentInfo.class, new Builder<ProductComponentInfo, ProductComponent>(this, ProductComponentInfo.class, ProductComponentSearchResultInfo.class));
		builders.put(TestCaseStepInfo.class, new Builder<TestCaseStepInfo, TestCaseStep>(this, TestCaseStepInfo.class, TestCaseStepSearchResultInfo.class));
		builders.put(TestCaseInfo.class, new Builder<TestCaseInfo, TestCase>(this, TestCaseInfo.class, TestCaseSearchResultInfo.class));
		builders.put(TestCaseVersionInfo.class, new Builder<TestCaseVersionInfo, TestCaseVersionView>(this, TestCaseVersionInfo.class, TestCaseVersionSearchResultInfo.class));
		builders.put(TestRunTestCaseInfo.class, new Builder<TestRunTestCaseInfo, TestRunTestCaseView>(this, TestRunTestCaseInfo.class, TestRunTestCaseSearchResultInfo.class));
		builders.put(TestSuiteTestCaseInfo.class, new Builder<TestSuiteTestCaseInfo, TestSuiteTestCaseView>(this, TestSuiteTestCaseInfo.class,
				TestSuiteTestCaseSearchResultInfo.class));
		builders.put(TestSuiteInfo.class, new Builder<TestSuiteInfo, TestSuite>(this, TestSuiteInfo.class, TestSuiteSearchResultInfo.class));
		builders.put(TestPlanInfo.class, new Builder<TestPlanInfo, TestPlan>(this, TestPlanInfo.class, TestPlanSearchResultInfo.class));
		builders.put(TestCycleInfo.class, new Builder<TestCycleInfo, TestCycle>(this, TestCycleInfo.class, TestCycleSearchResultInfo.class));
		builders.put(TestRunInfo.class, new Builder<TestRunInfo, TestRun>(this, TestRunInfo.class, TestRunSearchResultInfo.class));
		builders.put(TestRunTestCaseAssignmentInfo.class, new Builder<TestRunTestCaseAssignmentInfo, TestRunTestCaseAssignment>(this, TestRunTestCaseAssignmentInfo.class,
				TestRunTestCaseAssignmentSearchResultInfo.class));
		builders.put(TestRunResultInfo.class, new Builder<TestRunResultInfo, TestRunResult>(this, TestRunResultInfo.class, TestRunResultSearchResultInfo.class));
	}

	@SuppressWarnings("unchecked")
	private <Ti, To> Builder getBuilder(final Class<Ti> clazz, final Class<To> clazz1)
	{
		if (builders.get(clazz) == null)
		{
			builders.put(clazz, new Builder<Ti, To>(this, clazz, null));
		}
		return builders.get(clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Ti, To> Ti toInfo(final Class<Ti> clazz, final To object, final UriBuilder ub, final Object... uriBuilderArgs) throws Exception
	{
		final Builder<Ti, To> builder = getBuilder(clazz, object.getClass());
		return builder.toInfo(object, ub, uriBuilderArgs);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Ti, To> List<Ti> toInfo(final Class<Ti> clazz, final List<To> objects, final UriBuilder ub, final Object... uriBuilderArgs) throws Exception
	{
		if ((objects == null) || objects.isEmpty())
		{
			return Collections.emptyList();
		}
		final Builder<Ti, To> builder = getBuilder(clazz, objects.get(0).getClass());
		if (builder == null)
		{
			throw new IllegalArgumentException("No builder found for class " + clazz.getName());
		}
		return builder.toInfo(objects, ub, uriBuilderArgs);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Ti, To> To toObject(final Class<To> clazz, final Ti info) throws Exception
	{
		final Builder<Ti, To> builder = getBuilder(info.getClass(), clazz);
		if (builder == null)
		{
			throw new IllegalArgumentException("No builder found for class " + clazz.getName());
		}
		return builder.toObject(clazz, info);
	}

	@Override
	public <Ti, T extends UtestSearchRequest> UtestSearch createSearch(final Class<Ti> objectClass, final T request, final UriInfo ui) throws Exception
	{
		final UtestSearch search = new UtestSearch();
		if (request.getPageSize() != null)
		{
			search.setMaxResults(request.getPageSize());
		}
		if (request.getPageNumber() != null)
		{
			search.setPage(request.getPageNumber());
		}
		if (request.getSortField() != null)
		{
			final UtestSort sort = new UtestSort();
			sort.setProperty(request.getSortField());
			sort.setDesc("desc".equalsIgnoreCase(request.getSortDirection()));
			search.addSort(sort);
		}
		if (ui != null)
		{
			addFilter(objectClass, ui, search);
		}
		return search;

	}

	@SuppressWarnings("unchecked")
	private <Ti> void addFilter(final Class<Ti> objectClass, final UriInfo ui, final UtestSearch search)
	{
		for (final Entry<String, List<String>> entry : ui.getQueryParameters().entrySet())
		{
			String propertyName = entry.getKey();
			if (propertyName.endsWith(FROM))
			{
				propertyName = propertyName.substring(0, propertyName.lastIndexOf(FROM));
			}
			if (propertyName.endsWith(TO))
			{
				propertyName = propertyName.substring(0, propertyName.lastIndexOf(TO));
			}

			Class propertyType = null;
			Method getter = null;
			// special properties derived from Resourse Identity and Timeline
			// nodes
			if (propertyName.equalsIgnoreCase(ID))
			{
				propertyType = Integer.class;
			}
			else if (propertyName.equalsIgnoreCase(CREATE_USER_ID))
			{
				propertyType = Integer.class;
			}
			else if (propertyName.equalsIgnoreCase(CHANGE_USER_ID))
			{
				propertyType = Integer.class;
			}
			else if (propertyName.equalsIgnoreCase(CREATE_DATE))
			{
				propertyType = Date.class;
			}
			else if (propertyName.equalsIgnoreCase(CHANGE_DATE))
			{
				propertyType = Date.class;
			}
			else
			{
				try
				{
					getter = objectClass.getMethod("get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1), new Class[] {});
					propertyType = getter.getReturnType();
				}
				catch (final Exception e)
				{
					try
					{
						getter = objectClass.getMethod("is" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1), new Class[] {});
						propertyType = getter.getReturnType();
					}
					catch (final Exception ex)
					{
						continue;
					}
				}
			}
			if (propertyType == null)
			{
				continue;
			}
			Object propertyValue = entry.getValue();
			if (entry.getValue().size() == 1)
			{
				propertyValue = entry.getValue().get(0);
			}
			if (entry.getValue().size() == 0)
			{
				propertyValue = true;
			}
			// special handling for Boolean types
			if (String.class.equals(propertyType) && (propertyValue instanceof String))
			{
				if (((String) propertyValue).equalsIgnoreCase("true"))
				{
					propertyValue = Boolean.TRUE;
				}
				else if (((String) propertyValue).equalsIgnoreCase("false"))
				{
					propertyValue = Boolean.FALSE;
				}
			}
			final UtestFilter filter = new UtestFilter();
			filter.setProperty(propertyName);
			filter.setValue(propertyValue);
			if (Date.class.equals(propertyType))
			{
				if (propertyValue instanceof List)
				{
					continue;
				}
				propertyValue = RestUtil.stringToDate((String) propertyValue);
				if (propertyValue == null)
				{
					continue;
				}
				filter.setValue(propertyValue);
			}
			if (entry.getKey().endsWith(FROM))
			{
				if (propertyValue instanceof List)
				{
					continue;
				}
				filter.setOperator(UtestFilter.OP_GREATER_OR_EQUAL);
			}
			else if (entry.getKey().endsWith(TO))
			{
				if (propertyValue instanceof List)
				{
					continue;
				}
				filter.setOperator(UtestFilter.OP_LESS_OR_EQUAL);
			}

			if (String.class.equals(propertyType) && (propertyValue instanceof String))
			{
				filter.setValue(propertyValue);
				filter.setOperator(UtestFilter.OP_LIKE);
			}
			else if (propertyValue instanceof List)
			{
				filter.setOperator(UtestFilter.OP_IN);
			}
			else if (filter.getValue() == null)
			{
				Method valueOf = null;
				try
				{
					valueOf = propertyType.getMethod("valueOf", new Class[] { String.class });
				}
				catch (final Exception ex)
				{
					continue;
				}
				try
				{
					filter.setValue(valueOf.invoke(null, propertyValue));
				}
				catch (final Exception e)
				{
					continue;
				}
			}
			search.addFilter(filter);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public <Ti, To> SearchResultInfo<Ti> createResult(final Class<Ti> clazz, final Class<To> clazz1, final UtestSearchRequest request, final UtestSearchResult result,
			final UriBuilder ub) throws Exception
	{
		final Builder<Ti, To> builder = getBuilder(clazz, clazz1);
		return builder.createResult(request, result, ub);
	}

}
