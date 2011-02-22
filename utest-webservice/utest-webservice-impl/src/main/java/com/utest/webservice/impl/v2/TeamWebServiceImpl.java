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

import com.utest.domain.AccessRole;
import com.utest.domain.Permission;
import com.utest.domain.Team;
import com.utest.domain.User;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.TeamService;
import com.utest.webservice.api.v2.TeamWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.PermissionInfo;
import com.utest.webservice.model.v2.RoleInfo;
import com.utest.webservice.model.v2.TeamInfo;
import com.utest.webservice.model.v2.TeamSearchResultInfo;
import com.utest.webservice.model.v2.UserInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

@Path("/teams/")
public class TeamWebServiceImpl extends BaseWebServiceImpl implements TeamWebService
{
	private final TeamService	teamService;

	public TeamWebServiceImpl(final ObjectBuilderFactory objectBuildFactory, final TeamService teamService)
	{
		super(objectBuildFactory);
		this.teamService = teamService;
	}

	@PUT
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEAM_EDIT })
	public TeamInfo updateTeam(@Context final UriInfo ui_, @PathParam("id") final Integer teamId, @FormParam("companyId") final Integer companyId_,
			@FormParam("name") final String name_, @FormParam("description") final String description_, @FormParam("originalVersionId") final Integer originalVersionId_)
			throws Exception
	{
		final Team team = teamService.saveTeam(teamId, name_, description_, originalVersionId_);
		return objectBuilderFactory.toInfo(TeamInfo.class, team, ui_.getBaseUriBuilder());
	}

	@POST
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEAM_EDIT })
	public TeamInfo createTeam(@Context final UriInfo ui_, @FormParam("companyId") final Integer companyId_, @FormParam("name") final String name_,
			@FormParam("description") final String description_) throws Exception
	{
		final Team team = teamService.addTeam(companyId_, name_, description_);
		return objectBuilderFactory.toInfo(TeamInfo.class, team, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEAM_VIEW)
	public TeamInfo getTeam(@Context final UriInfo ui_, @PathParam("id") final Integer teamId) throws Exception
	{
		final Team team = teamService.getTeam(teamId);
		return objectBuilderFactory.toInfo(TeamInfo.class, team, ui_.getBaseUriBuilder());
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEAM_VIEW)
	public TeamSearchResultInfo findTeams(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(TeamInfo.class, request, ui_);
		final UtestSearchResult result = teamService.findTeams(search);
		return (TeamSearchResultInfo) objectBuilderFactory.createResult(TeamInfo.class, Team.class, request, result, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEAM_EDIT })
	public Boolean deleteTeam(@Context final UriInfo ui_, @PathParam("id") final Integer teamId_, @FormParam("originalVersionId") final Integer originalVersionId_)
			throws Exception
	{
		teamService.deleteTeam(teamId_, originalVersionId_);
		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/members/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEAM_VIEW)
	public List<UserInfo> getTeamMembers(@Context UriInfo ui_, @PathParam("id") final Integer teamId_) throws Exception
	{
		final List<User> users = teamService.getTeamUsers(teamId_);
		final List<UserInfo> usersInfo = objectBuilderFactory.toInfo(UserInfo.class, users, ui_.getBaseUriBuilder());
		return usersInfo;
	}

	@GET
	@Path("/{id}/members/{memberId}/roles/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEAM_VIEW)
	public List<RoleInfo> getTeamMemberRoles(@Context UriInfo ui_, @PathParam("id") final Integer teamId_, @PathParam("memberId") final Integer userId_) throws Exception
	{
		final List<AccessRole> roles = teamService.getTeamUserRoles(teamId_, userId_);
		final List<RoleInfo> rolesInfo = objectBuilderFactory.toInfo(RoleInfo.class, roles, ui_.getBaseUriBuilder());
		return rolesInfo;
	}

	@GET
	@Path("/{id}/members/{memberId}/permissions/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.TEAM_VIEW)
	public List<PermissionInfo> getTeamMemberPermissions(@Context UriInfo ui_, @PathParam("id") final Integer teamId_, @PathParam("memberId") final Integer userId_)
			throws Exception
	{
		final List<Permission> permissions = teamService.getTeamUserPermissions(teamId_, userId_);
		final List<PermissionInfo> permissionsInfo = objectBuilderFactory.toInfo(PermissionInfo.class, permissions, ui_.getBaseUriBuilder());
		return permissionsInfo;
	}

	@DELETE
	@Path("/{id}/members/{memberId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEAM_EDIT })
	public Boolean deleteTeamMember(@Context final UriInfo ui_, @PathParam("id") final Integer teamId_, @PathParam("memberId") final Integer userId_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		teamService.deleteTeamUser(teamId_, userId_, originalVersionId_);
		return Boolean.TRUE;
	}

	@PUT
	@Path("/{id}/members/{memberId}/roles/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEAM_EDIT })
	public Boolean updateTeamMemberRoles(@Context final UriInfo ui_, @PathParam("id") final Integer teamId_, @PathParam("memberId") final Integer userId_,
			@FormParam("roleIds") final ArrayList<Integer> roleIds_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		teamService.saveTeamUserRoles(teamId_, userId_, roleIds_, originalVersionId_);
		return Boolean.TRUE;
	}

	@PUT
	@Path("/{id}/members/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEAM_EDIT })
	public Boolean updateTeamMembers(@Context final UriInfo ui_, @PathParam("id") final Integer teamId_, @FormParam("userIds") final ArrayList<Integer> userIds_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		teamService.saveTeamUsers(teamId_, userIds_, originalVersionId_);
		return Boolean.TRUE;
	}

	@POST
	@Path("/{id}/members/{memberId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEAM_EDIT })
	public Boolean addTeamMember(@Context final UriInfo ui_, @PathParam("id") final Integer teamId_, @PathParam("memberId") final Integer userId_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		teamService.addTeamUser(teamId_, userId_, originalVersionId_);
		return Boolean.TRUE;
	}

	@POST
	@Path("/{id}/members/{memberId}/roles/{roleId}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.TEAM_EDIT })
	public Boolean addTeamMemberRole(@Context final UriInfo ui_, @PathParam("id") final Integer teamId_, @PathParam("memberId") final Integer userId_,
			@PathParam("roleId") final Integer roleId_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		teamService.addTeamUserRole(teamId_, userId_, roleId_, originalVersionId_);
		return Boolean.TRUE;
	}

}
