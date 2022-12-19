package com.liferay.samples.fbo.commerce.specification.values.list;

import com.liferay.commerce.product.constants.CPOptionCategoryConstants;
import com.liferay.commerce.product.display.context.BaseCPDefinitionsDisplayContext;
import com.liferay.commerce.product.item.selector.criterion.CPSpecificationOptionItemSelectorCriterion;
import com.liferay.commerce.product.model.CPDefinitionSpecificationOptionValue;
import com.liferay.commerce.product.model.CPOptionCategory;
import com.liferay.commerce.product.portlet.action.ActionHelper;
import com.liferay.commerce.product.service.CPOptionCategoryService;
import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.criteria.UUIDItemSelectorReturnType;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.taglib.util.CustomAttributesUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomCPDefinitionSpecificationOptionValueDisplayContext extends BaseCPDefinitionsDisplayContext {

	private final static Logger LOG = LoggerFactory.getLogger(CustomCPDefinitionSpecificationOptionValueDisplayContext.class);
	
	public CustomCPDefinitionSpecificationOptionValueDisplayContext(
		ActionHelper actionHelper, HttpServletRequest httpServletRequest,
		CPOptionCategoryService cpOptionCategoryService,
		ItemSelector itemSelector, CPDefinitionSpecificationValuesProvider cpDefinitionSpecificationValuesProvider) {

		super(actionHelper, httpServletRequest);

		_cpOptionCategoryService = cpOptionCategoryService;
		_cpDefinitionSpecificationValuesProvider = cpDefinitionSpecificationValuesProvider;
		
		_itemSelector = itemSelector;
	}

	public CPDefinitionSpecificationOptionValue
			getCPDefinitionSpecificationOptionValue()
		throws PortalException {

		if (_cpDefinitionSpecificationOptionValue != null) {
			return _cpDefinitionSpecificationOptionValue;
		}

		_cpDefinitionSpecificationOptionValue =
			actionHelper.getCPDefinitionSpecificationOptionValue(
				cpRequestHelper.getRenderRequest());

		return _cpDefinitionSpecificationOptionValue;
	}
	
	public List<String> getCPOptionValues() throws PortalException {
		
		CPDefinitionSpecificationOptionValue cpDefinitionSpecificationOptionValue = getCPDefinitionSpecificationOptionValue();
		String key = cpDefinitionSpecificationOptionValue.getCPSpecificationOption().getKey();

		return _cpDefinitionSpecificationValuesProvider.getCPDefinitionSpecificationValues(key);
		
	}

	public List<CPOptionCategory> getCPOptionCategories()
		throws PortalException {

		BaseModelSearchResult<CPOptionCategory>
			cpOptionCategoryBaseModelSearchResult =
				_cpOptionCategoryService.searchCPOptionCategories(
					cpRequestHelper.getCompanyId(), null, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null);

		return cpOptionCategoryBaseModelSearchResult.getBaseModels();
	}

	public String getCPOptionCategoryTitle(
			CPDefinitionSpecificationOptionValue
				cpDefinitionSpecificationOptionValue)
		throws PortalException {

		long cpOptionCategoryId =
			cpDefinitionSpecificationOptionValue.getCPOptionCategoryId();

		if (cpOptionCategoryId ==
				CPOptionCategoryConstants.DEFAULT_CP_OPTION_CATEGORY_ID) {

			return StringPool.BLANK;
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		try {
			CPOptionCategory cpOptionCategory =
				_cpOptionCategoryService.getCPOptionCategory(
					cpOptionCategoryId);

			return cpOptionCategory.getTitle(themeDisplay.getLocale());
		}
		catch (PrincipalException principalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(principalException);
			}
		}

		return StringPool.BLANK;
	}

	public String getItemSelectorUrl() {
		RequestBackedPortletURLFactory requestBackedPortletURLFactory =
			RequestBackedPortletURLFactoryUtil.create(
				cpRequestHelper.getRenderRequest());

		CPSpecificationOptionItemSelectorCriterion
			cpSpecificationOptionItemSelectorCriterion =
				new CPSpecificationOptionItemSelectorCriterion();

		cpSpecificationOptionItemSelectorCriterion.
			setDesiredItemSelectorReturnTypes(
				Collections.<ItemSelectorReturnType>singletonList(
					new UUIDItemSelectorReturnType()));

		return String.valueOf(
			_itemSelector.getItemSelectorURL(
				requestBackedPortletURLFactory,
				"productSpecificationOptionsSelectItem",
				cpSpecificationOptionItemSelectorCriterion));
	}

	@Override
	public PortletURL getPortletURL() throws PortalException {
		return PortletURLBuilder.create(
			super.getPortletURL()
		).setMVCRenderCommandName(
			"/cp_definitions/edit_cp_definition"
		).setParameter(
			"screenNavigationCategoryKey", getScreenNavigationCategoryKey()
		).buildPortletURL();
	}

	public boolean hasCustomAttributesAvailable() throws Exception {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		long cpDefinitionSpecificationOptionValueId = 0;

		CPDefinitionSpecificationOptionValue
			cpDefinitionSpecificationOptionValue =
				getCPDefinitionSpecificationOptionValue();

		if (cpDefinitionSpecificationOptionValue != null) {
			cpDefinitionSpecificationOptionValueId =
				cpDefinitionSpecificationOptionValue.
					getCPDefinitionSpecificationOptionValueId();
		}

		return CustomAttributesUtil.hasCustomAttributes(
			themeDisplay.getCompanyId(),
			CPDefinitionSpecificationOptionValue.class.getName(),
			cpDefinitionSpecificationOptionValueId, null);
	}

	private static final Log _log = LogFactoryUtil.getLog(
			CustomCPDefinitionSpecificationOptionValueDisplayContext.class);

	private CPDefinitionSpecificationOptionValue
		_cpDefinitionSpecificationOptionValue;
	private CPDefinitionSpecificationValuesProvider _cpDefinitionSpecificationValuesProvider;
	
	private final CPOptionCategoryService _cpOptionCategoryService;
	private final ItemSelector _itemSelector;

}
