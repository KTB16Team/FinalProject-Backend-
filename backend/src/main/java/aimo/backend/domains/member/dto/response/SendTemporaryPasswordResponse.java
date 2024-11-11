package aimo.backend.domains.member.dto.response;

public record SendTemporaryPasswordResponse(
	String to,
	String title,
	String content,
	String from
) {

	public static SendTemporaryPasswordResponse of(
		String to,
		String title,
		String content,
		String from
	) {
		return new SendTemporaryPasswordResponse(to, title, content, from);
	}
}
