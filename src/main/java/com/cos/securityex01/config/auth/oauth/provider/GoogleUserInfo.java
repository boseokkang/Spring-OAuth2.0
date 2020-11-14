package com.cos.securityex01.config.auth.oauth.provider;

import java.util.Map;

//  2. implements
public class GoogleUserInfo implements OAuth2UserInfo {
			
			// 1. 전역 선언
			private Map<String, Object> attributes;
	
			// 3. 생성자 만들기
			public GoogleUserInfo(Map<String, Object> attributes) {
				this.attributes = attributes;
			}
	
			// 4. override
			@Override
			public String getProviderId() {
				return (String) attributes.get("sub");
			}
	
			@Override
			public String getProvider() {
				return "google";
			}
	
			@Override
			public String getName() {
				return (String) attributes.get("name");
			}
	
			@Override
			public String getEmail() {
				return (String) attributes.get("email");
			}
}
