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

import java.util.List;

public class EnvironmentGroupExploded extends EnvironmentGroup
{
	private List<Environment>	environments;

	public EnvironmentGroupExploded()
	{
		super();
	}

	public void setEnvironments(List<Environment> environments)
	{
		this.environments = environments;
	}

	public List<Environment> getEnvironments()
	{
		return environments;
	}
}
