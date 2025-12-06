package com.miniProjets.covoiturage.config;

import com.miniProjets.covoiturage.security.CustomUserDetailsService;
import com.miniProjets.covoiturage.security.JwtAuthenticationEntryPoint;
import com.miniProjets.covoiturage.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    @Bean
    public JwtAuthenticationFilter authenticationJwtTokenFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF disabled for stateless JWT authentication
        // All endpoints require valid JWT token in Authorization header
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ENABLE CORS
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/trajets", "/trajets/**").permitAll()
                        .requestMatchers("/trajets/rechercher", "/trajets/search").permitAll()

                        // Trip management - only drivers
                        .requestMatchers(HttpMethod.POST, "/trajets").hasRole("DRIVER")
                        .requestMatchers(HttpMethod.PUT, "/trajets/**").hasRole("DRIVER")
                        .requestMatchers(HttpMethod.DELETE, "/trajets/**").hasRole("DRIVER")

                        // Driver-specific endpoints
                        .requestMatchers("/conducteurs/{id}/trajets/**").hasAnyRole("DRIVER", "ADMIN")
                        .requestMatchers("/conducteurs/{id}/voiture/**").hasAnyRole("DRIVER", "ADMIN")

                        // Passenger-specific endpoints
                        .requestMatchers("/passagers/{id}/reservations/**").authenticated()

                        // Vehicle management - authenticated users
                        .requestMatchers("/voitures", "/voitures/**").authenticated()

                        // Reservations - authenticated users
                        .requestMatchers("/reservations", "/reservations/**").authenticated()

                        // Admin-only endpoints
                        .requestMatchers("/conducteurs", "/passagers").hasRole("ADMIN")

                        // All other requests must be authenticated
                        .anyRequest().authenticated());

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Use configurable origins from application.properties
        // Can be overridden with ALLOWED_ORIGINS environment variable
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Specify allowed headers instead of wildcard
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
