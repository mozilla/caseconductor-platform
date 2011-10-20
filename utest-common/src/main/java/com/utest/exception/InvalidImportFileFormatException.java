/**
 *
 * Licensed under the GNU General Public License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/gpl.txt
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

public class InvalidImportFileFormatException extends DomainException
{
	public static final String	ERROR_TEST_CASE_STEP_MUST_FOLLOW_HEADER		= "test.case.step.must.follow.header";
	public static final String	ERROR_INVALID_TEST_CASE_STEP_NUMBER			= "invalid.test.case.step.number";
	public static final String	ERROR_TEST_CASE_STEP_INSTRUCTION_IS_EMPTY	= "test.case.step.instruction.can.not.be.empty";
	public static final String	ERROR_INVALID_TEST_CASE_HEADER_TYPE			= "test.case.type.can.not.be.empty";

	public InvalidImportFileFormatException()
	{
		super(DomainErrorMessage.invalidImportFormat);
	}

	public InvalidImportFileFormatException(final String message_)
	{
		super(DomainErrorMessage.invalidImportFormat, message_);
	}
}
