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
package com.utest.domain.service;

import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentMap;

import com.utest.domain.CodeValueEntity;

public interface StaticDataService extends Initializable
{
	ConcurrentMap<String, Vector<CodeValueEntity>> getCodeData();

	ConcurrentMap<String, Vector<CodeValueEntity>> getLocalizedCodeData(String localeCode_);

	ConcurrentMap<String, ConcurrentMap<String, Vector<CodeValueEntity>>> getLocalizedParentDependableCodeData(String localeCode_);

	ConcurrentMap<String, ConcurrentMap<String, Vector<CodeValueEntity>>> getParentDependableCodeData();

	<T> List<T> getEntities(Class<T> clazz_, List<Integer> ids_);

	<T> T getEntity(Class<T> clazz_, Integer id_);

	<T> void saveProfileCollectionChanges(Class<T> clazz_, List<Integer> ids_, String parentField_, Integer profileId_) throws Exception;

	<T> void saveProfileOtherCollectionChanges(Class<T> clazz_, List<?> newOtherValues_, String parentField_, Integer parentId_) throws Exception;

	String getCodeDescription(Class<?> type_, Integer id_);

	String getCodeDescription(Class<?> type_, Integer id_, String localeCode_);

	String getCodeDescription(String className, Integer id_);

	String getCodeDescription(String className, Integer id_, String localeCode_);

	<T> T saveEntity(T entity_);

	<T> void deleteEntity(T entity_);

	<T> void deleteEntity(Class<T> clazz_, Integer id_);

	<T> Vector<T> getNativeDataObjects(Class<T> clazz_);

	<T> void evictEntity(T entity_);

	@SuppressWarnings("unchecked")
	void addStaticData(Class clazz_, String description, List<Integer> parentId) throws Exception;

	@SuppressWarnings("unchecked")
	void updateStaticData(Class clazz_, Integer id, String description) throws Exception;

	@SuppressWarnings("unchecked")
	void addToParent(final Class parentDependableClazz_, final List<Integer> parentId, final Class childClazz_, final Integer childId) throws Exception;

	List<CodeValueEntity> getCodeDescriptions(String className);

	List<CodeValueEntity> getCodeDescriptions(String className, String localeCode);

	Set<String> getCodeKeys();

	List<CodeValueEntity> getSupportedLocales();

}
