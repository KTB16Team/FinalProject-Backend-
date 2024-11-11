package aimo.backend.domains.member.dto.request;

public record DeleteRequest(String password) {

	public static DeleteRequest of(String password) {
		return new DeleteRequest(password);
	}
}
