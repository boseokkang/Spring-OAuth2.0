package com.cos.securityex01.config.auth.oauth;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.securityex01.config.auth.PrincipalDetails;
import com.cos.securityex01.config.auth.oauth.provider.FacebookUserInfo;
import com.cos.securityex01.config.auth.oauth.provider.GoogleUserInfo;
import com.cos.securityex01.config.auth.oauth.provider.OAuth2UserInfo;
import com.cos.securityex01.model.User;
import com.cos.securityex01.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService  {

	@Autowired
	private UserRepository userRepository;
	
	// userRequest는 code를 받아서 accessToken을 응답 받은 객체
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
						
							OAuth2User oAuth2User = super.loadUser(userRequest); // google의 회원 프로필 조회
							// oAuth2User 정보를 어디에 담아서 무엇을 리턴하면 될까
							// 1. PrincipalDetails 에 OAuth2User 정보를 넣어준다.
							// 2. PrincipalDetails 를 리턴한다.
							// 기존의 로그인 + OAuth 로그인 => 세션 하나로 관리할 수 있음!
							System.out.println("userRequest clientRegistration: " + userRequest.getClientRegistration()); // code를 통해 응답 받은
																															// 회원정보
							System.out.println("oAuth2User : " + oAuth2User); // Token을 통해 응답 받은 회원정보
							System.out.println("userRequest tokenValue: " + userRequest.getAccessToken().getTokenValue());
					
							return processOAuth2User(userRequest, oAuth2User); // 세션에 등록되는 것
							// userRequest : 회원정보 요청하기 전에 회원의 모든 정보
							// oAuth2User : 회원정보 요청 후 정보
						}
					
						private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
							// 일반적으로 로그인할 때 유저 정보는 User Object가 들고 있음
							// OAuth2로 로그인할 때 유저 정보는 attributes ← 구성할 것!
							// DB에 해당하는 유저가 있는지 확인함
							// 있으면? → 유저 정보 updatec
					
							// Attribute를 파싱해서 공통 객체로 묶음 → 관리가 편함
							OAuth2UserInfo oAuth2UserInfo = null;
							if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
								oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
							} else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
								oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());			
							} else {
								System.out.println("여기는 Google, Facebook만 지원합니다.");
							}
					
							System.out.println("oAuth2UserInfo.getProvider() : " + oAuth2UserInfo.getProvider());
							System.out.println("oAuth2UserInfo.getProviderId() : " + oAuth2UserInfo.getProviderId());
					
							Optional<User> userOptional = userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());
					
							User user;
							if (userOptional.isPresent()) {
								user = userOptional.get();
								// 만약 OAuth 유저만 받을 거라면 update 해줘야 함
							} else {
								// user의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음 
								// provider, providerId 가 있으면 OAuth 유저 아니면 일반 유저 
								user = User.builder()
										.username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
										.email(oAuth2UserInfo.getEmail())
										.role("ROLE_USER")
										.Provider(oAuth2UserInfo.getProvider())
										.providerId(oAuth2UserInfo.getProviderId())
										.build();
								userRepository.save(user); // DB에 save
							}
					
							// 없으면? → 회원 가입 insert
							// return PrincipalDetails(); - Map 안에 attributes
							return new PrincipalDetails(user, oAuth2User.getAttributes()); // 세션에 담음
							// 일반적인 로그인할 때는 new PrincipalDetails(user); 
						}
}
