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
import java.util.Date;
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

import com.utest.domain.AccessRole;
import com.utest.domain.Attachment;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentGroupExploded;
import com.utest.domain.Permission;
import com.utest.domain.TestCycle;
import com.utest.domain.TestRun;
import com.utest.domain.User;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.TestCycleService;
import com.utest.domain.service.TestRunService;
import com.utest.domain.view.CategoryValue;
import com.utest.webservice.api.v2.TestCycleWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.AttachmentInfo;
import com.utest.webservice.model.v2.CategoryValueInfo;
import com.utest.webservice.model.v2.EnvironmentGroupExplodedInfo;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.RoleInfo;
import com.utest.webservice.model.v2.TestCycleInfo;
import com.utest.webservice.model.v2.TestCycleSearchResultInfo;
import com.utest.webservice.model.v2.TestRunInfo;
import com.utest.webservice.model.v2.UserInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

@Path("/testcycles/")
public class TestCycleWebServiceImpl extends BaseWebServiceImpl implements TestCycleWebService
{
	private final TestCycleService	testCycleService;
	private final TestRunService	testRunService;

	public TestCycleWebServiceImpl(final ObjectBuilderFactory objectBuildFactory, final TestCycleService testCycleService, final TestRunService testRunService)
	{
		super(objectBuildFactory);
		this.testCycleService = testCycleService;
		this.testRunService = testRunService;
	}

	@PUT
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CYCLE_EDIT })
	public TestCycleInfo updateTestCycle(@Context final UriInfo ui_, @PathParam("id") final Integer testCycleId_, @FormParam("name") final String name_,
			@FormParam("description") final String description_, @FormParam("productId") final Integer productId_,
			@FormParam("communityAuthoringAllowed") final String communityAuthoringAllowed_, @FormParam("communityAccessAllowed") final String communityAccessAllowed_,
			@FormParam("startDate") final Date startDate_, @FormParam("endDate") final Date endDate_, @FormParam("originalVersionId") final Integer originalVersionId_)

	throws Exception
	{

		final TestCycle testCycle = testCycleService.saveTestCycle(testCycleId_, name_, description_, startDate_, endDate_, "true".equalsIgnoreCase(communityAuthoringAllowed_),
				"true".equalsIgnoreCase(communityAccessAllowed_), originalVersionId_);
		return objectBuilderFactory.toInfo(TestCycleInfo.class, testCycle, ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/{id}/clone/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CYCLE_EDIT })
	public TestCycleInfo cloneTestCycle(@Context final UriInfo ui_, @PathParam("id") final Integer testCycleId_, @FormParam("cloneAssignments") final String cloneAssignments_)
			throws Exception
	{
		final TestCycle testCycle = testCycleService.cloneTestCycle(testCycleId_, "true".equalsIgnoreCase(cloneAssignments_));
		return objectBuilderFactory.toInfo(TestCycleInfo.class, testCycle, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/approveallresults/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_RESULT_APPROVE })
	public Boolean approveAllResultsForTestCycle(@Context final UriInfo ui_, @PathParam("id") final Integer testCycleId_) throws Exception
	{
		testCycleService.approveAllTestRunResultsForTestCycle(testCycleId_);
		return Boolean.TRUE;
	}

	@PUT
	@Path("/{id}/activate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CYCLE_ACTIVATE })
	public TestCycleInfo activateTestCycle(@Context final UriInfo ui_, @PathParam("id") final Integer testCycleId_, @FormParam("originalVersionId") final Integer originalVersionId_)
			throws Exception
	{

		final TestCycle testCycle = testCycleService.activateTestCycle(testCycleId_, originalVersionId_);
		return objectBuilderFactory.toInfo(TestCycleInfo.class, testCycle, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/deactivate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CYCLE_ACTIVATE })
	public TestCycleInfo deactivateTestCycle(@Context final UriInfo ui_, @PathParam("id") final Integer testCycleId_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{

		final TestCycle testCycle = testCycleService.lockTestCycle(testCycleId_, originalVersionId_);
		return objectBuilderFactory.toInfo(TestCycleInfo.class, testCycle, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/feature_begin/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.FEATURED_LIST_EDIT })
	public TestCycleInfo featureTestCycle(@Context final UriInfo ui_, @PathParam("id") final Integer testCycleId_, @FormParam("originalVersionId") final Integer originalVersionId_)
			throws Exception
	{

		final TestCycle testCycle = testCycleService.featureTestCycle(testCycleId_, originalVersionId_);
		return objectBuilderFactory.toInfo(TestCycleInfo.class, testCycle, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/feature_end/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.FEATURED_LIST_EDIT })
	public TestCycleInfo unfeatureTestCycle(@Context final UriInfo ui_, @PathParam("id") final Integer testCycleId_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{

		final TestCycle testCycle = testCycleService.featureTestCycle(testCycleId_, originalVersionId_);
		return objectBuilderFactory.toInfo(TestCycleInfo.class, testCycle, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CYCLE_EDIT })
	public Boolean updateTestCycleEnvironmentGroups(@Context final UriInfo ui_, @PathParam("id") final Integer testCycleId_,
			@FormParam("environmentGroupIds") final ArrayList<Integer> environmentGroupIds_, @FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{
		testCycleService.saveEnvironmentGroupsForTestCycle(testCycleId_, environmentGroupIds_, originalVesionId_);
		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/attachments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CYCLE_VIEW)
	public List<AttachmentInfo> getTestCycleAttachments(@Context UriInfo ui_, @PathParam("id") final Integer testCycleId) throws Exception
	{
		final List<Attachment> attachments = testCycleService.getAttachmentsForTestCycle(testCycleId);
		final List<AttachmentInfo> attachmentsInfo = objectBuilderFactory.toInfo(AttachmentInfo.class, attachments, ui_.getBaseUriBuilder());
		return attachmentsInfo;
	}

	@POST
	@Path("/{id}/attachments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CYCLE_EDIT })
	public AttachmentInfo createAttachment(@Context UriInfo ui, @PathParam("id") final Integer testCycleId, @FormParam("name") String name,
			@FormParam("description") String description, @FormParam("url") String url, @FormParam("size") Double size, @FormParam("attachmentTypeId") Integer attachmentTypeId)
			throws Exception
	{
		Attachment attachment = testCycleService.addAttachmentForTestCycle(name, description, url, size, testCycleId, attachmentTypeId);
		return objectBuilderFactory.toInfo(AttachmentInfo.class, attachment, ui.getBaseUriBuilder());
	}

	@DELETE
	@Path("/{id}/attachments/{attachmentId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CYCLE_EDIT })
	public Boolean deleteAttachment(@Context UriInfo ui, @PathParam("id") final Integer testCycleId, @PathParam("attachmentId") final Integer attachmentId,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		return testCycleService.deleteAttachment(attachmentId, testCycleId);
	}

	@GET
	@Path("/{id}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CYCLE_VIEW)
	public List<EnvironmentGroupInfo> getTestCycleEnvironmentGroups(@Context final UriInfo ui_, @PathParam("id") final Integer testCycleId_) throws Exception
	{
		final List<EnvironmentGroup> groups = testCycleService.getEnvironmentGroupsForTestCycle(testCycleId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, groups, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/environmentgroups/exploded/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CYCLE_VIEW)
	/**
	 * Returns all versions of a test case
	 */
	public List<EnvironmentGroupExplodedInfo> getTestCycleEnvironmentGroupsExploded(@Context final UriInfo ui_, @PathParam("id") final Integer productId_) throws Exception
	{
		final List<EnvironmentGroupExploded> groups = testCycleService.getEnvironmentGroupsExplodedForTestCycle(productId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupExplodedInfo.class, groups, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/testruns/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CYCLE_VIEW)
	public List<TestRunInfo> getTestCycleTestRuns(@Context final UriInfo ui_, @PathParam("id") final Integer testCycleId_) throws Exception
	{
		final List<TestRun> includedTestRuns = testCycleService.getTestRunsForTestCycle(testCycleId_);
		return objectBuilderFactory.toInfo(TestRunInfo.class, includedTestRuns, ui_.getBaseUriBuilder());
	}

	@POST
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CYCLE_EDIT })
	public TestCycleInfo createTestCycle(@Context final UriInfo ui_, @FormParam("name") final String name_, @FormParam("description") final String description_,
			@FormParam("productId") final Integer productId_, @FormParam("communityAuthoringAllowed") final String communityAuthoringAllowed_,
			@FormParam("communityAccessAllowed") final String communityAccessAllowed_, @FormParam("startDate") final Date startDate_, @FormParam("endDate") final Date endDate_)
			throws Exception
	{
		final TestCycle testCycle = testCycleService.addTestCycle(productId_, name_, description_, startDate_, endDate_, "true".equalsIgnoreCase(communityAuthoringAllowed_),
				"true".equalsIgnoreCase(communityAccessAllowed_));

		return objectBuilderFactory.toInfo(TestCycleInfo.class, testCycle, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CYCLE_EDIT)
	public Boolean deleteTestCycle(@Context final UriInfo ui_, @PathParam("id") final Integer testCycleId_, @FormParam("originalVersionId") final Integer originalVesionId_)
			throws Exception
	{
		testCycleService.deleteTestCycle(testCycleId_, originalVesionId_);

		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CYCLE_VIEW)
	public TestCycleInfo getTestCycle(@Context final UriInfo ui_, @PathParam("id") final Integer testCycleId_) throws Exception
	{
		final TestCycle testCycle = testCycleService.getTestCycle(testCycleId_);
		return objectBuilderFactory.toInfo(TestCycleInfo.class, testCycle, ui_.getBaseUriBuilder());
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CYCLE_VIEW)
	public TestCycleSearchResultInfo findTestCycles(@Context final UriInfo ui_, @QueryParam("includedEnvironmentId") final Integer includedEnvironmentId_,
			@QueryParam("teamMemberId") final Integer teamMemberId_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(TestCycleInfo.class, request_, ui_);
		final UtestSearchResult result = testCycleService.findTestCycles(search, teamMemberId_, includedEnvironmentId_);

		return (TestCycleSearchResultInfo) objectBuilderFactory.createResult(TestCycleInfo.class, TestCycle.class, request_, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/team/members/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CYCLE_VIEW)
	/**
	 * Returns all versions of a test case
	 */
	public List<UserInfo> getTestCycleTeamMembers(@Context final UriInfo ui_, @PathParam("id") final Integer testCycleId_) throws Exception
	{
		final List<User> users = testCycleService.getTestingTeamForTestCycle(testCycleId_);
		return objectBuilderFactory.toInfo(UserInfo.class, users, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/team/members/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CYCLE_EDIT })
	public Boolean updateTestCycleTeamMembers(@Context final UriInfo ui_, @PathParam("id") final Integer productId_, @FormParam("userIds") final ArrayList<Integer> userIds_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testCycleService.saveTestingTeamForTestCycle(productId_, userIds_, originalVersionId_);
		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/team/members/{userId}/roles/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CYCLE_VIEW)
	/**
	 * Returns all versions of a test case
	 */
	public List<RoleInfo> getTestCycleTeamMemberRoles(@Context final UriInfo ui_, @PathParam("id") final Integer productId_, @PathParam("userId") final Integer userId_)
			throws Exception
	{
		final List<AccessRole> roles = testCycleService.getTestingTeamMemberRolesForTestCycle(productId_, userId_);
		return objectBuilderFactory.toInfo(RoleInfo.class, roles, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/team/members/{userId}/roles/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CYCLE_EDIT })
	public Boolean updateTestCycleTeamMemberRoles(@Context final UriInfo ui_, @PathParam("id") final Integer productId_, @PathParam("userId") final Integer userId_,
			@FormParam("roleIds") final ArrayList<Integer> roleIds_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testCycleService.saveTestingTeamMemberRolesForTestCycle(productId_, userId_, roleIds_, originalVersionId_);
		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/reports/coverage/resultstatus")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CYCLE_VIEW)
	public List<CategoryValueInfo> getCoverageByResultStatus(@Context final UriInfo ui_, @PathParam("id") final Integer testCycleId_,
			@FormParam("testSuiteId") final Integer testSuiteId_, @FormParam("testRunId") final Integer testRunId_) throws Exception
	{
		final List<CategoryValue> results = testRunService.getCoverageByStatus(testRunId_, testCycleId_, testSuiteId_);
		return objectBuilderFactory.toInfo(CategoryValueInfo.class, results, ui_.getBaseUriBuilder());
	}

}
