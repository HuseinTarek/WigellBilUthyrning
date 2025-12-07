package se.wigell.biluthyrning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import se.wigell.biluthyrning.service.UserService;
import se.wigell.biluthyrning.model.User;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // java
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login.html",
                                "/user.html",
                                "/admin.html",
                                "/login.js",
                                "/user.js",
                                "/admin.js",
                                "/global.css",
                                "/login.css",
                                "/user.css",
                                "/admin.css",
                                "/KoncernensLogga.png"
                        ).permitAll()

                        // admin endpoints require ADMIN
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // allow both USER and ADMIN to call user APIs (avoid 403 when admin should access these)
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")   // make sure these match your login form inputs
                        .passwordParameter("password")
                        .successHandler((req, res, auth) -> {
                            System.out.println("LOGIN AUTHORITIES -> " + auth.getAuthorities());
                            auth.getAuthorities().forEach(a -> System.out.println("AUTH -> " + a.getAuthority()));

                            boolean isAdmin = auth.getAuthorities().stream()
                                    .map(a -> a.getAuthority().trim())
                                    .anyMatch(g -> "ROLE_ADMIN".equalsIgnoreCase(g) || "ADMIN".equalsIgnoreCase(g));

                            if (isAdmin) {
                                res.sendRedirect("/admin.html");
                            } else {
                                res.sendRedirect("/user.html");
                            }
                        })
                        .failureUrl("/login.html?error=true")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login.html")
                        .permitAll()
                )
                .userDetailsService(userService);

        return http.build();
    }




    // CORS CONFIGURATION
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // COMMENT: Allow frontend to call backend during development
        config.addAllowedOriginPattern("*");     // allow all origins
        config.addAllowedMethod("*");            // allow GET, POST, PUT, DELETE
        config.addAllowedHeader("*");            // allow all headers
        config.setAllowCredentials(true);        // allow credentials if needed

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
