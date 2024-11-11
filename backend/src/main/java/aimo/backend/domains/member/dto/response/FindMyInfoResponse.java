package aimo.backend.domains.member.dto.response;

public record FindMyInfoResponse(
	String nickname,
	String email,
	String profileImageUrl
) {

	public static FindMyInfoResponse of(
		String nickname,
		String email,
		String profileImageUrl
	) {
		return new FindMyInfoResponse(nickname, email, profileImageUrl);
	}
}
