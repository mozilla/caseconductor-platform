/**
 *
 * Licensed under the GNU General Public License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/gpl.txt
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
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.type.Type;

import com.trg.search.ISearch;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;

/**
 * 
 * Defines set of data access methods not typed to a specific Entity.
 * 
 */
public interface TypelessDAO
{
	/*
	 * Suplemment in complex queries where no real ids could be found
	 */
	public static final Integer	PLACE_HOLDER_ID	= -666;

	<T> List<T> getAll(Class<T> type_);

	<T> T getById(Class<T> type_, Serializable id_);

	<T> boolean exists(Class<T> type_, Serializable id_);

	<T> void update(Class<T> type_, Object[] transientEntities_);

	<T> T merge(T entity_);

	<T> Integer addAndReturnId(T entity_);

	<T> void addOrUpdate(T entity_);

	<T> void addOrUpdate(T[] entities_);

	<T> boolean delete(Class<T> type_, Serializable id_);

	<T> void delete(Class<T> type_, Serializable[] ids_);

	void delete(Object[] entities_);

	void delete(List<?> entities_);

	void delete(Object entity_);

	<T> Object searchUnique(Class<T> clazz_, ISearch search_);

	void flush();

	Object loadLazyObject(Object entity_);

	@SuppressWarnings("unchecked")
	Collection loadLazyCollection(Collection entities_);

	Object resolveDeepProxies(Object entity_);

	<T> List<T> search(Class<T> type_, ISearch search_);

	List<?> findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values, Type[] types, boolean uniqueResult, boolean cacheQuery, String cacheRegion);

	List<?> findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values, boolean uniqueResult, boolean cacheQuery);

	List<?> findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values, boolean uniqueResult, boolean cacheQuery, String cacheRegion);

	<T> List<T> getByIds(Class<T> clazz_, List<Integer> ids_);

	<T> void evict(T entity_);

	int updateByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values);

	UtestSearchResult getBySearch(Class<?> type_, UtestSearch search_);

	Session getSession();

	void update(Object transientEntities);

	UtestSearchResult getByLocalizedSearch(Class<?> type, Class<?> localLype, UtestSearch search);

	<T> List<T> searchDeleted(Class<T> type, ISearch search);

	UtestSearchResult getDeletedBySearch(Class<?> type, UtestSearch search);

	<T> boolean undoDeletedEntity(Class<T> type, Serializable id);

	<T> T getDeletedById(Class<T> type, Serializable id);

}
