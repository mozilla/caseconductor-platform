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
package com.utest.domain.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.trg.search.Search;
import com.utest.dao.TypelessDAO;
import com.utest.domain.Company;
import com.utest.domain.CompanyDependable;
import com.utest.domain.Entity;
import com.utest.domain.EnvironmentDependable;
import com.utest.domain.EnvironmentProfile;
import com.utest.domain.Locale;
import com.utest.domain.Named;
import com.utest.domain.ProductDependable;
import com.utest.domain.Versioned;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.service.EnvironmentService;
import com.utest.domain.service.util.UserUtil;
import com.utest.exception.DuplicateNameException;
import com.utest.exception.InvalidParentChildEnvironmentException;
import com.utest.exception.NoProductMatchException;
import com.utest.exception.NotFoundException;

public abstract class BaseServiceImpl
{
	protected final TypelessDAO		dao;
	protected EnvironmentService	environmentService;

	public BaseServiceImpl(final TypelessDAO dao, final EnvironmentService environmentService)
	{
		super();
		this.dao = dao;
		this.environmentService = environmentService;
	}

	public BaseServiceImpl(final TypelessDAO dao)
	{
		super();
		this.dao = dao;
	}

	protected UtestSearch applyLocalization(final UtestSearch search_)
	{
		// TODO - for localization implement user specific locale
		search_.addFilterEqual("localeCode", Locale.DEFAULT_LOCALE);
		return search_;
	}

	protected void adjustParentChildProfiles(final EnvironmentDependable parent_, final EnvironmentDependable child_, final Integer companyId_,
			final List<Integer> environmentGroupIds_) throws Exception
	{
		// update environment profile
		EnvironmentProfile environmentProfile = null;
		if (((parent_.getEnvironmentProfileId() != null) && (parent_.getEnvironmentProfileId().equals(child_.getEnvironmentProfileId())))
				|| (child_.getEnvironmentProfileId() == null))
		{
			environmentProfile = environmentService.addEnvironmentProfile(companyId_, "Created for : " + ((Entity) child_).getId(), "Included groups: "
					+ environmentGroupIds_.toString(), environmentGroupIds_);
			child_.setEnvironmentProfileId(environmentProfile.getId());
		}
		// or update existing profile
		else
		{
			environmentService.saveEnvironmentGroupsForProfile(child_.getEnvironmentProfileId(), environmentGroupIds_);
		}
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

	protected void checkProductMatch(ProductDependable entity1_, ProductDependable entity2_) throws DuplicateNameException
	{
		if (!entity1_.getProductId().equals(entity2_.getProductId()))
		{
			throw new NoProductMatchException();
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
			if (companyId != null && !Company.SYSTEM_WIDE_COMPANY_ID.equals(companyId) && !companyId.equals(companyId_))
			{
				return false;
			}
		}
		return true;
	}

	protected <T> T getRequiredEntityById(final Class<T> type_, final Serializable id_)
	{
		final T result = dao.getById(type_, id_);
		if (result == null)
		{
			throw new NotFoundException(type_.getSimpleName() + " not found: " + id_);
		}
		return result;
	}

}
