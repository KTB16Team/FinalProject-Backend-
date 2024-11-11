package aimo.backend.domains.member.dto.request;

public record DeleteMemberRequest(String password) {

	public static DeleteMemberRequest of(String password) {
		return new DeleteMemberRequest(password);
	}
}
