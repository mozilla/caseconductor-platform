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
package com.utest.domain;

import java.util.HashMap;
import java.util.Map;

public class LocalizedEntity extends Entity
{
	Map<String, LocaleDescriptable>	locales	= new HashMap<String, LocaleDescriptable>();

	public LocalizedEntity()
	{
	}

	public Map<String, LocaleDescriptable> getLocales()
	{
		return locales;
	}

	public void addLocale(final LocaleDescriptable locale_, final String localeCode_)
	{
		this.locales.put(localeCode_, locale_);
	}

	public void setLocales(final Map<String, LocaleDescriptable> locales_)
	{
		this.locales = locales_;
	}

	public LocaleDescriptable getLocale(final String localeCode_)
	{
		return locales.get(localeCode_);
	}
}
