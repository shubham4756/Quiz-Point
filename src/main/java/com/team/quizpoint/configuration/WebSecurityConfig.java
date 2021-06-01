package com.team.quizpoint.configuration;

import com.team.quizpoint.repository.RememberTokenRepository;
import com.team.quizpoint.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MyUserDetailService userDetailsService;

//    @Autowired
//    PersistentTokenRepository repository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**","/createquizform").hasAnyRole("ADMIN", "USER")
                .antMatchers("/", "/register", "/login","/checkEmail", "/login-error","/register/verify",
                        "/forgotPasswordEmail","/password/**","/dashboard").permitAll()
                .anyRequest()
                .authenticated()
                .and().rememberMe()
                .userDetailsService(this.userDetailsService)       // to use cookies
//                .tokenRepository(repository)       // to use databases
                .tokenValiditySeconds(1200)
                .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/",true)
                    .failureUrl("/login-error")
                    .usernameParameter("username")
                    .passwordParameter("password")
                .and()
                .logout()
                    .logoutUrl("/logout").permitAll()
                    .logoutSuccessUrl("/");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
