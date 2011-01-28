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
