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

import java.util.ArrayList;
import java.util.List;

import com.trg.search.Search;
import com.utest.dao.TypelessDAO;
import com.utest.domain.EntityExternalBug;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.ExternalBugService;

public class ExternalBugServiceImpl extends BaseServiceImpl implements ExternalBugService
{
	private final TypelessDAO	dao;

	/**
	 * Default constructor
	 */
	public ExternalBugServiceImpl(final TypelessDAO dao)
	{
		super(dao);
		this.dao = dao;
	}

	@Override
	public EntityExternalBug addEntityExternalBug(final String externalIdentifier, final String url, final Integer entityTypeId, final Integer entityId) throws Exception
	{
		final EntityExternalBug entityExternalBug = new EntityExternalBug();
		entityExternalBug.setEntityId(entityId);
		entityExternalBug.setEntityTypeId(entityTypeId);
		entityExternalBug.setExternalIdentifier(externalIdentifier);
		entityExternalBug.setUrl(url);

		final Integer entityExternalBugId = dao.addAndReturnId(entityExternalBug);
		return getEntityExternalBug(entityExternalBugId);
	}

	@Override
	public boolean deleteEntityExternalBug(final Integer entityExternalBugId_, final Integer entityId_, Integer entityTypeId_) throws Exception
	{
		EntityExternalBug entityExternalBug = getRequiredEntityById(EntityExternalBug.class, entityExternalBugId_);
		// make sure entity id and type matches before deleting
		if (entityExternalBug.getEntityId().equals(entityId_) && entityExternalBug.getEntityTypeId().equals(entityTypeId_))
		{
			dao.delete(entityExternalBug);
			return true;
		}
		return false;
	}

	@Override
	public UtestSearchResult findEntityExternalBugs(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(EntityExternalBug.class, search_);
	}

	@Override
	public EntityExternalBug getEntityExternalBug(final Integer entityExternalBugId_) throws Exception
	{
		final EntityExternalBug entityExternalBug = getRequiredEntityById(EntityExternalBug.class, entityExternalBugId_);
		return entityExternalBug;
	}

	@Override
	public List<EntityExternalBug> getEntityExternalBugsForEntity(final Integer entityId_, final Integer entityTypeId_) throws Exception
	{
		Search search = new Search(EntityExternalBug.class);
		search.addFilterEqual("entityId", entityId_);
		search.addFilterEqual("entityTypeId", entityTypeId_);
		final List<EntityExternalBug> entityExternalBugs = dao.search(EntityExternalBug.class, search);
		if ((entityExternalBugs != null) && !entityExternalBugs.isEmpty())
		{
			return entityExternalBugs;
		}
		else
		{
			return new ArrayList<EntityExternalBug>();
		}
	}
}
