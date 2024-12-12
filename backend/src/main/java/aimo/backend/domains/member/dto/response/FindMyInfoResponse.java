package aimo.backend.domains.member.dto.response;

import aimo.backend.domains.member.entity.Member;

public record FindMyInfoResponse(
	String nickname,
	String email,
	String profileImageUrl,
	Integer point
) {

	public static FindMyInfoResponse of(
		String nickname,
		String email,
		String profileImageUrl,
		Integer point
	) {
		return new FindMyInfoResponse(nickname, email, profileImageUrl, point);
	}

	public static FindMyInfoResponse from(Member member) {

		if (member.getProfileImage() == null) {
			return new FindMyInfoResponse(
				member.getNickname(),
				member.getEmail(),
				null,
				member.getPoint());
		}

		return new FindMyInfoResponse(
			member.getNickname(),
			member.getEmail(),
			member.getProfileImage().getUrl(),
			member.getPoint());
	}
}
