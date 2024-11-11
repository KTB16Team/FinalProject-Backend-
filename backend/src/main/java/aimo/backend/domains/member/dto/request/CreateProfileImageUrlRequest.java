package aimo.backend.domains.member.dto.request;

public record CreateProfileImageUrlRequest(
	String nickname,
	String extension
) {

	public static CreateProfileImageUrlRequest of(String nickname, String extension) {
		return new CreateProfileImageUrlRequest(nickname, extension);
	}
}
