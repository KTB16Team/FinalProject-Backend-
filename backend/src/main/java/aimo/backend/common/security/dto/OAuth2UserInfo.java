package aimo.backend.common.security.dto;

import java.util.Map;

import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.model.MemberRole;
import aimo.backend.domains.member.model.Provider;
import aimo.backend.common.security.dto.response.KakaoOAuth2Response;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class OAuth2UserInfo {
	private final static String KAKAO = "kakao";

	private String email;
	private String name;
	private Provider provider;
	private String providerId;

	public static OAuth2UserInfo of(String provider, Map<String, Object> attributes) {
		switch (provider) {
			case KAKAO:
				return ofKakao(attributes);
			default:
				throw new RuntimeException();
		}
	}

	private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
		KakaoOAuth2Response kakaoOAuth2Response = KakaoOAuth2Response.from(attributes);

		return OAuth2UserInfo.builder()
			.provider(Provider.KAKAO)
			.email(kakaoOAuth2Response.email())
			.name(kakaoOAuth2Response.nickname())
			.providerId(kakaoOAuth2Response.id())
			.build();
	}

	public Member toEntity() {
		return Member.createOAuthMember(email, name, MemberRole.USER, provider, providerId);
	}

}