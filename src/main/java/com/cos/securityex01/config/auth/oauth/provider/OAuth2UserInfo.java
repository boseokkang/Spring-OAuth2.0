package com.cos.securityex01.config.auth.oauth.provider;


//OAuth2.0 제공자들마다 응답해주는 속성 값이 다르기 때문에 공통으로 만들어준다.
public interface OAuth2UserInfo {
	String getProviderId();
	String getProvider();
	String getName();
	String getEmail();
}