package aimo.backend.infrastructure.s3.dto.request;

public record CreateResourceUrlParameter(
	String prefix,
	String filename,
	String extension
) {
}
