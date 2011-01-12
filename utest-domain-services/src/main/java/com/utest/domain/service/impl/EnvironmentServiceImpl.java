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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trg.search.Search;
import com.utest.dao.TypelessDAO;
import com.utest.domain.Company;
import com.utest.domain.CompanyDependable;
import com.utest.domain.Environment;
import com.utest.domain.EnvironmentDependable;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentGroupEnvironment;
import com.utest.domain.EnvironmentLocale;
import com.utest.domain.EnvironmentProfile;
import com.utest.domain.EnvironmentProfileEnvironmentGroup;
import com.utest.domain.EnvironmentType;
import com.utest.domain.EnvironmentTypeLocale;
import com.utest.domain.Locale;
import com.utest.domain.ParentDependableEnvironment;
import com.utest.domain.Product;
import com.utest.domain.Tag;
import com.utest.domain.TestCaseTag;
import com.utest.domain.TestCaseVersion;
import com.utest.domain.TestPlan;
import com.utest.domain.TestRun;
import com.utest.domain.TestRunResult;
import com.utest.domain.TestRunTestCase;
import com.utest.domain.TestRunTestCaseAssignment;
import com.utest.domain.TestSuite;
import com.utest.domain.TestSuiteTestCase;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.EnvironmentService;
import com.utest.domain.util.DomainUtil;
import com.utest.exception.ChangingActivatedEntityException;
import com.utest.exception.DeletingUsedEntityException;
import com.utest.exception.DuplicateNameException;
import com.utest.exception.InvalidParentChildEnvironmentException;
import com.utest.exception.NotFoundException;
import com.utest.exception.UnsupportedEnvironmentSelectionException;
import com.utest.util.NumberUtil;

public class EnvironmentServiceImpl implements EnvironmentService
{
	private final TypelessDAO	dao;

	/**
	 * Default constructor
	 */
	public EnvironmentServiceImpl(final TypelessDAO dao)
	{
		super();
		this.dao = dao;
	}

	@Override
	public Tag addTag(Integer companyId_, String tag_) throws Exception
	{

		if (companyId_ == null)
		{
			companyId_ = Company.SYSTEM_WIDE_COMPANY_ID;
		}
		final Search search = new Search(Tag.class);
		search.addFilterEqual("companyId", companyId_);
		search.addFilterEqual("name", tag_);
		final List<Tag> tags = dao.search(Tag.class, search);
		if ((tags != null) && !tags.isEmpty())
		{
			throw new DuplicateNameException();
		}
		final Tag tag = new Tag(companyId_, tag_);
		final Integer tagId = dao.addAndReturnId(tag);
		return dao.getById(Tag.class, tagId);
	}

	@Override
	public Environment addEnvironment(final Integer environmentTypeId_, final String description_) throws Exception
	{
		return addEnvironment(Company.SYSTEM_WIDE_COMPANY_ID, environmentTypeId_, description_, Locale.DEFAULT_LOCALE);
	}

	@Override
	public Environment addEnvironment(final Integer companyId_, final Integer environmentTypeId_, final String name_, final String localeCode_) throws Exception
	{
		if (!Company.SYSTEM_WIDE_COMPANY_ID.equals(companyId_))
		{
			final Company company = dao.getById(Company.class, companyId_);
			if (company == null)
			{
				throw new NotFoundException("Company not found: " + companyId_);
			}
		}
		// validate type for environment
		checkValidEnvironmentType(companyId_, environmentTypeId_, false);

		// check if exists already with default locale
		final Search search = new Search(EnvironmentLocale.class);
		search.addFilterEqual("name", name_);
		search.addFilterEqual("localeCode", localeCode_);
		final List<EnvironmentLocale> foundTypes = dao.search(EnvironmentLocale.class, search);
		// check if exists for the same company
		if ((foundTypes != null) && !foundTypes.isEmpty())
		{
			for (final EnvironmentLocale locale : foundTypes)
			{
				final Environment environment = dao.getById(Environment.class, locale.getEnvironmentId());
				if (environment.getCompanyId().equals(companyId_))
				{
					throw new DuplicateNameException();
				}
			}
		}
		// add new environment
		final Environment environment = new Environment();
		environment.setEnvironmentTypeId(environmentTypeId_);
		environment.setCompanyId(companyId_);
		final Integer environmentId = dao.addAndReturnId(environment);
		// TODO - read defaults from application settings
		saveEnvironmentLocale(environmentId, name_, localeCode_, Locale.DEFAULT_SORT_ORDER);
		return getEnvironment(environmentId);
	}

	private void checkValidEnvironmentType(Integer companyId_, Integer environmentTypeId_, boolean isGroupType_)
	{
		if (environmentTypeId_ == null)
		{
			// optional for groups, required for single environments
			if (!isGroupType_)
			{
				throw new IllegalArgumentException("EnvironmentTypeId is null.");
			}
		}
		final EnvironmentType environmentType = dao.getById(EnvironmentType.class, environmentTypeId_);
		if (environmentType == null)
		{
			throw new NotFoundException("EnvironmentType#" + environmentTypeId_);
		}
		if (environmentType.isGroupType() != isGroupType_)
		{
			throw new IllegalArgumentException("Group type no match for EnvironmentType#" + environmentTypeId_);
		}

		if (!Company.SYSTEM_WIDE_COMPANY_ID.equals(companyId_) && !Company.SYSTEM_WIDE_COMPANY_ID.equals(environmentType.getCompanyId())
				&& !companyId_.equals(environmentType.getCompanyId()))
		{
			throw new IllegalArgumentException("Different companies for environment and its type.");
		}
	}

	@Override
	public void saveParentDependableEnvironments(final Integer companyId_, final Integer parentEnvironmentId_, final List<Integer> environmentIds_) throws Exception
	{
		final Environment parentEnvironment = dao.getById(Environment.class, parentEnvironmentId_);
		if (parentEnvironment == null)
		{
			throw new NotFoundException("Parent environment not found: " + parentEnvironmentId_);
		}
		// check if valid parent environment for company
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(parentEnvironmentId_);
		checkValidEnvironmentSelectionForCompany(companyId_, ids, Environment.class);
		// find all children environments and check if they match valid type
		Search search = new Search(Environment.class);
		search.addFilterIn("id", environmentIds_);
		final List<Environment> environments = dao.search(Environment.class, search);
		// check if valid children environments for company
		checkValidEnvironmentSelectionForCompany(companyId_, environments);
		// check if all children of the same type and this type is a valid
		// child for parent
		checkValidParentChildSelection(parentEnvironment, environments);
		// delete old environments for parent
		search = new Search(ParentDependableEnvironment.class);
		search.addFilterEqual("parentEnvironmentId", parentEnvironmentId_);
		final List<?> foundTypes = dao.search(ParentDependableEnvironment.class, search);
		dao.delete(foundTypes);
		// add new group environments
		if (environmentIds_ != null)
		{
			for (final Integer environmentId : environmentIds_)
			{
				final ParentDependableEnvironment childEnvironment = new ParentDependableEnvironment();
				childEnvironment.setCompanyId(companyId_);
				childEnvironment.setEnvironmentId(environmentId);
				childEnvironment.setParentEnvironmentId(parentEnvironmentId_);
				dao.addAndReturnId(childEnvironment);
			}
		}
	}

	private void checkValidParentChildSelection(Environment parentEnvironment_, List<Environment> environments_)
	{
		if (environments_ == null || environments_.isEmpty())
		{
			return;
		}

		Search search = new Search(EnvironmentType.class);
		search.addFilterEqual("parentEnvironmentTypeId", parentEnvironment_.getEnvironmentTypeId());
		final List<EnvironmentType> childrenTypes = dao.search(EnvironmentType.class, search);
		if (childrenTypes == null || childrenTypes.isEmpty())
		{
			throw new InvalidParentChildEnvironmentException("No children types defined for parent environment: " + parentEnvironment_.getId());
		}
		final List<Integer> childrenTypeIds = DomainUtil.extractEntityIds(childrenTypes);

		Integer typeId = null;
		for (Environment child : environments_)
		{
			// only for first record
			if (typeId == null)
			{
				typeId = child.getEnvironmentTypeId();
			}
			// all types should match
			if (child.getEnvironmentTypeId() != typeId)
			{
				throw new InvalidParentChildEnvironmentException("Children are of different types: " + typeId + " and " + child.getEnvironmentTypeId());
			}
			if (!childrenTypeIds.contains(child.getEnvironmentTypeId()))
			{
				throw new InvalidParentChildEnvironmentException("Parent type: " + parentEnvironment_.getEnvironmentTypeId() + " doesn't support child type "
						+ child.getEnvironmentTypeId());
			}
		}
	}

	@Override
	public EnvironmentGroup addEnvironmentGroup(Integer companyId_, String name_, String description_, List<Integer> environmentIds_) throws Exception
	{
		return addEnvironmentGroup(companyId_, null, name_, description_, environmentIds_);
	}

	@Override
	public EnvironmentGroup addEnvironmentGroup(final Integer companyId_, Integer environmentTypeId_, final String name_, final String description_,
			final List<Integer> environmentIds_) throws Exception
	{
		final Company company = dao.getById(Company.class, companyId_);
		if (company == null)
		{
			throw new NotFoundException("Company not found: " + companyId_);
		}
		// validate environmnets velong to the same company
		checkValidEnvironmentSelectionForCompany(companyId_, environmentIds_, Environment.class);
		// validate type for group
		checkValidEnvironmentType(companyId_, environmentTypeId_, true);

		final EnvironmentGroup group = new EnvironmentGroup();
		group.setCompanyId(companyId_);
		group.setEnvironmentTypeId(environmentTypeId_);
		group.setName(name_);
		group.setDescription(description_);
		// add group
		final Integer groupId = dao.addAndReturnId(group);
		// add group environments
		if (environmentIds_ != null)
		{
			for (final Integer environmentId : environmentIds_)
			{
				final EnvironmentGroupEnvironment groupEnvironment = new EnvironmentGroupEnvironment();
				groupEnvironment.setEnvironmentGroupId(groupId);
				groupEnvironment.setEnvironmentId(environmentId);
				dao.addAndReturnId(groupEnvironment);
			}
		}
		// return populated group
		return getEnvironmentGroup(groupId);
	}

	@Override
	public List<EnvironmentGroup> addGeneratedEnvironmentGroups(final Integer companyId_, final List<Integer> environmentIds_) throws Exception
	{
		return addGeneratedEnvironmentGroups(companyId_, null, environmentIds_);
	}

	@Override
	public List<EnvironmentGroup> addGeneratedEnvironmentGroups(final Integer companyId_, Integer environmentTypeId_, final List<Integer> environmentIds_) throws Exception
	{
		checkValidEnvironmentSelectionForCompany(companyId_, environmentIds_, Environment.class);
		// separate environments by types
		final Map<Integer, List<Integer>> typedEnvironments = breakEnvironmentsByType(environmentIds_);
		// get all typed groups
		final List<List<Integer>> allValues = new ArrayList<List<Integer>>(typedEnvironments.values());
		// group all possible combinations between types
		final List<List<Integer>> groupedValues = NumberUtil.buildCombinations(allValues);
		// insert groups to db
		final List<EnvironmentGroup> createdGroups = new ArrayList<EnvironmentGroup>();
		for (final List<Integer> singleGroup : groupedValues)
		{
			createdGroups.add(addEnvironmentGroup(companyId_, environmentTypeId_, "Generated: " + singleGroup.toString(), "Generated", singleGroup));
		}
		return createdGroups;
	}

	/**
	 * Separate all environments by type
	 * 
	 * @param environments_
	 * @return
	 */
	private Map<Integer, List<Integer>> breakEnvironmentsByType(final List<Integer> environmentIds_)
	{
		final Search search = new Search(Environment.class);
		search.addFilterIn("id", environmentIds_);
		final List<Environment> environments = dao.search(Environment.class, search);

		final Map<Integer, List<Integer>> typedEnvironments = new HashMap<Integer, List<Integer>>();
		for (final Environment environment : environments)
		{
			List<Integer> singleTypedList = typedEnvironments.get(environment.getEnvironmentTypeId());
			if (singleTypedList == null)
			{
				singleTypedList = new ArrayList<Integer>();
				typedEnvironments.put(environment.getEnvironmentTypeId(), singleTypedList);
			}
			singleTypedList.add(environment.getId());
		}
		return typedEnvironments;
	}

	@Override
	public EnvironmentProfile addEnvironmentProfile(final Integer companyId_, final String name_, final String description_, final List<Integer> environmentGroupIds_)
			throws Exception
	{
		final Company company = dao.getById(Company.class, companyId_);
		if (company == null)
		{
			throw new NotFoundException("Company not found: " + companyId_);
		}
		checkValidEnvironmentSelectionForCompany(companyId_, environmentGroupIds_, EnvironmentGroup.class);

		final EnvironmentProfile profile = new EnvironmentProfile();
		profile.setName(name_);
		profile.setDescription(description_);
		profile.setCompanyId(companyId_);
		// add profile
		final Integer profileId = dao.addAndReturnId(profile);
		// add profile groups
		for (final Integer environmentGroupId : environmentGroupIds_)
		{
			final EnvironmentProfileEnvironmentGroup profileEnvironment = new EnvironmentProfileEnvironmentGroup();
			profileEnvironment.setEnvironmentGroupId(environmentGroupId);
			profileEnvironment.setEnvironmentProfileId(profileId);
			dao.addAndReturnId(profileEnvironment);
		}
		// return populated profile
		return getEnvironmentProfile(profileId);
	}

	@Override
	public EnvironmentType addEnvironmentType(final Integer parentEnvironmentTypeId_, final String name_, boolean isGroupType_) throws Exception
	{
		return addEnvironmentType(Company.SYSTEM_WIDE_COMPANY_ID, parentEnvironmentTypeId_, name_, isGroupType_, Locale.DEFAULT_LOCALE);
	}

	@Override
	public EnvironmentType addEnvironmentType(final Integer companyId_, final Integer parentEnvironmentTypeId_, final String name_, boolean isGroupType_, final String localeCode_)
			throws Exception
	{

		if (!Company.SYSTEM_WIDE_COMPANY_ID.equals(companyId_))
		{
			final Company company = dao.getById(Company.class, companyId_);
			if (company == null)
			{
				throw new NotFoundException("Company not found: " + companyId_);
			}
		}
		if (parentEnvironmentTypeId_ != null)
		{
			final EnvironmentType parentEnvironmentType = dao.getById(EnvironmentType.class, parentEnvironmentTypeId_);
			if (parentEnvironmentType == null)
			{
				throw new NotFoundException("Environment type for parent not found: " + parentEnvironmentTypeId_);
			}
			if (!Company.SYSTEM_WIDE_COMPANY_ID.equals(companyId_) && !Company.SYSTEM_WIDE_COMPANY_ID.equals(parentEnvironmentType.getCompanyId())
					&& !companyId_.equals(parentEnvironmentType.getCompanyId()))
			{
				throw new IllegalArgumentException("Different companies for environment type and its parent.");
			}
		}
		// check if exists already with this locale
		final Search search = new Search(EnvironmentTypeLocale.class);
		search.addFilterEqual("name", name_);
		search.addFilterEqual("localeCode", localeCode_);
		final List<EnvironmentTypeLocale> foundTypes = dao.search(EnvironmentTypeLocale.class, search);
		for (final EnvironmentTypeLocale locale : foundTypes)
		{
			final EnvironmentType environmentType = dao.getById(EnvironmentType.class, locale.getEnvironmentTypeId());
			if (environmentType.getCompanyId().equals(companyId_))
			{
				throw new DuplicateNameException();
			}
		}
		// add new environment type
		final EnvironmentType environmentType = new EnvironmentType();
		environmentType.setParentEnvironmentTypeId(parentEnvironmentTypeId_);
		environmentType.setCompanyId(companyId_);
		environmentType.setGroupType(isGroupType_);
		final Integer environmentTypeId = dao.addAndReturnId(environmentType);
		// TODO - read defaults from application settings
		saveEnvironmentTypeLocale(environmentTypeId, name_, localeCode_, Locale.DEFAULT_SORT_ORDER);
		return getEnvironmentType(environmentTypeId);
	}

	@Override
	public void deleteEnvironment(final Integer environmentId_) throws Exception
	{
		// check if used in any groups before deleting
		Search search = new Search(EnvironmentGroupEnvironment.class);
		search.addFilterEqual("environmentId", environmentId_);
		List<?> foundTypes = dao.search(EnvironmentGroupEnvironment.class, search);
		if ((foundTypes != null) && !foundTypes.isEmpty())
		{
			throw new DeletingUsedEntityException(EnvironmentGroupEnvironment.class.getSimpleName());
		}
		// delete all locales
		search = new Search(EnvironmentLocale.class);
		search.addFilterEqual("environmentId", environmentId_);
		foundTypes = dao.search(EnvironmentLocale.class, search);
		dao.delete(foundTypes);
		// delete if not used anywhere
		dao.delete(Environment.class, environmentId_);
	}

	@Override
	public void deleteTag(final Integer tagId_) throws Exception
	{

		// check if used in any groups before deleting
		Search search = new Search(TestCaseTag.class);
		search.addFilterEqual("tagId", tagId_);
		List<?> foundTypes = dao.search(TestCaseTag.class, search);
		if ((foundTypes != null) && !foundTypes.isEmpty())
		{
			throw new DeletingUsedEntityException(TestCaseTag.class.getSimpleName());
		}
		// delete if not used anywhere
		dao.delete(Tag.class, tagId_);
	}

	@Override
	public void deleteEnvironmentGroup(final Integer environmentGroupId_) throws Exception
	{
		// check if used in any environments before deleting
		Search search = new Search(EnvironmentProfileEnvironmentGroup.class);
		search.addFilterEqual("environmentGroupId", environmentGroupId_);
		List<?> foundTypes = dao.search(EnvironmentProfileEnvironmentGroup.class, search);
		if ((foundTypes != null) && !foundTypes.isEmpty())
		{
			throw new DeletingUsedEntityException(EnvironmentProfile.class.getSimpleName());
		}
		search = new Search(TestRunResult.class);
		search.addFilterEqual("environmentGroupId", environmentGroupId_);
		foundTypes = dao.search(TestRunResult.class, search);
		if ((foundTypes != null) && !foundTypes.isEmpty())
		{
			throw new DeletingUsedEntityException(TestRunResult.class.getSimpleName());
		}
		// continue if not used anywhere
		// delete group environments
		search = new Search(EnvironmentGroupEnvironment.class);
		search.addFilterEqual("environmentGroupId", environmentGroupId_);
		foundTypes = dao.search(EnvironmentGroupEnvironment.class, search);
		dao.delete(foundTypes);
		// delete group
		dao.delete(EnvironmentGroup.class, environmentGroupId_);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.utest.domain.service.EnvironmentService#deleteEnvironmentProfile
	 * (com.utest.domain.User, java.lang.Integer)
	 */
	@Override
	public void deleteEnvironmentProfile(final Integer environmentProfileId_) throws Exception
	{
		checkEnvironmentProfileUsage(environmentProfileId_);
		// continue if not used anywhere
		// delete profile groups
		final Search search = new Search(EnvironmentProfileEnvironmentGroup.class);
		search.addFilterEqual("environmentProfileId", environmentProfileId_);
		final List<?> foundTypes = dao.search(EnvironmentProfileEnvironmentGroup.class, search);
		dao.delete(foundTypes);
		// delete group
		dao.delete(EnvironmentProfile.class, environmentProfileId_);
	}

	/**
	 * Checks if environment profile is used in any of the testing entities and
	 * throws the exception if found.
	 * 
	 * @param auth_
	 * @param environmentProfileId_
	 * @throws DeletingUsedEntityException
	 */
	private void checkEnvironmentProfileUsage(final Integer environmentProfileId_) throws DeletingUsedEntityException
	{
		// check if used in any environments before deleting
		String usedInEntities = "";
		if (isEnvironmentProfileUsed(environmentProfileId_, Product.class))
		{
			usedInEntities += Product.class.getSimpleName() + ";";
		}
		if (isEnvironmentProfileUsed(environmentProfileId_, TestCaseVersion.class))
		{
			usedInEntities += TestCaseVersion.class.getSimpleName() + ";";
		}
		if (isEnvironmentProfileUsed(environmentProfileId_, TestPlan.class))
		{
			usedInEntities += TestPlan.class.getSimpleName() + ";";
		}
		if (isEnvironmentProfileUsed(environmentProfileId_, TestRun.class))
		{
			usedInEntities += TestRun.class.getSimpleName() + ";";
		}
		if (isEnvironmentProfileUsed(environmentProfileId_, TestRunTestCase.class))
		{
			usedInEntities += TestRunTestCase.class.getSimpleName() + ";";
		}
		if (isEnvironmentProfileUsed(environmentProfileId_, TestRunTestCaseAssignment.class))
		{
			usedInEntities += TestRunTestCaseAssignment.class.getSimpleName() + ";";
		}
		if (isEnvironmentProfileUsed(environmentProfileId_, TestSuite.class))
		{
			usedInEntities += TestSuite.class.getSimpleName() + ";";
		}
		if (isEnvironmentProfileUsed(environmentProfileId_, TestSuiteTestCase.class))
		{
			usedInEntities += TestSuiteTestCase.class.getSimpleName() + ";";
		}
		if (usedInEntities.length() > 0)
		{
			throw new DeletingUsedEntityException(usedInEntities);
		}
	}

	/**
	 * Checks if environment is assigned to any EnvironmentDependable entity of
	 * a certain type.
	 * 
	 * @param auth_
	 *            - logged in user
	 * @param environmentProfileId_
	 *            - profile to check
	 * @param type_
	 *            - type to check
	 * @return
	 */
	private <T extends EnvironmentDependable> boolean isEnvironmentProfileUsed(final Integer environmentProfileId_, final Class<T> type_)
	{
		final Search search = new Search(type_);
		search.addFilterEqual("environmentProfileId", environmentProfileId_);
		final List<?> foundTypes = dao.search(type_, search);
		return ((foundTypes != null) && !foundTypes.isEmpty());
	}

	@Override
	public void deleteEnvironmentType(final Integer environmentTypeId_) throws Exception
	{
		// check if used in any environments before deleting
		Search search = new Search(Environment.class);
		search.addFilterEqual("environmentTypeId", environmentTypeId_);
		List<?> foundTypes = dao.search(Environment.class, search);
		if ((foundTypes != null) && !foundTypes.isEmpty())
		{
			throw new DeletingUsedEntityException(Environment.class.getSimpleName());
		}
		search = new Search(EnvironmentType.class);
		search.addFilterEqual("parentEnvironmentTypeId", environmentTypeId_);
		foundTypes = dao.search(EnvironmentType.class, search);
		if ((foundTypes != null) && !foundTypes.isEmpty())
		{
			throw new DeletingUsedEntityException("Used as parent type for existing type: " + EnvironmentType.class.getSimpleName());
		}
		// delete all locales
		search = new Search(EnvironmentTypeLocale.class);
		search.addFilterEqual("environmentTypeId", environmentTypeId_);
		foundTypes = dao.search(EnvironmentTypeLocale.class, search);
		dao.delete(foundTypes);
		// delete if not used anywhere
		dao.delete(EnvironmentType.class, environmentTypeId_);
	}

	@Override
	public UtestSearchResult findEnvironmentGroups(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(EnvironmentGroup.class, search_);
	}

	@Override
	public List<EnvironmentGroup> getEnvironmentGroupsForProfile(final Integer environmentProfileId_) throws Exception
	{
		Search search = new Search(EnvironmentProfileEnvironmentGroup.class);
		search.addFilterEqual("environmentProfileId", environmentProfileId_);
		final List<EnvironmentProfileEnvironmentGroup> foundGroups = dao.search(EnvironmentProfileEnvironmentGroup.class, search);
		if ((foundGroups != null) && !foundGroups.isEmpty())
		{
			final List<Integer> ids = new ArrayList<Integer>();
			for (final EnvironmentProfileEnvironmentGroup idHolder : foundGroups)
			{
				ids.add(idHolder.getEnvironmentGroupId());
			}
			search = new Search(EnvironmentGroup.class);
			search.addFilterIn("id", ids);
			search.addFilterNotEqual("deprecated", true);
			return dao.search(EnvironmentGroup.class, search);
		}
		else
		{
			return new ArrayList<EnvironmentGroup>();
		}
	}

	@Override
	public List<Environment> getParentDependableEnvironments(final Integer companyId_, final Integer parentEnvironmentId_) throws Exception
	{
		Search search = new Search(ParentDependableEnvironment.class);
		search.addFilterEqual("parentEnvironmentId", parentEnvironmentId_);
		search.addFilterEqual("companyId", companyId_);
		final List<ParentDependableEnvironment> foundGroups = dao.search(ParentDependableEnvironment.class, search);
		if ((foundGroups != null) && !foundGroups.isEmpty())
		{
			final List<Integer> ids = new ArrayList<Integer>();
			for (final ParentDependableEnvironment idHolder : foundGroups)
			{
				ids.add(idHolder.getEnvironmentId());
			}
			search = new Search(Environment.class);
			search.addFilterIn("id", ids);
			return dao.search(Environment.class, search);
		}
		else
		{
			return new ArrayList<Environment>();
		}
	}

	@Override
	public UtestSearchResult findEnvironmentProfiles(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(EnvironmentProfile.class, search_);
	}

	@Override
	public UtestSearchResult findEnvironmentTypes(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(EnvironmentType.class, search_);
	}

	@Override
	public UtestSearchResult findEnvironments(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(Environment.class, search_);
	}

	@Override
	public UtestSearchResult findTags(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(Tag.class, search_);
	}

	@Override
	public List<Environment> getEnvironmentsForGroup(final Integer environmentGroupId_) throws Exception
	{
		Search search = new Search(EnvironmentGroupEnvironment.class);
		search.addFilterEqual("environmentGroupId", environmentGroupId_);
		final List<EnvironmentGroupEnvironment> foundGroups = dao.search(EnvironmentGroupEnvironment.class, search);
		if ((foundGroups != null) && !foundGroups.isEmpty())
		{
			final List<Integer> ids = new ArrayList<Integer>();
			for (final EnvironmentGroupEnvironment idHolder : foundGroups)
			{
				ids.add(idHolder.getEnvironmentId());
			}
			search = new Search(Environment.class);
			search.addFilterIn("id", ids);
			return dao.search(Environment.class, search);
		}
		else
		{
			return new ArrayList<Environment>();
		}
	}

	@Override
	public List<Environment> getEnvironmentsForType(final Integer environmentTypeId_) throws Exception
	{
		final Search search = new Search(Environment.class);
		search.addFilterEqual("environmentTypeId", environmentTypeId_);
		return dao.search(Environment.class, search);
	}

	@Override
	public Environment getEnvironment(final Integer environmentId_) throws Exception
	{
		final Environment environment = dao.getById(Environment.class, environmentId_);
		if (environment == null)
		{
			throw new NotFoundException("Environment not found: " + environmentId_);
		}
		return environment;
	}

	@Override
	public Tag getTag(final Integer tagId_) throws Exception
	{
		final Tag tag = dao.getById(Tag.class, tagId_);
		if (tag == null)
		{
			throw new NotFoundException("Tag not found: " + tagId_);
		}
		return tag;
	}

	@Override
	public EnvironmentGroup getEnvironmentGroup(final Integer environmentGroupId_) throws Exception
	{
		final EnvironmentGroup environment = dao.getById(EnvironmentGroup.class, environmentGroupId_);
		if (environment == null)
		{
			throw new NotFoundException("EnvironmentGroup not found: " + environmentGroupId_);
		}
		return environment;
	}

	@Override
	public EnvironmentProfile getEnvironmentProfile(final Integer environmentProfileId_) throws Exception
	{
		final EnvironmentProfile environment = dao.getById(EnvironmentProfile.class, environmentProfileId_);
		if (environment == null)
		{
			throw new NotFoundException("EnvironmentProfile not found: " + environmentProfileId_);
		}
		return environment;
	}

	@Override
	public EnvironmentType getEnvironmentType(final Integer environmentTypeId_) throws Exception
	{
		final EnvironmentType environment = dao.getById(EnvironmentType.class, environmentTypeId_);
		if (environment == null)
		{
			throw new NotFoundException("EnvironmentType not found: " + environmentTypeId_);
		}
		return environment;
	}

	@Override
	public EnvironmentProfile saveEnvironmentGroupsForProfile(final Integer environmentProfileId_, final List<Integer> environmentGroupIds_) throws Exception
	{
		// TODO - do we need to check usage?
		// checkEnvironmentProfileUsage(environmentProfileId_);
		// continue if not used anywhere
		// delete old environment groups for profile
		final Search search = new Search(EnvironmentProfileEnvironmentGroup.class);
		search.addFilterEqual("environmentProfileId", environmentProfileId_);
		final List<?> foundTypes = dao.search(EnvironmentProfileEnvironmentGroup.class, search);
		dao.delete(foundTypes);
		// add new group environments
		if (environmentGroupIds_ != null)
		{
			for (final Integer environmentGroupId : environmentGroupIds_)
			{
				final EnvironmentProfileEnvironmentGroup groupEnvironment = new EnvironmentProfileEnvironmentGroup();
				groupEnvironment.setEnvironmentProfileId(environmentProfileId_);
				groupEnvironment.setEnvironmentGroupId(environmentGroupId);
				dao.addAndReturnId(groupEnvironment);
			}
		}
		// update time stamp
		final EnvironmentProfile profile = dao.getById(EnvironmentProfile.class, environmentProfileId_);
		profile.setDescription(profile.getDescription() + " [updated on: " + new Date().toString() + "]");
		// return updated profile
		return dao.merge(profile);
	}

	@Override
	public EnvironmentProfile saveEnvironmentProfile(final EnvironmentProfile environmentProfile_) throws Exception
	{
		// return updated group
		return dao.merge(environmentProfile_);
	}

	@Override
	public EnvironmentGroup saveEnvironmentGroup(final Integer environmentGroupId_, String name_, String description_, Integer originalVersionId_) throws Exception
	{
		final EnvironmentGroup group = dao.getById(EnvironmentGroup.class, environmentGroupId_);
		if (group == null)
		{
			throw new NotFoundException("EnvironmentGroup not found: " + environmentGroupId_);
		}
		group.setName(name_);
		group.setDescription(description_);
		group.setVersion(originalVersionId_);
		return dao.merge(group);

	}

	@Override
	public EnvironmentGroup saveEnvironmentsForGroup(final Integer environmentGroupId_, final List<Integer> environmentIds_) throws Exception
	{
		final EnvironmentGroup group = dao.getById(EnvironmentGroup.class, environmentGroupId_);
		if (group == null)
		{
			throw new NotFoundException("EnvironmentGroup not found: " + environmentGroupId_);
		}
		if (group.isDeprecated())
		{
			throw new ChangingActivatedEntityException(EnvironmentGroup.class.getSimpleName());
		}
		// cannot change group after execution
		if (group.isExecuted())
		{
			return deprecateAndCloneExecutedGroup(group, environmentIds_);
		}
		// delete old environments for group
		final Search search = new Search(EnvironmentGroupEnvironment.class);
		search.addFilterEqual("environmentGroupId", environmentGroupId_);
		final List<?> foundTypes = dao.search(EnvironmentGroupEnvironment.class, search);
		dao.delete(foundTypes);
		// add new group environments
		if (environmentIds_ != null)
		{
			for (final Integer environmentId : environmentIds_)
			{
				final EnvironmentGroupEnvironment groupEnvironment = new EnvironmentGroupEnvironment();
				groupEnvironment.setEnvironmentGroupId(environmentGroupId_);
				groupEnvironment.setEnvironmentId(environmentId);
				dao.addAndReturnId(groupEnvironment);
			}
		}
		// return updated group
		return dao.merge(group);
	}

	/**
	 * If group is already assigned to any execution task (TestRunResult) it
	 * should be deprecated for future use in profiles. Group's contents should
	 * be copied to a new group and new group should be associated with all
	 * profiles where the original group was associated.
	 * 
	 * @param auth
	 *            - user doing the modifications
	 * @param group
	 *            - group to be copied
	 * @param environmentIds
	 *            - environments assigned to the group
	 * @return - copied environment
	 */
	private EnvironmentGroup deprecateAndCloneExecutedGroup(final EnvironmentGroup group_, final List<Integer> environmentIds_)
	{
		// deprecate original group
		group_.setDeprecated(true);
		dao.merge(group_);

		// clone group
		final EnvironmentGroup newGroup = new EnvironmentGroup();
		newGroup.setName("Cloned from group: " + group_.getId() + " [" + group_.getName() + "]");
		newGroup.setDescription(group_.getDescription());
		final Integer newGroupId = dao.addAndReturnId(newGroup);
		// add environments to new group
		for (final Integer environmentId : environmentIds_)
		{
			final EnvironmentGroupEnvironment groupEnvironment = new EnvironmentGroupEnvironment();
			groupEnvironment.setEnvironmentGroupId(newGroupId);
			groupEnvironment.setEnvironmentId(environmentId);
			dao.addAndReturnId(groupEnvironment);
		}

		// add new group to all profiles where old group was used
		final Search search = new Search(EnvironmentProfileEnvironmentGroup.class);
		search.addFilterEqual("environmentGroupId", group_.getId());
		final List<EnvironmentProfileEnvironmentGroup> foundProfiles = dao.search(EnvironmentProfileEnvironmentGroup.class, search);
		for (final EnvironmentProfileEnvironmentGroup profile : foundProfiles)
		{
			final EnvironmentProfileEnvironmentGroup profileGroup = new EnvironmentProfileEnvironmentGroup();
			profileGroup.setEnvironmentGroupId(newGroupId);
			profileGroup.setEnvironmentProfileId(profile.getEnvironmentProfileId());
			dao.addAndReturnId(profileGroup);
		}
		// return updated group
		return dao.getById(EnvironmentGroup.class, newGroupId);
	}

	@Override
	public EnvironmentLocale saveEnvironmentLocale(final Integer environmentId_, final String name_, final String localeCode_, final Integer sortOrder_) throws Exception
	{
		// TODO - check for duplicate name
		final Search search = new Search(EnvironmentLocale.class);
		search.addFilterEqual("environmentId", environmentId_);
		search.addFilterEqual("localeCode", localeCode_);
		EnvironmentLocale locale = (EnvironmentLocale) dao.searchUnique(EnvironmentLocale.class, search);
		if (locale == null)
		{
			// add default locale for this environment
			locale = new EnvironmentLocale();
		}
		locale.setName(name_);
		locale.setEnvironmentId(environmentId_);
		locale.setLocaleCode(localeCode_);
		locale.setSortOrder(sortOrder_);
		return dao.merge(locale);
	}

	@Override
	public EnvironmentTypeLocale saveEnvironmentTypeLocale(final Integer environmentTypeId_, final String name_, final String localeCode_, final Integer sortOrder_)
			throws Exception
	{
		// TODO - check for duplicate name
		final Search search = new Search(EnvironmentTypeLocale.class);
		search.addFilterEqual("environmentTypeId", environmentTypeId_);
		search.addFilterEqual("localeCode", localeCode_);
		EnvironmentTypeLocale locale = (EnvironmentTypeLocale) dao.searchUnique(EnvironmentTypeLocale.class, search);
		if (locale == null)
		{
			// add default locale for this environment
			locale = new EnvironmentTypeLocale();
		}
		locale.setName(name_);
		locale.setEnvironmentTypeId(environmentTypeId_);
		locale.setLocaleCode(localeCode_);
		locale.setSortOrder(sortOrder_);
		return dao.merge(locale);
	}

	private <T extends CompanyDependable> void checkValidEnvironmentSelectionForCompany(final Integer companyId_, final List<Integer> companyDependableEntitiesIds_,
			final Class<T> type_) throws Exception
	{
		if (!isValidEnvironmentSelectionForCompany(companyId_, companyDependableEntitiesIds_, type_))
		{
			throw new IllegalArgumentException("Invalid environment selection for company: " + companyId_);
		}
	}

	private <T extends CompanyDependable> void checkValidEnvironmentSelectionForCompany(final Integer companyId_, final List<T> companyDependableEntities_) throws Exception
	{
		if (companyDependableEntities_ == null || companyDependableEntities_.isEmpty())
		{
			return;
		}
		if (!isValidEnvironmentSelectionForCompany(companyId_, companyDependableEntities_))
		{
			throw new IllegalArgumentException("Invalid environment selection for company: " + companyId_);
		}
	}

	@Override
	public <T extends CompanyDependable> boolean isValidEnvironmentSelectionForCompany(final Integer companyId_, final List<Integer> companyDependableEntitiesIds_,
			final Class<T> type_) throws Exception
	{
		final Search search = new Search(type_);
		search.addFilterIn("id", companyDependableEntitiesIds_);
		final List<T> foundTypes = dao.search(type_, search);
		return isValidEnvironmentSelectionForCompany(companyId_, foundTypes);
	}

	private <T extends CompanyDependable> boolean isValidEnvironmentSelectionForCompany(final Integer companyId_, final List<T> companyDependableEntities_) throws Exception
	{
		for (final T foundType : companyDependableEntities_)
		{
			final Integer companyId = foundType.getCompanyId();
			if (!Company.SYSTEM_WIDE_COMPANY_ID.equals(companyId) && !companyId.equals(companyId_))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isValidEnvironmentGroupSelectionForProfile(final Integer parentEnvironmentProfile_, final List<Integer> environmentGroupIds_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		// TODO - should we allow to have missing parent environment profile?
		if (parentEnvironmentProfile_ == null)
		{
			return true;
		}
		final List<EnvironmentGroup> parentGroups = getEnvironmentGroupsForProfile(parentEnvironmentProfile_);
		final List<Integer> parentGroupIds = DomainUtil.extractEntityIds(parentGroups);
		return parentGroupIds.containsAll(environmentGroupIds_);
	}
}
