package aimo.backend.domains.post.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import aimo.backend.domains.post.model.Category;
import aimo.backend.domains.privatePost.model.OriginType;
import jakarta.validation.constraints.NotNull;

public record SavePostRequest(
	@NotNull(message = "privatePostId is null")
	Long privatePostId,
	@NotNull(message = "categoryId is null")
	String title,
	@NotNull(message = "summaryAi is null")
	String stancePlaintiff,
	@NotNull(message = "stanceDefendant is null")
	String stanceDefendant,
	@NotNull(message = "summaryAi is null")
	String summaryAi,
	@NotNull(message = "judgement is null")
	String judgement,
	@NotNull(message = "originType is null")
	OriginType originType,
	@JsonSetter(nulls = Nulls.SKIP)
	Category category
) {

	public SavePostRequest {
		if (category == null)
			category = Category.COMMON;
	}

}
