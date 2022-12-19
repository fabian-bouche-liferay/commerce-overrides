package com.liferay.samples.fbo.commerce.specification.values.list;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(
		immediate = true,
		service = CPDefinitionSpecificationValuesProvider.class
)
public class CPDefinitionSpecificationValuesProviderImpl implements CPDefinitionSpecificationValuesProvider {

	@Override
	public List<String> getCPDefinitionSpecificationValues(String key) {

		if("chipset".equals(key)) {
			List<String> values = new ArrayList<String>();
			values.add("AMD AM4");
			values.add("AMD AM5");
			values.add("Intel 1700");
			return values;
		} else {
			return null;
		}

	}

}
