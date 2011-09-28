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
package com.utest.webservice.api.v2;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.utest.webservice.model.v2.CodeValue;

public interface StaticDataWebService
{
	List<CodeValue> getCodeValues(@Context final UriInfo ui_, String dataType) throws Exception;

	List<CodeValue> getParentCodeValues(@Context final UriInfo ui_, String dataType, Integer parentId) throws Exception;

	Map<String, List<CodeValue>> getParentMap(@Context final UriInfo ui_, String dataType) throws Exception;

	Set<String> getCodeKeys() throws Exception;

	List<CodeValue> getCodeValues(@Context final UriInfo ui_, String id, String locale) throws Exception;

	List<CodeValue> getLocales(UriInfo ui) throws Exception;

	Set<String> getErrorKeys() throws Exception;
}
