# Spring Security 기본 Version.1

### MySQL DB 및 사용자 생성

```sql
create user 'cos'@'%' identified by 'cos1234';
GRANT ALL PRIVILEGES ON *.* TO 'cos'@'%';
create database security;
use security;
```

### SecurityConfig.java 권한 설정 방법

```java
// 1. 함수 내부에 권한 설정하기
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
	        .antMatchers("/user/**").authenticated()
            .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // ADMIN만 들어갈 수 있음
            .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
            .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')")
    }
// 2. 컨트롤러에 권한 설정하기
// (1) SecurityConfig.java에 설정 : 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화 (Controller 접근 전에 낚아서 처리)
    @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
// (2) IndexController.java에 어노테이션 거는 방법 3가지
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostAuthorize("hasRole('ROLE_ADMIN')")
    @Secured("ROLE_ADMIN")
```
