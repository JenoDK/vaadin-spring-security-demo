package com.jeno.demo.util;

import com.google.common.collect.Maps;
import com.vaadin.data.*;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.PasswordField;

import java.util.Map;
import java.util.Optional;

public class VaadinUtil {

	public static void addValidator(AbstractField field, Validator validator) {
		field.addValueChangeListener(event -> {
			ValidationResult result = validator.apply(event.getValue(), new ValueContext(field));

			if (result.isError()) {
				UserError error = new UserError(result.getErrorMessage());
				field.setComponentError(error);
			} else {
				field.setComponentError(null);
			}
		});
	}

	public static Validator<String> getPasswordsMatchValidator(PasswordField passwordField) {
		return (Validator<String>) (value, context) -> {
			if (!value.isEmpty() && !passwordField.getOptionalValue().equals(Optional.ofNullable(value))) {
				return ValidationResult.error("Needs to be the same as password");
			}
			return ValidationResult.ok();
		};
	}

	public static String getRootRequestURL() {
		String completeUrl = ((VaadinServletRequest) VaadinService.getCurrentRequest()).getHttpServletRequest().getRequestURL().toString();
		String uri = ((VaadinServletRequest) VaadinService.getCurrentRequest()).getHttpServletRequest().getRequestURI();
		return completeUrl.replace(uri, "");
	}

	public static <T> Map<String, String> setErrorMap(BeanValidationBinder<T> binder, Map<String, String> errorMap) {
		Map<String, String> errorsWithoutBinding = Maps.newHashMap();
		errorMap.entrySet().forEach(entry -> {
			Optional<Binder.Binding<T, ?>> binding = binder.getBinding(entry.getKey());
			if (binding.isPresent()) {
				((AbstractComponent) binding.get().getField()).setComponentError(new ErrorMessage() {
					@Override
					public ErrorLevel getErrorLevel() {
						return ErrorLevel.ERROR;
					}

					@Override
					public String getFormattedHtmlMessage() {
						return entry.getValue();
					}
				});
			} else {
				errorsWithoutBinding.put(entry.getKey(), entry.getValue());
			}
		});
		return errorsWithoutBinding;
	}
}
