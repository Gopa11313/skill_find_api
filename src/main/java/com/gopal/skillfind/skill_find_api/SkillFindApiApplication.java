package com.gopal.skillfind.skill_find_api;

import com.gopal.skillfind.skill_find_api.utils.JWTAuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
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
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/user/getUserInfo").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/skillFind/v1/user/getFeaturedProfile/{for}").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/skillFind/v1/user/getUserSkills").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/user/updateUserSkills").permitAll()
                    //post
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/post/createPost").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/post/editPost").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/post/getPost").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/post/deletePost").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/skillFind/v1/post/getUserPosts").permitAll()


                    //preference
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/userPref/createPref").permitAll()


                    //favorites
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/favorite/createFavorite").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/skillFind/v1/favorite/getFavorites").permitAll()
                    .antMatchers(HttpMethod.DELETE, "/api/skillFind/v1/favorite/deleteFavorite/{id}").permitAll()

                    //chat
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/chat/createChat").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/skillFind/v1/chat/getChat/{page}").permitAll()

                    //device
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/device/registerDevice").permitAll()

                    //admin
                    .antMatchers(HttpMethod.POST, "/api/skillFind/adminUser/createAdminUser").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/skillFind/adminUser/adminLogin").permitAll()

                    //services
                    .antMatchers(HttpMethod.POST, "/api/skillFind/service/createService").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/skillFind/service/getServices").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/skillFind/service/deleteService").permitAll()


                    //search
                    .antMatchers(HttpMethod.POST, "/api/skillFind/v1/search/createSearch").permitAll()

                    .antMatchers("/my-ws", "/my-ws/**").permitAll() // Adjust this based on your security needs
                    .anyRequest().authenticated();


        }

    }

}
