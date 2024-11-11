package aimo.backend.infrastructure.s3.dto.parameter;

public record CreateResourceUrlParameter(
	String prefix,
	String filename,
	String extension
) {

	public static CreateResourceUrlParameter of(String prefix, String filename, String extension) {
		return new CreateResourceUrlParameter(prefix, filename, extension);
	}
}
