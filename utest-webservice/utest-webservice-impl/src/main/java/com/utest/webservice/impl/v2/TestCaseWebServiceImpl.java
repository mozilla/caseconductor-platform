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
import com.utest.domain.ProductComponent;
import com.utest.domain.Tag;
import com.utest.domain.TestCase;
import com.utest.domain.TestCaseStep;
import com.utest.domain.TestCaseVersion;
import com.utest.domain.VersionIncrement;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.TestCaseService;
import com.utest.webservice.api.v2.TestCaseWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.ProductComponentInfo;
import com.utest.webservice.model.v2.ResourceIdentity;
import com.utest.webservice.model.v2.TagInfo;
import com.utest.webservice.model.v2.TestCaseInfo;
import com.utest.webservice.model.v2.TestCaseResultInfo;
import com.utest.webservice.model.v2.TestCaseStepInfo;
import com.utest.webservice.model.v2.TestCaseVersionInfo;
import com.utest.webservice.model.v2.TestCaseVersionResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

@Path("/testcases/")
public class TestCaseWebServiceImpl extends BaseWebServiceImpl implements TestCaseWebService
{
	private final TestCaseService	testCaseService;

	public TestCaseWebServiceImpl(final ObjectBuilderFactory objectBuildFactory, final TestCaseService testCaseService)
	{
		super(objectBuildFactory);
		this.testCaseService = testCaseService;
	}

	@PUT
	@Path("/versions/{id}/versionincrement/{increment}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public TestCaseVersionInfo updateTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_,
			@PathParam("increment") final String versionIncrement_, @FormParam("") final TestCaseVersionInfo testCaseVersionInfo_) throws Exception
	{

		final TestCaseVersion testCaseVersion = testCaseService.saveTestCaseVersion(testCaseVersionId_, testCaseVersionInfo_.getDescription(), testCaseVersionInfo_
				.getResourceIdentity().getVersion(), VersionIncrement.valueOf(versionIncrement_));
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersion, ui_.getBaseUriBuilder().path("/testcases/versions/{id}/"));
	}

	@PUT
	@Path("/versions/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public TestCaseVersionInfo updateTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_,
			@FormParam("") final TestCaseVersionInfo testCaseVersionInfo_) throws Exception
	{

		final TestCaseVersion testCaseVersion = testCaseService.saveTestCaseVersion(testCaseVersionId_, testCaseVersionInfo_.getDescription(), testCaseVersionInfo_
				.getResourceIdentity().getVersion(), VersionIncrement.NONE);
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersion, ui_.getBaseUriBuilder().path("/testcases/versions/{id}/"));
	}

	@PUT
	@Path("/{id}/versions/{versionId}/approve/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public TestCaseVersionInfo approveTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_,
			@PathParam("versionId") final Integer testCaseVersionId_, @FormParam("") final TestCaseVersionInfo testCaseVersionInfo_) throws Exception
	{

		final TestCaseVersion testCaseVersion = testCaseService.approveTestCaseVersion(testCaseVersionId_, testCaseVersionInfo_.getResourceIdentity().getVersion());
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersion, ui_.getBaseUriBuilder().path("/testcases/versions/{id}/"));
	}

	@PUT
	@Path("/versions/{id}/reject/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public TestCaseVersionInfo rejectTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_,
			@FormParam("") final TestCaseVersionInfo testCaseVersionInfo_) throws Exception
	{

		final TestCaseVersion testCaseVersion = testCaseService.rejectTestCaseVersion(testCaseVersionId_, testCaseVersionInfo_.getResourceIdentity().getVersion());
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersion, ui_.getBaseUriBuilder().path("/testcases/versions/{id}/"));
	}

	@PUT
	@Path("/{id}/versions/{versionId}/activate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public TestCaseVersionInfo activateTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_,
			@PathParam("versionId") final Integer testCaseVersionId_, @FormParam("") final TestCaseVersionInfo testCaseVersionInfo_) throws Exception
	{

		final TestCaseVersion testCaseVersion = testCaseService.activateTestCaseVersion(testCaseVersionId_, testCaseVersionInfo_.getResourceIdentity().getVersion());
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersion, ui_.getBaseUriBuilder().path("/testcases/versions/{id}/"));
	}

	@PUT
	@Path("versions/{id}/deactivate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public TestCaseVersionInfo deactivateTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_,
			@FormParam("") final TestCaseVersionInfo testCaseVersionInfo_) throws Exception
	{

		final TestCaseVersion testCaseVersion = testCaseService.lockTestCaseVersion(testCaseVersionId_, testCaseVersionInfo_.getResourceIdentity().getVersion());
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersion, ui_.getBaseUriBuilder().path("/testcases/versions/{id}/"));
	}

	@PUT
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public TestCaseInfo updateTestCase(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_, @FormParam("") final TestCaseInfo testCaseInfo_) throws Exception
	{
		final TestCase testCase = testCaseService.saveTestCase(testCaseId_, testCaseInfo_.getName(), testCaseInfo_.getMaxAttachmentSizeInMbytes(), testCaseInfo_
				.getMaxNumberOfAttachments());
		return objectBuilderFactory.toInfo(TestCaseInfo.class, testCase, ui_.getBaseUriBuilder().path("/{id}/"));
	}

	@POST
	@Path("/{id}/clone/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public TestCaseVersionInfo cloneTestCase(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_) throws Exception
	{
		final TestCase testCase = testCaseService.cloneTestCase(testCaseId_);

		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCase.getLatestVersion(), ui_.getBaseUriBuilder().path("/testcases/versions/{id}/"));
	}

	@PUT
	@Path("/{id}/productcomponents/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public Boolean updateTestCaseComponents(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_,
			@FormParam("productComponentIds") final ArrayList<Integer> productComponentIds_) throws Exception
	{
		testCaseService.saveProductComponentsForTestCase(testCaseId_, productComponentIds_);
		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/productcomponents/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	/**
	 * Returns all product components of a test case
	 */
	public List<ProductComponentInfo> getTestCaseComponents(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_) throws Exception
	{
		final List<ProductComponent> components = testCaseService.getComponentsForTestCase(testCaseId_);
		return objectBuilderFactory.toInfo(ProductComponentInfo.class, components, ui_.getAbsolutePathBuilder().path("/{id}/productcomponents/"));
	}

	@GET
	@Path("/{id}/tags/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	/**
	 * Returns all tags of a test case
	 */
	public List<TagInfo> getTestCaseTags(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_) throws Exception
	{
		final List<Tag> tags = testCaseService.getTestCaseTags(testCaseId_);
		return objectBuilderFactory.toInfo(TagInfo.class, tags, ui_.getAbsolutePathBuilder().path("/{id}/tags/"));
	}

	@PUT
	@Path("/{id}/tags/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public Boolean updateTestCaseTags(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_, @FormParam("tagIds") final ArrayList<Integer> tagIds_)
			throws Exception
	{
		testCaseService.saveTagsForTestCase(testCaseId_, tagIds_);
		return Boolean.TRUE;
	}

	@PUT
	@Path("/versions/{id}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public Boolean updateTestCaseEnvironmentGroups(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_,
			@FormParam("environmentGroupIds") final ArrayList<Integer> environmentGroupIds_, @FormParam("environmentGroupIds") final ResourceIdentity testCaseVersionIdentity_)
			throws Exception
	{
		testCaseService.saveEnvironmentGroupsForTestCaseVersion(testCaseVersionId_, environmentGroupIds_);
		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/versions/{versionId}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	/**
	 * Returns all versions of a test case
	 */
	public List<EnvironmentGroupInfo> getTestCaseEnvironmentGroups(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_,
			@PathParam("versionId") final Integer testCaseVersionId_) throws Exception
	{
		final List<EnvironmentGroup> groups = testCaseService.getEnvironmentGroupsForTestCaseVersion(testCaseVersionId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, groups, ui_.getAbsolutePathBuilder().path("/{id}/versions/{versionId}/environmentgroups/"));
	}

	@POST
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public TestCaseVersionInfo createTestCase(@Context final UriInfo ui_, @FormParam("") final TestCaseVersionInfo testCaseVersionInfo_) throws Exception
	{
		final TestCase testCase = testCaseService.addTestCase(testCaseVersionInfo_.getProductId(), testCaseVersionInfo_.getMaxAttachmentSizeInMbytes(), testCaseVersionInfo_
				.getMaxNumberOfAttachments(), testCaseVersionInfo_.getName(), testCaseVersionInfo_.getDescription());

		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCase.getLatestVersion(), ui_.getBaseUriBuilder().path("/testcases/versions/{id}/"));
	}

	@DELETE
	@Path("/versions/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_EDIT)
	public Boolean deleteTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_) throws Exception
	{
		testCaseService.deleteTestCaseVersion(testCaseVersionId_);

		return Boolean.TRUE;
	}

	@POST
	@Path("/versions/{id}/steps/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public TestCaseStepInfo createTestCaseStep(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_,
			@FormParam("") final TestCaseStepInfo testCaseStepInfo_) throws Exception
	{

		final TestCaseStep testCaseStep = testCaseService.addTestCaseStep(testCaseVersionId_, testCaseStepInfo_.getName(), testCaseStepInfo_.getStepNumber(), testCaseStepInfo_
				.getInstruction(), testCaseStepInfo_.getExpectedResult(), testCaseStepInfo_.getEstimatedTimeInMin());
		return objectBuilderFactory.toInfo(TestCaseStepInfo.class, testCaseStep, ui_.getBaseUriBuilder().path("/versions/{id}/steps/{stepId}/"));
	}

	@DELETE
	@Path("/versions/{id}/steps/{stepId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_EDIT)
	public Boolean deleteTestCaseStep(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_, @PathParam("stepId") final Integer testCaseStepId_)
			throws Exception
	{
		testCaseService.deleteTestCaseStep(testCaseStepId_);

		return Boolean.TRUE;
	}

	@PUT
	@Path("/versions/{id}/steps/{stepId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT })
	public TestCaseStepInfo updateTestCaseStep(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_, @PathParam("stepId") final Integer testCaseStepId_,
			@FormParam("") final TestCaseStepInfo testCaseStepInfo_) throws Exception
	{

		final TestCaseStep testCaseStep = testCaseService.saveTestCaseStep(testCaseStepId_, testCaseStepInfo_.getName(), testCaseStepInfo_.getStepNumber(), testCaseStepInfo_
				.getInstruction(), testCaseStepInfo_.getExpectedResult(), testCaseStepInfo_.getEstimatedTimeInMin(), testCaseStepInfo_.getResourceIdentity().getVersion());
		return objectBuilderFactory.toInfo(TestCaseStepInfo.class, testCaseStep, ui_.getAbsolutePathBuilder().path("/versions/{id}/steps/{stepId}/"));
	}

	@DELETE
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_EDIT)
	public Boolean deleteTestCase(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_) throws Exception
	{
		testCaseService.deleteTestCase(testCaseId_);

		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/latestversion/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	/**
	 * Returns latest version of test case by default
	 */
	public TestCaseVersionInfo getLatestTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_) throws Exception
	{
		final TestCase testCase = testCaseService.getTestCase(testCaseId_);
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCase.getLatestVersion(), ui_.getBaseUriBuilder().path("/testcases/versions/{id}/"));
	}

	@GET
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	/**
	 * Returns latest version of test case by default
	 */
	public TestCaseInfo getTestCase(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_) throws Exception
	{
		final TestCase testCase = testCaseService.getTestCase(testCaseId_);
		return objectBuilderFactory.toInfo(TestCaseInfo.class, testCase, ui_.getAbsolutePathBuilder().path("/{id}"));
	}

	@GET
	@Path("/versions/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	/**
	 * Returns specified version of test case
	 */
	public TestCaseVersionInfo getTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_) throws Exception
	{
		final TestCaseVersion testCaseVersion = testCaseService.getTestCaseVersion(testCaseVersionId_);
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersion, ui_.getBaseUriBuilder().path("/testcases/versions/{id}/"));
	}

	@GET
	@Path("/versions/{id}/steps/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	/**
	 * Returns specified version of test case
	 */
	public List<TestCaseStepInfo> getTestCaseVersionSteps(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_) throws Exception
	{
		final List<TestCaseStep> steps = testCaseService.getTestCaseVersionSteps(testCaseVersionId_);
		return objectBuilderFactory.toInfo(TestCaseStepInfo.class, steps, ui_.getAbsolutePathBuilder().path("/{id}"));
	}

	@GET
	@Path("/versions/{id}/steps/{stepId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	/**
	 * Returns specified version of test case
	 */
	public TestCaseStepInfo getTestCaseVersionStep(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_, @PathParam("stepId") final Integer testCaseStepId_)
			throws Exception
	{
		final TestCaseStep step = testCaseService.getTestCaseStep(testCaseStepId_);

		return objectBuilderFactory.toInfo(TestCaseStepInfo.class, step, ui_.getAbsolutePathBuilder().path("/{id}"));
	}

	@GET
	@Path("/versions/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	/**
	 * Returns versions of test cases based on search parameters
	 */
	public TestCaseVersionResultInfo findTestCaseVersions(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(TestCaseVersionInfo.class, request_, ui_);
		final UtestSearchResult result = testCaseService.findTestCaseVersions(search);

		return (TestCaseVersionResultInfo) objectBuilderFactory.createResult(TestCaseVersionInfo.class, TestCaseVersion.class, request_, result, ui_.getBaseUriBuilder().path(
				"/versions/{id}/"));
	}

	@GET
	@Path("/latestversions/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	/**
	 * Returns latest versions of test cases by default
	 */
	public TestCaseVersionResultInfo findLatestTestCaseVersions(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(TestCaseVersionInfo.class, request_, ui_);
		final UtestSearchResult result = testCaseService.findLatestTestCaseVersions(search);

		return (TestCaseVersionResultInfo) objectBuilderFactory.createResult(TestCaseVersionInfo.class, TestCaseVersion.class, request_, result, ui_.getBaseUriBuilder().path(
				"/versions/{id}/"));
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	/**
	 * Returns latest versions of test cases by default
	 */
	public TestCaseResultInfo findTestCases(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(TestCaseInfo.class, request_, ui_);
		final UtestSearchResult result = testCaseService.findTestCases(search);

		return (TestCaseResultInfo) objectBuilderFactory.createResult(TestCaseInfo.class, TestCase.class, request_, result, ui_.getAbsolutePathBuilder().path("/{id}/"));
	}

	@GET
	@Path("/{id}/versions/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	/**
	 * Returns all versions of a test case
	 */
	public List<TestCaseVersionInfo> getTestCaseVersions(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_) throws Exception
	{
		final List<TestCaseVersion> testCaseVersions = testCaseService.getTestCaseVersions(testCaseId_);
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersions, ui_.getBaseUriBuilder().path("/testcases/versions/{id}/"));
	}
}