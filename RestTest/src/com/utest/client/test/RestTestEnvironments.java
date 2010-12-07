package com.utest.client.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;

import com.utest.webservice.client.rest.RestClient;

public class RestTestEnvironments
{
	@SuppressWarnings("unchecked")
	private static HttpMethod testGetMethod(RestClient cl) throws HttpException, IOException
	{
		HttpMethod method = cl.createGet("users", "1/permissions/", Collections.EMPTY_MAP);
		cl.executeMethod(method);
		return method;
	}

	@SuppressWarnings("unchecked")
	private static HttpMethod testPostMethod(RestClient cl) throws HttpException, IOException
	{

		/*
		 * This is declaration of UserInfo properties. Required elements must be
		 * defined in the parameters map for testing POST/PUT methods.
		 * 
		 * @XmlElement(required = false) private String password;
		 * 
		 * @XmlElement(required = false) private String firstName;
		 * 
		 * @XmlElement(required = false) private String lastName;
		 * 
		 * @XmlElement(required = true) private String email;
		 * 
		 * @XmlElement(required = false) private boolean forumUser;
		 * 
		 * @XmlElement(required = false) private boolean confirmedEmail;
		 * 
		 * @XmlElement(required = false) private Integer userStatusId;
		 * 
		 * @XmlElement(required = false) private Integer companyId;
		 */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("firstName", "first company 9");
		params.put("lastName", "last company 9");
		params.put("email", "vmkcomp9@utest.com");
		params.put("password", "123456");
		params.put("companyId", 9);

		HttpMethod method = cl.createPost("users", "", params);
		cl.executeMethod(method);
		return method;
	}

	@SuppressWarnings("unchecked")
	private static HttpMethod testPutMethod(RestClient cl) throws HttpException, IOException
	{

		/*
		 * This is declaration of CompanyInfo properties. Required elements must
		 * be defined in the parameters map for testing POST/PUT methods.
		 * 
		 * @XmlElement(required = true) private String name;
		 * 
		 * @XmlElement(required = false) private String phone;
		 * 
		 * @XmlElement(required = false) private String address;
		 * 
		 * @XmlElement(required = false) private String city;
		 * 
		 * @XmlElement(required = false) private String zip;
		 * 
		 * @XmlElement(required = true) private String url;
		 * 
		 * @XmlElement(required = true) private Integer countryId;
		 */
		Integer id = 18;

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "versioned updated from Web Service " + id);
		params.put("url", "vmk123updated.com");
		params.put("countryId", 133);
		params.put("resourceidentity.id", id);
		params.put("resourceidentity.version", 9);

		HttpMethod method = cl.createPut("companies", id + "/", params);
		cl.executeMethod(method);
		return method;
	}

	@SuppressWarnings("unchecked")
	private static HttpMethod testActivateMethod(RestClient cl) throws HttpException, IOException
	{

		Integer id = 4;
		HttpMethod method = cl.createPut("users", id + "/activate/", Collections.EMPTY_MAP);
		cl.executeMethod(method);
		return method;
	}

	public static void main(String[] args)
	{
		// RestClient.setKeyStoreFile("./client_utest.jks");
		// RestClient.setKestorepassword("utest12");
		RestClient cl = new RestClient("https://localhost", "/tcm/services/v2/rest/");
		try
		{
			// https://localhost/tcm/services/v2/rest/testCycles/1671/decline
			String login = "admin@utest.com";
			cl.login(login, "admin");
			// System.out.println(cl.getResponseObject(Object.class));
			cl.setAccept("application/json");
			// cl.setAccept("application/xml");
			// HttpMethod method = testGetMethod(cl);
			// HttpMethod method = testPostMethod(cl);
			// HttpMethod method = testPutMethod(cl);
			// HttpMethod method = testDeleteMethod(cl);
			// HttpMethod method = testActivateMethod(cl);
			// HttpMethod method = testCreateRoleMethod(cl);
			// HttpMethod method = testUpdateRolePermissions(cl);
			// HttpMethod method = testAddRolePermission(cl);
			// HttpMethod method = testDeleteRolePermission(cl);
			HttpMethod method = testDeleteRole(cl);
			System.out.println(method.getResponseBodyAsString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}

	}

	@SuppressWarnings("unchecked")
	private static HttpMethod testCreateRoleMethod(RestClient cl) throws HttpException, IOException
	{

		/*
		 * This is declaration of RoleInfo properties. Required elements must be
		 * defined in the parameters map for testing POST/PUT methods.
		 * 
		 * @XmlElement(required = true) private String name;
		 * 
		 * @XmlElement(required = true) private Integer companyId;
		 * 
		 * @XmlElement(required = false) private final Integer sortOrder = 0;
		 */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "role for company 9");
		params.put("companyId", 9);

		HttpMethod method = cl.createPost("roles", "", params);
		cl.executeMethod(method);
		return method;
	}

	@SuppressWarnings("unchecked")
	private static HttpMethod testUpdateRolePermissions(RestClient cl) throws HttpException, IOException
	{

		Integer id = 6;
		ArrayList<Integer> permissionIds = new ArrayList<Integer>();
		permissionIds.add(6);
		permissionIds.add(7);
		permissionIds.add(8);
		permissionIds.add(9);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("permissionIds", permissionIds);

		HttpMethod method = cl.createPut("roles", id + "/permissions/", params);
		cl.executeMethod(method);
		return method;
	}

	@SuppressWarnings("unchecked")
	private static HttpMethod testAddRolePermission(RestClient cl) throws HttpException, IOException
	{

		Integer id = 6;
		Map<String, Object> params = new HashMap<String, Object>();

		HttpMethod method = cl.createPost("roles", id + "/permissions/10/", params);
		cl.executeMethod(method);
		return method;
	}

	@SuppressWarnings("unchecked")
	private static HttpMethod testDeleteRolePermission(RestClient cl) throws HttpException, IOException
	{

		Integer id = 6;
		Map<String, Object> params = new HashMap<String, Object>();

		HttpMethod method = cl.createDelete("roles", id + "/permissions/8/", params);
		cl.executeMethod(method);
		return method;
	}

	@SuppressWarnings("unchecked")
	private static HttpMethod testDeleteRole(RestClient cl) throws HttpException, IOException
	{

		Integer id = 6;
		Map<String, Object> params = new HashMap<String, Object>();

		HttpMethod method = cl.createDelete("roles", id + "/", params);
		cl.executeMethod(method);
		return method;
	}
}
