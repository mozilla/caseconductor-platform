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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.HibernateProxyHelper;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;

import com.trg.dao.hibernate.HibernateBaseDAO;
import com.trg.search.Filter;
import com.trg.search.ISearch;
import com.trg.search.Search;
import com.trg.search.SearchResult;
import com.trg.search.Sort;
import com.utest.domain.Entity;
import com.utest.domain.LocaleDescriptable;
import com.utest.domain.LocalizedEntity;
import com.utest.domain.search.UtestFilter;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.search.UtestSort;
import com.utest.domain.util.DomainUtil;

/**
 * An implementation of <code>GenericDAO</code> which extends
 * <code>HibernateBaseDAO</code>. This class will provide common persistence
 * methods for any Class. Class must be passed to each method.
 */
public class TypelessHibernateDAOImpl extends HibernateBaseDAO implements TypelessDAO
{
	private Boolean	permanentDeletionEnabled;

	public TypelessHibernateDAOImpl()
	{
		super();
	}

	@Autowired
	@Override
	public void setSessionFactory(final SessionFactory sessionFactory)
	{
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * Get the current Hibernate session
	 */
	@Override
	public Session getSession()
	{
		return super.getSession();
	}

	@Override
	public Object loadLazyObject(final Object entity_)
	{

		if (entity_ instanceof HibernateProxy)
		{
			Hibernate.initialize(entity_);
			final HibernateProxy proxy = (HibernateProxy) entity_;
			return proxy.getHibernateLazyInitializer().getImplementation();
		}
		else
		{
			return entity_;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection loadLazyCollection(final Collection entities_)
	{
		Hibernate.initialize(entities_);
		final Collection resolved = new ArrayList();
		for (final Object entity : entities_)
		{
			resolved.add(loadLazyObject(entity));
		}
		return resolved;
	}

	/**
	 * Resolve entities if not defined in skipEntities_ collection
	 * 
	 * @param entity_
	 * @param arrayList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object resolveDeepProxies(final Object entity_, final ArrayList<Class<?>> arrayList, final Collection<String> resolvedEntities_)
	{
		// if not found object
		if (entity_ == null)
		{
			return entity_;
		}

		Class<? extends Object> clazz;
		if (entity_ instanceof HibernateProxy)
		{
			clazz = HibernateProxyHelper.getClassWithoutInitializingProxy(entity_);
		}
		else
		{
			clazz = entity_.getClass();
		}

		loadLazyObject(entity_);

		final ClassMetadata metadata = this.getSessionFactory().getClassMetadata(clazz);
		if (metadata != null)
		{
			final String[] propertyNames = metadata.getPropertyNames();
			final Type[] propertyTypes = metadata.getPropertyTypes();
			String propName = null;
			Type propType = null;
			// recursively resolve all child associations
			for (int i = 0; i < propertyNames.length; i++)
			{
				propName = propertyNames[i];
				propType = propertyTypes[i];

				// many-to-one and one-to-one associations
				if (propType.isEntityType())
				{
					Object assoc = metadata.getPropertyValue(entity_, propName, EntityMode.POJO);
					Hibernate.initialize(assoc);
					if (assoc != null)
					{
						assoc = resolveDeepProxies(assoc, arrayList, resolvedEntities_);
						metadata.setPropertyValue(entity_, propName, assoc, EntityMode.POJO);
					}
				}
				// one-to-many associations
				else if (propType.isCollectionType())
				{
					// PersistentMap (Collection<Object>)
					final Object children = metadata.getPropertyValue(entity_, propName, EntityMode.POJO);
					if (children instanceof Collection)
					{
						Hibernate.initialize(children);
						if (children != null)
						{
							final Collection<Object> resolvedChildren = new ArrayList();
							for (final Object child : ((Collection) children))
							{
								final Object resolvedChild = resolveDeepProxies(child, arrayList, resolvedEntities_);
								resolvedChildren.add(resolvedChild);
							}
							metadata.setPropertyValue(entity_, propName, resolvedChildren, EntityMode.POJO);
						}
					}
				}
			}
		}

		// resolve Parent object
		return loadLazyObject(entity_);
	}

	/**
	 * Resolve Entity with nothing to skip
	 * 
	 * @param entity_
	 * @return
	 */
	public Object resolveDeepProxies(final Object entity_, final ArrayList<Class<?>> arrayList)
	{
		return resolveDeepProxies(entity_, arrayList, new ArrayList<String>());
	}

	/**
	 * Resolve Entity with nothing to skip
	 * 
	 * @param entity_
	 * @return
	 */
	@Override
	public Object resolveDeepProxies(final Object entity_)
	{
		return resolveDeepProxies(entity_, new ArrayList<Class<?>>());
	}

	/**
	 * @return the Hibernate Metadata for this class
	 */
	protected ClassMetadata getClassMetadata(final Object entity_)
	{
		return getSessionFactory().getClassMetadata(entity_.getClass().getSimpleName());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getById(final Class<T> type_, final Serializable id_)
	{
		if (id_ == null)
		{
			throw new IllegalArgumentException(type_.getSimpleName() + " ID is null.");
		}
		final Search s = new Search(type_);
		s.addFilterEqual("id", id_);
		applyDeletedFilter(s, type_);
		return (T) super._searchUnique(type_, s);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getDeletedById(final Class<T> type_, final Serializable id_)
	{
		if (id_ == null)
		{
			throw new IllegalArgumentException(type_.getSimpleName() + " ID is null.");
		}
		final Search s = new Search(type_);
		s.addFilterEqual("id", id_);
		s.addFilterEqual("deleted", true);
		return (T) super._searchUnique(type_, s);
	}

	@Override
	public <T> List<T> getByIds(final Class<T> type_, final List<Integer> ids_)
	{

		final Search s = new Search(type_);
		s.addFilterIn("id", ids_);
		applyDeletedFilter(s, type_);
		final List<T> list = search(type_, s);
		return list;
	}

	@Override
	public <T> List<T> getAll(final Class<T> type_)
	{
		final Search s = new Search(type_);
		applyDeletedFilter(s, type_);
		final List<T> list = search(type_, s);
		return list;
	}

	private <T> void applyDeletedFilter(final Search search, final Class<T> type)
	{
		if (!permanentDeletionEnabled)
		{
			search.addFilterNotEqual("deleted", true);
		}
	}

	/**
	 * Retrieves a list of Localized Entities based on dynamic search, page and
	 * sort.
	 * 
	 * @param type_
	 * @param search_
	 * @return
	 */
	@Override
	public UtestSearchResult getByLocalizedSearch(final Class<?> type_, final Class<?> localType_, final UtestSearch search_)
	{
		UtestSearch s = applyLocalizedSearch(localType_, search_);
		return this.getBySearch(type_, s);
	}

	@SuppressWarnings("unchecked")
	private UtestSearch applyLocalizedSearch(Class<?> localType_, UtestSearch search_)
	{
		UtestSearch localeSearch = new UtestSearch();
		UtestSearch mainSearch = new UtestSearch();
		for (UtestFilter filter : search_.getFilters())
		{
			if (LocaleDescriptable.NAME.equals(filter.getProperty()) || LocaleDescriptable.LOCALE_CODE.equals(filter.getProperty())
					|| LocaleDescriptable.SORT_ORDER.equals(filter.getProperty()))
			{
				localeSearch.addFilter(filter);
			}
			else
			{
				mainSearch.addFilter(filter);
			}
		}
		if (!localeSearch.getFilters().isEmpty())
		{
			UtestSearchResult searchResult = this.getBySearch(localType_, localeSearch);
			List<Integer> ids = new ArrayList<Integer>();
			if (searchResult.getTotalRecords() > 0)
			{
				List<? extends LocalizedEntity> locales = (List<? extends LocalizedEntity>) searchResult.getResults();
				ids = DomainUtil.extractLocalDescriptableIds(locales);
			}
			else
			{
				ids.add(PLACE_HOLDER_ID);
			}
			mainSearch.addFilterIn("id", ids);
		}
		return mainSearch;

	}

	/**
	 * Retrieves a list of Entities based on dynamic search, page and sort.
	 * 
	 * @param type_
	 * @param search_
	 * @return
	 */
	@Override
	public UtestSearchResult getDeletedBySearch(final Class<?> type_, final UtestSearch search_)
	{
		final Search s = convertSearch(type_, search_);
		(s).addFilterEqual("deleted", true);
		final SearchResult<?> result = super._searchAndCount(type_, s);
		return convertResult(result);
	}

	/**
	 * Retrieves a list of Entities based on dynamic search, page and sort.
	 * 
	 * @param type_
	 * @param search_
	 * @return
	 */
	@Override
	public UtestSearchResult getBySearch(final Class<?> type_, final UtestSearch search_)
	{
		final Search s = convertSearch(type_, search_);
		applyDeletedFilter(s, type_);
		final SearchResult<?> result = super._searchAndCount(type_, s);
		return convertResult(result);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> search(final Class<T> type_, final ISearch search_)
	{
		if (search_ != null)
		{
			checkForNullFilters(search_.getFilters());
		}
		applyDeletedFilter((Search) search_, type_);
		return super._search(type_, search_);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> searchDeleted(final Class<T> type_, final ISearch search_)
	{
		if (search_ != null)
		{
			checkForNullFilters(search_.getFilters());
		}
		((Search) search_).addFilterEqual("deleted", true);
		return super._search(type_, search_);
	}

	@Override
	public <T> Object searchUnique(final Class<T> type_, final ISearch search_)
	{
		if (search_ != null)
		{
			checkForNullFilters(search_.getFilters());
		}
		applyDeletedFilter((Search) search_, type_);
		return super._searchUnique(type_, search_);
	}

	@Override
	public <T> boolean exists(final Class<T> type_, final Serializable id_)
	{
		return super._exists(type_, id_);
	}

	@Override
	public <T> void update(final Class<T> type_, final Object[] transientEntities_)
	{
		super._update(transientEntities_);
	}

	@Override
	public void update(final Object transientEntity_)
	{
		super._update(transientEntity_);
		this.flush();
	}

	@Override
	public <T> T merge(final T entity_)
	{
		T updatedEntity = super._merge(entity_);
		this.flush();
		return updatedEntity;
	}

	@Override
	public <T> void evict(final T entity_)
	{
		super.getSession().evict(entity_);
	}

	@Override
	public <T> Integer addAndReturnId(final T entity_)
	{
		return (Integer) super._save(entity_);
	}

	@Override
	public <T> void addOrUpdate(final T entity_)
	{
		super._saveOrUpdate(entity_);
	}

	@Override
	public <T> void addOrUpdate(final T[] entities_)
	{
		super._saveOrUpdateIsNew(entities_);
	}

	@Override
	public <T> boolean delete(final Class<T> type_, final Serializable id_)
	{
		if (!permanentDeletionEnabled)
		{
			T t = getById(type_, id_);
			if (t instanceof Entity)
			{
				((Entity) t).setDeleted(true);
				update(t);
				return true;
			}
			else
			{
				return super._deleteById(type_, id_);
			}
		}
		else
		{
			return super._deleteById(type_, id_);
		}
	}

	@Override
	public <T> boolean undoDeletedEntity(final Class<T> type_, final Serializable id_)
	{
		if (!permanentDeletionEnabled)
		{
			T t = getDeletedById(type_, id_);
			if (t instanceof Entity)
			{
				((Entity) t).setDeleted(false);
				update(t);
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	@Override
	public <T> void delete(final Class<T> type_, final Serializable[] ids_)
	{
		for (Serializable id : Arrays.asList(ids_))
		{
			delete(type_, id);
		}
	}

	@Override
	public void delete(final Object[] entities_)
	{
		delete(Arrays.asList(entities_));
	}

	@Override
	public void delete(final List<?> entities_)
	{
		for (Object entity : entities_)
		{
			delete(entity);
		}
	}

	@Override
	public void delete(final Object entity_)
	{
		if (!permanentDeletionEnabled)
		{
			if (entity_ instanceof Entity)
			{
				((Entity) entity_).setDeleted(true);
				update(entity_);
			}
			else
			{
				super._deleteEntities(entity_);
			}
		}
		else
		{
			super._deleteEntities(entity_);
		}

	}

	@Override
	public void flush()
	{
		super._flush();
	}

	// ////////////////////// Named Query related code
	/**
	 * Execute a named query, binding one value to a ":" named parameter in the
	 * query string. A named query is defined in a Hibernate mapping file.
	 * 
	 * @param queryName
	 *            the name of a Hibernate query in a mapping file.
	 * @param paramNames
	 *            the names of parameters.
	 * @param values
	 *            the values of the parameters.
	 * @param uniqueResult
	 *            boolean to indicate to re
	 * @param cacheQuery
	 *            boolean to cache the query or not.
	 * @return a List containing the results of the query execution
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List findByNamedQueryAndNamedParam(final String queryName, final String[] paramNames, final Object[] values, final boolean uniqueResult, final boolean cacheQuery)
	{

		return findByNamedQueryAndNamedParam(queryName, paramNames, values, null, uniqueResult, cacheQuery, null);

	}

	/**
	 * Execute a named query, binding one value to a ":" named parameter in the
	 * query string. A named query is defined in a Hibernate mapping file.
	 * 
	 * @param queryName
	 *            the name of a Hibernate query in a mapping file.
	 * @param paramNames
	 *            the names of parameters.
	 * @param values
	 *            the values of the parameters.
	 * @param uniqueResult
	 *            boolean to indicate to re
	 * @param cacheQuery
	 *            boolean to cache the query or not.
	 * @param cacheRegion
	 *            the cache region name
	 * @return a List containing the results of the query execution
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List findByNamedQueryAndNamedParam(final String queryName, final String[] paramNames, final Object[] values, final boolean uniqueResult, final boolean cacheQuery,
			final String cacheRegion)
	{

		return findByNamedQueryAndNamedParam(queryName, paramNames, values, null, uniqueResult, cacheQuery, cacheRegion);

	}

	/**
	 * Execute a named query, binding one value to a ":" named parameter in the
	 * query string. A named query is defined in a Hibernate mapping file.
	 * 
	 * @param queryName
	 *            the name of a Hibernate query in a mapping file.
	 * @param paramNames
	 *            the names of parameters.
	 * @param values
	 *            the values of the parameters.
	 * @param types
	 *            the types of the parameters.
	 * 
	 * @param uniqueResult
	 *            boolean to indicate the unique result. Returns a single
	 *            instance that matches the query, or null if the query returns
	 *            no results. Throws NonUniqueResultException if there is more
	 *            than one matching result.
	 * @param cacheQuery
	 *            boolean to cache the query or not.
	 * @param cacheRegion
	 *            the cache region name
	 * @return a List containing the results of the query execution
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List findByNamedQueryAndNamedParam(final String queryName, final String[] paramNames, final Object[] values, final Type[] types, final boolean uniqueResult,
			final boolean cacheQuery, final String cacheRegion)
	{
		Session session = null;
		List queryResult = new ArrayList();

		if ((paramNames != null) && (values != null) && (paramNames.length != values.length))
		{
			throw new RuntimeException("BaseDAO.findByNamedQueryAndNamedParam method - " + "Length of paramNames array must match length of values array");
		}

		if ((types != null) && (values != null) && (types.length != values.length))
		{
			throw new RuntimeException("BaseDAO.findByNamedQueryAndNamedParam method - " + "Length of types array must match length of values array");
		}

		try
		{
			session = getSession();

			final Query queryObject = session.getNamedQuery(queryName);

			// VMKTEST dynamic pagination
			// use dummy Search to calculate starting row
			// final Search s = new Search();
			// s.setMaxResults(10);
			// s.setPage(3);

			// int firstResult = SearchUtil.calcFirstResult(s);
			// if (firstResult > 0) {
			// query.setFirstResult(firstResult);
			// }
			// final int firstResult = SearchUtil.calcFirstResult(s);
			// if (firstResult > 0)
			// {
			// queryObject.setFirstResult(firstResult);
			// queryObject.setMaxResults(maxResults);
			// }

			// dynamic sort - need to add wrapper

			if (queryObject == null)
			{
				throw new RuntimeException("BaseDAO.findByNamedQueryAndNamedParam method - " + "Named query is not found in the Hibernate session");
			}

			// Set parameter names and values
			if (values != null)
			{
				for (int i = 0; i < values.length; i++)
				{
					if (types == null)
					{
						if (paramNames == null)
						{
							queryObject.setParameter(i, values[i]);
						}
						else
						{
							applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
						}
					}
					else
					{
						if (paramNames == null)
						{
							queryObject.setParameter(i, values[i], types[i]);
						}
						else
						{
							applyNamedParameterToQuery(queryObject, paramNames[i], values[i], types[i]);
						}
					}
				}
			}

			if (uniqueResult)
			{
				final Object obj = queryObject.uniqueResult();
				if (obj != null)
				{
					queryResult.add(obj);
				}
			}
			else
			{
				queryResult = queryObject.list();
			}
		}
		catch (final GenericJDBCException exception)
		{
			final StringBuffer msg = new StringBuffer();
			msg.append(" findByNamedQueryAndNamedParam:");
			msg.append(queryName);
			msg.append(" Parameter Name(s): ");
			if (paramNames != null)
			{
				for (int i = 0; i < paramNames.length; i++)
				{
					msg.append(i).append(" : ").append(paramNames[i]).append(" ");
				}
			}
			msg.append(" Parameter Value(s): ");
			if (values != null)
			{
				for (int i = 0; i < values.length; i++)
				{
					msg.append(i).append(" : ").append(values[i]).append(" ");
				}
			}

			throw new RuntimeException("GenericJDBCException: " + msg.toString());
		}
		return queryResult;

	}

	/**
	 * Execute an Update or Delete named query, binding one value to a ":" named
	 * parameter in the query string. A named query is defined in a Hibernate
	 * mapping file.
	 * 
	 * @param queryName
	 *            the name of a Hibernate query in a mapping file.
	 * @param paramNames
	 *            the names of parameters.
	 * @param values
	 *            the values of the parameters.
	 * @return a number of records updated
	 */
	@Override
	public int updateByNamedQueryAndNamedParam(final String queryName, final String[] paramNames, final Object[] values)
	{
		Session session = null;
		int queryResult;

		if ((paramNames != null) && (values != null) && (paramNames.length != values.length))
		{
			throw new RuntimeException("BaseDAO.updateByNamedQueryAndNamedParam method - " + "Length of paramNames array must match length of values array");
		}
		try
		{
			session = getSession();

			final Query queryObject = session.getNamedQuery(queryName);

			if (queryObject == null)
			{
				throw new RuntimeException("BaseDAO.updateByNamedQueryAndNamedParam method - " + "Named query is not found in the Hibernate session -> " + queryName);
			}

			// Set parameter names and values
			if (values != null)
			{
				for (int i = 0; i < values.length; i++)
				{
					if (paramNames == null)
					{
						queryObject.setParameter(i, values[i]);
					}
					else
					{
						applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
					}
				}
			}
			queryResult = queryObject.executeUpdate();
		}
		catch (final GenericJDBCException exception)
		{
			final StringBuffer msg = new StringBuffer();
			msg.append(" updateByNamedQueryAndNamedParam:");
			msg.append(queryName);
			msg.append(" Parameter Name(s): ");
			if (paramNames != null)
			{
				for (int i = 0; i < paramNames.length; i++)
				{
					msg.append(i).append(" : ").append(paramNames[i]).append(" ");
				}
			}
			msg.append(" Parameter Value(s): ");
			if (values != null)
			{
				for (int i = 0; i < values.length; i++)
				{
					msg.append(i).append(" : ").append(values[i]).append(" ");
				}
			}

			throw new RuntimeException("GenericJDBCException: " + msg.toString());
		}
		return queryResult;

	}

	/**
	 * Apply the given name parameter to the given Query object. This method is
	 * copied from HibernateTemplate
	 * 
	 * @param queryObject
	 *            the Query object
	 * @param paramName
	 *            the name of the parameter
	 * @param value
	 *            the value of the parameter
	 */
	@SuppressWarnings("unchecked")
	private void applyNamedParameterToQuery(final Query queryObject, final String paramName, final Object value)
	{
		if (value instanceof Collection)
		{
			queryObject.setParameterList(paramName, (Collection) value);
		}
		else if (value instanceof Object[])
		{
			queryObject.setParameterList(paramName, (Object[]) value);
		}
		else
		{
			queryObject.setParameter(paramName, value);
		}
	}

	/**
	 * Apply the given name parameter to the given Query object. This method is
	 * copied from HibernateTemplate
	 * 
	 * @param queryObject
	 *            the Query object
	 * @param paramName
	 *            the name of the parameter
	 * @param value
	 *            the value of the parameter
	 * @param type
	 *            the type of the parameter
	 */
	@SuppressWarnings("unchecked")
	private void applyNamedParameterToQuery(final Query queryObject, final String paramName, final Object value, final Type type)
	{
		if (value instanceof Collection)
		{
			queryObject.setParameterList(paramName, (Collection) value);
		}
		else if (value instanceof Object[])
		{
			queryObject.setParameterList(paramName, (Object[]) value);
		}
		else
		{
			queryObject.setParameter(paramName, value, type);
		}
	}

	/**
	 * Convert from Utest wrapper bean to implementing class.
	 * 
	 * @param search_
	 * @return
	 */
	private Search convertSearch(final Class<?> type_, final UtestSearch search_)
	{
		final Search s = new Search(type_);
		// set filters
		final List<UtestFilter> filters = search_.getFilters();
		final List<Filter> convertedFilters = convertFilters(filters);
		s.addFilters(convertedFilters.toArray(new Filter[convertedFilters.size()]));
		// set sorts
		for (final UtestSort sort : search_.getSorts())
		{
			s.addSort(new Sort(sort.getProperty(), sort.isDesc(), sort.isIgnoreCase()));
		}
		// set pagination
		// since implementing page is zero-based, adjust to represent real page
		// number requested
		s.setFirstResult(search_.getFirstResult());
		int page = search_.getPage();
		s.setPage((page > 0 ? --page : 0));
		s.setMaxResults(search_.getMaxResults());
		return s;
	}

	@SuppressWarnings("unchecked")
	private List<Filter> convertFilters(final List<UtestFilter> filters)
	{
		final List<Filter> convertedFilters = new ArrayList<Filter>();
		for (final UtestFilter filter : filters)
		{
			if (UtestFilter.OP_OR == filter.getOperator())
			{
				final List<UtestFilter> nestedFilters = (List<UtestFilter>) filter.getValue();
				final List<Filter> tempFilters = convertFilters(nestedFilters);
				final Filter orFilter = Filter.or(tempFilters.toArray(new Filter[tempFilters.size()]));
				convertedFilters.add(orFilter);
			}
			else if (UtestFilter.OP_AND == filter.getOperator())
			{
				final List<UtestFilter> nestedFilters = (List<UtestFilter>) filter.getValue();
				final List<Filter> tempFilters = convertFilters(nestedFilters);
				final Filter orFilter = Filter.and(tempFilters.toArray(new Filter[tempFilters.size()]));
				convertedFilters.add(orFilter);
			}
			else
			{
				convertedFilters.add(new Filter(filter.getProperty(), filter.getValue(), filter.getOperator()));
			}
		}
		return convertedFilters;
	}

	/**
	 * Convert from Utest wrapper bean to implementing class.
	 * 
	 * @param search_
	 * @return
	 */
	private UtestSearchResult convertResult(final SearchResult<?> result_)
	{
		final UtestSearchResult usr = new UtestSearchResult();
		usr.setResults(result_.getResult());
		usr.setTotalRecords(result_.getTotalCount());
		return usr;
	}

	@SuppressWarnings("unchecked")
	private void checkForNullFilters(final List<Filter> filters_)
	{
		if (filters_ == null)
		{
			return;
		}
		// set filters
		for (final Filter filter : filters_)
		{

			if ((Filter.OP_OR == filter.getOperator()) || (Filter.OP_AND == filter.getOperator()))
			{
				final List<Filter> nestedFilters = (List<Filter>) filter.getValue();
				checkForNullFilters(nestedFilters);
			}
			else if ((Filter.OP_NULL == filter.getOperator()) || (Filter.OP_NOT_NULL == filter.getOperator()))
			{
				continue;
			}
			else
			{
				if (filter.getValue() == null)
				{
					throw new IllegalArgumentException("Cannot use NULL values in WHERE clause comparisons, except for IS NULL or IS NOT NULL. Filter passed: ["
							+ filter.getProperty() + translateOperator(filter.getOperator()) + " null ]");
				}

			}
		}
	}

	private String translateOperator(final int operator)
	{
		if (Filter.OP_EQUAL == operator)
		{
			return " = ";
		}
		else if (Filter.OP_GREATER_OR_EQUAL == operator)
		{
			return " >= ";
		}
		else if (Filter.OP_GREATER_THAN == operator)
		{
			return " > ";
		}
		else if (Filter.OP_ILIKE == operator)
		{
			return " ILIKE ";
		}
		else if (Filter.OP_IN == operator)
		{
			return " IN ";
		}
		else if (Filter.OP_NOT_EQUAL == operator)
		{
			return " != ";
		}
		else if (Filter.OP_NOT_IN == operator)
		{
			return " NOT IN ";
		}
		return String.valueOf(operator);
	}

	public Boolean getPermanentDeletionEnabled()
	{
		return permanentDeletionEnabled;
	}

	public void setPermanentDeletionEnabled(Boolean permanentDeletionEnabled)
	{
		this.permanentDeletionEnabled = permanentDeletionEnabled;
	}

}
