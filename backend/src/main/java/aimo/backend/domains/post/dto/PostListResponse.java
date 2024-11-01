package aimo.backend.domains.post.dto;

import java.util.List;

public record PostListResponse(List<PostPreviewResponse> postPreviews) {
}
