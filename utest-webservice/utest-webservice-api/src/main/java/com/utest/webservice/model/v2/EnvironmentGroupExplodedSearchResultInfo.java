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
package com.utest.webservice.model.v2;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "searchResult")
public class EnvironmentGroupExplodedSearchResultInfo implements SearchResultInfo<EnvironmentGroupExplodedInfo>
{
	List<EnvironmentGroupExplodedInfo>	rows	= new ArrayList<EnvironmentGroupExplodedInfo>();
	@XmlElement(required = true)
	Integer								totalResults;

	@Override
	public void addRow(final EnvironmentGroupExplodedInfo row)
	{
		rows.add(row);
	}

	@Override
	@XmlElement(name = "environmentgroups")
	public List<EnvironmentGroupExplodedInfo> getRows()
	{
		return rows;
	}

	@Override
	public Integer getTotalResults()
	{
		return totalResults;
	}

	@Override
	public void setRows(final List<EnvironmentGroupExplodedInfo> rows)
	{
		this.rows = rows;
	}

	@Override
	public void setTotalResults(final Integer totalResults)
	{
		this.totalResults = totalResults;
	}

}
