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
 * @author Greg Zheng
 *
 * copyright 2010 by uTest
 */
package com.utest.webservice.impl.v2;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.springframework.security.access.annotation.Secured;

import com.utest.domain.AccessRole;
import com.utest.domain.Attachment;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentGroupExploded;
import com.utest.domain.Permission;
import com.utest.domain.Product;
import com.utest.domain.ProductComponent;
import com.utest.domain.User;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.ProductService;
import com.utest.domain.service.TestCaseService;
import com.utest.webservice.api.v2.ProductWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.AttachmentInfo;
import com.utest.webservice.model.v2.EnvironmentGroupExplodedInfo;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.ProductComponentInfo;
import com.utest.webservice.model.v2.ProductComponentSearchResultInfo;
import com.utest.webservice.model.v2.ProductInfo;
import com.utest.webservice.model.v2.ProductSearchResultInfo;
import com.utest.webservice.model.v2.RoleInfo;
import com.utest.webservice.model.v2.UserInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

@Path("/products/")
public class ProductWebServiceImpl extends BaseWebServiceImpl implements ProductWebService
{
	private final ProductService	productService;
	private final TestCaseService	testCaseService;

	public ProductWebServiceImpl(ObjectBuilderFactory objectBuildFactory, ProductService productService, TestCaseService testCaseService)
	{
		super(objectBuildFactory);
		this.productService = productService;
		this.testCaseService = testCaseService;
	}

	@POST
	@Path("/{id}/components/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public ProductComponentInfo createProductComponent(@Context final UriInfo ui_, @PathParam("id") final Integer productId_, @FormParam("name") final String name_,
			@FormParam("description") final String description_) throws Exception
	{
		final ProductComponent productComponent = productService.addProductComponent(productId_, name_, description_);
		return objectBuilderFactory.toInfo(ProductComponentInfo.class, productComponent, ui_.getBaseUriBuilder());
	}

	@POST
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public ProductInfo createProduct(@Context final UriInfo ui_, @FormParam("companyId") final Integer companyId_, @FormParam("name") final String name_,
			@FormParam("description") final String description_) throws Exception
	{
		final Product product = productService.addProduct(companyId_, name_, description_);
		return objectBuilderFactory.toInfo(ProductInfo.class, product, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public Boolean deleteProduct(@Context final UriInfo ui_, @PathParam("id") final Integer productId, @FormParam("originalVersionId") final Integer originalVersionId_)
			throws Exception
	{
		productService.deleteProduct(productId, originalVersionId_);

		return Boolean.TRUE;
	}

	@PUT
	@Path("/{id}/undo_delete/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT, Permission.DELETED_ENTITY_UNDO })
	public Boolean undeleteProduct(@Context final UriInfo ui_, @PathParam("id") final Integer productId, @FormParam("originalVersionId") final Integer originalVersionId_)
			throws Exception
	{
		UtestSearch search = new UtestSearch();
		search.addFilterEqual("productId", productId);
		// undo components
		productService.undoAllDeletedEntities(ProductComponent.class, search);
		// undo product
		return productService.undoDeletedEntity(Product.class, productId);
	}

	@DELETE
	@Path("/components/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public Boolean deleteProductComponent(@Context final UriInfo ui_, @PathParam("id") final Integer productComponentId_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		productService.deleteProductComponent(productComponentId_, originalVersionId_);

		return Boolean.TRUE;
	}

	@PUT
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public ProductInfo updateProduct(@Context final UriInfo ui_, @PathParam("id") final Integer productId, @FormParam("name") final String name_,
			@FormParam("description") final String description_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		Product product = productService.saveProduct(productId, name_, description_, originalVersionId_);
		return objectBuilderFactory.toInfo(ProductInfo.class, product, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/components/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public ProductComponentInfo updateProductComponent(@Context final UriInfo ui_, @PathParam("id") final Integer productComponentId_, @FormParam("name") final String name_,
			@FormParam("description") final String description_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		ProductComponent productComponent = productService.saveProductComponent(productComponentId_, name_, description_, originalVersionId_);
		return objectBuilderFactory.toInfo(ProductComponentInfo.class, productComponent, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	public ProductInfo getProduct(@Context final UriInfo ui_, @PathParam("id") final Integer productId) throws Exception
	{
		Product product = productService.getProduct(productId);
		return objectBuilderFactory.toInfo(ProductInfo.class, product, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/components/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	public ProductComponentInfo getProductComponent(@Context final UriInfo ui_, @PathParam("id") final Integer productComponentId_) throws Exception
	{
		ProductComponent productComponent = productService.getProductComponent(productComponentId_);
		return objectBuilderFactory.toInfo(ProductComponentInfo.class, productComponent, ui_.getBaseUriBuilder());
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	public ProductSearchResultInfo findProducts(@Context final UriInfo ui_, @QueryParam("includedEnvironmentId") final Integer includedEnvironmentId_,
			@QueryParam("teamMemberId") final Integer teamMemberId, @QueryParam("") final UtestSearchRequest request) throws Exception
	{
		UtestSearch search = objectBuilderFactory.createSearch(ProductInfo.class, request, ui_);
		UtestSearchResult result = productService.findProducts(search, teamMemberId, includedEnvironmentId_);
		return (ProductSearchResultInfo) objectBuilderFactory.createResult(ProductInfo.class, Product.class, request, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_VIEW, Permission.DELETED_ENTITY_VIEW })
	@Path("/deleted/")
	public ProductSearchResultInfo findDeletedProducts(@Context final UriInfo ui_, @QueryParam("includedEnvironmentId") final Integer includedEnvironmentId_,
			@QueryParam("teamMemberId") final Integer teamMemberId, @QueryParam("") final UtestSearchRequest request) throws Exception
	{
		UtestSearch search = objectBuilderFactory.createSearch(ProductInfo.class, request, ui_);
		UtestSearchResult result = productService.findProducts(search, teamMemberId, includedEnvironmentId_);
		return (ProductSearchResultInfo) objectBuilderFactory.createResult(ProductInfo.class, Product.class, request, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/components/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	public ProductComponentSearchResultInfo findProductComponents(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request) throws Exception
	{
		UtestSearch search = objectBuilderFactory.createSearch(ProductComponentInfo.class, request, ui_);
		UtestSearchResult result = productService.findProductComponents(search);
		return (ProductComponentSearchResultInfo) objectBuilderFactory.createResult(ProductComponentInfo.class, ProductComponent.class, request, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/components/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	/**
	 * Returns all product components of a test case
	 */
	public List<ProductComponentInfo> getProductComponents(@Context final UriInfo ui_, @PathParam("id") final Integer productId_) throws Exception
	{
		final List<ProductComponent> components = productService.getComponentsForProduct(productId_);
		return objectBuilderFactory.toInfo(ProductComponentInfo.class, components, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	/**
	 * Returns all versions of a test case
	 */
	public List<EnvironmentGroupInfo> getProductEnvironmentGroups(@Context final UriInfo ui_, @PathParam("id") final Integer productId_,
			@QueryParam("includedEnvironmentId") final Integer includedEnvironmentId_) throws Exception
	{
		final List<EnvironmentGroup> groups = productService.getEnvironmentGroupsForProduct(productId_, includedEnvironmentId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, groups, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/attachments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	public List<AttachmentInfo> getProductAttachments(@Context UriInfo ui_, @PathParam("id") final Integer productId_) throws Exception
	{
		final List<Attachment> attachments = productService.getAttachmentsForProduct(productId_);
		final List<AttachmentInfo> attachmentsInfo = objectBuilderFactory.toInfo(AttachmentInfo.class, attachments, ui_.getBaseUriBuilder());
		return attachmentsInfo;
	}

	@POST
	@Path("/{id}/attachments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public AttachmentInfo createAttachment(@Context UriInfo ui, @PathParam("id") final Integer productId, @FormParam("name") String name,
			@FormParam("description") String description, @FormParam("url") String url, @FormParam("size") Double size, @FormParam("attachmentTypeId") Integer attachmentTypeId)
			throws Exception
	{
		Attachment attachment = productService.addAttachmentForProduct(name, description, url, size, productId, attachmentTypeId);
		return objectBuilderFactory.toInfo(AttachmentInfo.class, attachment, ui.getBaseUriBuilder());
	}

	@DELETE
	@Path("/{id}/attachments/{attachmentId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public Boolean deleteAttachment(@Context UriInfo ui, @PathParam("id") final Integer productId, @PathParam("attachmentId") final Integer attachmentId,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		return productService.deleteAttachment(attachmentId, productId);
	}

	@GET
	@Path("/{id}/environmentgroups/exploded/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	/**
	 * Returns all versions of a test case
	 */
	public List<EnvironmentGroupExplodedInfo> getProductEnvironmentGroupsExploded(@Context final UriInfo ui_, @PathParam("id") final Integer productId_,
			@QueryParam("includedEnvironmentId") final Integer includedEnvironmentId_) throws Exception
	{
		final List<EnvironmentGroupExploded> groups = productService.getEnvironmentGroupsExplodedForProduct(productId_, includedEnvironmentId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupExplodedInfo.class, groups, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/team/members/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	/**
	 * Returns all versions of a test case
	 */
	public List<UserInfo> getProductTeamMembers(@Context final UriInfo ui_, @PathParam("id") final Integer productId_) throws Exception
	{
		final List<User> users = productService.getTestingTeamForProduct(productId_);
		return objectBuilderFactory.toInfo(UserInfo.class, users, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/team/members/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public Boolean updateProductTeamMembers(@Context final UriInfo ui_, @PathParam("id") final Integer productId_, @FormParam("userIds") final ArrayList<Integer> userIds_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		productService.saveTestingTeamForProduct(productId_, userIds_, originalVersionId_);
		return Boolean.TRUE;
	}

	@GET
	@Path("/{id}/team/members/{userId}/roles/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	/**
	 * Returns all versions of a test case
	 */
	public List<RoleInfo> getProductTeamMemberRoles(@Context final UriInfo ui_, @PathParam("id") final Integer productId_, @PathParam("userId") final Integer userId_)
			throws Exception
	{
		final List<AccessRole> roles = productService.getTestingTeamMemberRolesForProduct(productId_, userId_);
		return objectBuilderFactory.toInfo(RoleInfo.class, roles, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/team/members/{userId}/roles/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public Boolean updateProductTeamMemberRoles(@Context final UriInfo ui_, @PathParam("id") final Integer productId_, @PathParam("userId") final Integer userId_,
			@FormParam("roleIds") final ArrayList<Integer> roleIds_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		productService.saveTestingTeamMemberRolesForProduct(productId_, userId_, roleIds_, originalVersionId_);
		return Boolean.TRUE;
	}

	@PUT
	@Path("/{id}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public Boolean updateProductEnvironmentGroups(@Context final UriInfo ui_, @PathParam("id") final Integer productId_,
			@FormParam("environmentGroupIds") final ArrayList<Integer> environmentGroupIds_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		productService.saveEnvironmentGroupsForProduct(productId_, environmentGroupIds_, originalVersionId_);
		return Boolean.TRUE;
	}

	@PUT
	@Path("/{id}/environmentgroups/autogenerate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public List<EnvironmentGroupInfo> generateEnvironmentGroupFromEnvironments(@Context final UriInfo ui_, @PathParam("id") final Integer productId_,
			@FormParam("environmentIds") final ArrayList<Integer> environmentIds_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		List<EnvironmentGroup> environmentGroups = productService.addGeneratedEnvironmentGroupsForProduct(productId_, environmentIds_, originalVersionId_);
		final List<EnvironmentGroupInfo> environmentGroupsInfo = objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, environmentGroups, ui_.getBaseUriBuilder());
		return environmentGroupsInfo;
	}

	@PUT
	@Path("/{id}/environmentgroups/environmenttypes/{typeId}/autogenerate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public List<EnvironmentGroupInfo> generateEnvironmentGroupFromEnvironments(@Context final UriInfo ui_, @PathParam("id") final Integer productId_,
			@PathParam("typeId") final Integer environmentTypeId_, @FormParam("environmentIds") final ArrayList<Integer> environmentIds_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		List<EnvironmentGroup> environmentGroups = productService.addGeneratedEnvironmentGroupsForProduct(productId_, environmentTypeId_, environmentIds_, originalVersionId_);
		final List<EnvironmentGroupInfo> environmentGroupsInfo = objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, environmentGroups, ui_.getBaseUriBuilder());
		return environmentGroupsInfo;
	}

	@Override
	@Secured(Permission.TEST_CASE_EDIT)
	@POST
	@Path("/{id}/import_multistep_testcases/")
	public Boolean importMultiStepTestCasesFromCsv(MultipartBody body_, @PathParam("id") final Integer productId_) throws Exception
	{
		List<org.apache.cxf.jaxrs.ext.multipart.Attachment> attachments = body_.getAllAttachments();
		javax.activation.DataHandler dataHandler = attachments.get(0).getDataHandler();
		InputStream inputStream = dataHandler.getInputStream();
		OutputStream outputStream = new ByteArrayOutputStream();
		IOUtils.copy(inputStream, outputStream);
		inputStream.close();
		outputStream.close();
		testCaseService.importMultiStepTestCasesFromCsv(outputStream.toString(), productId_);
		return Boolean.TRUE;
	}

	@Override
	@Secured(Permission.TEST_CASE_EDIT)
	@POST
	@Path("/{id}/import_singlestep_testcases/")
	public Boolean importSingleStepTestCasesFromCsv(MultipartBody body_, @PathParam("id") final Integer productId_) throws Exception
	{
		List<org.apache.cxf.jaxrs.ext.multipart.Attachment> attachments = body_.getAllAttachments();
		javax.activation.DataHandler dataHandler = attachments.get(0).getDataHandler();
		InputStream inputStream = dataHandler.getInputStream();
		OutputStream outputStream = new ByteArrayOutputStream();
		IOUtils.copy(inputStream, outputStream);
		inputStream.close();
		outputStream.close();
		testCaseService.importSingleStepTestCasesFromCsv(outputStream.toString(), productId_);
		return Boolean.TRUE;
	}
}
