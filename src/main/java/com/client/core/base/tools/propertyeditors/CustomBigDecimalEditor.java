package com.client.core.base.tools.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class CustomBigDecimalEditor extends PropertyEditorSupport {

	private final int scale;
	private final boolean allowEmpty;

	public CustomBigDecimalEditor(int scale, boolean allowEmpty) {
		super();
		this.scale = scale;
		this.allowEmpty = allowEmpty;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if ((this.allowEmpty && StringUtils.isEmpty(text))) {
			setValue(null);
			return;
		}

		text = StringUtils.remove(text, ",");
		if (!NumberUtils.isNumber(text)) {
			setValue(null);
			return;
		}

		try {
			setValue(new BigDecimal(text));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Could not parse string to BigDecimal: " + e.getMessage(), e);
		}

	}

	@Override
	public String getAsText() {
		BigDecimal value = (BigDecimal) getValue();

		if (value == null) {
			return "";
		}

		return value.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
	}

}
