package com.utest.portal.log;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class LogContextListener implements ServletContextListener
{

	public void contextDestroyed(final ServletContextEvent contextEvent)
	{
		RepositorySelector.removeFromRepository();
	}

	public void contextInitialized(final ServletContextEvent contextEvent)
	{
		try
		{
			RepositorySelector.init(contextEvent.getServletContext());
		}
		catch (final Exception ex)
		{
			System.err.println(ex);
		}
	}
}
