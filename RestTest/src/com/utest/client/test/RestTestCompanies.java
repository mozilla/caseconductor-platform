package com.utest.client.test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;

import com.utest.webservice.client.rest.RestClient;

public class RestTestCompanies
{
	@SuppressWarnings("unchecked")
	private static HttpMethod testGetMethod(RestClient cl) throws HttpException, IOException
	{
		HttpMethod method = cl.createGet("companies", "", Collections.EMPTY_MAP);
		cl.executeMethod(method);
		return method;
	}

	@SuppressWarnings("unchecked")
	private static HttpMethod testDeleteMethod(RestClient cl) throws HttpException, IOException
	{
		HttpMethod method = cl.createDelete("companies", "20/", Collections.EMPTY_MAP);
		cl.executeMethod(method);
		return method;
	}

	@SuppressWarnings("unchecked")
	private static HttpMethod testPostMethod(RestClient cl) throws HttpException, IOException
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
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "VMK new company from Web Service 66");
		params.put("url", "vmk123.com");
		params.put("countryId", 123);

		HttpMethod method = cl.createPost("companies", "", params);
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
			HttpMethod method = testDeleteMethod(cl);

			System.out.println(method.getResponseBodyAsString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}

	}
}
