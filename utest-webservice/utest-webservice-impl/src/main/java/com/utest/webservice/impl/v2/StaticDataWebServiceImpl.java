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
package com.utest.webservice.impl.v2;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.utest.domain.CodeValueEntity;
import com.utest.domain.Locale;
import com.utest.domain.service.StaticDataService;
import com.utest.webservice.api.v2.StaticDataWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.CodeValue;

@Path("/")
public class StaticDataWebServiceImpl extends BaseWebServiceImpl implements StaticDataWebService
{

	private final StaticDataService	staticDataService;

	public StaticDataWebServiceImpl(final ObjectBuilderFactory objectBuildFactory, final StaticDataService staticDataService)
	{
		super(objectBuildFactory);
		this.staticDataService = staticDataService;
	}

	@Override
	@GET
	@Path("/keys/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Set<String> getCodeKeys() throws Exception
	{
		return staticDataService.getCodeKeys();

	}

	@Override
	@GET
	@Path("/locales/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<CodeValue> getLocales(@Context final UriInfo ui_) throws Exception
	{
		return objectBuilderFactory.toInfo(CodeValue.class, staticDataService.getSupportedLocales(), ui_.getBaseUriBuilder());
	}

	@Override
	@GET
	@Path("/values/{key}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<CodeValue> getCodeValues(@Context final UriInfo ui_, @PathParam("key") final String id) throws Exception
	{
		if (id == null)
		{
			throw new IllegalArgumentException("Data type is null.");
		}
		String name = id.toUpperCase();
		return objectBuilderFactory.toInfo(CodeValue.class, staticDataService.getCodeDescriptions(name), ui_.getBaseUriBuilder());
	}

	@Override
	@GET
	@Path("/values/{key}/locale/{locale}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<CodeValue> getCodeValues(@Context final UriInfo ui_, @PathParam("key") final String id, @PathParam("locale") final String locale) throws Exception
	{
		if (id == null)
		{
			throw new IllegalArgumentException("Data type is null.");
		}
		String name = id.toUpperCase();
		return objectBuilderFactory.toInfo(CodeValue.class, staticDataService.getCodeDescriptions(name, locale), ui_.getBaseUriBuilder());
	}

	// @Override
	// @GET
	// @Path("/values/{id}/parent/{parentId}")
	// @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	// @Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<CodeValue> getParentCodeValues(@Context final UriInfo ui_, @PathParam("id") final String id, @PathParam("parentId") final Integer parentId) throws Exception
	{
		if (id == null)
		{
			throw new IllegalArgumentException("Data type is null.");
		}
		String name = id.substring(0, 1).toUpperCase() + id.substring(1);
		if (id.endsWith("Id"))
		{
			name = name.substring(0, name.lastIndexOf("Id"));
		}

		ConcurrentMap<String, ConcurrentMap<String, Vector<CodeValueEntity>>> parentDepend = staticDataService.getParentDependableCodeData();
		if (!parentDepend.containsKey(name))
		{
			parentDepend = staticDataService.getLocalizedParentDependableCodeData(Locale.DEFAULT_LOCALE);
		}
		if (!parentDepend.containsKey(name))
		{
			return Collections.emptyList();
		}
		Vector<CodeValueEntity> list = parentDepend.get(name).get(parentId + "");
		if (list == null)
		{
			list = new Vector<CodeValueEntity>();
		}
		return objectBuilderFactory.toInfo(CodeValue.class, list, ui_.getBaseUriBuilder());
	}

	// @Override
	// @GET
	// @Path("/values/{id}/parent/")
	// @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	// @Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Map<String, List<CodeValue>> getParentMap(@Context final UriInfo ui_, @PathParam("id") final String id) throws Exception
	{
		if (id == null)
		{
			throw new IllegalArgumentException("Data type is null.");
		}
		String name = id.substring(0, 1).toUpperCase() + id.substring(1);
		if (id.endsWith("Id"))
		{
			name = name.substring(0, name.lastIndexOf("Id"));
		}
		final Map<String, List<CodeValue>> result = new HashMap<String, List<CodeValue>>();
		ConcurrentMap<String, ConcurrentMap<String, Vector<CodeValueEntity>>> parentDepend = staticDataService.getParentDependableCodeData();
		if (!parentDepend.containsKey(name))
		{
			parentDepend = staticDataService.getLocalizedParentDependableCodeData(Locale.DEFAULT_LOCALE);
		}
		if (!parentDepend.containsKey(name))
		{
			return result;
		}

		for (final Entry<String, Vector<CodeValueEntity>> entry : parentDepend.get(name).entrySet())
		{
			result.put(entry.getKey(), objectBuilderFactory.toInfo(CodeValue.class, entry.getValue(), ui_.getBaseUriBuilder()));
		}
		return result;
	}

}
