package org.example.picker.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.client.RestTemplate;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/login","/register").permitAll()
                                .requestMatchers("/getPfpEmbeddings").permitAll()
                                .requestMatchers("/getImageEmbeddings").permitAll()
                                .requestMatchers("/getdownloadPfpUrl").permitAll()
                                .requestMatchers("/getdownloadImageUrl").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessions ->
                        sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return (web) -> web.httpFirewall(firewall);
    }

}
