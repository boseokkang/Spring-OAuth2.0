package com.cos.securityex01.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.securityex01.model.User;
import com.cos.securityex01.repository.UserRepository;

@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 여기 등록해서 PrincipalDetailService 를 던져줌
		// request 시 username, password 받았을 때 데이터베이스에 있는지 없는지 판단함
		User user = userRepository.findByUsername(username);
		if (user == null) {
			return null;
		}
		return new PrincipalDetails(user);
	}
}
