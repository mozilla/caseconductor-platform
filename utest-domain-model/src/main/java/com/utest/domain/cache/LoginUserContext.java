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
