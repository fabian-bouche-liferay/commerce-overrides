package com.liferay.samples.fbo.commerce.specification.values.list;
import com.liferay.commerce.product.constants.CPPortletKeys;
import com.liferay.commerce.product.exception.NoSuchCPDefinitionSpecificationOptionValueException;
import com.liferay.commerce.product.portlet.action.ActionHelper;
import com.liferay.commerce.product.service.CPOptionCategoryService;
import com.liferay.commerce.product.service.CPOptionService;
import com.liferay.commerce.product.service.CPOptionValueService;
import com.liferay.item.selector.ItemSelector;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.constants.MVCRenderConstants;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	property = {
		"javax.portlet.name=" + CPPortletKeys.CP_DEFINITIONS,
		"mvc.command.name=/cp_definitions/edit_cp_definition_specification_option_value",
		"service.ranking:Integer=100"
	},
	service = MVCRenderCommand.class
)
public class CustomEditCPDefinitionSpecificationOptionValueMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		try {
			CustomCPDefinitionSpecificationOptionValueDisplayContext
				cpDefinitionSpecificationOptionValueDisplayContext =
					new CustomCPDefinitionSpecificationOptionValueDisplayContext(
						_actionHelper,
						_portal.getHttpServletRequest(renderRequest),
						_cpOptionCategoryService, _itemSelector,
						_cpDefinitionSpecificationValuesProvider);

			renderRequest.setAttribute(
				WebKeys.PORTLET_DISPLAY_CONTEXT,
				cpDefinitionSpecificationOptionValueDisplayContext);
		}
		catch (Exception exception) {
			if (exception instanceof
					NoSuchCPDefinitionSpecificationOptionValueException ||
				exception instanceof PrincipalException) {

				SessionErrors.add(renderRequest, exception.getClass());

				return "/error.jsp";
			}

			throw new PortletException(exception);
		}
		
		RequestDispatcher requestDispatcher = _servletContext.getRequestDispatcher("/custom_edit_cp_definition_specification_option_value.jsp");
		
        try {
            HttpServletRequest httpServletRequest = 
                PortalUtil.getHttpServletRequest(renderRequest);
            HttpServletResponse httpServletResponse = 
                PortalUtil.getHttpServletResponse(renderResponse);

            requestDispatcher.include
                (httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            throw new PortletException
                ("Unable to include /custom_edit_cp_definition_specification_option_value.jsp", e);
        }
		
		return MVCRenderConstants.MVC_PATH_VALUE_SKIP_DISPATCH;
	}

	@Reference
	private ActionHelper _actionHelper;

	@Reference
	private CPOptionCategoryService _cpOptionCategoryService;

	@Reference
	private CPOptionValueService _cpOptionValueService;

	@Reference
	private CPOptionService _cpOptionService;

	@Reference
	private ItemSelector _itemSelector;

	@Reference
	private Portal _portal;

	@Reference
	private CPDefinitionSpecificationValuesProvider _cpDefinitionSpecificationValuesProvider;
	
	@Reference(target = "(osgi.web.symbolicname=com.liferay.samples.fbo.specifications.values.list)")
	private ServletContext _servletContext;
}
