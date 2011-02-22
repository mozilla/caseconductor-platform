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
package com.utest.exception;

public class DomainException extends RuntimeException
{

	public interface IErrorMessage
	{
		public String getMessageKey();
	}

	public static enum DomainErrorMessage implements IErrorMessage
	{

		genericErrorMessage("generic.error.message"), noEnoughBudgetMessage("release.out.of.budget"), requiredAttachmentNotFound("attachment.not.found"), releaseInvalidStatus(
				"testcycle.invalid.status"), tmRegistrationExistentCompanyName("company.name.in.use"), tmRegistrationExistentCompanyUrl("company.url.in.use"), emailInUse(
				"email.in.use"), screenNameInUse("screenname.in.use"), hubspotPostExecution("hubspot.post.execution"), invalidConfigurationExecution("invalid.configuration"), invalidUserExecution(
				"invalid.user"), dataRequiredNotFoundException("required.data.not.found"), invalidRemoteAccess("invalid.remote.access"), notApplicableBusinessRule(
				"business.rule.not.applicable"), paymentTransaction("payment.transaction.failed"), releaseAlreadyExistsError("testcycle.already.exists"), projectAlreadyExistsError(
				"project.already.exists"), notEnoughCreditError("not.enough.credit"), applicationTypeIsRequired("application.type.required"), duplicateTestCycleName(
				"duplicate.testcycle.name"), duplicateProjectName("duplicate.project.name"), duplicateName("duplicate.name"), deletingActivatdEntity("deleting.activated.entity"), deletingUsedEntity(
				"deleting.used.entity"), matchingProfileNotFound("matching.profile.not.found"), testCycleClosedException("testcycle.closed"), bugTrackingUrlException(
				"bugtracking.invalid.url"), bugLimitException("bug.limit.reached"), bugTypeChangeException("bug.testcase.invalid.type.change"), noActivePurchaseOrderException(
				"no.active.purchase.order"), testerDoesNotMatchTestCycle("tester.does.not.match.testing.profile"), messagingExceptionQueueNotFound("queue.not.found"), favoriteTestersSharedSetupViolated(
				"favorite.tester.shared.setup"), favoriteTestersLimitViolated("favorite.tester.limit"), testerCantFileBugToTestCycleException(
				"test.cycle.notactive.or.tester.is.not.a.participant"), fileNoFoundException("file.not.found.exception"), activatingIncompleteEntity("activating.incomplete.entity"), changingUsedEntity(
				"changing.used.entity"), activatingNotApprovedEntity("activating.not.approved.entity"), includingMultipleTestCaseVersions("including.multiple.testcase.versions"), assigningSameTestCaseVersionsToTester(
				"assigning.same.testcase.versions.to.tester"), testCaseExecutionBlockedException("testcase.execution.blocked"), testCaseExecutionWithoutRestartException(
				"testcase.execution.without.restart"), includingNotActiveEntity("including.not.activated.entity"), entityNotFound("entity.not.found"), unsupportedEnvironmentSelection(
				"unsupported.environment.selection"), unsupportedTeamSelection("unsupported.team.selection"), teamNotDefined("team.not.defined"), teamMemberNotDefined(
				"team.member.not.defined"), testCycleStepsLimitException("steps.limit.reached"), duplicateStepNumberException("testcase.duplicate.step.number"), invalidParentChildRelationship(
				"invalid.parent.child"), approvingIncompleteEntity("approving.incomplete.entity");

		private String	messageKey;

		private DomainErrorMessage(final String messageKey)
		{
			this.messageKey = messageKey;
		}

		public String getMessageKey()
		{
			return messageKey;
		}
	}

	private static final long	serialVersionUID	= -3232024548396496217L;

	protected IErrorMessage		message				= DomainErrorMessage.genericErrorMessage;

	public DomainException(final Throwable cause)
	{
		super(cause);
	}

	public DomainException(final Throwable cause, final IErrorMessage message)
	{
		super(cause);
		this.message = message;
	}

	public DomainException(final IErrorMessage message, final String text_)
	{
		super(text_);
		this.message = message;
	}

	public DomainException(final IErrorMessage message)
	{
		this(null, message);
	}

	public DomainException(final String string, final Exception e)
	{
		super(string, e);
	}

	public String getErrorMessageKey()
	{
		return message.getMessageKey();
	}
}
