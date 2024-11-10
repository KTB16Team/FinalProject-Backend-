package aimo.backend.infrastructure.s3.dto.parameter;

public record CreateResourceUrlParameter(
	String prefix,
	String filename,
	String extension
) {
}
