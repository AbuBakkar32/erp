package com.asl.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.asl.enums.FormFieldType;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Data
public class FormFieldBuilder {

	private FormFieldType fieldType;
	private String prompt;
	private String fieldId;
	private String fieldName;
	private String cssClass;
	private boolean disabled;
	private boolean required;
	private int seqn;

	private String defaultInputValue;
	private int defaultIntValue;
	private Date defaultDateValue;
	private boolean startDate;
	private String defaultTime;
	private List<DropdownOption> options = new ArrayList<>();
	private String selectedOption;
	private String searchUrl;
	private String dependentUrl;
	private String defaultSearchVal;
	private String searchVal;
	private String searchDes;

	public void setSeqn(int seqn) {
		this.seqn = seqn;
		this.fieldId = "param" + seqn;
		this.fieldName = "param" + seqn;
	}

	/**
	 * Generate Hidden input field
	 * @param sequence
	 * @param fieldType
	 * @param defaultValue
	 * @return {@link FormFieldBuilder}
	 */
	public static FormFieldBuilder generateHiddenField(int sequence, String defaultValue) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setFieldType(FormFieldType.HIDDEN);
		ffb.setDefaultInputValue(defaultValue);
		return ffb;
	}
	
	public static FormFieldBuilder generateInputField(int sequence, String prompt, String defaultValue, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setPrompt(prompt);
		ffb.setSeqn(sequence);
		ffb.setFieldType(FormFieldType.INPUT);
		ffb.setDefaultInputValue(defaultValue);
		ffb.setRequired(required);
		return ffb;
	}
	
	public static FormFieldBuilder generateInputField(int sequence, String prompt, String defaultValue, boolean required, boolean disabled) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setPrompt(prompt);
		ffb.setSeqn(sequence);
		ffb.setFieldType(FormFieldType.INPUT);
		ffb.setDefaultInputValue(defaultValue);
		ffb.setRequired(required);
		ffb.setDisabled(disabled);
		return ffb;
	}
	
	public static FormFieldBuilder generateNumberField(int sequence, String prompt, int defaultValue, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setPrompt(prompt);
		ffb.setSeqn(sequence);
		ffb.setFieldType(FormFieldType.NUMBER);
		ffb.setDefaultInputValue(String.valueOf(defaultValue));
		ffb.setRequired(required);
		return ffb;
	}

	/**
	 * Generate Date field
	 * @param sequence
	 * @param prompt
	 * @param defaultValue
	 * @param required
	 * @return {@link FormFieldBuilder}
	 */
	public static FormFieldBuilder generateDateField(int sequence, String prompt, Date defaultValue, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.DATE);
		ffb.setRequired(required);
		ffb.setDefaultDateValue(defaultValue);
		return ffb;
	}

	/**
	 * 
	 * @param sequence
	 * @param startDate
	 * @param prompt
	 * @param defaultValue
	 * @param required
	 * @return
	 */
	public static FormFieldBuilder generateDateField(int sequence, boolean startDate, String prompt, Date defaultValue, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.DATE);
		ffb.setRequired(required);
		ffb.setDefaultDateValue(defaultValue);
		ffb.setStartDate(startDate);
		return ffb;
	}

	/**
	 * Generate Dropdown Field
	 * @param sequence
	 * @param prompt
	 * @param options
	 * @param selectedOption
	 * @param required
	 * @return {@link FormFieldBuilder}
	 */
	public static FormFieldBuilder generateDropdownField(int sequence, String prompt, List<DropdownOption> options, String selectedOption, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.DROPDOWN);
		ffb.setOptions(options);
		ffb.setSelectedOption(selectedOption);
		ffb.setRequired(required);
		return ffb;
	}

	/**
	 * 
	 * @param sequence
	 * @param prompt
	 * @param options
	 * @param selectedOption
	 * @param required
	 * @param customCss
	 * @return
	 */
	public static FormFieldBuilder generateDropdownField(int sequence, String prompt, List<DropdownOption> options, String selectedOption, boolean required, String customCss) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.DROPDOWN);
		ffb.setOptions(options);
		ffb.setSelectedOption(selectedOption);
		ffb.setRequired(required);
		ffb.setCssClass(customCss);
		return ffb;
	}

	/**
	 * Generate Dropdown Field
	 * @param sequence
	 * @param prompt
	 * @param options
	 * @param selectedOption
	 * @param required
	 * @return {@link FormFieldBuilder}
	 */
	public static FormFieldBuilder generateDropdownField(int sequence, String fieldId, String fieldName, String prompt, List<DropdownOption> options, String selectedOption, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setFieldId(fieldId);
		ffb.setFieldName(fieldName);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.DROPDOWN);
		ffb.setOptions(options);
		ffb.setSelectedOption(selectedOption);
		ffb.setRequired(required);
		return ffb;
	}

	/**
	 * Generate search field
	 * @param sequence
	 * @param prompt
	 * @param searchUrl
	 * @param defaultValue
	 * @param required
	 * @return {@link FormFieldBuilder}
	 */
	public static FormFieldBuilder generateSearchField(int sequence, String prompt, String searchUrl, String defaultValue, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.SEARCH);
		ffb.setSearchUrl(searchUrl);
		ffb.setDefaultSearchVal(defaultValue);
		ffb.setRequired(required);
		return ffb;
	}

	/**
	 * Generate search field
	 * @param sequence
	 * @param prompt
	 * @param searchUrl
	 * @param defaultValue
	 * @param required
	 * @param customCss
	 * @return {@link FormFieldBuilder}
	 */
	public static FormFieldBuilder generateSearchField(int sequence, String prompt, String searchUrl, String defaultValue, boolean required, String customCss, String dependentUrl) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.SEARCH);
		ffb.setSearchUrl(searchUrl);
		ffb.setDefaultSearchVal(defaultValue);
		ffb.setRequired(required);
		ffb.setCssClass(customCss);
		ffb.setDependentUrl(dependentUrl);
		return ffb;
	}

	/**
	 * Generate Time field
	 * @param sequence
	 * @param prompt
	 * @param defaultTime
	 * @param required
	 * @return
	 */
	public static FormFieldBuilder generateTimeField(int sequence, String prompt, String defaultTime, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.TIME);
		ffb.setRequired(required);
		ffb.setDefaultTime(defaultTime);
		return ffb;
	}
}
