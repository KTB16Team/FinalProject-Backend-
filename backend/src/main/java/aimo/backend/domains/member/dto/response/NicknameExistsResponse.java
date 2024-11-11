package aimo.backend.domains.member.dto.response;

public record NicknameExistsResponse(boolean exists) {

	public static NicknameExistsResponse of(boolean exists) {
		return new NicknameExistsResponse(exists);
	}
}
