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

import com.utest.domain.Attachment;
import com.utest.domain.EntityExternalBug;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentGroupExploded;
import com.utest.domain.Permission;
import com.utest.domain.ProductComponent;
import com.utest.domain.Tag;
import com.utest.domain.TestCase;
import com.utest.domain.TestCaseStep;
import com.utest.domain.TestCaseVersion;
import com.utest.domain.VersionIncrement;
import com.utest.domain.search.UtestFilter;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.TestCaseService;
import com.utest.domain.view.TestCaseVersionView;
import com.utest.webservice.api.v2.TestCaseWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.AttachmentInfo;
import com.utest.webservice.model.v2.EntityExternalBugInfo;
import com.utest.webservice.model.v2.EnvironmentGroupExplodedInfo;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.ProductComponentInfo;
import com.utest.webservice.model.v2.TagInfo;
import com.utest.webservice.model.v2.TestCaseInfo;
import com.utest.webservice.model.v2.TestCaseSearchResultInfo;
import com.utest.webservice.model.v2.TestCaseStepInfo;
import com.utest.webservice.model.v2.TestCaseVersionInfo;
import com.utest.webservice.model.v2.TestCaseVersionSearchResultInfo;
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
	@Secured( { Permission.TEST_CASE_ADD, Permission.TEST_CASE_EDIT })
	public TestCaseVersionInfo updateTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_,
			@PathParam("increment") final String versionIncrement_, @FormParam("description") final String description_,
			@FormParam("originalVersionId") final Integer originalVersionId_, @FormParam("automated") final String automated_,
			@FormParam("automationUri") final String automationUri_) throws Exception
	{

		final TestCaseVersion testCaseVersion = testCaseService.saveTestCaseVersion(testCaseVersionId_, description_, "TRUE".equalsIgnoreCase(automated_), automationUri_,
				originalVersionId_, VersionIncrement.valueOf(versionIncrement_));
		final TestCaseVersionView testCaseVersionView = testCaseService.getTestCaseVersionView(testCaseVersion.getId());
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersionView, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/versions/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ADD, Permission.TEST_CASE_EDIT })
	public TestCaseVersionInfo updateTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_,
			@FormParam("description") final String description_, @FormParam("originalVersionId") final Integer originalVersionId_, @FormParam("automated") final String automated_,
			@FormParam("automationUri") final String automationUri_) throws Exception
	{

		final TestCaseVersion testCaseVersion = testCaseService.saveTestCaseVersion(testCaseVersionId_, description_, "TRUE".equalsIgnoreCase(automated_), automationUri_,
				originalVersionId_, VersionIncrement.NONE);
		final TestCaseVersionView testCaseVersionView = testCaseService.getTestCaseVersionView(testCaseVersion.getId());
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersionView, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/versions/{id}/approve/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_APPROVE })
	public TestCaseVersionInfo approveTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{

		final TestCaseVersion testCaseVersion = testCaseService.approveTestCaseVersion(testCaseVersionId_, originalVersionId_);
		final TestCaseVersionView testCaseVersionView = testCaseService.getTestCaseVersionView(testCaseVersion.getId());
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersionView, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/versions/{id}/reject/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_APPROVE })
	public TestCaseVersionInfo rejectTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{

		final TestCaseVersion testCaseVersion = testCaseService.rejectTestCaseVersion(testCaseVersionId_, originalVersionId_);
		final TestCaseVersionView testCaseVersionView = testCaseService.getTestCaseVersionView(testCaseVersion.getId());
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersionView, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("versions/{id}/activate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ACTIVATE })
	public TestCaseVersionInfo activateTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{

		final TestCaseVersion testCaseVersion = testCaseService.activateTestCaseVersion(testCaseVersionId_, originalVersionId_);
		final TestCaseVersionView testCaseVersionView = testCaseService.getTestCaseVersionView(testCaseVersion.getId());
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersionView, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("versions/{id}/deactivate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ACTIVATE })
	public TestCaseVersionInfo deactivateTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{

		final TestCaseVersion testCaseVersion = testCaseService.lockTestCaseVersion(testCaseVersionId_, originalVersionId_);
		final TestCaseVersionView testCaseVersionView = testCaseService.getTestCaseVersionView(testCaseVersion.getId());
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersionView, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ADD, Permission.TEST_CASE_EDIT })
	public TestCaseInfo updateTestCase(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_,
			@FormParam("maxAttachmentSizeInMbytes") final Integer maxAttachmentSizeInMbytes_, @FormParam("maxNumberOfAttachments") final Integer maxNumberOfAttachments_,
			@FormParam("name") final String name_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		final TestCase testCase = testCaseService.saveTestCase(testCaseId_, name_, maxAttachmentSizeInMbytes_, maxNumberOfAttachments_, originalVersionId_);
		return objectBuilderFactory.toInfo(TestCaseInfo.class, testCase, ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/{id}/clone/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ADD, Permission.TEST_CASE_EDIT })
	public TestCaseVersionInfo cloneTestCase(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_) throws Exception
	{
		final TestCase testCase = testCaseService.cloneTestCase(testCaseId_);
		final TestCaseVersionView testCaseVersionView = testCaseService.getTestCaseVersionView(testCase.getLatestVersion().getId());
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersionView, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/components/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ADD, Permission.TEST_CASE_EDIT })
	public Boolean updateTestCaseComponents(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_,
			@FormParam("productComponentIds") final ArrayList<Integer> productComponentIds_) throws Exception
	{
		testCaseService.saveProductComponentsForTestCase(testCaseId_, productComponentIds_, 0);
		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/components/")
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
		return objectBuilderFactory.toInfo(ProductComponentInfo.class, components, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/relatedbugs/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	public List<EntityExternalBugInfo> getTestCaseBugs(@Context UriInfo ui_, @PathParam("id") final Integer testCaseId_) throws Exception
	{
		final List<EntityExternalBug> bugs = testCaseService.getExternalBugsForTestCase(testCaseId_);
		final List<EntityExternalBugInfo> bugsInfo = objectBuilderFactory.toInfo(EntityExternalBugInfo.class, bugs, ui_.getBaseUriBuilder());
		return bugsInfo;
	}

	@GET
	@Path("/versions/{id}/attachments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	public List<AttachmentInfo> getTestCaseAttachments(@Context UriInfo ui_, @PathParam("id") final Integer testCaseId_) throws Exception
	{
		final List<Attachment> attachments = testCaseService.getAttachmentsForTestCaseVersion(testCaseId_);
		final List<AttachmentInfo> attachmentsInfo = objectBuilderFactory.toInfo(AttachmentInfo.class, attachments, ui_.getBaseUriBuilder());
		return attachmentsInfo;
	}

	@POST
	@Path("/versions/{id}/attachments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_EDIT, Permission.TEST_CASE_ADD })
	public AttachmentInfo createAttachment(@Context UriInfo ui, @PathParam("id") final Integer testCaseId, @FormParam("name") String name,
			@FormParam("description") String description, @FormParam("url") String url, @FormParam("size") Double size, @FormParam("attachmentTypeId") Integer attachmentTypeId)
			throws Exception
	{
		Attachment attachment = testCaseService.addAttachmentForTestCaseVersion(name, description, url, size, testCaseId, attachmentTypeId);
		return objectBuilderFactory.toInfo(AttachmentInfo.class, attachment, ui.getBaseUriBuilder());
	}

	@DELETE
	@Path("/versions/{id}/attachments/{attachmentId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ADD, Permission.TEST_CASE_EDIT })
	public Boolean deleteAttachment(@Context UriInfo ui, @PathParam("id") final Integer testCaseId, @PathParam("attachmentId") final Integer attachmentId,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		return testCaseService.deleteAttachment(attachmentId, testCaseId);
	}

	@GET
	@Path("/versions/{id}/tags/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	/**
	 * Returns all tags of a test case
	 */
	public List<TagInfo> getTestCaseVersionTags(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_) throws Exception
	{
		final List<Tag> tags = testCaseService.getTestCaseVersionTags(testCaseVersionId_);
		return objectBuilderFactory.toInfo(TagInfo.class, tags, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/versions/{id}/tags/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ADD, Permission.TEST_CASE_EDIT })
	public Boolean updateTestCaseVersionTags(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_, @FormParam("tagIds") final ArrayList<Integer> tagIds_)
			throws Exception
	{
		testCaseService.saveTagsForTestCaseVersion(testCaseVersionId_, tagIds_, 0);
		return Boolean.TRUE;
	}

	@PUT
	@Path("/versions/{id}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ADD, Permission.TEST_CASE_EDIT })
	public Boolean updateTestCaseEnvironmentGroups(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_,
			@FormParam("environmentGroupIds") final ArrayList<Integer> environmentGroupIds_, @FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{
		testCaseService.saveEnvironmentGroupsForTestCaseVersion(testCaseVersionId_, environmentGroupIds_, originalVesionId_);
		return Boolean.TRUE;
	}

	@GET
	@Path("/versions/{id}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	public List<EnvironmentGroupInfo> getTestCaseEnvironmentGroups(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_,
			@QueryParam("includedEnvironmentId") final Integer includedEnvironmentId_) throws Exception
	{
		final List<EnvironmentGroup> groups = testCaseService.getEnvironmentGroupsForTestCaseVersion(testCaseVersionId_, includedEnvironmentId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, groups, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/versions/{id}/environmentgroups/exploded/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	/**
	 * Returns all versions of a test case
	 */
	public List<EnvironmentGroupExplodedInfo> getTestCaseEnvironmentGroupsExploded(@Context final UriInfo ui_, @PathParam("id") final Integer productId_,
			@QueryParam("includedEnvironmentId") final Integer includedEnvironmentId_) throws Exception
	{
		final List<EnvironmentGroupExploded> groups = testCaseService.getEnvironmentGroupsExplodedForTestCaseVersion(productId_, includedEnvironmentId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupExplodedInfo.class, groups, ui_.getBaseUriBuilder());
	}

	@POST
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ADD, Permission.TEST_CASE_EDIT })
	public TestCaseVersionInfo createTestCase(@Context final UriInfo ui_, @FormParam("productId") final Integer productId_, @FormParam("testCycleId") final Integer testCycleId_,
			@FormParam("maxAttachmentSizeInMbytes") final Integer maxAttachmentSizeInMbytes_, @FormParam("maxNumberOfAttachments") final Integer maxNumberOfAttachments_,
			@FormParam("name") final String name_, @FormParam("description") final String description_, @FormParam("automated") final String automated_,
			@FormParam("automationUri") final String automationUri_) throws Exception
	{
		final TestCase testCase = testCaseService.addTestCase(productId_, testCycleId_, maxAttachmentSizeInMbytes_, maxNumberOfAttachments_, name_, description_, "TRUE"
				.equalsIgnoreCase(automated_), automationUri_, null);
		final TestCaseVersionView testCaseVersionView = testCaseService.getTestCaseVersionView(testCase.getLatestVersion().getId());
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersionView, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/versions/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ADD, Permission.TEST_CASE_EDIT })
	public Boolean deleteTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_,
			@FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{
		testCaseService.deleteTestCaseVersion(testCaseVersionId_, originalVesionId_);

		return Boolean.TRUE;
	}

	@POST
	@Path("/versions/{id}/steps/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ADD, Permission.TEST_CASE_EDIT })
	public TestCaseStepInfo createTestCaseStep(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_, @FormParam("stepNumber") final Integer stepNumber_,
			@FormParam("name") final String name_, @FormParam("instruction") final String instruction_, @FormParam("expectedResult") final String expectedResult_,
			@FormParam("estimatedTimeInMin") final Integer estimatedTimeInMin_) throws Exception
	{

		final TestCaseStep testCaseStep = testCaseService.addTestCaseStep(testCaseVersionId_, name_, stepNumber_, instruction_, expectedResult_, estimatedTimeInMin_);
		return objectBuilderFactory.toInfo(TestCaseStepInfo.class, testCaseStep, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/steps/{stepId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ADD, Permission.TEST_CASE_EDIT })
	public Boolean deleteTestCaseStep(@Context final UriInfo ui_, @PathParam("stepId") final Integer testCaseStepId_,
			@FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{
		testCaseService.deleteTestCaseStep(testCaseStepId_, originalVesionId_);

		return Boolean.TRUE;
	}

	@PUT
	@Path("/steps/{stepId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ADD, Permission.TEST_CASE_EDIT })
	public TestCaseStepInfo updateTestCaseStep(@Context final UriInfo ui_, @PathParam("stepId") final Integer testCaseStepId_, @FormParam("stepNumber") final Integer stepNumber_,
			@FormParam("name") final String name_, @FormParam("instruction") final String instruction_, @FormParam("expectedResult") final String expectedResult_,
			@FormParam("estimatedTimeInMin") final Integer estimatedTimeInMin_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{

		final TestCaseStep testCaseStep = testCaseService.saveTestCaseStep(testCaseStepId_, name_, stepNumber_, instruction_, expectedResult_, estimatedTimeInMin_,
				originalVersionId_);
		return objectBuilderFactory.toInfo(TestCaseStepInfo.class, testCaseStep, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/undo_delete/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ADD, Permission.TEST_CASE_EDIT })
	public Boolean undeleteTestCase(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_, @FormParam("originalVersionId") final Integer originalVesionId_)
			throws Exception
	{
		testCaseService.undeleteTestCase(testCaseId_);

		return Boolean.TRUE;
	}

	@DELETE
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ADD, Permission.TEST_CASE_EDIT })
	public Boolean deleteTestCase(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseId_, @FormParam("originalVersionId") final Integer originalVesionId_)
			throws Exception
	{
		testCaseService.deleteTestCase(testCaseId_, originalVesionId_);

		return Boolean.TRUE;
	}

	@PUT
	@Path("/versions/{id}/undo_delete/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_ADD, Permission.TEST_CASE_EDIT, Permission.DELETED_ENTITY_UNDO })
	public Boolean undeleteTestCaseVersion(@Context final UriInfo ui_, @PathParam("id") final Integer testCaseVersionId_,
			@FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{
		testCaseService.undeleteTestCaseVersion(testCaseVersionId_);
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
		final TestCaseVersionView testCaseVersionView = testCaseService.getTestCaseVersionView(testCase.getLatestVersion().getId());
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersionView, ui_.getBaseUriBuilder());
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
		return objectBuilderFactory.toInfo(TestCaseInfo.class, testCase, ui_.getBaseUriBuilder());
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
		final TestCaseVersionView testCaseVersion = testCaseService.getTestCaseVersionView(testCaseVersionId_);
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersion, ui_.getBaseUriBuilder());
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
		return objectBuilderFactory.toInfo(TestCaseStepInfo.class, steps, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/steps/{stepId}/")
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

		return objectBuilderFactory.toInfo(TestCaseStepInfo.class, step, ui_.getBaseUriBuilder());
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
	public TestCaseVersionSearchResultInfo findTestCaseVersions(@Context final UriInfo ui_, @QueryParam("includedEnvironmentId") final Integer includedEnvironmentId_,
			@QueryParam("includedInTestSuiteId") final Integer includedInTestSuiteId_, @QueryParam("tag") final String tag_, @QueryParam("") final UtestSearchRequest request_)
			throws Exception
	{
		final UtestSearchResult result = testCaseService.findTestCaseVersions(constructCompleteTestCaseVersionSearch(request_, ui_), includedInTestSuiteId_,
				includedEnvironmentId_, tag_);
		return (TestCaseVersionSearchResultInfo) objectBuilderFactory.createResult(TestCaseVersionInfo.class, TestCaseVersionView.class, request_, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/versions/deleted/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEST_CASE_VIEW, Permission.DELETED_ENTITY_VIEW })
	/**
	 * Returns versions of test cases based on search parameters
	 */
	public TestCaseVersionSearchResultInfo findDeletedTestCaseVersions(@Context final UriInfo ui_, @QueryParam("includedEnvironmentId") final Integer includedEnvironmentId_,
			@QueryParam("includedInTestSuiteId") final Integer includedInTestSuiteId_, @QueryParam("tag") final String tag_, @QueryParam("") final UtestSearchRequest request_)
			throws Exception
	{
		final UtestSearchResult result = testCaseService.findDeletedEntities(TestCaseVersion.class, constructCompleteTestCaseVersionSearch(request_, ui_));
		return (TestCaseVersionSearchResultInfo) objectBuilderFactory.createResult(TestCaseVersionInfo.class, TestCaseVersionView.class, request_, result, ui_.getBaseUriBuilder());
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
	public TestCaseVersionSearchResultInfo findLatestTestCaseVersions(@Context final UriInfo ui_, @QueryParam("includedEnvironmentId") final Integer includedEnvironmentId_,
			@QueryParam("includedInTestSuiteId") final Integer includedInTestSuiteId_, @QueryParam("tag") final String tag_, @QueryParam("") final UtestSearchRequest request_)
			throws Exception
	{
		final UtestSearchResult result = testCaseService.findLatestTestCaseVersions(constructCompleteTestCaseVersionSearch(request_, ui_), includedInTestSuiteId_,
				includedEnvironmentId_, tag_);
		return (TestCaseVersionSearchResultInfo) objectBuilderFactory.createResult(TestCaseVersionInfo.class, TestCaseVersionView.class, request_, result, ui_.getBaseUriBuilder());
	}

	private UtestSearch constructCompleteTestCaseVersionSearch(UtestSearchRequest request_, UriInfo ui_) throws Exception
	{
		final UtestSearch stepSearch = objectBuilderFactory.createSearch(TestCaseStepInfo.class, request_, ui_);
		final UtestSearch search = objectBuilderFactory.createSearch(TestCaseVersionInfo.class, request_, ui_);
		List<UtestFilter> stepFilters = stepSearch.getFilters();
		if (stepFilters != null && !stepFilters.isEmpty())
		{
			for (UtestFilter stepFilter : stepFilters)
			{
				search.addFilter(stepFilter);
			}
		}
		return search;
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEST_CASE_VIEW)
	/**
	 * Returns latest versions of test cases by default
	 */
	public TestCaseSearchResultInfo findTestCases(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(TestCaseInfo.class, request_, ui_);
		final UtestSearchResult result = testCaseService.findTestCases(search);

		return (TestCaseSearchResultInfo) objectBuilderFactory.createResult(TestCaseInfo.class, TestCase.class, request_, result, ui_.getBaseUriBuilder());
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
		final List<TestCaseVersionView> testCaseVersions = testCaseService.getTestCaseVersionViews(testCaseId_);
		return objectBuilderFactory.toInfo(TestCaseVersionInfo.class, testCaseVersions, ui_.getBaseUriBuilder());
	}

}
