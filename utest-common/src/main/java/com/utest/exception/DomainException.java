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

		genericErrorMessage("genericErrorMessage"), noEnoughBudgetMessage("releaseRunOutOfBudget"), requiredAttachmentNotFound("requiredAttachmentNotFound"), releaseInvalidStatus(
				"releaseInvalidStatus"), tmRegistrationExistentCompanyName("tm.registration.company.existent.name"), tmRegistrationExistentCompanyUrl(
				"tm.registration.company.existent.url"), emailInUse("email.in.use"), screenNameInUse("screenName.in.use"), hubspotPostExecution("hubspot.post.execution"), invalidConfigurationExecution(
				"invalid.configuration"), invalidUserExecution("invalid.user"), dataRequiredNotFoundException("no.data.required"), invalidRemoteAccess("invalid.remote.access"), notApplicableBusinessRule(
				"not.applicable.business.rule"), paymentTransaction("payment.transaction"), releaseAlreadyExistsError("releaseAlreadyExistsError"), projectAlreadyExistsError(
				"projectAlreadyExistsError"), notEnoughCreditError("notEnoughCreditError"), applicationTypeIsRequired("applicationTypeIsRequired"), duplicateTestCycleName(
				"duplicateTestCycleName"), duplicateProjectName("duplicateProjectName"), duplicateName("duplicateName"), deletingActivatdEntity("deletingActivatdEntity"), deletingUsedEntity(
				"deletingUsedEntity"), matchingProfileNotFound("matchingProfileNotFound"), testCycleClosedException("testCycle.closed"), bugTrackingUrlException(
				"bugtracking.invalid.url"), bugLimitException("bug.limit.reached"), bugTypeChangeException("bug.testcase.invalid.type.change"), noActivePurchaseOrderException(
				"no.active.purchase.order"), testerDoesNotMatchTestCycle("tester.does.not.match.testcycle.profile"), messagingExceptionQueueNotFound("QueueNotFound"), favoriteTestersSharedSetupViolated(
				"favorite.tester.shared.setup.error"), favoriteTestersLimitViolated("favorite.tester.limit.error"), testerCantFileBugToTestCycleException(
				"test.cycle.notactive.or.tester.is.not.a.participant"), fileNoFoundException("file.no.found.exception"), activatingIncompleteEntity("activatingIncompleteEntity"), changingUsedEntity(
				"changingUsedEntity"), activatingNotApprovedEntity("activatingNotApprovedEntity"), includingMultipleTestCaseVersions("includingMultipleTestCaseVersions"), testCaseExecutionBlockedException(
				"testCaseExecutionBlockedException"), testCaseExecutionWithoutRestartException("testCaseExecutionWithoutRestartException"), includingNotActiveEntity(
				"includingNotActiveEntity"), entityNotFound("entityNotFound"), unsupportedEnvironmentSelection("unsupportedEnvironmentSelection"), testCycleStepsLimitException(
				"steps.limit.reached"), duplicateStepNumberException("duplicate.step.number"), invalidParentChildRelationship("invalid.parent.child"), approvingIncompleteEntity(
				"approvingIncompleteEntity");

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
