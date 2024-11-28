package com.codenal.security.config;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.codenal.employee.service.EmployeeService;
import com.codenal.security.service.SecurityService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final DataSource dataSource;
    private EmployeeService employeeService;

    @Autowired
    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityService securityService) throws Exception {
        http
        // CORS 설정 적용
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/auth-signin-basic", "/assets/**", "/api/approved-annual-leaves").permitAll()
                .requestMatchers("/admin/list").permitAll()
                .requestMatchers("/admin/join").permitAll()
                .requestMatchers("/admin/update/**").permitAll()
                .requestMatchers("/admin/dept").permitAll()
                .requestMatchers("/announce/createEnd").permitAll()
                .requestMatchers("/announce/delete/**").permitAll()
                .requestMatchers("/announce/updateEnd/**").permitAll()
                .requestMatchers("/approval/leaveUpdate/**").permitAll()
                .requestMatchers("/approval/detail/**").permitAll()
                .requestMatchers("/api/approved-annual-leaves").permitAll()
                .requestMatchers("/employee/addressBook/**").permitAll()
                .requestMatchers("/approval/**").authenticated()
                .requestMatchers("/approval/update/**").authenticated()
                .requestMatchers("/list").permitAll()
                .requestMatchers("/mypage/**", "/").authenticated()
                .requestMatchers("/documents/**").authenticated()
                .requestMatchers("/api/attendance/**").permitAll()
                .requestMatchers("/topbar/**").permitAll()
                .requestMatchers("/chatList/**", "/chatting").permitAll()
                .anyRequest().authenticated()
        )
        .formLogin(login ->
            login.loginPage("/auth-signin-basic")
                .loginProcessingUrl("/auth-signin-basic")
                .usernameParameter("emp_id")
                .passwordParameter("emp_pw")
                .permitAll()
                .defaultSuccessUrl("/", true)
                .successHandler(myLoginSuccessHandler())
                .failureHandler(myLoginFailureHandler())
        )
        .logout((logout) -> logout
            .logoutUrl("/auth-logout-basic")
            .logoutSuccessUrl("/auth-signin-basic?auth-logout-basic=true")
            .permitAll()
        )
        .rememberMe((rememberMe) -> rememberMe
            .key("uniqueAndSecret")
            .tokenRepository(tokenRepository())
            .tokenValiditySeconds(86400 * 7)
            .userDetailsService(securityService)
        );

        return http.build(); // SecurityFilterChain 객체를 반환하도록 수정
    }


    @Bean
    public MyLoginSuccessHandler myLoginSuccessHandler() {
        return new MyLoginSuccessHandler(employeeService);
    }

    @Bean
    public MyLoginFailureHandler myLoginFailureHandler() {
        return new MyLoginFailureHandler();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> 
            web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        );
    }

    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/uploads/**")
                .addResourceLocations("/classpath:/uploads/");
        }
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // CORS 설정을 위한 메서드 추가
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 허용할 도메인 설정
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // 허용할 HTTP 메서드 설정
        configuration.setAllowedHeaders(List.of("*")); // 허용할 헤더 설정
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
