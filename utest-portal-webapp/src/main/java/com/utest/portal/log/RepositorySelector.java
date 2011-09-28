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
/***************************************
 *                                     *
 *  JBoss: The OpenSource J2EE WebOS   *
 *                                     *
 *  Distributable under LGPL license.  *
 *  See terms of license at gnu.org.   *
 *                                     *
 ***************************************/
package com.utest.portal.log;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RootLogger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;

/**
 * This RepositorySelector is for use with web applications. It assumes that
 * your log4j.xml file is in the WEB-INF directory.
 * 
 */
public class RepositorySelector implements
		org.apache.log4j.spi.RepositorySelector
{
	private static boolean initialized = false;
	private static Object guard = LogManager.getRootLogger();
	private static Map<ClassLoader, Hierarchy> repositories = new HashMap<ClassLoader, Hierarchy>();
	private static LoggerRepository defaultRepository;

	public static synchronized void init(final ServletConfig servletConfig)
			throws ServletException
	{
		init(servletConfig.getServletContext());
	}

	public static synchronized void init(final ServletContext servletContext)
			throws ServletException
	{
		if (!initialized) // set the global RepositorySelector
		{
			defaultRepository = LogManager.getLoggerRepository();
			final RepositorySelector theSelector = new RepositorySelector();
			LogManager.setRepositorySelector(theSelector, guard);
			initialized = true;
		}

		final Hierarchy hierarchy = new Hierarchy(new RootLogger(Level.DEBUG));
		loadLog4JConfig(servletContext, hierarchy);
		final ClassLoader loader = Thread.currentThread()
				.getContextClassLoader();
		repositories.put(loader, hierarchy);
	}

	public static synchronized void removeFromRepository()
	{
		repositories.remove(Thread.currentThread().getContextClassLoader());
	}

	// load log4j.xml from WEB-INF/config
	private static void loadLog4JConfig(final ServletContext servletContext,
			final Hierarchy hierarchy) throws ServletException
	{
		try
		{
			final String log4jFile = "/WEB-INF/config/log4j.xml";
			final InputStream log4JConfig = servletContext
					.getResourceAsStream(log4jFile);
			final Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(log4JConfig);
			final DOMConfigurator conf = new DOMConfigurator();
			conf.doConfigure(doc.getDocumentElement(), hierarchy);
		}
		catch (final Exception e)
		{
			throw new ServletException(e);
		}
	}

	private RepositorySelector()
	{
	}

	public LoggerRepository getLoggerRepository()
	{
		final ClassLoader loader = Thread.currentThread()
				.getContextClassLoader();
		final LoggerRepository repository = repositories.get(loader);
		if (repository == null)
		{
			return defaultRepository;
		}
		else
		{
			return repository;
		}
	}
}
