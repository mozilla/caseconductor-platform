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

import org.hibernate.HibernateException;
import org.hibernate.StaleObjectStateException;
import org.hibernate.engine.EntityEntry;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.event.FlushEntityEvent;
import org.hibernate.event.def.DefaultFlushEntityEventListener;
import org.hibernate.persister.entity.EntityPersister;

import com.utest.domain.TimelineEntity;

/**
 * Enforces optimistic locking for attached entities.
 */
public class UtestFlushEntityEventListener extends DefaultFlushEntityEventListener
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8516535801412513783L;

	@Override
	public void onFlushEntity(final FlushEntityEvent event) throws HibernateException
	{

		final Object object = event.getEntity();
		final EntityEntry entry = event.getEntityEntry();
		final EntityPersister persister = entry.getPersister();
		// final Status status = entry.getStatus();
		final SessionImplementor session = event.getSession();
		if (persister.isVersioned())
		{
			final Object version = persister.getVersion(object, session.getEntityMode());
			// Make sure version has not changed on updated entities
			if ((object instanceof TimelineEntity) && !((TimelineEntity) object).isNew())
			{
				if (!persister.getVersionType().isEqual(version, entry.getVersion()))
				{
					throw new StaleObjectStateException(persister.getEntityName(), entry.getId());
				}
			}
		}
		super.onFlushEntity(event);
	}
}
