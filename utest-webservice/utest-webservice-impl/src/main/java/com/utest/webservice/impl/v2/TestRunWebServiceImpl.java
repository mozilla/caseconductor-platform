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
import com.utest.domain.Environment;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentGroupExploded;
import com.utest.domain.Permission;
import com.utest.domain.ProductComponent;
import com.utest.domain.TestRun;
import com.utest.domain.TestRunResult;
import com.utest.domain.TestRunTestCase;
import com.utest.domain.TestRunTestCaseAssignment;
import com.utest.domain.TestSuite;
import com.utest.domain.User;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.TestRunService;
import com.utest.domain.view.CategoryValue;
import com.utest.domain.view.TestRunTestCaseView;
import com.utest.webservice.api.v2.TestRunWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.AttachmentInfo;
import com.utest.webservice.model.v2.CategoryValueInfo;
import com.utest.webservice.model.v2.EnvironmentGroupExplodedInfo;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.EnvironmentInfo;
import com.utest.webservice.model.v2.TestRunTestCaseInfo;
import com.utest.webservice.model.v2.TestRunTestCaseSearchResultInfo;
import com.utest.webservice.model.v2.ProductComponentInfo;
import com.utest.webservice.model.v2.RoleInfo;
import com.utest.webservice.model.v2.TestRunInfo;
import com.utest.webservice.model.v2.TestRunResultInfo;
import com.utest.webservice.model.v2.TestRunResultSearchResultInfo;
import com.utest.webservice.model.v2.TestRunSearchResultInfo;
import com.utest.webservice.model.v2.TestRunTestCaseAssignmentInfo;
import com.utest.webservice.model.v2.TestRunTestCaseAssignmentSearchResultInfo;
import com.utest.webservice.model.v2.TestSuiteInfo;
import com.utest.webservice.model.v2.UserInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

@Path("/testruns/")
public class TestRunWebServiceImpl extends BaseWebServiceImpl implements TestRunWebService
{
	private final TestRunService	testRunService;

	public TestRunWebServiceImpl(final ObjectBuilderFactory objectBuildFactory, final TestRunService testRunService)
	{
		super(objectBuildFactory);
		this.testRunService = testRunService;
	}

	// /////////// TEST RUN RELATED //////////////

	@PUT
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public TestRunInfo updateTestRun(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_, @FormParam("name") final String name_,
			@FormParam("description") final String description_, @FormParam("useLatestVersions") final String useLatestVersions_,
			@FormParam("selfAssignAllowed") final String selfAssignAllowed_, @FormParam("selfAssignPerEnvironment") final String selfAssignPerEnvironment_,
			@FormParam("selfAssignLimit") final Integer selfAssignLimit_, @FormParam("startDate") final Date startDate_, @FormParam("endDate") final Date endDate_,
			@FormParam("originalVersionId") final Integer originalVersionId_, @FormParam("autoAssignToTeam") final String autoAssignToTeam_) throws Exception
	{

		final TestRun testRun = testRunService.saveTestRun(testRunId_, "true".equalsIgnoreCase(useLatestVersions_), name_, description_, startDate_, endDate_, "true"
				.equalsIgnoreCase(selfAssignAllowed_), "true".equalsIgnoreCase(selfAssignPerEnvironment_), selfAssignLimit_, originalVersionId_, "true"
				.equalsIgnoreCase(autoAssignToTeam_));

		return objectBuilderFactory.toInfo(TestRunInfo.class, testRun, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/activate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_ACTIVATE })
	public TestRunInfo activateTestRun(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_, @FormParam("originalVersionId") final Integer originalVersionId_)
			throws Exception
	{

		final TestRun testRun = testRunService.activateTestRun(testRunId_, originalVersionId_);
		return objectBuilderFactory.toInfo(TestRunInfo.class, testRun, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/approveallresults/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_RESULT_APPROVE })
	public Boolean approveAllResultsForTestRun(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_) throws Exception
	{
		testRunService.approveAllTestRunResultsForTestRun(testRunId_);
		return Boolean.TRUE;
	}

	@PUT
	@Path("/{id}/approvetestcaseresults/{testCaseId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_RESULT_APPROVE })
	public Boolean approveAllResultsForTestRunTestCase(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_, @PathParam("testCaseId") final Integer testCaseId_)
			throws Exception
	{
		testRunService.approveAllTestRunResultsForTestRunTestCase(testRunId_, testCaseId_);
		return Boolean.TRUE;
	}

	@PUT
	@Path("/{id}/deactivate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_ACTIVATE })
	public TestRunInfo deactivateTestRun(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_, @FormParam("originalVersionId") final Integer originalVersionId_)
			throws Exception
	{

		final TestRun testRun = testRunService.lockTestRun(testRunId_, originalVersionId_);
		return objectBuilderFactory.toInfo(TestRunInfo.class, testRun, ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/{id}/retest/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public List<TestRunResultInfo> retestTestRun(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_,
			@FormParam("failedResultsOnly") final String failedResultsOnly_) throws Exception
	{

		final List<TestRunResult> results = testRunService.retestTestRun(testRunId_, "true".equalsIgnoreCase(failedResultsOnly_));
		return objectBuilderFactory.toInfo(TestRunResultInfo.class, results, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public Boolean updateTestRunEnvironmentGroups(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_,
			@FormParam("environmentGroupIds") final ArrayList<Integer> environmentGroupIds_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testRunService.saveEnvironmentGroupsForTestRun(testRunId_, environmentGroupIds_, originalVersionId_);
		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/attachments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public List<AttachmentInfo> getTestRunAttachments(@Context UriInfo ui_, @PathParam("id") final Integer testRunId_) throws Exception
	{
		final List<Attachment> attachments = testRunService.getAttachmentsForTestRun(testRunId_);
		final List<AttachmentInfo> attachmentsInfo = objectBuilderFactory.toInfo(AttachmentInfo.class, attachments, ui_.getBaseUriBuilder());
		return attachmentsInfo;
	}

	@POST
	@Path("/{id}/attachments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT, Permission.TEST_CASE_ADD })
	public AttachmentInfo createAttachment(@Context UriInfo ui, @PathParam("id") final Integer testRunId, @FormParam("name") String name,
			@FormParam("description") String description, @FormParam("url") String url, @FormParam("size") Double size, @FormParam("attachmentTypeId") Integer attachmentTypeId)
			throws Exception
	{
		Attachment attachment = testRunService.addAttachmentForTestRun(name, description, url, size, testRunId, attachmentTypeId);
		return objectBuilderFactory.toInfo(AttachmentInfo.class, attachment, ui.getBaseUriBuilder());
	}

	@DELETE
	@Path("/{id}/attachments/{attachmentId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public Boolean deleteAttachment(@Context UriInfo ui, @PathParam("id") final Integer testRunId, @PathParam("attachmentId") final Integer attachmentId,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testRunService.deleteAttachmentForTestRun(attachmentId, testRunId);
		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public List<EnvironmentGroupInfo> getTestRunEnvironmentGroups(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_) throws Exception
	{
		final List<EnvironmentGroup> groups = testRunService.getEnvironmentGroupsForTestRun(testRunId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, groups, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/environmentgroups/exploded/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public List<EnvironmentGroupExplodedInfo> getTestRunEnvironmentGroupsExploded(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_) throws Exception
	{
		final List<EnvironmentGroupExploded> groups = testRunService.getEnvironmentGroupsExplodedForTestRun(testRunId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupExplodedInfo.class, groups, ui_.getBaseUriBuilder());
	}

	@POST
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public TestRunInfo createTestRun(@Context final UriInfo ui_, @FormParam("testCycleId") final Integer testCycleId_, @FormParam("name") final String name_,
			@FormParam("description") final String description_, @FormParam("selfAssignAllowed") final String selfAssignAllowed_,
			@FormParam("selfAssignPerEnvironment") final String selfAssignPerEnvironment_, @FormParam("selfAssignLimit") final Integer selfAssignLimit_,
			@FormParam("startDate") final Date startDate_, @FormParam("endDate") final Date endDate_, @FormParam("useLatestVersions") final String useLatestVersions_,
			@FormParam("autoAssignToTeam") final String autoAssignToTeam_) throws Exception
	{
		final TestRun testRun = testRunService.addTestRun(testCycleId_, "true".equalsIgnoreCase(useLatestVersions_), name_, description_, startDate_, endDate_, "true"
				.equalsIgnoreCase(selfAssignAllowed_), "true".equalsIgnoreCase(selfAssignPerEnvironment_), selfAssignLimit_, "true".equalsIgnoreCase(autoAssignToTeam_));
		return objectBuilderFactory.toInfo(TestRunInfo.class, testRun, ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/{id}/clone/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public TestRunInfo cloneTestRun(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_, @FormParam("cloneAssignments") final String cloneAssignments_)
			throws Exception
	{
		final TestRun testRun = testRunService.cloneTestRun(testRunId_, "true".equalsIgnoreCase(cloneAssignments_));
		return objectBuilderFactory.toInfo(TestRunInfo.class, testRun, ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/{id}/clone/{targetTestCycleId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public TestRunInfo cloneTestRun(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_, @PathParam("targetTestCycleId") final Integer targetTestCycleId_,
			@FormParam("cloneAssignments") final String cloneAssignments_) throws Exception
	{
		final TestRun testRun = testRunService.cloneTestRun(testRunId_, targetTestCycleId_, "true".equalsIgnoreCase(cloneAssignments_));
		return objectBuilderFactory.toInfo(TestRunInfo.class, testRun, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_EDIT)
	public Boolean deleteTestRun(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_, @FormParam("originalVersionId") final Integer originalVersionId_)
			throws Exception
	{
		testRunService.deleteTestRun(testRunId_, originalVersionId_);

		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public TestRunInfo getTestRun(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_) throws Exception
	{
		final TestRun testRun = testRunService.getTestRun(testRunId_);
		return objectBuilderFactory.toInfo(TestRunInfo.class, testRun, ui_.getBaseUriBuilder());
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public TestRunSearchResultInfo findTestRuns(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(TestRunInfo.class, request_, ui_);
		final UtestSearchResult result = testRunService.findTestRuns(search);

		return (TestRunSearchResultInfo) objectBuilderFactory.createResult(TestRunInfo.class, TestRun.class, request_, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/components/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public List<ProductComponentInfo> getTestRunComponents(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_) throws Exception
	{
		final List<ProductComponent> components = testRunService.getTestRunComponents(testRunId_);
		return objectBuilderFactory.toInfo(ProductComponentInfo.class, components, ui_.getBaseUriBuilder());
	}

	// /////////// TEST RUN TEST CASES RELATED //////////////

	@GET
	@Path("/{id}/testsuites/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public List<TestSuiteInfo> getTestRunTestSuites(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_) throws Exception
	{
		final List<TestSuite> includedTestSuites = testRunService.getTestRunTestSuites(testRunId_);
		return objectBuilderFactory.toInfo(TestSuiteInfo.class, includedTestSuites, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/includedtestcases/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public List<TestRunTestCaseInfo> getTestRunTestCases(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_) throws Exception
	{
		final List<TestRunTestCaseView> includedTestCases = testRunService.getTestRunTestCasesViews(testRunId_);
		return objectBuilderFactory.toInfo(TestRunTestCaseInfo.class, includedTestCases, ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/{id}/includedtestcases/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public TestRunTestCaseInfo createTestRunTestCase(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_,
			@FormParam("testCaseVersionId") final Integer testCaseVersionId_, @FormParam("priorityId") final Integer priorityId_, @FormParam("runOrder") final Integer runOrder_,
			@FormParam("blocking") final String blocking_) throws Exception
	{

		final TestRunTestCase includedTestCase = testRunService.addTestRunTestCase(testRunId_, testCaseVersionId_, priorityId_, runOrder_, "true".equalsIgnoreCase(blocking_));

		return objectBuilderFactory.toInfo(TestRunTestCaseInfo.class, testRunService.getTestRunTestCaseView(includedTestCase.getId()), ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/{id}/includedtestcases/testplan/{testPlanId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public List<TestRunTestCaseInfo> createTestCasesFromTestPlan(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_,
			@PathParam("testPlanId") final Integer testPlanId_) throws Exception
	{
		testRunService.addTestCasesFromTestPlan(testRunId_, testPlanId_);
		return objectBuilderFactory.toInfo(TestRunTestCaseInfo.class, testRunService.getTestRunTestCasesViews(testRunId_), ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/{id}/includedtestcases/testsuite/{testSuiteId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public List<TestRunTestCaseInfo> createTestCasesFromTestSuite(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_,
			@PathParam("testSuiteId") final Integer testSuiteId_) throws Exception
	{
		testRunService.addTestCasesFromTestSuite(testRunId_, testSuiteId_);
		return objectBuilderFactory.toInfo(TestRunTestCaseInfo.class, testRunService.getTestRunTestCasesViews(testRunId_), ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/includedtestcases/{includedTestCaseId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public TestRunTestCaseInfo getTestRunTestCase(@Context final UriInfo ui_, @PathParam("includedTestCaseId") final Integer includedTestCaseId_) throws Exception
	{
		final TestRunTestCaseView includedTestCase = testRunService.getTestRunTestCaseView(includedTestCaseId_);
		return objectBuilderFactory.toInfo(TestRunTestCaseInfo.class, includedTestCase, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/includedtestcases/{includedTestCaseId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_EDIT)
	public Boolean deleteTestRunTestCase(@Context final UriInfo ui_, @PathParam("includedTestCaseId") final Integer includedTestCaseId_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testRunService.deleteTestRunTestCase(includedTestCaseId_, originalVersionId_);

		return Boolean.TRUE;
	}

	@PUT
	@Path("/includedtestcases/{includedTestCaseId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public TestRunTestCaseInfo updateTestRunTestCase(@Context final UriInfo ui_, @PathParam("includedTestCaseId") final Integer includedTestCaseId_,
			@FormParam("testCaseVersionId") final Integer testCaseVersionId_, @FormParam("priorityId") final Integer priorityId_, @FormParam("runOrder") final Integer runOrder_,
			@FormParam("blocking") final String blocking_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{

		final TestRunTestCase includedTestCase = testRunService.saveTestRunTestCase(includedTestCaseId_, priorityId_, runOrder_, "true".equalsIgnoreCase(blocking_),
				originalVersionId_);

		return objectBuilderFactory.toInfo(TestRunTestCaseInfo.class, testRunService.getTestRunTestCaseView(includedTestCase.getId()), ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/includedtestcases/{includedTestCaseId}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public Boolean updateTestRunTestCaseEnvironmentGroups(@Context final UriInfo ui_, @PathParam("includedTestCaseId") final Integer includedTestCaseId_,
			@FormParam("environmentGroupIds") final ArrayList<Integer> environmentGroupIds_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testRunService.saveEnvironmentGroupsForTestRunTestCase(includedTestCaseId_, environmentGroupIds_, originalVersionId_);
		return Boolean.TRUE;
	}

	@GET
	@Path("/includedtestcases/{includedTestCaseId}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public List<EnvironmentGroupInfo> getTestRunTestCaseEnvironmentGroups(@Context final UriInfo ui_, @PathParam("includedTestCaseId") final Integer includedTestCaseId_)
			throws Exception
	{
		final List<EnvironmentGroup> groups = testRunService.getEnvironmentGroupsForTestRunTestCase(includedTestCaseId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, groups, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/includedtestcases/{includedTestCaseId}/environmentgroups/exploded/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public List<EnvironmentGroupExplodedInfo> getTestRunTestCaseEnvironmentGroupsExploded(@Context final UriInfo ui_,
			@PathParam("includedTestCaseId") final Integer includedTestCaseId_) throws Exception
	{
		final List<EnvironmentGroupExploded> groups = testRunService.getEnvironmentGroupsExplodedForTestRunTestCase(includedTestCaseId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupExplodedInfo.class, groups, ui_.getBaseUriBuilder());
	}

	// /////////// TEST CASES ASSIGNMENTS RELATED //////////////

	@GET
	@Path("/includedtestcases/{includedTestCaseId}/assignments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public List<TestRunTestCaseAssignmentInfo> getTestRunTestCaseAssignments(@Context final UriInfo ui_, @PathParam("includedTestCaseId") final Integer includedTestCaseId_)
			throws Exception
	{
		final List<TestRunTestCaseAssignment> assignments = testRunService.getTestRunTestCaseAssignments(includedTestCaseId_);
		return objectBuilderFactory.toInfo(TestRunTestCaseAssignmentInfo.class, assignments, ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/includedtestcases/{includedTestCaseId}/assignments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_TEST_CASE_ASSIGN, Permission.TEST_RUN_TEST_CASE_SELF_ASSIGN })
	public TestRunTestCaseAssignmentInfo createTestRunTestCaseAssignment(@Context final UriInfo ui_, @PathParam("includedTestCaseId") final Integer includedTestCaseId_,
			@FormParam("testerId") final Integer testerId_) throws Exception
	{

		final TestRunTestCaseAssignment assignment = testRunService.addAssignment(includedTestCaseId_, testerId_);
		return objectBuilderFactory.toInfo(TestRunTestCaseAssignmentInfo.class, assignment, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/assignments/{assignmentId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public TestRunTestCaseAssignmentInfo getTestRunTestCaseAssignment(@Context final UriInfo ui_, @PathParam("assignmentId") final Integer assignmentId_) throws Exception
	{
		final TestRunTestCaseAssignment assignment = testRunService.getTestRunTestCaseAssignment(assignmentId_);
		return objectBuilderFactory.toInfo(TestRunTestCaseAssignmentInfo.class, assignment, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/assignments/{assignmentId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_TEST_CASE_ASSIGN)
	public Boolean deleteTestRunTestCaseAssignment(@Context final UriInfo ui_, @PathParam("assignmentId") final Integer assignmentId_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testRunService.deleteAssignment(assignmentId_, originalVersionId_);
		return Boolean.TRUE;
	}

	@PUT
	@Path("/assignments/{assignmentId}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_TEST_CASE_ASSIGN })
	public Boolean updateTestRunTestCaseAssignmentEnvironmentGroups(@Context final UriInfo ui_, @PathParam("assignmentId") final Integer assignmentId_,
			@FormParam("environmentGroupIds") final ArrayList<Integer> environmentGroupIds_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testRunService.saveEnvironmentGroupsForAssignment(assignmentId_, environmentGroupIds_, originalVersionId_);
		return Boolean.TRUE;
	}

	@GET
	@Path("/assignments/{assignmentId}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public List<EnvironmentGroupInfo> getTestRunTestCaseAssignmentEnvironmentGroups(@Context final UriInfo ui_, @PathParam("assignmentId") final Integer assignmentId_)
			throws Exception
	{
		final List<EnvironmentGroup> groups = testRunService.getEnvironmentGroupsForAssignment(assignmentId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, groups, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/assignments/{assignmentId}/environmentgroups/exploded/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public List<EnvironmentGroupExplodedInfo> getTestRunTestCaseAssignmentEnvironmentGroupsExploded(@Context final UriInfo ui_,
			@PathParam("assignmentId") final Integer assignmentId_) throws Exception
	{
		final List<EnvironmentGroupExploded> groups = testRunService.getEnvironmentGroupsExplodedForAssignment(assignmentId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupExplodedInfo.class, groups, ui_.getBaseUriBuilder());
	}

	// /////////// TEST CASES ASSIGNMENT RESULTS RELATED //////////////

	@GET
	@Path("/assignments/{assignmentId}/results/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public List<TestRunResultInfo> getTestRunTestCaseAssignmentResults(@Context final UriInfo ui_, @PathParam("assignmentId") final Integer assignmentId_) throws Exception
	{
		final List<TestRunResult> results = testRunService.getTestRunResultsForAssignment(assignmentId_);
		return objectBuilderFactory.toInfo(TestRunResultInfo.class, results, ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/results/{resultId}/retest/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public TestRunResultInfo retestTestRunResult(@Context final UriInfo ui_, @PathParam("resultId") final Integer resultId_, @FormParam("testerId") final Integer testerId_)
			throws Exception
	{

		final TestRunResult result = testRunService.retestTestRunResult(resultId_, testerId_);
		return objectBuilderFactory.toInfo(TestRunResultInfo.class, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/results/{resultId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public TestRunResultInfo getTestRunResult(@Context final UriInfo ui_, @PathParam("resultId") final Integer resultId_) throws Exception
	{
		final TestRunResult result = testRunService.getTestRunResult(resultId_);
		return objectBuilderFactory.toInfo(TestRunResultInfo.class, result, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/results/{resultId}/start/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_ASSIGNMENT_EXECUTE })
	public TestRunResultInfo startTestRunResultExecution(@Context final UriInfo ui_, @PathParam("resultId") final Integer resultId_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testRunService.startExecutingAssignedTestCase(resultId_, originalVersionId_);
		return getTestRunResult(ui_, resultId_);
	}

	@PUT
	@Path("/results/{resultId}/finishfail/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_ASSIGNMENT_EXECUTE })
	public TestRunResultInfo finishFailedTestRunResultExecution(@Context final UriInfo ui_, @PathParam("resultId") final Integer resultId_,
			@FormParam("failedStepNumber") final Integer failedStepNumber_, @FormParam("actualResult") final String actualResult_, @FormParam("comment") final String comment_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testRunService.finishExecutingAssignedTestCaseWithFailure(resultId_, failedStepNumber_, actualResult_, comment_, originalVersionId_);
		return getTestRunResult(ui_, resultId_);
	}

	@PUT
	@Path("/results/{resultId}/finishsucceed/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_ASSIGNMENT_EXECUTE })
	public TestRunResultInfo finishSuccessfulTestRunResultExecution(@Context final UriInfo ui_, @PathParam("resultId") final Integer resultId_,
			@FormParam("comment") final String comment_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testRunService.finishExecutingAssignedTestCaseWithSuccess(resultId_, comment_, originalVersionId_);
		return getTestRunResult(ui_, resultId_);
	}

	@PUT
	@Path("/results/{resultId}/finishinvalidate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_ASSIGNMENT_EXECUTE })
	public TestRunResultInfo finishInvalidatedTestRunResultExecution(@Context final UriInfo ui_, @PathParam("resultId") final Integer resultId_,
			@FormParam("comment") final String comment_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testRunService.finishExecutingAssignedTestCaseWithInvalidation(resultId_, comment_, originalVersionId_);
		return getTestRunResult(ui_, resultId_);
	}

	@PUT
	@Path("/results/{resultId}/finishskip/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_ASSIGNMENT_EXECUTE })
	public TestRunResultInfo finishSkippedTestRunResultExecution(@Context final UriInfo ui_, @PathParam("resultId") final Integer resultId_,
			@FormParam("comment") final String comment_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testRunService.finishExecutingAssignedTestCaseWithSkip(resultId_, comment_, originalVersionId_);
		return getTestRunResult(ui_, resultId_);
	}

	@PUT
	@Path("/results/{resultId}/approve/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_RESULT_APPROVE })
	public TestRunResultInfo approveTestRunResult(@Context final UriInfo ui_, @PathParam("resultId") final Integer resultId_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testRunService.approveTestRunResult(resultId_, originalVersionId_);
		return getTestRunResult(ui_, resultId_);
	}

	@PUT
	@Path("/results/{resultId}/reject/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_RESULT_APPROVE })
	public TestRunResultInfo rejectTestRunResult(@Context final UriInfo ui_, @PathParam("resultId") final Integer resultId_, @FormParam("comment") final String comment_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testRunService.rejectTestRunResult(resultId_, comment_, originalVersionId_);
		return getTestRunResult(ui_, resultId_);
	}

	@GET
	@Path("/results/{id}/attachments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public List<AttachmentInfo> getTestRunResultAttachments(@Context UriInfo ui_, @PathParam("id") final Integer testRunResultId) throws Exception
	{
		final List<Attachment> attachments = testRunService.getAttachmentsForTestRunResult(testRunResultId);
		final List<AttachmentInfo> attachmentsInfo = objectBuilderFactory.toInfo(AttachmentInfo.class, attachments, ui_.getBaseUriBuilder());
		return attachmentsInfo;
	}

	@POST
	@Path("/results/{id}/attachments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public AttachmentInfo createAttachmentForTestRunResult(@Context UriInfo ui, @PathParam("id") final Integer testRunResultId, @FormParam("name") String name,
			@FormParam("description") String description, @FormParam("url") String url, @FormParam("size") Double size, @FormParam("attachmentTypeId") Integer attachmentTypeId)
			throws Exception
	{
		Attachment attachment = testRunService.addAttachmentForTestRunResult(name, description, url, size, testRunResultId, attachmentTypeId);
		return objectBuilderFactory.toInfo(AttachmentInfo.class, attachment, ui.getBaseUriBuilder());
	}

	@DELETE
	@Path("/results/{id}/attachments/{attachmentId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public Boolean deleteAttachmentForTestRunResult(@Context UriInfo ui, @PathParam("id") final Integer testRunResultId, @PathParam("attachmentId") final Integer attachmentId,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testRunService.deleteAttachmentForTestRunResult(attachmentId, testRunResultId);
		return Boolean.TRUE;
	}

	@GET
	@Path("/results/{resultId}/environments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public List<EnvironmentInfo> getTestRunResultEnvironments(@Context final UriInfo ui_, @PathParam("resultId") final Integer resultId_) throws Exception
	{
		final List<Environment> environments = testRunService.getEnvironmentsForTestResult(resultId_);
		return objectBuilderFactory.toInfo(EnvironmentInfo.class, environments, ui_.getBaseUriBuilder());
	}

	// /////// TEST RUN COVERAGE RELATED //////////////

	@GET
	@Path("/includedtestcases/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public TestRunTestCaseSearchResultInfo findTestRunTestCases(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(TestRunTestCaseInfo.class, request_, ui_);
		final UtestSearchResult result = testRunService.findTestRunTestCases(search);

		return (TestRunTestCaseSearchResultInfo) objectBuilderFactory.createResult(TestRunTestCaseInfo.class, TestRunTestCase.class, request_, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/assignments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public TestRunTestCaseAssignmentSearchResultInfo findTestRunAssignments(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(TestRunTestCaseAssignmentInfo.class, request_, ui_);
		final UtestSearchResult result = testRunService.findTestRunAssignments(search);

		return (TestRunTestCaseAssignmentSearchResultInfo) objectBuilderFactory.createResult(TestRunTestCaseAssignmentInfo.class, TestRunTestCaseAssignment.class, request_,
				result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/results/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	public TestRunResultSearchResultInfo findTestRunResults(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(TestRunResultInfo.class, request_, ui_);
		final UtestSearchResult result = testRunService.findTestRunResults(search);
		return (TestRunResultSearchResultInfo) objectBuilderFactory.createResult(TestRunResultInfo.class, TestRunResult.class, request_, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/team/members/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	/**
	 * Returns all versions of a test case
	 */
	public List<UserInfo> getTestRunTeamMembers(@Context final UriInfo ui_, @PathParam("id") final Integer testCycleId_) throws Exception
	{
		final List<User> users = testRunService.getTestingTeamForTestRun(testCycleId_);
		return objectBuilderFactory.toInfo(UserInfo.class, users, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/team/members/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public Boolean updateTestRunTeamMembers(@Context final UriInfo ui_, @PathParam("id") final Integer productId_, @FormParam("userIds") final ArrayList<Integer> userIds_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testRunService.saveTestingTeamForTestRun(productId_, userIds_, originalVersionId_);
		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/team/members/{userId}/roles/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_RUN_VIEW)
	/**
	 * Returns all versions of a test case
	 */
	public List<RoleInfo> getTestRunTeamMemberRoles(@Context final UriInfo ui_, @PathParam("id") final Integer productId_, @PathParam("userId") final Integer userId_)
			throws Exception
	{
		final List<AccessRole> roles = testRunService.getTestingTeamMemberRolesForTestRun(productId_, userId_);
		return objectBuilderFactory.toInfo(RoleInfo.class, roles, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/team/members/{userId}/roles/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_RUN_EDIT })
	public Boolean updateTestRunTeamMemberRoles(@Context final UriInfo ui_, @PathParam("id") final Integer productId_, @PathParam("userId") final Integer userId_,
			@FormParam("roleIds") final ArrayList<Integer> roleIds_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		testRunService.saveTestingTeamMemberRolesForTestRun(productId_, userId_, roleIds_, originalVersionId_);
		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/reports/coverage/resultstatus")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CYCLE_VIEW)
	public List<CategoryValueInfo> getCoverageByResultStatus(@Context final UriInfo ui_, @PathParam("id") final Integer testRunId_) throws Exception
	{
		final List<CategoryValue> results = testRunService.getCoverageByStatus(testRunId_);
		return objectBuilderFactory.toInfo(CategoryValueInfo.class, results, ui_.getBaseUriBuilder());
	}

}
