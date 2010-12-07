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

// TODO - replace with separate classes for each entity
public class TcmEntityStatus extends LocalizedEntity
{

	public static final Integer	DRAFT		= new Integer(1);
	public static final Integer	ACTIVATED	= new Integer(2);
	public static final Integer	LOCKED		= new Integer(3);
	//
	public static final Integer	PASSED		= new Integer(6);
	public static final Integer	FAILED		= new Integer(7);
	public static final Integer	BLOCKED		= new Integer(8);

	// TODO revise for all others

	public TcmEntityStatus()
	{
	}

}
