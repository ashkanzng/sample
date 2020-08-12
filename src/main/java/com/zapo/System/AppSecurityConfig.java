package com.zapo.System;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import Services.UserDetailsServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserDetailsServiceImpl  userDetailsServiceImpl;
	
	@Bean
	public AuthenticationProvider authProvider()
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsServiceImpl);
		/* provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());*/
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/css/**","/images/**","/js/**").permitAll()
			.antMatchers("/static/**").permitAll() //for reactjs
			.antMatchers("/").permitAll()
			.antMatchers("/privateMedia/**").access("hasRole('ADMIN') or hasRole('USER') ")
			.antMatchers("/inventory").access("hasRole('ADMIN') or hasRole('USER') ")
			.antMatchers("/customers").access("hasRole('ADMIN') or hasRole('USER') ")
			.antMatchers("/vendors").access("hasRole('ADMIN') or hasRole('USER') ")
			.antMatchers("/items").access("hasRole('ADMIN') or hasRole('USER') ")
			.antMatchers("/users").access("hasRole('ADMIN')")
			.antMatchers("/attributes").access("hasRole('ADMIN') or hasRole('USER') ")
			.antMatchers("/purchases").access("hasRole('ADMIN') or hasRole('USER') ")
			.antMatchers("/orders").access("hasRole('ADMIN') or hasRole('USER') ")
			.antMatchers("/tracking").access("hasRole('ADMIN') or hasRole('USER') ")
			.anyRequest().authenticated()
			.and()
			.formLogin().loginPage("/login").permitAll()
			.and()
			.logout().logoutSuccessUrl("/login").permitAll()
			.and()
			.rememberMe()
			.userDetailsService(userDetailsServiceImpl)
			.tokenRepository(tokenRepository())
			.tokenValiditySeconds(1286400);
		http.csrf().disable();
        http.headers().frameOptions().disable();
	}

	PasswordEncoder passwordEncoder ()
	{
		return new BCryptPasswordEncoder();
	}

	@Bean
	public GrantedAuthorityDefaults grantedAuthorityDefaults() {
		return new GrantedAuthorityDefaults("role_");
	}

	@Bean
	public PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		jdbcTokenRepositoryImpl.setDataSource(dataSource);
		return jdbcTokenRepositoryImpl;
	}

}
