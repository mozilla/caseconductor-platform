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
package com.utest.domain.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.trg.search.Search;
import com.utest.dao.TypelessDAO;
import com.utest.domain.Company;
import com.utest.domain.CompanyDependable;
import com.utest.domain.Named;
import com.utest.domain.Versioned;
import com.utest.domain.service.util.UserUtil;
import com.utest.exception.DuplicateNameException;
import com.utest.exception.InvalidParentChildEnvironmentException;
import com.utest.exception.NotFoundException;

public abstract class BaseServiceImpl
{
	private final TypelessDAO	dao;

	public BaseServiceImpl(final TypelessDAO dao)
	{
		super();
		this.dao = dao;
	}

	protected Integer getCurrentUserId()
	{
		return UserUtil.getCurrentUserId();
	}

	protected <T extends Named> void checkForDuplicateNameWithinParent(final Class<T> type_, final String name_, final Integer parentId_, final String parentColumn_,
			Integer entityId_) throws DuplicateNameException
	{
		// check for duplicate name within a product
		final Search search = new Search(type_);
		search.addFilterEqual(parentColumn_, parentId_);
		search.addFilterEqual("name", name_);
		if (entityId_ != null)
		{
			search.addFilterNotEqual("id", entityId_);
		}
		final List<T> foundEntities = dao.search(type_, search);
		// Versioned entities like TestCase may have the same name for different
		// versions of the same test case
		if ((foundEntities != null) && !foundEntities.isEmpty())
		{
			if (!Versioned.class.isAssignableFrom(type_))
			{
				throw new DuplicateNameException();
			}
			else
			{
				final List<Integer> mainEnities = new ArrayList<Integer>();
				for (final Object foundEntity : foundEntities)
				{
					final Integer mainId = ((Versioned) foundEntity).getMainEntityIdentifier();
					if (!mainEnities.contains(mainId))
					{
						mainEnities.add(mainId);
						if (mainEnities.size() > 1)
						{
							throw new DuplicateNameException();
						}
					}
				}
			}
		}
	}

	protected <T extends Named> void checkForDuplicateName(final Class<T> type_, final String name_, Integer entityId_) throws DuplicateNameException
	{
		// check for duplicate name within a product
		final Search search = new Search(type_);
		search.addFilterEqual("name", name_);
		if (entityId_ != null)
		{
			search.addFilterNotEqual("id", entityId_);
		}
		final List<T> foundEntities = dao.search(type_, search);
		if ((foundEntities != null) && !foundEntities.isEmpty())
		{
			throw new DuplicateNameException();
		}
	}

	protected <T extends CompanyDependable> void checkValidSelectionForCompany(final Integer companyId_, final List<Integer> companyDependableEntitiesIds_, final Class<T> type_)
			throws Exception
	{
		if (!isValidSelectionForCompany(companyId_, companyDependableEntitiesIds_, type_))
		{
			throw new InvalidParentChildEnvironmentException("Invalid selection for company: " + companyId_);
		}
	}

	protected <T extends CompanyDependable> void checkValidSelectionForCompany(final Integer companyId_, final List<T> companyDependableEntities_) throws Exception
	{
		if (companyDependableEntities_ == null || companyDependableEntities_.isEmpty())
		{
			return;
		}
		if (!isValidSelectionForCompany(companyId_, companyDependableEntities_))
		{
			throw new InvalidParentChildEnvironmentException("Invalid selection for company: " + companyId_);
		}
	}

	protected <T extends CompanyDependable> boolean isValidSelectionForCompany(final Integer companyId_, final List<Integer> companyDependableEntitiesIds_, final Class<T> type_)
			throws Exception
	{
		final Search search = new Search(type_);
		search.addFilterIn("id", companyDependableEntitiesIds_);
		final List<T> foundTypes = dao.search(type_, search);
		return isValidSelectionForCompany(companyId_, foundTypes);
	}

	protected <T extends CompanyDependable> boolean isValidSelectionForCompany(final Integer companyId_, final List<T> companyDependableEntities_) throws Exception
	{
		for (final T foundType : companyDependableEntities_)
		{
			final Integer companyId = foundType.getCompanyId();
			if (!Company.SYSTEM_WIDE_COMPANY_ID.equals(companyId) && !companyId.equals(companyId_))
			{
				return false;
			}
		}
		return true;
	}

	protected <T> T findEntityById(final Class<T> type_, final Serializable id_)
	{
		final T result = dao.getById(type_, id_);
		if (result == null)
		{
			throw new NotFoundException(type_.getSimpleName() + " not found: " + id_);
		}
		return result;
	}

}
