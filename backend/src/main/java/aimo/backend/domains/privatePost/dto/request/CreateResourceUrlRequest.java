package aimo.backend.domains.privatePost.dto.request;

public record CreateResourceUrlRequest(
	String prefix,
	String filename,
	String extension
) {
}
