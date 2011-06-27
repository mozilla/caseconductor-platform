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
package com.utest.domain.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;

import com.trg.search.Search;
import com.utest.dao.TypelessDAO;
import com.utest.domain.ApprovalStatus;
import com.utest.domain.CompanyDependable;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.Permission;
import com.utest.domain.Product;
import com.utest.domain.ProductComponent;
import com.utest.domain.Tag;
import com.utest.domain.TestCase;
import com.utest.domain.TestCaseProductComponent;
import com.utest.domain.TestCaseStatus;
import com.utest.domain.TestCaseStep;
import com.utest.domain.TestCaseTag;
import com.utest.domain.TestCaseVersion;
import com.utest.domain.VersionIncrement;
import com.utest.domain.search.UtestFilter;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.EnvironmentService;
import com.utest.domain.service.TestCaseService;
import com.utest.domain.service.UserService;
import com.utest.domain.util.DomainUtil;
import com.utest.domain.view.TestCaseVersionView;
import com.utest.exception.ActivatingIncompleteEntityException;
import com.utest.exception.ActivatingNotApprovedEntityException;
import com.utest.exception.ApprovingIncompleteEntityException;
import com.utest.exception.ChangingActivatedEntityException;
import com.utest.exception.DeletingActivatedEntityException;
import com.utest.exception.DuplicateTestCaseStepException;
import com.utest.exception.InvalidUserException;
import com.utest.exception.NotFoundException;
import com.utest.exception.UnsupportedEnvironmentSelectionException;

public class TestCaseServiceImpl extends BaseServiceImpl implements TestCaseService
{
	private static final Integer		DEFAULT_MAJOR_VERSION	= 0;
	private static final Integer		DEFAULT_MINOR_VERSION	= 1;

	private static final List<String>	TEST_CASE_STEP_FIELDS	= Arrays.asList("instruction", "expectedResult");
	private final UserService			userService;

	/**
	 * Default constructor
	 */
	public TestCaseServiceImpl(final TypelessDAO dao, final EnvironmentService environmentService, final UserService userService)
	{
		super(dao, environmentService);
		this.userService = userService;
	}

	@Override
	public TestCase addTestCase(final Integer productId_, final Integer maxAttachmentSizeInMbytes_, final Integer maxNumberOfAttachments_, final String name_,
			final String description_) throws Exception
	{
		return addTestCase(productId_, null, maxAttachmentSizeInMbytes_, maxNumberOfAttachments_, name_, description_);
	}

	@Override
	public TestCase addTestCase(final Integer productId_, final Integer testCycleId_, final Integer maxAttachmentSizeInMbytes_, final Integer maxNumberOfAttachments_,
			final String name_, final String description_) throws Exception
	{
		return addTestCase(productId_, testCycleId_, maxAttachmentSizeInMbytes_, maxNumberOfAttachments_, name_, description_, false, null);
	}

	@Override
	public TestCase addTestCase(final Integer productId_, final Integer testCycleId_, final Integer maxAttachmentSizeInMbytes_, final Integer maxNumberOfAttachments_,
			final String name_, final String description_, final boolean automated_, final String automationUri_) throws Exception
	{
		final Product product = getRequiredEntityById(Product.class, productId_);
		checkForDuplicateNameWithinParent(TestCase.class, name_, productId_, "productId", null);

		final TestCase testCase = new TestCase(name_, productId_, product.getCompanyId(), maxAttachmentSizeInMbytes_, maxNumberOfAttachments_, testCycleId_);
		final Integer testCaseId = dao.addAndReturnId(testCase);

		final TestCaseVersion testCaseVersion = new TestCaseVersion();
		testCaseVersion.setTestCaseId(testCaseId);
		initializeTestCaseVersion(testCaseVersion, 0, 0, VersionIncrement.INITIAL);
		testCaseVersion.setProductId(productId_);
		testCaseVersion.setCompanyId(product.getCompanyId());
		testCaseVersion.setDescription(description_);
		testCaseVersion.setAutomated(automated_);
		testCaseVersion.setAutomationUri(automationUri_);
		// set environment profile from the product by default
		testCaseVersion.setEnvironmentProfileId(product.getEnvironmentProfileId());

		dao.addAndReturnId(testCaseVersion);
		return getTestCase(testCaseId);
	}

	@Override
	public void addTestCaseTag(final Integer testCaseId_, final Integer tagId_) throws Exception
	{
		final TestCase testCase = getRequiredEntityById(TestCase.class, testCaseId_);
		final Tag tag = getRequiredEntityById(Tag.class, tagId_);
		// check that Tag and TestCase from the same Company
		Product product = getRequiredEntityById(Product.class, testCase.getProductId());
		List<CompanyDependable> entities = new ArrayList<CompanyDependable>();
		entities.add(tag);
		checkValidSelectionForCompany(product.getCompanyId(), entities);

		final Search search = new Search(TestCaseTag.class);
		search.addFilterEqual("testCaseId", testCaseId_);
		search.addFilterEqual("tagId", tagId_);
		TestCaseTag testCaseTag = (TestCaseTag) dao.searchUnique(TestCaseTag.class, search);
		if (testCaseTag == null)
		{
			testCaseTag = new TestCaseTag(testCaseId_, tagId_);
			dao.addAndReturnId(testCaseTag);
		}
	}

	@Override
	public TestCaseStep addTestCaseStep(final Integer testCaseVersionId_, final String name_, final Integer stepNumber_, final String instruction_, final String expectedResult_,
			final Integer estimatedTimeInMin_) throws Exception
	{
		final TestCaseVersion testCaseVersion = getRequiredEntityById(TestCaseVersion.class, testCaseVersionId_);
		checkForDuplicateNameWithinParent(TestCaseStep.class, name_, testCaseVersionId_, "testCaseVersionId", null);
		checkForDuplicateStepNumber(testCaseVersionId_, stepNumber_);

		final TestCaseStep step = new TestCaseStep();
		step.setTestCaseVersionId(testCaseVersion.getId());
		step.setName(name_);
		step.setStepNumber(stepNumber_);
		step.setEstimatedTimeInMin(estimatedTimeInMin_);
		step.setInstruction(instruction_);
		step.setExpectedResult(expectedResult_);
		final Integer id = dao.addAndReturnId(step);
		return getRequiredEntityById(TestCaseStep.class, id);
	}

	private void checkForDuplicateStepNumber(final Integer testCaseVersionId_, final Integer stepNumber_) throws DuplicateTestCaseStepException
	{
		// check for duplicate name within a product
		final Search search = new Search(TestCaseStep.class);
		search.addFilterEqual("stepNumber", stepNumber_);
		search.addFilterEqual("testCaseVersionId", testCaseVersionId_);
		final List<TestCaseStep> foundEntities = dao.search(TestCaseStep.class, search);
		if ((foundEntities != null) && !foundEntities.isEmpty())
		{
			throw new DuplicateTestCaseStepException("Duplicate step " + stepNumber_ + " for TestCaseVersion " + testCaseVersionId_);
		}
	}

	/**
	 * Sets default settings for new version of a TestCase
	 * 
	 * @param auth_
	 * @param testCaseVersion_
	 * @param versionIncrement_
	 */
	private void initializeTestCaseVersion(final TestCaseVersion testCaseVersion_, final Integer priorMajorVersion_, final Integer priorMinorVersion_,
			final VersionIncrement versionIncrement_)
	{
		testCaseVersion_.setId(null);
		testCaseVersion_.setApprovalStatusId(ApprovalStatus.PENDING);
		testCaseVersion_.setApproveDate(null);
		testCaseVersion_.setApprovedBy(null);
		testCaseVersion_.setTestCaseStatusId(TestCaseStatus.PENDING);
		testCaseVersion_.setLatestVersion(true);

		if (versionIncrement_.equals(VersionIncrement.INITIAL))
		{
			testCaseVersion_.setMajorVersion(DEFAULT_MAJOR_VERSION);
			testCaseVersion_.setMinorVersion(DEFAULT_MINOR_VERSION);
		}
		else if (versionIncrement_.equals(VersionIncrement.MINOR))
		{
			final Integer priorVersion = priorMinorVersion_;
			testCaseVersion_.setMinorVersion(priorVersion + 1);
			testCaseVersion_.setMajorVersion(priorMajorVersion_);
		}
		else if (versionIncrement_.equals(VersionIncrement.MAJOR))
		{
			final Integer priorVersion = priorMajorVersion_;
			testCaseVersion_.setMajorVersion(priorVersion + 1);
			testCaseVersion_.setMinorVersion(0);
		}
		else if (versionIncrement_.equals(VersionIncrement.BOTH))
		{
			Integer priorVersion = priorMajorVersion_;
			testCaseVersion_.setMajorVersion(priorVersion + 1);
			priorVersion = priorMinorVersion_;
			testCaseVersion_.setMinorVersion(priorVersion + 1);
		}
	}

	@Override
	public void deleteTestCase(final Integer testCaseId_, final Integer originalVersionId_) throws Exception
	{
		TestCase testCase = getRequiredEntityById(TestCase.class, testCaseId_);
		// everyone has add test case permission by default, so need to check if
		// user has permission to edit others test cases
		checkEditPermission(testCase.getCreatedBy());

		final Search search = new Search(TestCaseVersion.class);
		search.addFilterEqual("testCaseId", testCaseId_);
		final List<TestCaseVersion> foundEntities = dao.search(TestCaseVersion.class, search);
		if ((foundEntities != null) && !foundEntities.isEmpty())
		{
			for (final TestCaseVersion testCaseVersion : foundEntities)
			{
				// cannot delete if any of test case versions activated
				deleteTestCaseVersion(testCaseVersion.getId(), testCaseVersion.getVersion());
			}
		}
		// delete main test case
		testCase.setVersion(originalVersionId_);
		dao.delete(testCase);
	}

	@Override
	public void deleteTestCaseStep(final Integer testCaseStepId_, final Integer originalVersionId_) throws Exception
	{
		final TestCaseStep testCaseStep = getRequiredEntityById(TestCaseStep.class, testCaseStepId_);
		final TestCaseVersion testCaseVersion = getRequiredEntityById(TestCaseVersion.class, testCaseStep.getTestCaseVersionId());
		// everyone has add test case permission by default, so need to check if
		// user has permission to edit others test cases
		checkEditPermission(testCaseVersion.getCreatedBy());
		// cannot delete if not DRAFT
		if (!TestCaseStatus.PENDING.equals(testCaseVersion.getTestCaseStatusId()))
		{
			throw new DeletingActivatedEntityException(TestCaseStep.class.getSimpleName());
		}

		testCaseStep.setVersion(originalVersionId_);
		dao.delete(testCaseStep);
	}

	@Override
	public TestCaseVersion approveTestCaseVersion(final Integer testCaseVersionId_, final Integer originalVersionId_) throws Exception
	{
		return updateApprovalStatus(testCaseVersionId_, ApprovalStatus.APPROVED, originalVersionId_);
	}

	@Override
	public TestCaseVersion rejectTestCaseVersion(final Integer testCaseVersionId_, final Integer originalVersionId_) throws Exception
	{
		return updateApprovalStatus(testCaseVersionId_, ApprovalStatus.REJECTED, originalVersionId_);
	}

	private TestCaseVersion updateApprovalStatus(final Integer testCaseVersionId_, final Integer approvalStatus_, final Integer originalVersionId_) throws Exception
	{
		final TestCaseVersion testCaseVersion = getTestCaseVersion(testCaseVersionId_);
		if (approvalStatus_ != testCaseVersion.getApprovalStatusId())
		{
			// make sure user approving the result is not the same as assigned
			if (getCurrentUserId().equals(testCaseVersion.getCreatedBy()))
			{
				throw new InvalidUserException();
			}
			List<TestCaseStep> steps = testCaseVersion.getSteps();
			if (((steps == null) || steps.isEmpty()) && ApprovalStatus.APPROVED.equals(approvalStatus_))
			{
				throw new ApprovingIncompleteEntityException(TestCaseVersion.class.getSimpleName() + " : " + testCaseVersionId_);
			}
			testCaseVersion.setVersion(originalVersionId_);
			testCaseVersion.setApprovalStatusId(approvalStatus_);
			testCaseVersion.setApproveDate(new Date());
			testCaseVersion.setApprovedBy(getCurrentUserId());
			dao.addOrUpdate(testCaseVersion);
			dao.flush();
			return getTestCaseVersion(testCaseVersionId_);
		}
		else
		{
			return testCaseVersion;
		}
	}

	@Override
	public TestCaseVersion activateTestCaseVersion(final Integer testCaseVersionId_, final Integer originalVersionId_) throws Exception
	{
		return updateActivationStatus(testCaseVersionId_, TestCaseStatus.ACTIVE, originalVersionId_);
	}

	@Override
	public TestCaseVersion lockTestCaseVersion(final Integer testCaseVersionId_, final Integer originalVersionId_) throws Exception
	{
		return updateActivationStatus(testCaseVersionId_, TestCaseStatus.LOCKED, originalVersionId_);
	}

	private TestCaseVersion updateActivationStatus(final Integer testCaseVersionId_, final Integer activationStatusId_, final Integer originalVersionId_) throws Exception
	{
		final TestCaseVersion testCaseVersion = getRequiredEntityById(TestCaseVersion.class, testCaseVersionId_);
		if (activationStatusId_ != testCaseVersion.getTestCaseStatusId())
		{
			// test case must be approved before activation
			if (TestCaseStatus.ACTIVE.equals(activationStatusId_) && !ApprovalStatus.APPROVED.equals(testCaseVersion.getApprovalStatusId()))
			{
				throw new ActivatingNotApprovedEntityException(TestCaseVersion.class.getSimpleName() + " : " + testCaseVersionId_);
			}
			List<TestCaseStep> steps = getTestCaseVersionSteps(testCaseVersionId_);
			if ((steps == null) || steps.isEmpty())
			{
				throw new ActivatingIncompleteEntityException(TestCaseVersion.class.getSimpleName() + " : " + testCaseVersionId_);
			}
			testCaseVersion.setVersion(originalVersionId_);
			testCaseVersion.setTestCaseStatusId(activationStatusId_);
			dao.addOrUpdate(testCaseVersion);
			dao.flush();
			return getTestCaseVersion(testCaseVersionId_);
		}
		else
		{
			return testCaseVersion;
		}
	}

	@Override
	public void deleteTestCaseTag(final Integer testCaseId_, final Integer tagId_)
	{
		final Search search = new Search(TestCaseTag.class);
		search.addFilterEqual("testCaseId", testCaseId_);
		search.addFilterEqual("tagId", tagId_);
		final TestCaseTag testCaseTag = (TestCaseTag) dao.searchUnique(TestCaseTag.class, search);
		if (testCaseTag == null)
		{
			throw new NotFoundException("Tag not found. Id: " + tagId_ + " For TestCase: " + testCaseId_);
		}
		dao.delete(testCaseTag);
	}

	@Override
	public void deleteTestCaseVersion(final Integer testCaseVersionId_, final Integer originalVersionId_) throws Exception
	{
		final TestCaseVersion testCaseVersion = getRequiredEntityById(TestCaseVersion.class, testCaseVersionId_);
		// everyone has add test case permission by default, so need to check if
		// user has permission to edit others test cases
		checkEditPermission(testCaseVersion.getCreatedBy());
		if (!TestCaseStatus.PENDING.equals(testCaseVersion.getTestCaseStatusId()))
		{
			throw new DeletingActivatedEntityException(TestCaseVersion.class.getSimpleName());
		}
		// delete all steps
		final List<TestCaseStep> steps = getTestCaseVersionSteps(testCaseVersionId_);
		dao.delete(steps);
		testCaseVersion.setVersion(originalVersionId_);
		dao.delete(testCaseVersion);
	}

	@Override
	public UtestSearchResult findTestCaseVersions(final UtestSearch search_) throws Exception
	{
		List<UtestFilter> filters = search_.getFilters();
		UtestSearch testCaseStepSearch = new UtestSearch();
		UtestSearch testCaseSearch = new UtestSearch();
		boolean found = false;
		for (UtestFilter filter : filters)
		{
			if (TEST_CASE_STEP_FIELDS.contains(filter.getProperty()))
			{
				found = true;
				testCaseStepSearch.addFilter(filter);
			}
			else
			{
				testCaseSearch.addFilter(filter);
			}
		}
		if (found)
		{
			UtestSearchResult testCaseStepSearchResult = dao.getBySearch(TestCaseStep.class, testCaseStepSearch);
			List<?> testCaseSteps = testCaseStepSearchResult.getResults();
			if (testCaseSteps == null || testCaseSteps.isEmpty())
			{
				return testCaseStepSearchResult;
			}
			List<Integer> ids = new ArrayList<Integer>();
			for (Object testCaseStep : testCaseSteps)
			{
				ids.add(((TestCaseStep) testCaseStep).getTestCaseVersionId());
			}
			testCaseSearch.addFilterIn("id", ids);
		}
		return dao.getBySearch(TestCaseVersionView.class, testCaseSearch);
	}

	@Override
	public UtestSearchResult findTestCases(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(TestCase.class, search_);
	}

	@Override
	public UtestSearchResult findLatestTestCaseVersions() throws Exception
	{
		final UtestSearch search = new UtestSearch();
		return findLatestTestCaseVersions(search);
	}

	@Override
	public UtestSearchResult findLatestTestCaseVersions(final UtestSearch search_) throws Exception
	{
		search_.addFilterEqual("latestVersion", true);
		return findTestCaseVersions(search_);
	}

	@Override
	public List<ProductComponent> getComponentsForTestCase(final Integer testCaseId_) throws Exception
	{
		Search search = new Search(TestCaseProductComponent.class);
		search.addFilterEqual("testCaseId", testCaseId_);
		final List<TestCaseProductComponent> foundGroups = dao.search(TestCaseProductComponent.class, search);
		if ((foundGroups != null) && !foundGroups.isEmpty())
		{
			final List<Integer> ids = new ArrayList<Integer>();
			for (final TestCaseProductComponent idHolder : foundGroups)
			{
				ids.add(idHolder.getProductComponentId());
			}
			search = new Search(ProductComponent.class);
			search.addFilterIn("id", ids);
			return dao.search(ProductComponent.class, search);
		}
		else
		{
			return new ArrayList<ProductComponent>();
		}
	}

	@Override
	public void saveProductComponentsForTestCase(final Integer testCaseId_, final List<Integer> productComponentIds_, final Integer originalVersionId_) throws Exception
	{
		final TestCase testCase = getRequiredEntityById(TestCase.class, testCaseId_);
		// delete old components for test case
		final Search search = new Search(TestCaseProductComponent.class);
		search.addFilterEqual("testCaseId", testCaseId_);
		final List<?> foundTypes = dao.search(TestCaseProductComponent.class, search);
		dao.delete(foundTypes);

		// add new components for test case
		for (final Integer productComponentId : productComponentIds_)
		{
			final ProductComponent productComponent = getRequiredEntityById(ProductComponent.class, productComponentId);
			if (productComponent != null)
			{
				if (!productComponent.getProductId().equals(testCase.getProductId()))
				{
					throw new UnsupportedEnvironmentSelectionException(ProductComponent.class.getSimpleName() + " : " + productComponentId);
				}
				final TestCaseProductComponent testCaseProductComponent = new TestCaseProductComponent();
				testCaseProductComponent.setProductComponentId(productComponentId);
				testCaseProductComponent.setTestCaseId(testCaseId_);
				dao.addAndReturnId(testCaseProductComponent);
			}
		}
	}

	@Override
	public List<EnvironmentGroup> getEnvironmentGroupsForTestCaseVersion(final Integer testCaseVersionId_) throws Exception
	{
		final TestCaseVersion testCaseVersion = getRequiredEntityById(TestCaseVersion.class, testCaseVersionId_);
		if (testCaseVersion != null)
		{
			if (testCaseVersion.getEnvironmentProfileId() != null)
			{
				return environmentService.getEnvironmentGroupsForProfile(testCaseVersion.getEnvironmentProfileId());
			}
		}
		return new ArrayList<EnvironmentGroup>();
	}

	@Override
	public TestCase getTestCase(final Integer testCaseId_) throws Exception
	{
		final TestCase testCase = getRequiredEntityById(TestCase.class, testCaseId_);
		final TestCaseVersion latestVersion = getLatestTestCaseVersion(testCaseId_);
		// hiberante doesn't map on newly added objects, need workaround
		latestVersion.setTestCase(testCase);
		testCase.setLatestVersion(latestVersion);
		return testCase;
	}

	@Override
	public List<TestCaseStep> getTestCaseVersionSteps(final Integer testCaseVersionId_) throws Exception
	{
		final Search search = new Search(TestCaseStep.class);
		search.addFilterEqual("testCaseVersionId", testCaseVersionId_);
		return dao.search(TestCaseStep.class, search);
	}

	@Override
	public List<TestCaseVersion> getTestCaseVersions(final Integer testCaseId_) throws Exception
	{
		final Search search = new Search(TestCaseVersion.class);
		search.addFilterEqual("testCaseId", testCaseId_);
		return dao.search(TestCaseVersion.class, search);
	}

	@Override
	public List<TestCaseVersionView> getTestCaseVersionViews(final Integer testCaseId_) throws Exception
	{
		final Search search = new Search(TestCaseVersionView.class);
		search.addFilterEqual("testCaseId", testCaseId_);
		return dao.search(TestCaseVersionView.class, search);
	}

	@Override
	public List<Tag> getTestCaseTags(final Integer testCaseId_) throws Exception
	{
		Search search = new Search(TestCaseTag.class);
		search.addField("tagId");
		search.addFilterEqual("testCaseId", testCaseId_);
		final List<?> tagIdList = dao.search(TestCaseTag.class, search);
		search = new Search(Tag.class);
		search.addFilterIn("id", tagIdList);
		final List<Tag> list = dao.search(Tag.class, search);
		return list;
	}

	@Override
	public TestCaseVersion getLatestTestCaseVersion(final Integer testCaseId_) throws Exception
	{
		final Search search = new Search(TestCaseVersion.class);
		search.addFilterEqual("testCaseId", testCaseId_);
		search.addFilterEqual("latestVersion", true);
		final TestCaseVersion testCaseVersion = (TestCaseVersion) dao.searchUnique(TestCaseVersion.class, search);
		if (testCaseVersion != null)
		{
			testCaseVersion.setSteps(getTestCaseVersionSteps(testCaseVersion.getId()));
		}
		return testCaseVersion;
	}

	@Override
	public TestCaseVersionView getLatestTestCaseVersionView(final Integer testCaseId_) throws Exception
	{
		final Search search = new Search(TestCaseVersionView.class);
		search.addFilterEqual("testCaseId", testCaseId_);
		search.addFilterEqual("latestVersion", true);
		final TestCaseVersionView testCaseVersion = (TestCaseVersionView) dao.searchUnique(TestCaseVersionView.class, search);
		return testCaseVersion;
	}

	@Override
	public TestCaseVersion getLastApprovedTestCaseVersion(final Integer testCaseId_) throws Exception
	{
		final Search search = new Search(TestCaseVersion.class);
		search.addFilterEqual("testCaseId", testCaseId_);
		search.addFilterEqual("approvalStatusId", ApprovalStatus.APPROVED);
		search.addSortDesc("id");
		final List<TestCaseVersion> foundEntities = dao.search(TestCaseVersion.class, search);
		if ((foundEntities != null) && !foundEntities.isEmpty())
		{
			return foundEntities.get(0);
		}
		else
		{
			return null;
		}
	}

	@Override
	public TestCaseVersionView getLastApprovedTestCaseVersionView(final Integer testCaseId_) throws Exception
	{
		final Search search = new Search(TestCaseVersionView.class);
		search.addFilterEqual("testCaseId", testCaseId_);
		search.addFilterEqual("approvalStatusId", ApprovalStatus.APPROVED);
		search.addSortDesc("id");
		final List<TestCaseVersionView> foundEntities = dao.search(TestCaseVersionView.class, search);
		if ((foundEntities != null) && !foundEntities.isEmpty())
		{
			return foundEntities.get(0);
		}
		else
		{
			return null;
		}
	}

	@Override
	public TestCaseVersion getTestCaseVersion(final Integer testCaseVersionId_) throws Exception
	{
		final TestCaseVersion testCaseVersion = getRequiredEntityById(TestCaseVersion.class, testCaseVersionId_);
		testCaseVersion.setSteps(getTestCaseVersionSteps(testCaseVersionId_));
		// hibernate workaround for newly added entities
		if (testCaseVersion.getTestCase() == null)
		{
			TestCase testCase = getTestCase(testCaseVersion.getTestCaseId());
			testCaseVersion.setTestCase(testCase);
		}
		return testCaseVersion;
	}

	@Override
	public TestCaseVersionView getTestCaseVersionView(final Integer testCaseVersionId_) throws Exception
	{
		final TestCaseVersionView testCaseVersion = getRequiredEntityById(TestCaseVersionView.class, testCaseVersionId_);
		return testCaseVersion;
	}

	@Override
	public TestCaseStep getTestCaseStep(final Integer testCaseStepId_) throws Exception
	{
		final TestCaseStep testCaseStep = getRequiredEntityById(TestCaseStep.class, testCaseStepId_);
		return testCaseStep;
	}

	@Override
	public void saveTagsForTestCase(final Integer testCaseId_, final List<Integer> tagIds_, final Integer originalVersionId_) throws Exception
	{
		// delete old tags before inserting new ones
		Search search = new Search(TestCaseTag.class);
		search.addFilterEqual("testCaseId", testCaseId_);
		final List<TestCaseTag> foundTags = dao.search(TestCaseTag.class, search);
		dao.delete(foundTags);
		// insert new ones
		if (tagIds_ != null && !tagIds_.isEmpty())
		{
			for (Integer tagId : tagIds_)
			{
				addTestCaseTag(testCaseId_, tagId);
			}
		}
	}

	@Override
	public void saveEnvironmentGroupsForTestCaseVersion(final Integer testCaseVersionId_, final List<Integer> environmentGroupIds_, final Integer originalVersionId_)
			throws ChangingActivatedEntityException, UnsupportedEnvironmentSelectionException, Exception
	{
		final TestCaseVersion testCaseVersion = getRequiredEntityById(TestCaseVersion.class, testCaseVersionId_);
		// cannot change after activation
		if (!TestCaseStatus.PENDING.equals(testCaseVersion.getTestCaseStatusId()))
		{
			throw new ChangingActivatedEntityException(TestCaseVersion.class.getSimpleName());
		}
		// check that groups are included in the parent profile
		final TestCase testCase = getRequiredEntityById(TestCase.class, testCaseVersion.getTestCaseId());
		final Product product = getRequiredEntityById(Product.class, testCase.getProductId());

		if (!environmentService.isValidEnvironmentGroupSelectionForProfile(product.getEnvironmentProfileId(), environmentGroupIds_))
		{
			throw new UnsupportedEnvironmentSelectionException();
		}
		// update environment profile
		adjustParentChildProfiles(product, testCaseVersion, product.getCompanyId(), environmentGroupIds_);
		testCaseVersion.setVersion(originalVersionId_);
		dao.merge(testCaseVersion);
	}

	@Override
	public TestCase saveTestCase(final Integer testCaseId_, final String name_, final Integer maxAttachmentSizeInMbytes_, final Integer maxNumberOfAttachments_,
			final Integer originalVersionId_) throws Exception
	{
		final TestCase testCase = getRequiredEntityById(TestCase.class, testCaseId_);
		// everyone has add test case permission by default, so need to check if
		// user has permission to edit others test cases
		checkEditPermission(testCase.getCreatedBy());
		checkForDuplicateNameWithinParent(TestCase.class, name_, testCase.getProductId(), "productId", testCaseId_);

		testCase.setName(name_);
		testCase.setMaxAttachmentSizeInMbytes(maxAttachmentSizeInMbytes_);
		testCase.setMaxNumberOfAttachments(maxNumberOfAttachments_);
		testCase.setVersion(originalVersionId_);
		dao.merge(testCase);
		return getTestCase(testCaseId_);
	}

	@Override
	public TestCaseStep saveTestCaseStep(final Integer testCaseStepId_, final String name_, final Integer stepNumber_, final String instruction_, final String expectedResult_,
			final Integer estimatedTimeInMin_, final Integer originalVersionId_) throws Exception
	{
		final TestCaseStep step = getRequiredEntityById(TestCaseStep.class, testCaseStepId_);
		// everyone has add test case permission by default, so need to check if
		// user has permission to edit others test cases
		checkEditPermission(step.getCreatedBy());
		checkForDuplicateNameWithinParent(TestCaseStep.class, name_, step.getTestCaseVersionId(), "testCaseVersionId", testCaseStepId_);

		// cannot change after activation
		final TestCaseVersion testCaseVersion = getRequiredEntityById(TestCaseVersion.class, step.getTestCaseVersionId());
		if (!TestCaseStatus.PENDING.equals(testCaseVersion.getTestCaseStatusId()))
		{
			throw new ChangingActivatedEntityException(TestCaseStep.class.getSimpleName());
		}
		step.setVersion(originalVersionId_);
		step.setName(name_);
		step.setStepNumber(stepNumber_);
		step.setEstimatedTimeInMin(estimatedTimeInMin_);
		step.setInstruction(instruction_);
		step.setExpectedResult(expectedResult_);
		dao.merge(step);
		return getRequiredEntityById(TestCaseStep.class, step.getId());
	}

	@Override
	public TestCaseVersion saveTestCaseVersion(final Integer testCaseVersionId_, final String description_, final boolean automated_, final String automationUri_,
			final Integer originalVersion_, final VersionIncrement versionIncrement_) throws Exception
	{
		final TestCaseVersion testCaseVersion = getRequiredEntityById(TestCaseVersion.class, testCaseVersionId_);
		// everyone has add test case permission by default, so need to check if
		// user has permission to edit others test cases
		checkEditPermission(testCaseVersion.getCreatedBy());
		if (versionIncrement_.equals(VersionIncrement.NONE))
		{
			testCaseVersion.setDescription(description_);
			testCaseVersion.setAutomated(automated_);
			testCaseVersion.setAutomationUri(automationUri_);
			testCaseVersion.setVersion(originalVersion_);
			return dao.merge(testCaseVersion);
		}
		else
		{
			// undo latest version flag for existing version
			// TODO - change to be able to edit older version and still mark the
			// prior latest version as not latest anymore

			testCaseVersion.setLatestVersion(false);
			testCaseVersion.setVersion(originalVersion_);
			dao.merge(testCaseVersion);
			// insert new version
			final TestCaseVersion newTestCaseVersion = new TestCaseVersion();
			newTestCaseVersion.setProductId(testCaseVersion.getProductId());
			newTestCaseVersion.setCompanyId(testCaseVersion.getCompanyId());
			newTestCaseVersion.setTestCaseId(testCaseVersion.getTestCaseId());
			newTestCaseVersion.setDescription(description_);
			newTestCaseVersion.setAutomated(automated_);
			newTestCaseVersion.setAutomationUri(automationUri_);
			newTestCaseVersion.setEnvironmentProfileId(testCaseVersion.getEnvironmentProfileId());
			initializeTestCaseVersion(newTestCaseVersion, testCaseVersion.getMajorVersion(), testCaseVersion.getMinorVersion(), versionIncrement_);
			final Integer newVersionid = dao.addAndReturnId(newTestCaseVersion);
			// copy steps from last version
			// clone steps
			List<TestCaseStep> steps = getTestCaseVersionSteps(testCaseVersionId_);
			if ((steps != null) && !steps.isEmpty())
			{
				for (final TestCaseStep step : steps)
				{
					final TestCaseStep clonedStep = new TestCaseStep();
					clonedStep.setTestCaseVersionId(newVersionid);
					clonedStep.setName(step.getName());
					clonedStep.setStepNumber(step.getStepNumber());
					clonedStep.setEstimatedTimeInMin(step.getEstimatedTimeInMin());
					clonedStep.setInstruction(step.getInstruction());
					clonedStep.setExpectedResult(step.getExpectedResult());
					dao.addAndReturnId(clonedStep);
				}
			}
			return getTestCaseVersion(newVersionid);
		}
	}

	@Override
	public TestCase cloneTestCase(final Integer testCaseId_) throws Exception
	{
		final TestCase testCase = getTestCase(testCaseId_);
		final TestCaseVersion testCaseVersion = testCase.getLatestVersion();
		// clone test case
		final TestCase clonedTestCase = new TestCase("Cloned [" + new Date() + "] " + testCase.getName(), testCase.getProductId(), testCase.getMaxAttachmentSizeInMbytes(),
				testCase.getMaxNumberOfAttachments());
		final Integer clonedTestCaseId = dao.addAndReturnId(clonedTestCase);
		// clone latest version
		final TestCaseVersion clonedTestCaseVersion = new TestCaseVersion();
		clonedTestCaseVersion.setTestCaseId(clonedTestCaseId);
		initializeTestCaseVersion(clonedTestCaseVersion, 0, 0, VersionIncrement.INITIAL);
		clonedTestCaseVersion.setDescription(testCaseVersion.getDescription());
		clonedTestCaseVersion.setEnvironmentProfileId(testCaseVersion.getEnvironmentProfileId());
		clonedTestCaseVersion.setProductId(testCaseVersion.getProductId());
		clonedTestCaseVersion.setCompanyId(testCaseVersion.getCompanyId());
		clonedTestCaseVersion.setAutomated(testCaseVersion.isAutomated());
		clonedTestCaseVersion.setAutomationUri(testCaseVersion.getAutomationUri());
		final Integer clonedVersionId = dao.addAndReturnId(clonedTestCaseVersion);
		// clone steps
		List<TestCaseStep> steps = getTestCaseVersionSteps(testCaseVersion.getId());
		if ((steps != null) && !steps.isEmpty())
		{
			for (final TestCaseStep step : steps)
			{
				final TestCaseStep clonedStep = new TestCaseStep();
				clonedStep.setTestCaseVersionId(clonedVersionId);
				clonedStep.setName("Cloned [" + new Date() + "] " + step.getName());
				clonedStep.setStepNumber(step.getStepNumber());
				clonedStep.setEstimatedTimeInMin(step.getEstimatedTimeInMin());
				clonedStep.setInstruction(step.getInstruction());
				clonedStep.setExpectedResult(step.getExpectedResult());
				dao.addAndReturnId(clonedStep);
			}
		}
		// clone product components
		final List<ProductComponent> components = getComponentsForTestCase(testCaseId_);
		if ((components != null) && !components.isEmpty())
		{
			saveProductComponentsForTestCase(clonedTestCaseId, DomainUtil.extractEntityIds(components), 0);
		}

		return getTestCase(clonedTestCaseId);
	}

	private void checkEditPermission(Integer testerId_) throws AccessDeniedException
	{
		if (!getCurrentUserId().equals(testerId_) && !userService.isUserInPermission(getCurrentUserId(), Permission.TEST_CASE_EDIT))
		{
			throw new AccessDeniedException("User doesn't have permissions to edit test cases, created by someone else.");
		}
	}
}
