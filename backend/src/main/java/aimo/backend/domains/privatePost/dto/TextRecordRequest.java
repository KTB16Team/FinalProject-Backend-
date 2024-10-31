package aimo.backend.domains.privatePost.dto;

import jakarta.validation.constraints.NotNull;

public record TextRecordRequest(
	@NotNull(message = "Title is required")
	String title,
	@NotNull(message = "Stance of plaintiff is required")
	String script
) {
}
