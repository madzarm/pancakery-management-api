package com.example.pancakes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfiguration(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.userDetailsService(userDetailsService2());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/ingredient**").hasAnyRole("EMPLOYEE","ADMIN")
                .antMatchers("/order**").hasAnyRole("EMPLOYEE","ADMIN")
                .antMatchers("/pancake**").hasAnyRole("CUSTOMER","ADMIN")
                .antMatchers("/report**").hasAnyRole("STOREOWNER","ADMIN")
                .antMatchers("/register**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }


    @Bean
    protected UserDetailsService userDetailsService2() {
        UserDetails firstUser = User.builder()
                .username("customer")
                .password(passwordEncoder.encode("customer"))
                .roles("CUSTOMER")
                .build();
        UserDetails secondUser = User.builder()
                .username("employee")
                .password(passwordEncoder.encode("employee"))
                .roles("EMPLOYEE")
                .build();
        UserDetails thirdUser = User.builder()
                .username("storeowner")
                .password(passwordEncoder.encode("storeowner"))
                .roles("STOREOWNER")
                .build();

        return new InMemoryUserDetailsManager(
                firstUser,secondUser,thirdUser
        );
    }

}
