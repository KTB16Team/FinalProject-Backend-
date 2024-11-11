package aimo.backend.common.util.validator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileExtensionValidator implements ConstraintValidator<ValidFileExtension, String> {

	private Set<String> allowedExtensions;

	@Override
	public void initialize(ValidFileExtension constraintAnnotation) {
		this.allowedExtensions = new HashSet<>(Arrays.asList(constraintAnnotation.allowedExtensions()));
	}

	@Override
	public boolean isValid(String extension, ConstraintValidatorContext context) {
		if (extension == null || extension.isEmpty()) {
			throw ApiException.from(ErrorCode.INVALID_FILE_EXTENSION);
		}

		if (!allowedExtensions.contains(extension)) {
			throw ApiException.from(ErrorCode.INVALID_FILE_EXTENSION);
		}

		return allowedExtensions.contains(extension);
	}
}
