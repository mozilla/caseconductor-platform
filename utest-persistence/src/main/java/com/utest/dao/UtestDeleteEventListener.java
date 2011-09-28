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
package com.utest.dao;

import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.StaleObjectStateException;
import org.hibernate.engine.EntityEntry;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.event.DeleteEvent;
import org.hibernate.event.EventSource;
import org.hibernate.event.def.DefaultDeleteEventListener;
import org.hibernate.persister.entity.EntityPersister;

import com.utest.domain.TimelineEntity;

/**
 * Enforces optimistic locking for attached entities.
 */
public class UtestDeleteEventListener extends DefaultDeleteEventListener
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8516535801412321783L;

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * Handle the given delete event.  This is the cascaded form.
	 *
	 * @param event The delete event.
	 * @param transientEntities The cache of entities already deleted
	 *
	 * @throws HibernateException
	 */
	public void onDelete(DeleteEvent event, Set transientEntities) throws HibernateException
	{
		final EventSource source = event.getSession();

		final PersistenceContext persistenceContext = source.getPersistenceContext();
		Object entity = persistenceContext.unproxyAndReassociate(event.getObject());
		EntityEntry entityEntry = persistenceContext.getEntry(entity);

		final EntityPersister persister = source.getEntityPersister(event.getEntityName(), entity);
		final Object version;

		if (persister.isVersioned())
		{
			version = persister.getVersion(entity, source.getEntityMode());
			// Make sure version has not changed on deleted entities
			if ((entity instanceof TimelineEntity) && !((TimelineEntity) entity).isNew())
			{
				if (!persister.getVersionType().isEqual(version, entityEntry.getVersion()))
				{
					throw new StaleObjectStateException(persister.getEntityName(), entityEntry.getId());
				}
			}
		}
		super.onDelete(event, transientEntities);

	}
}
