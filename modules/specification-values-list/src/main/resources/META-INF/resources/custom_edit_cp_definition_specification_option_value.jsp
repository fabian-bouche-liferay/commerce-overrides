<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/init.jsp" %>

<%
CustomCPDefinitionSpecificationOptionValueDisplayContext cpDefinitionSpecificationOptionValueDisplayContext = (CustomCPDefinitionSpecificationOptionValueDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
CPDefinitionSpecificationOptionValue cpDefinitionSpecificationOptionValue = cpDefinitionSpecificationOptionValueDisplayContext.getCPDefinitionSpecificationOptionValue();
List<CPOptionCategory> cpOptionCategories = cpDefinitionSpecificationOptionValueDisplayContext.getCPOptionCategories();
CPSpecificationOption cpSpecificationOption = cpDefinitionSpecificationOptionValue.getCPSpecificationOption();
long cpOptionCategoryId = BeanParamUtil.getLong(cpDefinitionSpecificationOptionValue, request, "CPOptionCategoryId");
List<String> cpOptionValues = cpDefinitionSpecificationOptionValueDisplayContext.getCPOptionValues();
%>

<portlet:actionURL name="/cp_definitions/edit_cp_definition_specification_option_value" var="editProductDefinitionSpecificationOptionValueActionURL" />

<liferay-frontend:side-panel-content
	title="<%= cpSpecificationOption.getTitle(locale) %>"
>
	<commerce-ui:panel
		title='<%= LanguageUtil.get(request, "detail") %>'
	>
		<aui:form action="<%= editProductDefinitionSpecificationOptionValueActionURL %>" method="post" name="fm">
			<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
			<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
			<aui:input name="cpDefinitionSpecificationOptionValueId" type="hidden" value="<%= String.valueOf(cpDefinitionSpecificationOptionValue.getCPDefinitionSpecificationOptionValueId()) %>" />

			<c:choose>
				<c:when test="<%= cpOptionValues == null %>">
					<aui:field-wrapper label='<%= LanguageUtil.get(resourceBundle, "value") %>' name="valueFieldWrapper">
						<liferay-ui:input-localized
							name="value"
							xml="<%= (cpDefinitionSpecificationOptionValue == null) ? StringPool.BLANK : cpDefinitionSpecificationOptionValue.getValue() %>"
						/>
					</aui:field-wrapper>
				</c:when>
				<c:otherwise>
					<aui:select label="<%= LanguageUtil.get(resourceBundle, "value") %>" name="<%= "value_" + defaultLanguageId %>" showEmptyOption="<%= true %>">

						<%
						for (String item : cpOptionValues) {
						%>

							<aui:option label="<%= item %>" selected="<%= (cpDefinitionSpecificationOptionValue == null) ? false : item.equals(cpDefinitionSpecificationOptionValue.getValue(defaultLocale)) %>" value="<%= item %>" />

						<%
						}
						%>
		
					</aui:select>
				</c:otherwise>			
			</c:choose>

			<aui:select label="group" name="CPOptionCategoryId" showEmptyOption="<%= true %>">

				<%
				for (CPOptionCategory cpOptionCategory : cpOptionCategories) {
				%>

					<aui:option label="<%= cpOptionCategory.getTitle(locale) %>" selected="<%= cpOptionCategoryId == cpOptionCategory.getCPOptionCategoryId() %>" value="<%= cpOptionCategory.getCPOptionCategoryId() %>" />

				<%
				}
				%>

			</aui:select>

			<%
			NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
			numberFormat.setMinimumFractionDigits(0);
			%>

			<aui:input label="position" name="priority" value="<%= numberFormat.format(cpDefinitionSpecificationOptionValue.getPriority()) %>">
				<aui:validator name="min">[0]</aui:validator>
				<aui:validator name="number" />
			</aui:input>

			<c:if test="<%= cpDefinitionSpecificationOptionValueDisplayContext.hasCustomAttributesAvailable() %>">
				<liferay-expando:custom-attribute-list
					className="<%= CPDefinitionSpecificationOptionValue.class.getName() %>"
					classPK="<%= (cpDefinitionSpecificationOptionValue != null) ? cpDefinitionSpecificationOptionValue.getCPDefinitionSpecificationOptionValueId() : 0 %>"
					editable="<%= true %>"
					label="<%= true %>"
				/>
			</c:if>

			<aui:button-row>
				<aui:button cssClass="btn-lg" type="submit" value="save" />

				<aui:button cssClass="btn-lg" type="cancel" />
			</aui:button-row>
		</aui:form>
	</commerce-ui:panel>
</liferay-frontend:side-panel-content>