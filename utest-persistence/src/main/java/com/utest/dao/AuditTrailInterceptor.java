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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.utest.annotations.Audit;
import com.utest.domain.AuditRecord;
import com.utest.domain.AuthenticatedUserInfo;
import com.utest.domain.Entity;
import com.utest.domain.TimelineVersionable;

public class AuditTrailInterceptor extends EmptyInterceptor
{

	/**
	 * 
	 */
	private static final long			serialVersionUID	= -3515436262982521201L;

	private SessionFactory				sessionFactory;

	private final Vector<AuditRecord>	logRecords			= new Vector<AuditRecord>();

	/**
	 * @param sessionFactory
	 *            The sessionFactory to set.
	 */
	public void setSessionFactory(final SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void onDelete(final Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types) throws CallbackException
	{
		if (entity.getClass().isAnnotationPresent(Audit.class))
		{
			try
			{
				logChanges(entity, null, id, AuditRecord.DELETE, entity.getClass().getSimpleName());
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	@Override
	public Boolean isTransient(final Object entity)
	{
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onFlushDirty(final Object entity, final Serializable id, final Object[] currentState, final Object[] previousState, final String[] propertyNames,
			final Type[] types) throws CallbackException
	{
		boolean returnCode = false;
		if (entity instanceof TimelineVersionable)
		{
			returnCode = true;
			final Date date = new Date();
			final Integer userId = getCurrentUserId();
			setValue(currentState, propertyNames, "lastChangedBy", userId);
			setValue(currentState, propertyNames, "lastChangeDate", date);
		}

		if (entity.getClass().isAnnotationPresent(Audit.class))
		{
			returnCode = true;
			final Session session = sessionFactory.openSession();
			final Class objectClass = entity.getClass();
			final String className = objectClass.getSimpleName();

			// Use the id and class to get the pre-update state from the
			// database
			final Serializable persistedObjectId = getObjectId(entity);
			final Object preUpdateState = session.get(objectClass, persistedObjectId);

			try
			{
				logChanges(entity, preUpdateState, persistedObjectId, AuditRecord.UPDATE, className);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
			session.close();
		}
		return returnCode;
	}

	@Override
	public boolean onSave(final Object entity, final Serializable id, final Object[] currentState, final String[] propertyNames, final Type[] types) throws CallbackException
	{
		boolean returnCode = false;
		if (entity instanceof TimelineVersionable)
		{
			returnCode = true;
			final Date date = new Date();
			final Integer userId = getCurrentUserId();
			setValue(currentState, propertyNames, "createdBy", userId);
			setValue(currentState, propertyNames, "createDate", date);
			setValue(currentState, propertyNames, "lastChangedBy", userId);
			setValue(currentState, propertyNames, "lastChangeDate", date);
		}

		if (entity.getClass().isAnnotationPresent(Audit.class))
		{
			returnCode = true;
			try
			{
				logChanges(entity, null, null, AuditRecord.INSERT, entity.getClass().getSimpleName());

			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		}
		return returnCode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void postFlush(final Iterator entities) throws CallbackException
	{
		final Session session = sessionFactory.openSession();

		try
		{
			while (!logRecords.isEmpty())
			{
				final AuditRecord logRecord = logRecords.firstElement();
				logRecords.remove(logRecord);
				if (AuditRecord.INSERT.equals(logRecord.getEventId()))
				{
					final Integer id = getObjectId(logRecord.getEntityObject());
					if (id != null)
					{
						logRecord.setEntityId(getObjectId(logRecord.getEntityObject()));
						session.save(logRecord);
					}
					else
					{
						session.evict(logRecord);
					}
				}
				else
				{
					session.save(logRecord);
				}
			}
		}
		catch (final HibernateException e)
		{
			throw new CallbackException(e);
		}
		finally
		{
			// logRecords.clear();
			session.flush();
			session.close();
		}

	}

	@SuppressWarnings("unchecked")
	private void logChanges(final Object newObject, final Object existingObject, final Serializable persistedObjectId, final Integer event, final String className)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		final Class objectClass = newObject.getClass();
		// Iterate through all the fields in the object
		for (final Field field : getAllFields(objectClass))
		{

			// make private fields accessible so we can access their values
			field.setAccessible(true);

			final String fieldName = field.getName();
			if (fieldName.equals("id"))
			{
				continue;
			}

			String propertyNewState = "";
			String propertyPreUpdateState = "";

			if (!AuditRecord.DELETE.equals(event))
			{
				try
				{
					final Object objPropNewState = field.get(newObject);
					if (objPropNewState != null)
					{
						propertyNewState = objPropNewState.toString();
					}
				}
				catch (final Exception e)
				{
					propertyNewState = "";
				}
			}
			if (AuditRecord.UPDATE.equals(event))
			{

				try
				{
					final Object objPreUpdateState = field.get(existingObject);
					if (objPreUpdateState != null)
					{
						propertyPreUpdateState = objPreUpdateState.toString();
					}
				}
				catch (final Exception e)
				{
					propertyPreUpdateState = "";
				}

				if (propertyNewState.equals(propertyPreUpdateState))
				{
					continue;
				}
			}

			final AuditRecord entry = new AuditRecord();
			entry.setDate(new Date());
			entry.setEntityId((Integer) persistedObjectId);
			entry.setPropertyName(fieldName);
			entry.setPropertyValue(propertyNewState);
			entry.setEntity(className);
			entry.setEventId(event);
			entry.setEntityObject(newObject);
			entry.setUserId(getCurrentUserId());
			if (!logRecords.contains(entry))
			{
				logRecords.add(entry);
			}
		}

	}

	@SuppressWarnings("unchecked")
	private Field[] getAllFields(final Class objectClass)
	{

		final List<Field> fields = new ArrayList<Field>();
		for (final Method method : objectClass.getMethods())
		{
			if (method.isAnnotationPresent(Audit.class))
			{
				try
				{
					final Field methodField = objectClass.getDeclaredField(method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4));
					if (methodField != null)
					{
						fields.add(methodField);
					}
				}
				catch (final Exception e)
				{
				}
			}
		}
		for (final Field field : objectClass.getDeclaredFields())
		{
			if (field.isAnnotationPresent(Audit.class))
			{
				fields.add(field);
			}
		}

		return fields.toArray(new Field[fields.size()]);

	}

	private Integer getObjectId(final Object obj)
	{
		Integer persistedObjectId = null;
		try
		{
			if (obj instanceof Entity)
			{
				final Entity entity = (Entity) obj;
				persistedObjectId = entity.getId();
			}
		}
		catch (final Exception e)
		{

		}
		return persistedObjectId;
	}

	@Override
	public String onPrepareStatement(final String sql)
	{
		return sql;
	}

	@Override
	public void afterTransactionCompletion(final Transaction tx)
	{
		logRecords.clear();
	}

	private void setValue(final Object[] currentState, final String[] propertyNames, final String propertyToSet, final Object value)
	{
		final List<String> names = Arrays.asList(propertyNames);
		final int index = names.indexOf(propertyToSet);
		if (index >= 0)
		{
			currentState[index] = value;
		}
	}

	private Integer getCurrentUserId()
	{
		final SecurityContext ctx = SecurityContextHolder.getContext();
		Authentication auth = null;
		if (ctx != null)
		{
			auth = ctx.getAuthentication();
		}
		return ((AuthenticatedUserInfo) auth.getPrincipal()).getLoggedInUserId();
	}

}
