package com.asl.model.validator;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
public class ConstraintValidator {

	/**
	 * Validates a bean instance annotated with javax.validation constraints and store constraint violations in {@link Errors}.
	 * 
	 * @param ob POJO instance
	 * @param errors Spring validation {@link Errors} instance
	 * @param validator bean {@link Validator} instance
	 */
	public <T> void validate(T ob, Errors errors, Validator validator) {
		Iterator<ConstraintViolation<T>> iter = validator.validate(ob).iterator();
		while (iter.hasNext()) {
			ConstraintViolation<T> item = iter.next();
			errors.rejectValue(item.getPropertyPath().toString(), "", item.getMessage());
		}
	}

	/**
	 * Cal this methid if binding result has error
	 * @param bindingResult
	 * @return {@link Map} of String, Object
	 */
	public Map<String, Object> getValidationMessage(BindingResult bindingResult) {
		Map<String, Object> response = new HashMap<>();
		if (!bindingResult.hasErrors()) return response;

		int count = 0;
		Map<String, String> errors = new HashMap<>();
		for (ObjectError obError : bindingResult.getAllErrors()) {
			if (obError instanceof FieldError) {
				FieldError error = (FieldError) obError;
				errors.put(error.getField(), isNotBlank(error.getCode()) ? error.getCode() : error.getDefaultMessage());
			} else {
				ObjectError error = obError;
				if (!errors.containsValue(error.getCode())) {
					errors.put("ERROR-" + (++count), error.getCode());
				}
			}
		}
		response.put("status", "error");
		response.put("message", formatErrorMessage(errors));
		return response;
	}

	private String formatErrorMessage(Map<String, String> errors) {
		StringBuilder errMsg = new StringBuilder("");

		if (errors.size() > 0) {
			Iterator<Entry<String, String>> iterator = errors.entrySet().iterator();
			while (iterator.hasNext()) {
				errMsg.append(iterator.next().getValue()).append(";");

			}
		}
		if (errMsg.lastIndexOf(";") > 0) {
			return errMsg.toString().substring(0, errMsg.length() - 1);
		}
		return errMsg.toString();
	}
}
