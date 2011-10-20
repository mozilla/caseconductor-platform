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
package com.utest.domain.service.impl;

import java.util.List;

import com.trg.search.Search;
import com.utest.dao.TypelessDAO;
import com.utest.domain.AuditRecord;
import com.utest.domain.service.AuditService;

public class AuditServiceImpl implements AuditService
{
	private final TypelessDAO	dao;

	public AuditServiceImpl(final TypelessDAO dao)
	{
		this.dao = dao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuditRecord> getAllForType(final Class type)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addSortDesc("date");
		return dao.search(AuditRecord.class, s);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuditRecord> getAllForTypeAndField(final Class type, final String field)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addFilterEqual("propertyName", field);
		s.addSortDesc("date");
		return dao.search(AuditRecord.class, s);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuditRecord> getAllForTypeAndFieldAndId(final Class type, final String field, final Integer id)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addFilterEqual("propertyName", field);
		s.addFilterEqual("entityId", id);
		s.addField("id");
		s.addSortDesc("date");
		return dao.search(AuditRecord.class, s);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuditRecord> getAllForTypeAndId(final Class type, final Integer id)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addFilterEqual("entityId", id);
		s.addField("id");
		s.addSortDesc("date");
		return dao.search(AuditRecord.class, s);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuditRecord> getAllForUserAndType(final Integer userId, final Class type)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addFilterEqual("userId", userId);
		s.addField("id");
		s.addSortDesc("date");
		return dao.search(AuditRecord.class, s);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuditRecord> getAllForUserAndTypeAndField(final Integer userId, final Class type, final String field)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addFilterEqual("propertyName", field);
		s.addFilterEqual("userId", userId);
		s.addField("id");
		s.addSortDesc("date");
		return dao.search(AuditRecord.class, s);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuditRecord> getAllForUserAndTypeAndFieldAndId(final Integer userId, final Class type, final String field, final Integer id)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addFilterEqual("propertyName", field);
		s.addFilterEqual("userId", userId);
		s.addField("id");
		s.addFilterEqual("entityId", id);
		s.addField("id");
		s.addSortDesc("date");
		return dao.search(AuditRecord.class, s);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuditRecord> getAllForUserAndTypeAndId(final Integer userId, final Class type, final Integer id)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addFilterEqual("userId", userId);
		s.addField("id");
		s.addFilterEqual("entityId", id);
		s.addField("id");
		s.addSortDesc("date");
		return dao.search(AuditRecord.class, s);
	}

	@SuppressWarnings("unchecked")
	@Override
	public AuditRecord getLastForType(final Class type)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addSortDesc("date");
		final List<AuditRecord> list = dao.search(AuditRecord.class, s);
		if (!list.isEmpty())
		{
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AuditRecord getLastForTypeAndField(final Class type, final String field)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addFilterEqual("propertyName", field);
		s.addSortDesc("date");
		s.setMaxResults(1);

		final List<AuditRecord> list = dao.search(AuditRecord.class, s);
		if (!list.isEmpty())
		{
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AuditRecord getLastForTypeAndFieldAndId(final Class type, final String field, final Integer id)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addFilterEqual("propertyName", field);
		s.addFilterEqual("entityId", id);
		s.addField("id");
		s.addSortDesc("date");
		s.setMaxResults(1);

		final List<AuditRecord> list = dao.search(AuditRecord.class, s);
		if (!list.isEmpty())
		{
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AuditRecord getLastForTypeAndId(final Class type, final Integer id)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addFilterEqual("entityId", id);
		s.addField("id");
		s.addSortDesc("date");
		s.setMaxResults(1);

		final List<AuditRecord> list = dao.search(AuditRecord.class, s);
		if (!list.isEmpty())
		{
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AuditRecord getLastForUserAndType(final Integer userId, final Class type)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addFilterEqual("userId", userId);
		s.addField("id");
		s.addSortDesc("date");
		s.setMaxResults(1);

		final List<AuditRecord> list = dao.search(AuditRecord.class, s);
		if (!list.isEmpty())
		{
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AuditRecord getLastForUserAndTypeAndField(final Integer userId, final Class type, final String field)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addFilterEqual("propertyName", field);
		s.addFilterEqual("userId", userId);
		s.addField("id");
		s.addSortDesc("date");
		s.setMaxResults(1);

		final List<AuditRecord> list = dao.search(AuditRecord.class, s);
		if (!list.isEmpty())
		{
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AuditRecord getLastForUserAndTypeAndFieldAndId(final Integer userId, final Class type, final String field, final Integer id)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addFilterEqual("propertyName", field);
		s.addFilterEqual("userId", userId);
		s.addField("id");
		s.addFilterEqual("entityId", id);
		s.addField("id");
		s.addSortDesc("date");
		s.setMaxResults(1);

		final List<AuditRecord> list = dao.search(AuditRecord.class, s);
		if (!list.isEmpty())
		{
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AuditRecord getLastForUserAndTypeAndId(final Integer userId, final Class type, final Integer id)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addFilterEqual("userId", userId);
		s.addField("id");
		s.addFilterEqual("entityId", id);
		s.addField("id");
		s.addSortDesc("date");
		s.setMaxResults(1);

		final List<AuditRecord> list = dao.search(AuditRecord.class, s);
		if (!list.isEmpty())
		{
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuditRecord> getAllForUserAndTypeAndFieldAndIdAndValue(final Integer userId, final Class type, final String field, final Integer id, final String value)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addFilterEqual("propertyName", field);
		s.addFilterEqual("propertyValue", value);
		s.addFilterEqual("userId", userId);
		s.addField("id");
		s.addFilterEqual("entityId", id);
		s.addField("id");
		s.addSortDesc("date");

		return dao.search(AuditRecord.class, s);
	}

	@SuppressWarnings("unchecked")
	@Override
	public AuditRecord getLastForUserAndTypeAndFieldAndIdAndValue(final Integer userId, final Class type, final String field, final Integer id, final String value)
	{
		final Search s = new Search(AuditRecord.class);
		s.addFilterEqual("entity", type.getSimpleName());
		s.addFilterEqual("propertyName", field);
		s.addFilterEqual("propertyValue", value);
		s.addFilterEqual("userId", userId);
		s.addField("id");
		s.addFilterEqual("entityId", id);
		s.addField("id");
		s.addSortDesc("date");
		s.setMaxResults(1);

		final List<AuditRecord> list = dao.search(AuditRecord.class, s);
		if (!list.isEmpty())
		{
			return list.get(0);
		}

		return null;
	}

}
