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
 * @author Miguel Bautista
 *
 * copyright 2010 by uTest 
 */
package com.utest.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.trg.dao.hibernate.HibernateBaseDAO;
import com.trg.search.ExampleOptions;
import com.trg.search.Filter;
import com.trg.search.ISearch;
import com.trg.search.Search;
import com.trg.search.SearchResult;

/**
 * An implementation of <code>GenericDAO</code> which extends
 * <code>HibernateBaseDAO</code>. This class will provide common persistence
 * methods. <b>NOTE:</b> In order for this generic class to work, you MUST call
 * <code>setPersistentClass</code>, this will set the class that it will use to
 * persistent with.
 * 
 * @param <T>
 *            Domain class to use
 */
public class GenericHibernateDAOImpl<T> extends HibernateBaseDAO implements GenericDAO<T>
{

	protected Class<T>	persistentClass	= null;

	@Autowired
	@Override
	public void setSessionFactory(final SessionFactory sessionFactory)
	{
		super.setSessionFactory(sessionFactory);
	}

	public void setPersistentClass(final Class<T> persistentClass)
	{
		this.persistentClass = persistentClass;
	}

	public int count(ISearch search)
	{
		if (search == null)
		{
			search = new Search();
		}
		return _count(persistentClass, search);
	}

	public T find(final Integer id)
	{
		return _get(persistentClass, id);
	}

	public T[] find(final Integer... ids)
	{
		return _get(persistentClass, (Serializable[]) ids);
	}

	public List<T> findAll()
	{
		return _all(persistentClass);
	}

	public void flush()
	{
		_flush();
	}

	public T getReference(final Integer id)
	{
		return _load(persistentClass, id);
	}

	public T[] getReferences(final Integer... ids)
	{
		return _load(persistentClass, (Serializable[]) ids);
	}

	public boolean isAttached(final T entity)
	{
		return _sessionContains(entity);
	}

	public void refresh(final T... entities)
	{
		_refresh(entities);
	}

	public boolean remove(final T entity)
	{
		return _deleteEntity(entity);
	}

	public void remove(final T... entities)
	{
		_deleteEntities(entities);
	}

	public boolean removeById(final Integer id)
	{
		return _deleteById(persistentClass, id);
	}

	public void removeByIds(final Integer... ids)
	{
		_deleteById(persistentClass, (Serializable[]) ids);
	}

	public boolean save(final T entity)
	{
		return _saveOrUpdateIsNew(entity);
	}

	public boolean[] save(final T... entities)
	{
		return _saveOrUpdateIsNew(entities);
	}

	public Serializable saveAndReturnId(final T entity)
	{
		return _save(entity);
	}

	public T merge(final T entity)
	{
		return _merge(entity);
	}

	@SuppressWarnings("unchecked")
	public List<T> search(final ISearch search)
	{
		if (search == null)
		{
			return findAll();
		}
		return _search(persistentClass, search);
	}

	@SuppressWarnings("unchecked")
	public SearchResult<T> searchAndCount(final ISearch search)
	{
		if (search == null)
		{
			final SearchResult<T> result = new SearchResult<T>();
			result.setResult(findAll());
			result.setTotalCount(result.getResult().size());
			return result;
		}
		return _searchAndCount(persistentClass, search);
	}

	@SuppressWarnings("unchecked")
	public List searchGeneric(final ISearch search)
	{
		if (search == null)
		{
			return findAll();
		}
		return _search(persistentClass, search);
	}

	@SuppressWarnings("unchecked")
	public T searchUnique(final ISearch search)
	{
		return (T) _searchUnique(persistentClass, search);
	}

	public Object searchUniqueGeneric(final ISearch search)
	{
		return _searchUnique(persistentClass, search);
	}

	public Filter getFilterFromExample(final T example)
	{
		return _getFilterFromExample(example);
	}

	public Filter getFilterFromExample(final T example, final ExampleOptions options)
	{
		return _getFilterFromExample(example, options);
	}
}
