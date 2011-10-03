package com.utest.domain.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.BeanUtils;

import com.utest.domain.CodeValueEntity;
import com.utest.domain.Descriptable;
import com.utest.domain.Entity;
import com.utest.domain.Locale;
import com.utest.domain.LocaleDescriptable;
import com.utest.domain.LocalizedEntity;
import com.utest.domain.ParentDependable;
import com.utest.domain.TimelineVersionable;
import com.utest.domain.User;

/**
 * @author VKISEN
 *
 */
/**
 * @author VKISEN
 * 
 */
public class DomainUtil
{

	private static final Integer	ALL_IDS	= -666;

	public static List<Integer> extractEntityIds(final List<?> entities_)
	{
		final List<Integer> ids = new ArrayList<Integer>();
		if (entities_ != null)
		{
			for (final Object entity : entities_)
			{
				if (entity instanceof Entity)
				{
					ids.add(((Entity) entity).getId());
				}
			}
		}
		return ids;
	}

	public static List<Integer> extractLocalDescriptableIds(final List<?> entities_)
	{
		final List<Integer> ids = new ArrayList<Integer>();
		if (entities_ != null)
		{
			for (final Object entity : entities_)
			{
				if (entity instanceof LocaleDescriptable)
				{
					ids.add(((LocaleDescriptable) entity).getEntityId());
				}
			}
		}
		return ids;
	}

	public static <T> List<Entity> buildEntitiesFromIds(final Class<T> destinationClazz, final List<Integer> ids_) throws Exception
	{
		final List<Entity> entities = new ArrayList<Entity>();
		for (final Integer id : ids_)
		{
			final Object entity = Class.forName(destinationClazz.getName()).newInstance();
			if (entity instanceof Entity)
			{
				((Entity) entity).setId(id);
				entities.add((Entity) entity);
			}
		}
		return entities;
	}

	public static <T> Object copyProperties(final Class<T> destinationClazz, final Object source) throws Exception
	{
		final Object obj = Class.forName(destinationClazz.getName()).newInstance();
		return copyProperties(obj, source);

	}

	public static Object copyProperties(final Object dest, final Object source) throws Exception
	{
		BeanUtils.copyProperties(dest, source);
		return dest;
	}

	/**
	 * Filter child data by corresponding parent
	 * 
	 * @param allData_
	 * @return
	 */
	public static ConcurrentMap<String, Vector<CodeValueEntity>> filterDataByParent(final Collection<ParentDependable> allData_)
	{
		final ConcurrentMap<String, Vector<CodeValueEntity>> filteredData = new ConcurrentHashMap<String, Vector<CodeValueEntity>>();
		for (final ParentDependable child : allData_)
		{
			final String parentId = child.getParentId() + "";
			Vector<CodeValueEntity> parentData = filteredData.get(parentId);
			if (parentData == null)
			{
				parentData = new Vector<CodeValueEntity>();
				filteredData.put(parentId, parentData);
			}
			parentData.add(DomainUtil.convertToCodeValue(child));
		}
		return filteredData;
	}

	public static Vector<CodeValueEntity> convertToCodeValues(final Vector<LocalizedEntity> data_, final String localeCode_)
	{
		final Vector<CodeValueEntity> translated = new Vector<CodeValueEntity>();
		if (data_ != null)
		{
			for (final LocalizedEntity entity : data_)
			{
				final CodeValueEntity newValue = convertToCodeValue(entity.getLocale(localeCode_));
				if (newValue != null)
				{
					translated.add(newValue);
				}
			}
		}

		return translated;
	}

	public static <T> Vector<CodeValueEntity> convertToCodeValues(final Vector<T> data_)
	{
		final Vector<CodeValueEntity> translated = new Vector<CodeValueEntity>();
		if (data_ != null)
		{
			for (final T entity : data_)
			{
				final CodeValueEntity newValue = convertToCodeValue(entity);
				if (newValue != null)
				{
					translated.add(newValue);
				}
			}
		}

		return translated;
	}

	public static <T> List<CodeValueEntity> convertToCodeValues(final List<T> data_)
	{
		final List<CodeValueEntity> translated = new ArrayList<CodeValueEntity>();
		if (data_ != null)
		{
			for (final T entity : data_)
			{
				final CodeValueEntity newValue = convertToCodeValue(entity);
				if (newValue != null)
				{
					translated.add(newValue);
				}
			}
		}

		return translated;
	}

	public static CodeValueEntity convertToCodeValue(final Object old_)
	{
		final CodeValueEntity translated = new CodeValueEntity();
		if (old_ instanceof LocaleDescriptable)
		{
			final LocaleDescriptable locale = (LocaleDescriptable) old_;
			if (old_ instanceof ParentDependable)
			{
				translated.setId(((ParentDependable) locale).getChildId().toString());
			}
			else
			{
				translated.setId(locale.getEntityId().toString());
			}
			translated.setDescription(locale.getName());
			translated.setSortOrder(locale.getSortOrder());
		}
		else if (old_ instanceof Descriptable)
		{
			final Descriptable locale = (Descriptable) old_;
			if (old_ instanceof ParentDependable)
			{
				translated.setId(((ParentDependable) locale).getChildId().toString());
			}
			else
			{
				translated.setId(locale.getId().toString());
			}
			translated.setDescription(locale.getDescription());
			translated.setSortOrder(locale.getSortOrder());
		}
		else if (old_ instanceof Locale)
		{
			final Locale locale = (Locale) old_;
			translated.setId(locale.getCode());
			translated.setDescription(locale.getName());
			translated.setSortOrder(locale.getSortOrder());
		}
		return translated;
	}

	public static ConcurrentMap<String, Vector<CodeValueEntity>> convertToParentDependableCodeValues(final List<LocalizedEntity> data_, final String localeCode_)
	{
		final Vector<ParentDependable> filterable = new Vector<ParentDependable>();
		if (data_ != null)
		{
			for (final LocalizedEntity entity : data_)
			{
				final Object newValue = entity.getLocale(localeCode_);
				if (newValue instanceof ParentDependable)
				{
					filterable.add((ParentDependable) newValue);
				}
			}
		}
		return filterDataByParent(filterable);
	}

	/**
	 * Utility method which return special List indicating that there are no
	 * restrictions, i.e. for Admin
	 * 
	 * @return
	 */
	public static List<Integer> getAllEntityIds()
	{
		final List<Integer> allTestCycles = new ArrayList<Integer>();
		allTestCycles.add(ALL_IDS);
		return allTestCycles;
	}

	/**
	 * @param auth_
	 *            - user adding new entity
	 * @param timelineEntity_
	 *            - entity to be updated
	 * @return TimelineEntity - updated entity
	 */
	public static void loadAddedTimeline(final User auth_, final TimelineVersionable timelineEntity_)
	{
		loadAddedTimeline(auth_.getId(), timelineEntity_);
	}

	public static void loadAddedTimeline(final Integer userId_, final TimelineVersionable timelineEntity_)
	{
		final Date date = new Date();
		timelineEntity_.setCreateDate(date);
		timelineEntity_.setCreatedBy(userId_);
		timelineEntity_.setLastChangeDate(date);
		timelineEntity_.setLastChangedBy(userId_);
	}

	/**
	 * @param auth_
	 *            - user making changes to entity
	 * @param timelineEntity_
	 *            - entity to be updated
	 * @param originalVersionId_
	 *            - entity version before update. this is used to enforce
	 *            Optimistic locking
	 * @return TimelineEntity - updated entity
	 */
	public static void loadUpdatedTimeline(final User auth_, final TimelineVersionable timelineEntity_, final Integer originalVersionId_)
	{
		loadUpdatedTimeline(auth_.getId(), timelineEntity_, originalVersionId_);
	}

	public static void loadUpdatedTimeline(final Integer userId_, final TimelineVersionable timelineEntity_, final Integer originalVersionId_)
	{
		final Date date = new Date();
		timelineEntity_.setLastChangeDate(date);
		timelineEntity_.setLastChangedBy(userId_);
		timelineEntity_.setVersion(originalVersionId_);
	}

	/**
	 * Utility method which evaluates the List of ids to see if there are no
	 * restrictions, i.e. for Admin
	 * 
	 * @return
	 */
	public static boolean isAllEntityIds(final List<Integer> ids_)
	{
		return ((ids_ != null) && (ids_.size() == 1) && ids_.contains(ALL_IDS));
	}
}