package aimo.backend.domains.security.oAuth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.security.dto.CustomUserDetails;
import aimo.backend.domains.security.dto.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		String provider = userRequest.getClientRegistration().getRegistrationId();

		// 3. 필요한 정보를 provider에 따라 다르게 mapping
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(provider, oAuth2User.getAttributes());

		// 4. oAuth2UserInfo가 저장되어 있는지 유저 정보 확인
		//    없으면 DB 저장 후 해당 유저를 저장
		//    있으면 해당 유저를 저장
		Member member = memberRepository.findByProviderAndProviderId(
				oAuth2UserInfo.getProvider(),
				oAuth2UserInfo.getProviderId())
			.orElseGet(() -> memberRepository.save(oAuth2UserInfo.toEntity()));

		// 5. UserDetails와 OAuth2User를 다중 상속한 CustomUserDetails
		return new CustomUserDetails(member, oAuth2User.getAttributes());
	}
}
