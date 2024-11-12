package aimo.backend.domains.member.dto.response;

import aimo.backend.domains.member.entity.Member;

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

	public static FindMyInfoResponse from(Member member){

		if (member.getProfileImage() == null) {
			return new FindMyInfoResponse(member.getNickname(), member.getEmail(), null);
		}

		return new FindMyInfoResponse(member.getNickname(), member.getEmail(), member.getProfileImage().getUrl());
	}
}
