package lv.venta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lv.venta.service.impl.MyUserDetailsManagerService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public MyUserDetailsManagerService createDetailsService() {
		return new MyUserDetailsManagerService();
	}
	
	@Bean
	public DaoAuthenticationProvider createProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(createDetailsService());
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		provider.setPasswordEncoder(encoder);
		return provider;
	}
	
	@Bean
	public SecurityFilterChain configureEndpoints(HttpSecurity http) {
		http.authorizeHttpRequests(
				auth->auth
				.requestMatchers("/home").hasAnyAuthority("ADMIN", "USER")
				.requestMatchers("/manage/**").hasAnyAuthority("ADMIN")
				.anyRequest().permitAll()
		);
		
		http.formLogin(auth->auth.permitAll());
		return http.build();
	}
}

