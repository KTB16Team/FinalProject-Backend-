package aimo.backend.domains.member.dto.request;

public record DeleteMemberRequest(String password) {

	public static DeleteMemberRequest from(String password) {
		return new DeleteMemberRequest(password);
	}
}
