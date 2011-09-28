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
package com.utest.domain.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.utest.domain.User;

public class LoginUserContext
{
	private volatile User			user;

	private List<Integer>	testcycles	= null;

	public LoginUserContext(final User user)
	{
		this.user = user;
	}

	public LoginUserContext()
	{
		this(null);
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(final User user)
	{
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public User getUser()
	{
		return user;
	}

	public synchronized List<Integer> getTestcycles()
	{
		return testcycles != null ? Collections.unmodifiableList(testcycles) : null;
	}

	public synchronized void setTestcycles(final List<Integer> newTestCycles)
	{
		if (newTestCycles == null)
		{
			testcycles = null;
			return;
		}
		else if (testcycles == null)
		{
			testcycles = Collections.synchronizedList(new ArrayList<Integer>());
		}
		testcycles.clear();
		testcycles.addAll(newTestCycles);
	}

	public synchronized boolean removeTestCycle(final Integer testcycle)
	{
		if (testcycles != null)
		{
			return testcycles.remove(testcycle);
		}
		return false;
	}

	public synchronized boolean addTestCycle(final Integer testcycle)
	{
		if (testcycles != null)
		{
			return testcycles.add(testcycle);
		}
		return false;
	}
}
