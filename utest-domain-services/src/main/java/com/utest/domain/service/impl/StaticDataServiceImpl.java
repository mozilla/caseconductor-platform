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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.trg.search.Search;
import com.utest.annotations.ParentDependableType;
import com.utest.dao.TypelessDAO;
import com.utest.domain.ApprovalStatus;
import com.utest.domain.CodeValueEntity;
import com.utest.domain.Country;
import com.utest.domain.Descriptable;
import com.utest.domain.Locale;
import com.utest.domain.LocaleDescriptable;
import com.utest.domain.LocalizedEntity;
import com.utest.domain.ParentDependable;
import com.utest.domain.ProfileDependable;
import com.utest.domain.service.StaticDataService;
import com.utest.domain.util.DescriptionComparator;
import com.utest.domain.util.DomainUtil;

public class StaticDataServiceImpl implements StaticDataService
{
	/**
	 * 
	 */
	private static final long																					serialVersionUID			= 1L;
	private final Logger																						logger						= LogManager
																																					.getLogger(StaticDataServiceImpl.class);
	private final TypelessDAO																					dao;
	private final DescriptionComparator																			comparator					= new DescriptionComparator();
	// native objects
	private static ConcurrentMap<String, Vector<?>>																_nativeObjects				= new ConcurrentHashMap<String, Vector<?>>();
	// className, CodeValueEntity
	private static ConcurrentMap<String, Vector<CodeValueEntity>>												_codeMap					= null;
	// localeCode -> className, CodeValueEntity
	private static ConcurrentMap<String, ConcurrentMap<String, Vector<CodeValueEntity>>>						_localizedCodeMap			= null;
	// localeCode -> className -> parentID -> List of children
	private static ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<String, Vector<CodeValueEntity>>>>	_localizedParentDependable	= null;
	// className -> parentID -> List of children
	private static ConcurrentMap<String, ConcurrentMap<String, Vector<CodeValueEntity>>>						_parentDependable			= null;
	// parmName -> parmValue
	private static ConcurrentMap<String, String>																_defaultReportStyles		= null;

	public final static Integer																					DEFAULT_STYLE_ID			= -1;

	@SuppressWarnings("unchecked")
	private static List<Class>																					localizedData				= new ArrayList<Class>();
	@SuppressWarnings("unchecked")
	private static List<Class>																					nonTranslatableData			= new ArrayList<Class>();

	static
	{
		localizedData.add(Country.class);
		// localizedData.add(TestCycleStatus.class);
		// localizedData.add(TestingType.class);
		// localizedData.add(EntityType.class);
		// localizedData.add(UserStatus.class);
		localizedData.add(ApprovalStatus.class);

		// nonTranslatableData.add(Locale.class);
		// nonTranslatableData.add(OperatingSystem.class);
	}

	public StaticDataServiceImpl(final TypelessDAO dao)
	{
		this.dao = dao;
	}

	public void initialize()
	{
		loadStaticData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addToParent(final Class parentDependableClazz_, final List<Integer> parentId, final Class childClazz_, final Integer childId) throws Exception
	{
		final Object child = dao.getById(childClazz_, childId);
		if (child == null)
		{
			throw new IllegalArgumentException("Object of class " + childClazz_.getName() + " with id " + childId + " not found!");
		}
		dao.addOrUpdate(createRelationClasses(parentDependableClazz_, parentId, child).toArray());
		loadNonTranslatableData(nonTranslatableData);
	}

	@SuppressWarnings("unchecked")
	private Object createClass(final Class clazz_, final String description, final List<Integer> parentId) throws Exception
	{
		boolean found = false;
		boolean isParentDependable = false;
		for (final Class intf : clazz_.getInterfaces())
		{
			if (intf.equals(Descriptable.class))
			{
				found = true;
			}
			if (intf.equals(ParentDependable.class))
			{
				isParentDependable = true;
			}
		}
		if (!found)
		{
			throw new IllegalArgumentException("Class " + clazz_.getName() + " should implement " + Descriptable.class.getName() + " interface!");
		}
		if (isParentDependable && ((parentId == null) || parentId.isEmpty()))
		{
			throw new IllegalArgumentException("Class " + clazz_.getName() + " is parrent dependable and you should provide parent id!");
		}
		final Constructor constr = clazz_.getConstructor(new Class[] {});
		final Object created = constr.newInstance(new Object[] {});
		Method set = clazz_.getMethod("setDescription", String.class);
		set.invoke(created, description);
		set = clazz_.getMethod("setSortOrder", Integer.class);
		set.invoke(created, new Integer(0));
		if (isParentDependable)
		{
			((ParentDependable) created).setParentId(parentId.get(0));
		}

		return created;
	}

	@SuppressWarnings("unchecked")
	private List<Object> createRelationClasses(final Class parentDependableClazz_, final List<Integer> parentIds, final Object child) throws Exception
	{
		boolean found = false;
		for (final Class intf : parentDependableClazz_.getInterfaces())
		{
			if (intf.equals(ParentDependable.class))
			{
				found = true;
			}
		}
		if (!found)
		{
			throw new IllegalArgumentException("Class " + parentDependableClazz_.getName() + " should implement " + ParentDependable.class.getName() + " interface!");
		}
		final List relationObject = new ArrayList();

		Method childSetter = null;
		final Constructor constr = parentDependableClazz_.getConstructor(new Class[] {});
		for (final Method method : parentDependableClazz_.getMethods())
		{
			if (method.getName().startsWith("set"))
			{
				for (final Class param : method.getParameterTypes())
				{
					if (param.equals(child.getClass()))
					{
						childSetter = method;
						break;
					}
				}
			}
			if (childSetter != null)
			{
				break;
			}
		}
		if (childSetter == null)
		{
			throw new IllegalArgumentException("No method found to set child for class " + parentDependableClazz_.getName() + " with parameter type " + child.getClass().getName());
		}
		for (final Integer parentId : parentIds)
		{
			final ParentDependable pd = (ParentDependable) constr.newInstance(new Object[] {});
			childSetter.invoke(pd, child);
			pd.setParentId(parentId);
			relationObject.add(pd);
		}

		return relationObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addStaticData(final Class clazz_, final String description, final List<Integer> parentId) throws Exception
	{
		Class descriptableClass = clazz_;
		ParentDependableType descr = null;
		if (clazz_.isAnnotationPresent(ParentDependableType.class))
		{
			descr = (ParentDependableType) clazz_.getAnnotation(ParentDependableType.class);
			descriptableClass = Class.forName(descr.value());
		}
		final Object created = createClass(descriptableClass, description, parentId);
		final Integer id = dao.addAndReturnId(created);
		final Method setId = descriptableClass.getMethod("setId", new Class[] { Integer.class });
		if (setId == null)
		{
			throw new IllegalArgumentException("Method setId not found for class " + descriptableClass.getName());
		}
		setId.invoke(created, id);
		if (descr != null)
		{
			dao.addOrUpdate(createRelationClasses(clazz_, parentId, created).toArray());
		}

		loadNonTranslatableData(nonTranslatableData);
	}

	@SuppressWarnings("unchecked")
	public void updateStaticData(final Class clazz_, final Integer id, final String description) throws Exception
	{
		final Object toUpdate = dao.getById(clazz_, id);
		final Method set = clazz_.getMethod("setDescription", String.class);
		set.invoke(toUpdate, description);
		dao.addOrUpdate(toUpdate);
		loadNonTranslatableData(nonTranslatableData);
	}

	/**
	 * 
	 * @param <K>
	 */
	private void loadStaticData()
	{
		logger.info("Started loading static data");

		loadLocalizedData(localizedData);
		loadNonTranslatableData(nonTranslatableData);
		logger.info("Finished loading static data");
	}

	@SuppressWarnings("unchecked")
	private Vector<CodeValueEntity> sortLoadedData(final Vector<CodeValueEntity> unsortedList_)
	{
		Collections.sort(unsortedList_, comparator);
		return unsortedList_;
	}

	@SuppressWarnings("unchecked")
	private void loadLocalizedData(final List<Class> clazzes_)
	{
		if (_localizedCodeMap == null)
		{
			_localizedCodeMap = new ConcurrentHashMap<String, ConcurrentMap<String, Vector<CodeValueEntity>>>();
		}
		if (_localizedParentDependable == null)
		{
			_localizedParentDependable = new ConcurrentHashMap<String, ConcurrentMap<String, ConcurrentMap<String, Vector<CodeValueEntity>>>>();
		}
		for (final Class clazz : clazzes_)
		{
			final Vector<LocalizedEntity> list = new Vector<LocalizedEntity>();
			list.addAll(dao.getAll(clazz));
			// for each new Locale implemented in DB new map should be created
			// and loaded
			// database should be called only once per entity
			if (list.size() > 0)
			{
				final LocalizedEntity entity = list.get(0);
				final Map locales = entity.getLocales();
				final Set<String> keys = locales.keySet();
				for (final String key : keys)
				{
					final LocaleDescriptable localeData = (LocaleDescriptable) locales.get(key);
					if (localeData instanceof ParentDependable)
					{
						ConcurrentMap<String, ConcurrentMap<String, Vector<CodeValueEntity>>> localeMap = _localizedParentDependable.get(key);
						if (localeMap == null)
						{
							localeMap = new ConcurrentHashMap<String, ConcurrentMap<String, Vector<CodeValueEntity>>>();
							_localizedParentDependable.put(key, localeMap);
						}
						final ConcurrentMap<String, Vector<CodeValueEntity>> filtered = DomainUtil.convertToParentDependableCodeValues(list, key);
						for (final Vector<CodeValueEntity> filteredValues : filtered.values())
						{
							sortLoadedData(filteredValues);
						}
						localeMap.put(clazz.getSimpleName(), filtered);
					}
					ConcurrentMap<String, Vector<CodeValueEntity>> localeMap = _localizedCodeMap.get(key);
					if (localeMap == null)
					{
						localeMap = new ConcurrentHashMap<String, Vector<CodeValueEntity>>();
						_localizedCodeMap.put(key, localeMap);
					}
					localeMap.put(clazz.getSimpleName(), sortLoadedData(DomainUtil.convertToCodeValues(list, key)));
				}
			}
			_nativeObjects.put(clazz.getSimpleName(), list);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> void loadNonTranslatableData(final List<Class> clazzes_)
	{
		if (_codeMap == null)
		{
			_codeMap = new ConcurrentHashMap();
		}
		if (_parentDependable == null)
		{
			_parentDependable = new ConcurrentHashMap();
		}
		for (final Class clazz : clazzes_)
		{
			final Vector<T> list = new Vector<T>();
			list.addAll(dao.getAll(clazz));
			if (list.size() > 0)
			{
				final Object entity = list.get(0);
				if (entity instanceof ParentDependable)
				{
					final List converted = list;
					final ConcurrentMap<String, Vector<CodeValueEntity>> filtered = DomainUtil.filterDataByParent(converted);
					for (Vector<CodeValueEntity> filteredValues : filtered.values())
					{
						filteredValues = sortLoadedData(filteredValues);
					}
					_parentDependable.put(clazz.getSimpleName(), filtered);

				}
				_codeMap.put(clazz.getSimpleName(), sortLoadedData(DomainUtil.convertToCodeValues(list)));
			}
			_nativeObjects.put(clazz.getSimpleName(), list);
		}
	}

	@Override
	public String getCodeDescription(final String className_, final Integer id_)
	{
		return getCodeDescription(className_, id_, Locale.DEFAULT_LOCALE);
	}

	@Override
	public String getCodeDescription(final String className, final Integer id_, final String localeCode_)
	{
		final List<CodeValueEntity> list;
		if (_codeMap.containsKey(className))
		{
			list = _codeMap.get(className);
		}
		else
		{
			list = _localizedCodeMap.get(localeCode_).get(className);
		}
		if (list != null)
		{
			for (final CodeValueEntity entity : list)
			{
				if (id_.toString().equals(entity.getId()))
				{
					return entity.getDescription();
				}
			}
		}
		String description = getParentDependableDescription(className, id_);
		if (description == null)
		{
			description = getLocalizedParentDependableDescription(className, id_, localeCode_);
		}
		return description;
	}

	@Override
	public List<CodeValueEntity> getCodeDescriptions(final String className)
	{
		return getCodeDescriptions(className, Locale.DEFAULT_LOCALE);
	}

	@Override
	public List<CodeValueEntity> getCodeDescriptions(final String className, final String localeCode_)
	{
		List<CodeValueEntity> list;
		if (_codeMap.containsKey(className))
		{
			list = _codeMap.get(className);
		}
		else
		{
			list = _localizedCodeMap.get(localeCode_).get(className);
		}
		if (list != null)
		{
			return list;
		}
		list = new ArrayList<CodeValueEntity>();
		if (_parentDependable.get(className) != null)
		{

			for (final List<CodeValueEntity> lst : _parentDependable.get(className).values())
			{
				list.addAll(lst);
			}
			return list;
		}

		if ((_localizedParentDependable == null) || (_localizedParentDependable.get(localeCode_) == null) || (_localizedParentDependable.get(localeCode_).get(className) == null))
		{
			return list;
		}
		for (final List<CodeValueEntity> lst : _localizedParentDependable.get(localeCode_).get(className).values())
		{
			list.addAll(lst);
		}
		return list;
	}

	private String getParentDependableDescription(final String className, final Integer id_)
	{
		if (_parentDependable.get(className) == null)
		{
			return null;
		}
		for (final List<CodeValueEntity> list : _parentDependable.get(className).values())
		{
			for (final CodeValueEntity entity : list)
			{
				if (id_.toString().equals(entity.getId()))
				{
					return entity.getDescription();
				}
			}
		}
		return null;
	}

	private String getLocalizedParentDependableDescription(final String className, final Integer id_, final String localeCode_)
	{
		if ((_localizedParentDependable == null) || (_localizedParentDependable.get(localeCode_) == null) || (_localizedParentDependable.get(localeCode_).get(className) == null))
		{
			return null;
		}
		for (final List<CodeValueEntity> list : _localizedParentDependable.get(localeCode_).get(className).values())
		{
			for (final CodeValueEntity entity : list)
			{
				if (id_.toString().equals(entity.getId()))
				{
					return entity.getDescription();
				}
			}
		}
		return null;
	}

	@Override
	public String getCodeDescription(final Class<?> type_, final Integer id_)
	{
		return getCodeDescription(type_.getSimpleName(), id_, Locale.DEFAULT_LOCALE);
	}

	@Override
	public String getCodeDescription(final Class<?> type_, final Integer id_, final String localeCode_)
	{
		return getCodeDescription(type_.getSimpleName(), id_, localeCode_);
	}

	@Override
	public ConcurrentMap<String, Vector<CodeValueEntity>> getCodeData()
	{
		return _codeMap;
	}

	@Override
	public ConcurrentMap<String, Vector<CodeValueEntity>> getLocalizedCodeData(final String localeCode_)
	{
		return _localizedCodeMap.get(localeCode_);
	}

	@Override
	public ConcurrentMap<String, ConcurrentMap<String, Vector<CodeValueEntity>>> getLocalizedParentDependableCodeData(final String localeCode_)
	{
		return _localizedParentDependable.get(localeCode_);
	}

	@Override
	public ConcurrentMap<String, ConcurrentMap<String, Vector<CodeValueEntity>>> getParentDependableCodeData()
	{
		return _parentDependable;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Vector<T> getNativeDataObjects(final Class<T> clazz_)
	{
		return (Vector<T>) _nativeObjects.get(clazz_.getSimpleName());
	}

	@Override
	public <T> List<T> getEntities(final Class<T> clazz_, final List<Integer> ids_)
	{
		return dao.getByIds(clazz_, ids_);
	}

	@Override
	public <T> T saveEntity(T entity_)
	{
		entity_ = dao.merge(entity_);
		dao.flush();
		return entity_;
	}

	@Override
	public <T> void deleteEntity(final T entity_)
	{
		dao.delete(entity_);
		dao.flush();
	}

	@Override
	public <T> void evictEntity(final T entity_)
	{
		dao.evict(entity_);
	}

	@Override
	public <T> void deleteEntity(final Class<T> clazz_, final Integer id_)
	{
		dao.delete(clazz_, id_);
		dao.flush();
	}

	@Override
	public <T> void saveProfileCollectionChanges(final Class<T> clazz_, final List<Integer> ids_, final String parentField_, final Integer profileId_) throws Exception
	{
		final Search s = new Search(clazz_);
		s.addFilterEqual(parentField_, profileId_);
		final List<?> oldList = dao.search(clazz_, s);

		final Object[] arr = new Object[oldList.size()];
		// delete old entries
		dao.delete(oldList.toArray(arr));
		dao.flush();
		// insert new entries
		if (ids_ != null)
		{
			for (final Integer id : ids_)
			{
				final Object obj = Class.forName(clazz_.getName()).newInstance();
				if (obj instanceof ProfileDependable)
				{
					final ProfileDependable entry = (ProfileDependable) obj;
					entry.setProfileId(profileId_);
					entry.setId(id);
					dao.addOrUpdate(entry);
					// dao.merge(entry);
				}
			}
		}
		dao.flush();
	}

	@Override
	public <T> void saveProfileOtherCollectionChanges(final Class<T> clazz_, final List<?> newOtherValues_, final String parentField_, final Integer parentId_) throws Exception
	{

		final Search s = new Search(clazz_);
		s.addFilterEqual(parentField_, parentId_);
		final List<?> oldList = dao.search(clazz_, s);

		final Object[] arr = new Object[oldList.size()];
		// delete old entries
		dao.delete(oldList.toArray(arr));
		dao.flush();
		// insert new entries
		if (newOtherValues_ != null)
		{
			for (final Object obj : newOtherValues_)
			{
				// make sure profile id were populated
				if (obj instanceof ProfileDependable)
				{
					if ((((ProfileDependable) obj).getProfileId() == null) || (((ProfileDependable) obj).getProfileId() == 0))
					{
						((ProfileDependable) obj).setProfileId(parentId_);
					}
				}
				dao.merge(obj);
			}
		}
		dao.flush();
	}

	public <T> T getEntity(final Class<T> clazz_, final Integer id_)
	{
		return dao.getById(clazz_, id_);
	}
}
