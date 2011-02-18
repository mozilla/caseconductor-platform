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

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.springframework.security.access.annotation.Secured;

import com.utest.domain.EnvironmentGroup;
import com.utest.domain.Permission;
import com.utest.domain.TestPlan;
import com.utest.domain.TestPlanTestSuite;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.TestPlanService;
import com.utest.webservice.api.v2.TestPlanWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.IncludedTestSuiteInfo;
import com.utest.webservice.model.v2.TestPlanInfo;
import com.utest.webservice.model.v2.TestPlanSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

@Path("/testplans/")
public class TestPlanWebServiceImpl extends BaseWebServiceImpl implements TestPlanWebService
{
	private final TestPlanService	testPlanService;

	public TestPlanWebServiceImpl(final ObjectBuilderFactory objectBuildFactory, final TestPlanService testPlanService)
	{
		super(objectBuildFactory);
		this.testPlanService = testPlanService;
	}

	@PUT
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_PLAN_EDIT })
	public TestPlanInfo updateTestPlan(@Context final UriInfo ui_, @PathParam("id") final Integer testPlanId_, @FormParam("name") final String name_,
			@FormParam("description") final String description_, @FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{

		final TestPlan testPlan = testPlanService.saveTestPlan(testPlanId_, name_, description_, originalVesionId_);
		return objectBuilderFactory.toInfo(TestPlanInfo.class, testPlan, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/activate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_PLAN_ACTIVATE })
	public TestPlanInfo activateTestPlan(@Context final UriInfo ui_, @PathParam("id") final Integer testPlanId_, @FormParam("originalVersionId") final Integer originalVesionId_)
			throws Exception
	{

		final TestPlan testPlan = testPlanService.activateTestPlan(testPlanId_, originalVesionId_);
		return objectBuilderFactory.toInfo(TestPlanInfo.class, testPlan, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/deactivate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_PLAN_ACTIVATE })
	public TestPlanInfo deactivateTestPlan(@Context final UriInfo ui_, @PathParam("id") final Integer testPlanId_, @FormParam("originalVersionId") final Integer originalVesionId_)
			throws Exception
	{

		final TestPlan testPlan = testPlanService.lockTestPlan(testPlanId_, originalVesionId_);
		return objectBuilderFactory.toInfo(TestPlanInfo.class, testPlan, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_PLAN_EDIT })
	public Boolean updateTestPlanEnvironmentGroups(@Context final UriInfo ui_, @PathParam("id") final Integer testPlanId_,
			@FormParam("environmentGroupIds") final ArrayList<Integer> environmentGroupIds_, @FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{
		testPlanService.saveEnvironmentGroupsForTestPlan(testPlanId_, environmentGroupIds_, originalVesionId_);
		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_PLAN_VIEW)
	public List<EnvironmentGroupInfo> getTestPlanEnvironmentGroups(@Context final UriInfo ui_, @PathParam("id") final Integer testPlanId_) throws Exception
	{
		final List<EnvironmentGroup> groups = testPlanService.getEnvironmentGroupsForTestPlan(testPlanId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, groups, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/includedtestsuites/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_PLAN_VIEW)
	public List<IncludedTestSuiteInfo> getTestPlanTestSuites(@Context final UriInfo ui_, @PathParam("id") final Integer testPlanId_) throws Exception
	{
		final List<TestPlanTestSuite> includedTestSuites = testPlanService.getTestPlanTestSuites(testPlanId_);
		return objectBuilderFactory.toInfo(IncludedTestSuiteInfo.class, includedTestSuites, ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/{id}/includedtestsuites/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_PLAN_EDIT })
	public IncludedTestSuiteInfo createTestPlanTestSuite(@Context final UriInfo ui_, @PathParam("id") final Integer testPlanId_,
			@FormParam("testSuiteId") final Integer testSuiteId_, @FormParam("runOrder") final Integer runOrder_) throws Exception
	{

		final TestPlanTestSuite includedTestSuite = testPlanService.addTestPlanTestSuite(testPlanId_, testSuiteId_, runOrder_);
		return objectBuilderFactory.toInfo(IncludedTestSuiteInfo.class, includedTestSuite, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/includedtestsuites/{includedTestSuiteId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_PLAN_EDIT)
	public Boolean deleteTestPlanTestSuite(@Context final UriInfo ui_, @PathParam("includedTestSuiteId") final Integer includedTestSuiteId_,
			@FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{
		testPlanService.deleteTestPlanTestSuite(includedTestSuiteId_, originalVesionId_);

		return Boolean.TRUE;
	}

	@GET
	@Path("/includedtestsuites/{includedTestSuiteId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_PLAN_VIEW })
	public IncludedTestSuiteInfo getTestPlanTestSuite(@Context final UriInfo ui_, @PathParam("includedTestSuiteId") final Integer includedTestSuiteId_) throws Exception
	{
		final TestPlanTestSuite includedTestSuite = testPlanService.getTestPlanTestSuite(includedTestSuiteId_);
		return objectBuilderFactory.toInfo(IncludedTestSuiteInfo.class, includedTestSuite, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/includedtestsuites/{includedTestSuiteId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_PLAN_EDIT })
	public IncludedTestSuiteInfo updateTestPlanTestSuite(@Context final UriInfo ui_, @PathParam("includedTestSuiteId") final Integer includedTestSuiteId_,
			@FormParam("runOrder") final Integer runOrder_, @FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{

		final TestPlanTestSuite includedTestSuite = testPlanService.saveTestPlanTestSuite(includedTestSuiteId_, runOrder_, originalVesionId_);
		return objectBuilderFactory.toInfo(IncludedTestSuiteInfo.class, includedTestSuite, ui_.getBaseUriBuilder());
	}

	@POST
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_PLAN_EDIT })
	public TestPlanInfo createTestPlan(@Context final UriInfo ui_, @FormParam("productId") final Integer productId_, @FormParam("name") final String name_,
			@FormParam("description") final String description_) throws Exception
	{
		final TestPlan testPlan = testPlanService.addTestPlan(productId_, name_, description_);

		return objectBuilderFactory.toInfo(TestPlanInfo.class, testPlan, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_PLAN_EDIT)
	public Boolean deleteTestPlan(@Context final UriInfo ui_, @PathParam("id") final Integer testPlanId_, @FormParam("originalVersionId") final Integer originalVesionId_)
			throws Exception
	{
		testPlanService.deleteTestPlan(testPlanId_, originalVesionId_);

		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_PLAN_VIEW)
	/**
	 * Returns latest version of test case by default
	 */
	public TestPlanInfo getTestPlan(@Context final UriInfo ui_, @PathParam("id") final Integer testPlanId_) throws Exception
	{
		final TestPlan testPlan = testPlanService.getTestPlan(testPlanId_);
		return objectBuilderFactory.toInfo(TestPlanInfo.class, testPlan, ui_.getBaseUriBuilder());
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_PLAN_VIEW)
	/**
	 * Returns latest versions of test cases by default
	 */
	public TestPlanSearchResultInfo findTestPlans(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(TestPlanInfo.class, request_, ui_);
		final UtestSearchResult result = testPlanService.findTestPlans(search);

		return (TestPlanSearchResultInfo) objectBuilderFactory.createResult(TestPlanInfo.class, TestPlan.class, request_, result, ui_.getBaseUriBuilder());
	}

}
