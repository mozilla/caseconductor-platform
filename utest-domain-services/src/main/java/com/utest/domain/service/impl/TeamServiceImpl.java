/*
Case Conductor is a Test Case Management system.
Copyright (C) 2011 uTest Inc.

This file is part of Case Conductor.

Case Conductor is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Case Conductor is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Case Conductor.  If not, see <http://www.gnu.org/licenses/>.

*/
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
import java.util.List;

import com.trg.search.Search;
import com.utest.dao.TypelessDAO;
import com.utest.domain.AccessRole;
import com.utest.domain.Company;
import com.utest.domain.Permission;
import com.utest.domain.RolePermission;
import com.utest.domain.Team;
import com.utest.domain.TeamUser;
import com.utest.domain.TeamUserRole;
import com.utest.domain.User;
import com.utest.domain.UserRole;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.TeamService;
import com.utest.domain.service.UserService;
import com.utest.exception.InvalidTeamMemberException;

public class TeamServiceImpl extends BaseServiceImpl implements TeamService
{
	private final TypelessDAO	dao;
	private final UserService	userService;

	/**
	 * Default constructor
	 */
	public TeamServiceImpl(final TypelessDAO dao, final UserService userService)
	{
		super(dao);
		this.dao = dao;
		this.userService = userService;
	}

	@Override
	public Team addTeam(final Integer companyId_, final String name_, final String description_) throws Exception
	{
		// make sure valid company
		getRequiredEntityById(Company.class, companyId_);

		final Team team = new Team();
		team.setCompanyId(companyId_);
		team.setName(name_);
		team.setDescription(description_);
		return dao.merge(team);
	}

	@Override
	public UtestSearchResult findTeams(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(Team.class, search_);
	}

	@Override
	public Team getTeam(final Integer teamId_) throws Exception
	{
		return getRequiredEntityById(Team.class, teamId_);
	}

	@Override
	public void deleteTeam(final Integer teamId_, final Integer originalVersionId_) throws Exception
	{
		// delete team
		final Team team = getRequiredEntityById(Team.class, teamId_);
		team.setVersion(originalVersionId_);
		dao.merge(team);
	}

	@Override
	public Team saveTeam(final Integer teamId_, final String name_, final String description_, final Integer originalVersionId_) throws Exception
	{
		final Team team = getRequiredEntityById(Team.class, teamId_);
		team.setName(name_);
		team.setDescription(description_);
		team.setVersion(originalVersionId_);
		return dao.merge(team);
	}

	@Override
	public void addTeamUser(final Integer teamId_, final Integer userId_, final Integer originalVersionId_)
	{
		TeamUser teamUser = getTeamUser(teamId_, userId_);
		if (teamUser == null)
		{
			final Team team = getRequiredEntityById(Team.class, teamId_);
			team.setVersion(originalVersionId_);
			dao.merge(team);

			teamUser = new TeamUser();
			teamUser.setTeamId(teamId_);
			teamUser.setUserId(userId_);
			teamUser = dao.merge(teamUser);
		}
	}

	private TeamUser getTeamUser(Integer teamId_, Integer userId_)
	{
		// make sure valid entities
		final Team team = getRequiredEntityById(Team.class, teamId_);
		final User user = getRequiredEntityById(User.class, userId_);

		final Search search = new Search(TeamUser.class);
		search.addFilterEqual("teamId", team.getId());
		search.addFilterEqual("userId", user.getId());
		TeamUser teamUser = (TeamUser) dao.searchUnique(TeamUser.class, search);
		return teamUser;
	}

	@Override
	public void deleteTeamUser(final Integer teamId_, final Integer userId_, final Integer originalVersionId_) throws Exception
	{
		TeamUser teamUser = getTeamUser(teamId_, userId_);
		if (teamUser != null)
		{
			// update team version
			final Team team = getRequiredEntityById(Team.class, teamId_);
			team.setVersion(originalVersionId_);
			dao.merge(team);
			// delete user roles from team
			saveTeamUserRoles(teamId_, userId_, new ArrayList<Integer>(), originalVersionId_);
			// delete user from team
			dao.delete(teamUser);
		}
	}

	@Override
	public void saveTeamUsers(final Integer teamId_, final List<Integer> userIds_, final Integer originalVersionId_)
	{
		final Team team = getRequiredEntityById(Team.class, teamId_);
		// delete old users
		final Search search = new Search(TeamUser.class);
		search.addFilterEqual("teamId", team.getId());
		final List<TeamUser> teamUsers = dao.search(TeamUser.class, search);
		List<Integer> remainingMembers = new ArrayList<Integer>();
		if (teamUsers != null)
		{
			// delete member roles
			for (TeamUser teamUser : teamUsers)
			{
				if (userIds_ != null && userIds_.contains(teamUser.getUserId()))
				{
					remainingMembers.add(teamUser.getUserId());
				}
				else
				{
					// delete roles
					saveTeamUserRoles(teamId_, teamUser.getUserId(), new ArrayList<Integer>(), originalVersionId_);
					// delete member
					dao.delete(teamUser);
				}
			}
		}
		// add new users if not already a member
		if (userIds_ != null)
		{
			for (final Integer userId : userIds_)
			{
				if (!remainingMembers.contains(userId))
				{
					addTeamUser(teamId_, userId, originalVersionId_);
				}
			}
		}
	}

	@Override
	public void copyTeams(final Integer fromTeamId_, final Integer toTeamId_, final Integer originalVersionId_)
	{
		// get old users
		Search search = new Search(TeamUser.class);
		search.addFilterEqual("teamId", fromTeamId_);
		final List<TeamUser> teamUsers = dao.search(TeamUser.class, search);
		for (TeamUser teamUser : teamUsers)
		{
			// add to new team
			addTeamUser(toTeamId_, teamUser.getUserId(), originalVersionId_);
			// find old roles
			search = new Search(TeamUserRole.class);
			search.addFilterEqual("teamId", fromTeamId_);
			search.addFilterEqual("userId", teamUser.getUserId());
			final List<TeamUserRole> teamUserRoles = dao.search(TeamUserRole.class, search);
			for (TeamUserRole teamUserRole : teamUserRoles)
			{
				// add roles to new team member
				addTeamUserRole(toTeamId_, teamUser.getUserId(), teamUserRole.getAccessRoleId(), originalVersionId_);
			}
		}
	}

	@Override
	public void saveTeamUserRoles(final Integer teamId_, final Integer userId_, final List<Integer> roleIds_, final Integer originalVersionId_)
	{
		if (getTeamUser(teamId_, userId_) == null)
		{
			throw new InvalidTeamMemberException("User: " + userId_ + ", Team: " + teamId_);
		}
		// delete old roles
		final Search search = new Search(TeamUserRole.class);
		search.addFilterEqual("userId", userId_);
		search.addFilterEqual("teamId", teamId_);
		final List<TeamUserRole> userRoles = dao.search(TeamUserRole.class, search);
		dao.delete(userRoles);

		if (roleIds_ != null)
		{
			for (final Integer roleId : roleIds_)
			{
				addTeamUserRole(teamId_, userId_, roleId, originalVersionId_);
			}
		}
	}

	@Override
	public void addTeamUserRole(final Integer teamId_, final Integer userId_, final Integer roleId_, final Integer originalVersionId_)
	{
		if (getTeamUser(teamId_, userId_) == null)
		{
			throw new InvalidTeamMemberException("User: " + userId_ + ", Team: " + teamId_);
		}
		final AccessRole role = getRequiredEntityById(AccessRole.class, roleId_);
		final Search search = new Search(TeamUserRole.class);
		search.addFilterEqual("accessRoleId", role.getId());
		search.addFilterEqual("userId", userId_);
		search.addFilterEqual("teamId", teamId_);
		TeamUserRole userRole = (TeamUserRole) dao.searchUnique(TeamUserRole.class, search);
		if (userRole == null)
		{
			userRole = new TeamUserRole();
			userRole.setAccessRoleId(roleId_);
			userRole.setUserId(userId_);
			userRole.setTeamId(teamId_);
			dao.addAndReturnId(userRole);
		}
	}

	@Override
	public List<Permission> getTeamUserPermissions(final Integer teamId_, final Integer userId_)
	{
		if (getTeamUser(teamId_, userId_) == null)
		{
			throw new InvalidTeamMemberException("User: " + userId_ + ", Team: " + teamId_);
		}
		Search search = new Search(TeamUserRole.class);
		search.addFilterEqual("teamId", teamId_);
		search.addFilterEqual("userId", userId_);
		search.addField("accessRoleId");
		final List<?> roleIdList = dao.search(UserRole.class, search);
		if (roleIdList != null && !roleIdList.isEmpty())
		{
			search = new Search(RolePermission.class);
			search.addField("permissionId");
			search.addFilterIn("accessRoleId", roleIdList);
			final List<?> permissionIdList = dao.search(RolePermission.class, search);
			search = new Search(Permission.class);
			search.addFilterIn("id", permissionIdList);
			final List<Permission> list = dao.search(Permission.class, search);
			return list;
		}
		// return default user permissions
		else
		{
			return userService.getUserPermissions(userId_);
		}

	}

	@Override
	public List<AccessRole> getTeamUserRoles(final Integer teamId_, final Integer userId_)
	{
		if (getTeamUser(teamId_, userId_) == null)
		{
			throw new InvalidTeamMemberException("User: " + userId_ + ", Team: " + teamId_);
		}
		Search search = new Search(TeamUserRole.class);
		search.addFilterEqual("teamId", teamId_);
		search.addFilterEqual("userId", userId_);
		search.addField("accessRoleId");
		final List<?> roleIdList = dao.search(TeamUserRole.class, search);
		if (roleIdList != null && !roleIdList.isEmpty())
		{
			search = new Search(AccessRole.class);
			search.addFilterIn("id", roleIdList);
			final List<AccessRole> list = dao.search(AccessRole.class, search);
			return list;
		}
		// return default user roles
		else
		{
			return userService.getUserRoles(userId_);
		}
	}

	@Override
	public List<User> getTeamUsers(final Integer teamId_)
	{
		Team team = getRequiredEntityById(Team.class, teamId_);
		Search search = new Search(TeamUser.class);
		search.addFilterEqual("teamId", team.getId());
		search.addField("userId");
		final List<?> userIdList = dao.search(TeamUser.class, search);
		if (userIdList != null && !userIdList.isEmpty())
		{
			search = new Search(User.class);
			search.addFilterIn("id", userIdList);
			final List<User> list = dao.search(User.class, search);
			return list;
		}
		else
		{
			return new ArrayList<User>();
		}
	}

}
