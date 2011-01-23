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
import com.utest.webservice.api.v2.TestSuiteWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.IncludedTestCaseInfo;
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
	public TestSuiteInfo updateTestSuite(@Context final UriInfo ui_, @PathParam("id") final Integer testSuiteId_, @FormParam("") final TestSuiteInfo testSuiteInfo_)
			throws Exception
	{

		final TestSuite testSuite = testSuiteService.saveTestSuite(testSuiteId_, testSuiteInfo_.getName(), testSuiteInfo_.getDescription(), testSuiteInfo_.getResourceIdentity()
				.getVersion());
		return objectBuilderFactory.toInfo(TestSuiteInfo.class, testSuite, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/activate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_SUITE_EDIT })
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
	@Secured( { Permission.TEST_SUITE_EDIT })
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
	public List<IncludedTestCaseInfo> getTestSuiteTestCases(@Context final UriInfo ui_, @PathParam("id") final Integer testSuiteId_) throws Exception
	{
		final List<TestSuiteTestCase> includedTestCases = testSuiteService.getTestSuiteTestCases(testSuiteId_);
		return objectBuilderFactory.toInfo(IncludedTestCaseInfo.class, includedTestCases, ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/{id}/includedtestcases/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_SUITE_EDIT })
	public IncludedTestCaseInfo createTestSuiteTestCase(@Context final UriInfo ui_, @PathParam("id") final Integer testSuiteId_,
			@FormParam("") final IncludedTestCaseInfo testCaseInfo_) throws Exception
	{

		final TestSuiteTestCase includedTestCase = testSuiteService.addTestSuiteTestCase(testSuiteId_, testCaseInfo_.getTestCaseVersionId(), testCaseInfo_.getPriorityId(),
				testCaseInfo_.getRunOrder(), testCaseInfo_.isBlocking());
		return objectBuilderFactory.toInfo(IncludedTestCaseInfo.class, includedTestCase, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/{id}/includedtestcases/{includedTestCaseId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_EDIT)
	public Boolean deleteTestSuiteTestCase(@Context final UriInfo ui_, @PathParam("id") final Integer testSuiteId_,
			@PathParam("includedTestCaseId") final Integer includedTestCaseId_, @FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{
		testSuiteService.deleteTestSuiteTestCase(includedTestCaseId_, originalVesionId_);

		return Boolean.TRUE;
	}

	@PUT
	@Path("/{id}/includedtestcases/{includedTestCaseId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public IncludedTestCaseInfo updateTestSuiteTestCase(@Context final UriInfo ui_, @PathParam("id") final Integer testSuiteId_,
			@PathParam("includedTestCaseId") final Integer includedTestCaseId_, @FormParam("") final IncludedTestCaseInfo includedTestCaseInfo_) throws Exception
	{

		final TestSuiteTestCase includedTestCase = testSuiteService.saveTestSuiteTestCase(includedTestCaseId_, includedTestCaseInfo_.getPriorityId(), includedTestCaseInfo_
				.getRunOrder(), includedTestCaseInfo_.isBlocking(), includedTestCaseInfo_.getResourceIdentity().getVersion());
		return objectBuilderFactory.toInfo(IncludedTestCaseInfo.class, includedTestCase, ui_.getBaseUriBuilder());
	}

	@POST
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_SUITE_EDIT })
	public TestSuiteInfo createTestSuite(@Context final UriInfo ui_, @FormParam("") final TestSuiteInfo testSuiteInfo_) throws Exception
	{
		final TestSuite testSuite = testSuiteService.addTestSuite(testSuiteInfo_.getProductId(), testSuiteInfo_.isUseLatestVersions(), testSuiteInfo_.getName(), testSuiteInfo_
				.getDescription());

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
