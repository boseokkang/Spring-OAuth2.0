package com.cos.securityex01.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // IoC에 Bean(빈) 을 등록
@EnableWebSecurity // // 필터 체인 관리할 수 있는 어노테이션
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화 (Controller 접근 전에 낚아서 처리)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean // IoC 등록 - 메소드를 IoC → 리턴 타입이 있어야 함
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder(); 
	}
	
   // 해당 주소만 잠그고 나머지 다 열어 놓음
   @Override
   protected void configure(HttpSecurity http) throws Exception {
	   http.csrf().disable(); // 내가 form 태그를 생성해서 보내기 때문에 비활성화
	   http.authorizeRequests()
	   	 .antMatchers("/user/**").authenticated()
//       .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // ADMIN만 들어갈 수 있음
//       .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//       .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')")
         .anyRequest().permitAll()
      .and()
         .formLogin()
         .loginPage("/login") // 잠근 페이지를 해당 페이지로 맵핑
         .loginProcessingUrl("/loginProc") // login.mustache 에서 post 방식으로 보냄
         .defaultSuccessUrl("/"); // 성공 시 redirection되는 페이지 
   }
}