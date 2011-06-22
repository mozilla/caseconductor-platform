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
import com.utest.domain.TestSuite;
import com.utest.domain.TestSuiteTestCase;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.TestSuiteService;
import com.utest.domain.view.TestSuiteTestCaseView;
import com.utest.webservice.api.v2.TestSuiteWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.TestSuiteTestCaseInfo;
import com.utest.webservice.model.v2.TestSuiteInfo;
import com.utest.webservice.model.v2.TestSuiteSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

@Path("/testsuites/")
public class TestSuiteWebServiceImpl extends BaseWebServiceImpl implements TestSuiteWebService
{
	private final TestSuiteService	testSuiteService;

	public TestSuiteWebServiceImpl(final ObjectBuilderFactory objectBuildFactory, final TestSuiteService testSuiteService)
	{
		super(objectBuildFactory);
		this.testSuiteService = testSuiteService;
	}

	@PUT
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_SUITE_EDIT })
	public TestSuiteInfo updateTestSuite(@Context final UriInfo ui_, @PathParam("id") final Integer testSuiteId_, @FormParam("name") final String name_,
			@FormParam("description") final String description_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		final TestSuite testSuite = testSuiteService.saveTestSuite(testSuiteId_, name_, description_, originalVersionId_);
		return objectBuilderFactory.toInfo(TestSuiteInfo.class, testSuite, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/activate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_SUITE_ACTIVATE })
	public TestSuiteInfo activateTestSuite(@Context final UriInfo ui_, @PathParam("id") final Integer testSuiteId_, @FormParam("originalVersionId") final Integer originalVesionId_)
			throws Exception
	{

		final TestSuite testSuite = testSuiteService.activateTestSuite(testSuiteId_, originalVesionId_);
		return objectBuilderFactory.toInfo(TestSuiteInfo.class, testSuite, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/deactivate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_SUITE_ACTIVATE })
	public TestSuiteInfo deactivateTestSuite(@Context final UriInfo ui_, @PathParam("id") final Integer testSuiteId_,
			@FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{

		final TestSuite testSuite = testSuiteService.lockTestSuite(testSuiteId_, originalVesionId_);
		return objectBuilderFactory.toInfo(TestSuiteInfo.class, testSuite, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_SUITE_EDIT })
	public Boolean updateTestSuiteEnvironmentGroups(@Context final UriInfo ui_, @PathParam("id") final Integer testSuiteId_,
			@FormParam("environmentGroupIds") final ArrayList<Integer> environmentGroupIds_, @FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{
		testSuiteService.saveEnvironmentGroupsForTestSuite(testSuiteId_, environmentGroupIds_, originalVesionId_);
		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_SUITE_VIEW)
	public List<EnvironmentGroupInfo> getTestSuiteEnvironmentGroups(@Context final UriInfo ui_, @PathParam("id") final Integer testSuiteId_) throws Exception
	{
		final List<EnvironmentGroup> groups = testSuiteService.getEnvironmentGroupsForTestSuite(testSuiteId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, groups, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/includedtestcases/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_SUITE_VIEW)
	public List<TestSuiteTestCaseInfo> getTestSuiteTestCases(@Context final UriInfo ui_, @PathParam("id") final Integer testSuiteId_) throws Exception
	{
		final List<TestSuiteTestCaseView> includedTestCases = testSuiteService.getTestSuiteTestCasesViews(testSuiteId_);
		return objectBuilderFactory.toInfo(TestSuiteTestCaseInfo.class, includedTestCases, ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/{id}/includedtestcases/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_SUITE_EDIT })
	public TestSuiteTestCaseInfo createTestSuiteTestCase(@Context final UriInfo ui_, @PathParam("id") final Integer testSuiteId_,
			@FormParam("testCaseVersionId") final Integer testCaseVersionId_, @FormParam("priorityId") final Integer priorityId_, @FormParam("runOrder") final Integer runOrder_,
			@FormParam("blocking") final String blocking_) throws Exception
	{

		final TestSuiteTestCase includedTestCase = testSuiteService.addTestSuiteTestCase(testSuiteId_, testCaseVersionId_, priorityId_, runOrder_, "true"
				.equalsIgnoreCase(blocking_));
		return objectBuilderFactory.toInfo(TestSuiteTestCaseInfo.class, testSuiteService.getTestSuiteTestCaseView(includedTestCase.getId()), ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/includedtestcases/{includedTestCaseId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_EDIT)
	public Boolean deleteTestSuiteTestCase(@Context final UriInfo ui_, @PathParam("includedTestCaseId") final Integer includedTestCaseId_,
			@FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{
		testSuiteService.deleteTestSuiteTestCase(includedTestCaseId_, originalVesionId_);

		return Boolean.TRUE;
	}

	@GET
	@Path("/includedtestcases/{includedTestCaseId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public TestSuiteTestCaseInfo getTestSuiteTestCase(@Context final UriInfo ui_, @PathParam("includedTestCaseId") final Integer includedTestCaseId_) throws Exception
	{

		final TestSuiteTestCaseView includedTestCase = testSuiteService.getTestSuiteTestCaseView(includedTestCaseId_);
		return objectBuilderFactory.toInfo(TestSuiteTestCaseInfo.class, includedTestCase, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/includedtestcases/{includedTestCaseId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public TestSuiteTestCaseInfo updateTestSuiteTestCase(@Context final UriInfo ui_, @PathParam("includedTestCaseId") final Integer includedTestCaseId_,
			@FormParam("testCaseVersionId") final Integer testCaseVersionId_, @FormParam("priorityId") final Integer priorityId_, @FormParam("runOrder") final Integer runOrder_,
			@FormParam("blocking") final String blocking_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{

		final TestSuiteTestCase includedTestCase = testSuiteService.saveTestSuiteTestCase(includedTestCaseId_, priorityId_, runOrder_, "true".equalsIgnoreCase(blocking_),
				originalVersionId_);
		return objectBuilderFactory.toInfo(TestSuiteTestCaseInfo.class, testSuiteService.getTestSuiteTestCaseView(includedTestCase.getId()), ui_.getBaseUriBuilder());
	}

	@POST
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_SUITE_EDIT })
	public TestSuiteInfo createTestSuite(@Context final UriInfo ui_, @FormParam("productId") final Integer productId_,
			@FormParam("useLatestVersions") final String useLatestVersions_, @FormParam("name") final String name_, @FormParam("description") final String description_)
			throws Exception
	{
		final TestSuite testSuite = testSuiteService.addTestSuite(productId_, "true".equalsIgnoreCase(useLatestVersions_), name_, description_);
		return objectBuilderFactory.toInfo(TestSuiteInfo.class, testSuite, ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/{id}/clone/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_SUITE_EDIT })
	public TestSuiteInfo cloneTestSuite(@Context final UriInfo ui_, @PathParam("id") final Integer testSuiteId_) throws Exception
	{
		final TestSuite testSuite = testSuiteService.cloneTestSuite(testSuiteId_);
		return objectBuilderFactory.toInfo(TestSuiteInfo.class, testSuite, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_SUITE_EDIT)
	public Boolean deleteTestSuite(@Context final UriInfo ui_, @PathParam("id") final Integer testSuiteId_, @FormParam("originalVersionId") final Integer originalVesionId_)
			throws Exception
	{
		testSuiteService.deleteTestSuite(testSuiteId_, originalVesionId_);

		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_SUITE_VIEW)
	/**
	 * Returns latest version of test case by default
	 */
	public TestSuiteInfo getTestSuite(@Context final UriInfo ui_, @PathParam("id") final Integer testSuiteId_) throws Exception
	{
		final TestSuite testSuite = testSuiteService.getTestSuite(testSuiteId_);
		return objectBuilderFactory.toInfo(TestSuiteInfo.class, testSuite, ui_.getBaseUriBuilder());
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_SUITE_VIEW)
	/**
	 * Returns latest versions of test cases by default
	 */
	public TestSuiteSearchResultInfo findTestSuites(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(TestSuiteInfo.class, request_, ui_);
		final UtestSearchResult result = testSuiteService.findTestSuites(search);

		return (TestSuiteSearchResultInfo) objectBuilderFactory.createResult(TestSuiteInfo.class, TestSuite.class, request_, result, ui_.getBaseUriBuilder());
	}

}
