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
package com.utest.domain.service;

import java.util.List;

import com.utest.domain.AuditRecord;

@SuppressWarnings("unchecked")
public interface AuditService
{
	List<AuditRecord> getAllForType(Class type);

	List<AuditRecord> getAllForUserAndType(Integer userId, Class type);

	List<AuditRecord> getAllForTypeAndId(Class type, Integer id);

	List<AuditRecord> getAllForUserAndTypeAndId(Integer userId, Class type, Integer id);

	List<AuditRecord> getAllForTypeAndField(Class type, String field);

	List<AuditRecord> getAllForUserAndTypeAndField(Integer userId, Class type, String field);

	List<AuditRecord> getAllForTypeAndFieldAndId(Class type, String field, Integer id);

	List<AuditRecord> getAllForUserAndTypeAndFieldAndId(Integer userId, Class type, String field, Integer id);

	List<AuditRecord> getAllForUserAndTypeAndFieldAndIdAndValue(Integer userId, Class type, String field, Integer id, String value);

	AuditRecord getLastForType(Class type);

	AuditRecord getLastForUserAndType(Integer userId, Class type);

	AuditRecord getLastForTypeAndId(Class type, Integer id);

	AuditRecord getLastForUserAndTypeAndId(Integer userId, Class type, Integer id);

	AuditRecord getLastForTypeAndField(Class type, String field);

	AuditRecord getLastForUserAndTypeAndField(Integer userId, Class type, String field);

	AuditRecord getLastForTypeAndFieldAndId(Class type, String field, Integer id);

	AuditRecord getLastForUserAndTypeAndFieldAndId(Integer userId, Class type, String field, Integer id);

	AuditRecord getLastForUserAndTypeAndFieldAndIdAndValue(Integer userId, Class type, String field, Integer id, String value);

}
