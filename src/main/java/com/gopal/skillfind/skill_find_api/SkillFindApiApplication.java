package com.gopal.skillfind.skill_find_api;

import com.gopal.skillfind.skill_find_api.utils.JWTAuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SpringBootApplication
public class SkillFindApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkillFindApiApplication.class, args);
    }

    @EnableWebSecurity
    @Configuration
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    //user
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/user/createUser").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/user/login").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/user/guestLogin").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/user/updatePersonalInfo").permitAll()
                    //post
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/post/createPost").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/post/editPost").permitAll()



                    //preference
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/userPref/createPref").permitAll()

                    //admin
                    .antMatchers(HttpMethod.POST, "/api/skillFind/adminUser/createAdminUser").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/skillFind/adminUser/adminLogin").permitAll()

                    //services
                    .antMatchers(HttpMethod.POST, "/api/skillFind/service/createService").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/skillFind/service/getServices").permitAll()
                    .anyRequest().authenticated();
        }
    }

}
