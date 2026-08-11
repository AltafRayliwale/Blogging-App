package Blogging.App.config;

import Blogging.App.security.CustomerUserDetailsService;
import Blogging.App.security.JWTAuthenticationEntryPoint;
import Blogging.App.security.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.OpenAPI31;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import org.springframework.web.filter.CorsFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebSecurity

//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    public static final String[] PUBLIC_URLS = {"/auth/**",
            "posts/v1/**",
            "/categories/**",
            "/comments/**",
            "/v3/api-docs",
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/**"
    };

    @Autowired
    private CustomerUserDetailsService userDetailsService;

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
//    @Bean
//    public SecurityFilterChain securityFilterChain1(HttpSecurity http) throws Exception {
//        http  // Disable CSRF for simplicity, consider re-enabling it for production
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers("/auth/login").permitAll()
//                        .anyRequest()
//                        .authenticated()// All requests require authentication
//                       ).httpBasic(Customizer.withDefaults());  // Enable basic HTTP authentication
//
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//       http.exceptionHandling().authenticationEntryPoint(this.jwtAuthenticationEntryPoint);
//        return http.build();  
//        // Return the built SecurityFilterChain
//}


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//                .cors(Customizer.withDefaults())
//              
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/v1/auth/**").permitAll() 
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(httpBasic -> httpBasic.disable())
//                .formLogin(form -> form.disable()); 
//
//        return http.build();
//    }
    
    
    
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }




    protected void configure(AuthenticationManagerBuilder auth) throws Exception{

        auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }


}

