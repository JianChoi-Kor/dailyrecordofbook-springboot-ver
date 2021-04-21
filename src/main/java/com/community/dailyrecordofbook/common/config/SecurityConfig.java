package com.community.dailyrecordofbook.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/css/**", "/img/**", "/js/**"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 필터 등록
        http
                .httpBasic().disable() // REST API만 고려, 기본 설정을 해제
                .csrf().disable() // csrf 사용 X
                .authorizeRequests().antMatchers("/user/login", "/user/sign", "/")
                // 요청에 대한 사용 권한 체크
                .permitAll().anyRequest().authenticated()
                // 나머지 요청은 누구나 접근 가능
                .and().exceptionHandling().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                // 토큰 기반 인증이므로 세션도 사용 X
    }

    // 암호화에 필요한 PasswordEncoder Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // authenticationManager Bean 등록
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
