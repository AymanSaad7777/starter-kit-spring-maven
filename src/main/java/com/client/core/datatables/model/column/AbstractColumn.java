package com.client.core.datatables.model.column;

import com.client.core.datatables.tools.enumeration.FieldType;
import com.client.core.datatables.tools.enumeration.Entity;
import com.client.core.datatables.tools.enumeration.ShowOnForm;

public abstract class AbstractColumn implements Column {
	private String fieldName; // must correspond to the name of the field on the jpa entity
	private String fieldLabel; // the label used for display purposes
	private Object value; // the actual value, used for sorting etc
	private String displayValue; // the value displayed in the data table
	private FieldType fieldType; // the type of field, Date, Email,Id etc
	private Entity entity; // is this column part of the jpa entity or not
	private boolean editable; // can the column be edited
	private boolean visible; // is the column visable
	private boolean sortable; // is the column sortable
	private boolean searchable; // is the column searchable using the quicksearch field in datatables
	private boolean required; // should the column have a red asterisk next to it to indicate it's required
	private ShowOnForm showOnForm; // is the column/field visible for input on the add form
	private String url; // turn this value into a link using the url, also used to populate select values
	private String width;// sets the width of the data tables column using the sWidth option from the datatables api

	public AbstractColumn() {
	}

	public AbstractColumn(String fieldName, String fieldLabel, Object value, String displayValue, FieldType fieldType, Entity entity,
			boolean editable, boolean visible, boolean sortable, boolean searchable, boolean required, ShowOnForm showOnForm, String url) {
		this.fieldName = fieldName;
		this.fieldLabel = fieldLabel;
		this.value = value;
		this.fieldType = fieldType;
		this.entity = entity;
		this.editable = editable;
		this.visible = visible;
		this.sortable = sortable;
		this.searchable = searchable;
		this.required = required;
		this.showOnForm = showOnForm;
		this.url = url;
		this.displayValue = displayValue;
	}

	public AbstractColumn(String fieldName, String fieldLabel, Object value, String displayValue, FieldType fieldType, Entity entity,
			boolean editable, boolean visible, boolean sortable, boolean searchable, boolean required, ShowOnForm showOnForm, String url,
			String width) {
		this.fieldName = fieldName;
		this.fieldLabel = fieldLabel;
		this.value = value;
		this.fieldType = fieldType;
		this.entity = entity;
		this.editable = editable;
		this.visible = visible;
		this.sortable = sortable;
		this.searchable = searchable;
		this.required = required;
		this.showOnForm = showOnForm;
		this.url = url;
		this.displayValue = displayValue;
		this.width = width != null ? "'" + width + "'" : width;
	}

	/**
	 * Creates the configuration used in the javascript setup for dataTables.
	 * 
	 * The configuration will be inserted into "aoColumns" : [${columnConfigDataTables}] and will determine such things as is the column
	 * visible and/or sortable.
	 */
	@Override
	public String createColumnConfigForDataTables() {
		StringBuilder result = new StringBuilder();
		String NEW_LINE = System.getProperty("line.separator");
		result.append("{" + NEW_LINE);
		result.append(" bVisible: " + visible + "," + NEW_LINE);
		result.append(" bSortable : " + sortable + "," + NEW_LINE);
		result.append(" sWidth : " + width + NEW_LINE);
		result.append("},");
		return result.toString();
	}

	/**
	 * This method creates the configuration used in the javascript setup for dataTables.editable().
	 * 
	 * The configuration will be inserted into "aoColumns" : [${columnConfigDataTables}] and will determine such things as column data type.
	 */
	@Override
	public String createColumnConfigForDataTablesEditable() {
		if (!isVisible()) {
			return "";
		}

		if (!isEditable()) {
			return "null,";
		}

		StringBuilder result = new StringBuilder();
		String NEW_LINE = System.getProperty("line.separator");
		result.append("{" + NEW_LINE);
		result.append(" tooltip: 'Click to edit'," + NEW_LINE);
		result.append(" cssclass : '" + getFieldType().getCssClass() + "' " + NEW_LINE);
		result.append("},");

		return result.toString();
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public String getFieldLabel() {
		return fieldLabel;
	}

	@Override
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String getDisplayValue() {
		return displayValue;
	}

	@Override
	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	@Override
	public String getValueForDisplayOnEditForm() {
		return displayValue;
	}

	@Override
	public FieldType getFieldType() {
		return fieldType;
	}

	@Override
	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	@Override
	public Entity getEntity() {
		return entity;
	}

	@Override
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean isSortable() {
		return sortable;
	}

	@Override
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public String createDataTablesColumnSetting() {
		StringBuilder result = new StringBuilder();
		return result.toString();
	}

	@Override
	public boolean isSearchable() {
		return searchable;
	}

	@Override
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	@Override
	public boolean getRequired() {
		return required;
	}

	@Override
	public void setRequired(boolean required) {
		this.required = required;
	}

	@Override
	public ShowOnForm getShowOnForm() {
		return showOnForm;
	}

	@Override
	public void setShowOnForm(ShowOnForm showOnForm) {
		this.showOnForm = showOnForm;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(" {\n\"fieldName\":\"");
		builder.append(fieldName);
		builder.append("\", \nfieldLabel\":\"");
		builder.append(fieldLabel);
		builder.append("\", \nvalue\":\"");
		builder.append(value);
		builder.append("\", \ndisplayValue\":\"");
		builder.append(displayValue);
		builder.append("\", \nfieldType\":\"");
		builder.append(fieldType);
		builder.append("\", \nentity\":\"");
		builder.append(entity);
		builder.append("\", \neditable\":\"");
		builder.append(editable);
		builder.append("\", \nvisible\":\"");
		builder.append(visible);
		builder.append("\", \nsortable\":\"");
		builder.append(sortable);
		builder.append("\", \nsearchable\":\"");
		builder.append(searchable);
		builder.append("\", \nrequired\":\"");
		builder.append(required);
		builder.append("\", \nshowOnForm\":\"");
		builder.append(showOnForm);
		builder.append("\", \nurl\":\"");
		builder.append(url);
		builder.append("\", \nwidth\":\"");
		builder.append(width);
		builder.append("\n}");
		return builder.toString();
	}

}