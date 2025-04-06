package com.taller.bibliotecas;

import com.taller.bibliotecas.config.JWTAuthorizationFilter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@SpringBootApplication
public class BibliotecasApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotecasApplication.class, args);
	}

	@Configuration
    @EnableWebSecurity
	public class WebSecurityConfig {

		@Value("${jwt.secret.key}")
		private String secret;

		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			http.cors().and().csrf().disable()
					.headers(headers -> headers
							.frameOptions().disable()  // Desactiva el X-Frame-Options para el control por Content-Security-Policy
							.contentSecurityPolicy("frame-ancestors 'self' http://localhost:4200")  // Permite iframes solo desde localhost:4200
					)
					.addFilterAfter(new JWTAuthorizationFilter(secret), UsernamePasswordAuthenticationFilter.class)
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and().authorizeRequests()
					// Permitir acceso a Swagger UI sin autenticación
					.requestMatchers(
							"/swagger-ui/**",       // Ruta de la interfaz de Swagger UI
							"/v3/api-docs/**",      // Ruta del JSON de OpenAPI
							"/swagger-resources/**", // Recursos de Swagger
							"/swagger-ui.html",     // Ruta alternativa de Swagger UI
							"/webjars/**"           // Recursos estáticos de Swagger
					).permitAll()
					// Permitir acceso a las rutas de la API sin autenticación
					.requestMatchers(HttpMethod.POST, "/api/**").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/**").permitAll()
					.requestMatchers(HttpMethod.PUT, "/api/**").permitAll()
					// Todas las demás rutas requieren autenticación
					.anyRequest().authenticated();

			return http.build();
		}
/*
		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

			http.cors().and().csrf().disable()
					.headers(headers -> headers
							.frameOptions().disable()  // Desactiva el X-Frame-Options para el control por Content-Security-Policy
							.contentSecurityPolicy("frame-ancestors 'self' http://localhost:4200")  // Permite iframes solo desde localhost:4200
					)
					.addFilterAfter(new JWTAuthorizationFilter(secret), UsernamePasswordAuthenticationFilter.class)
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and().authorizeRequests()
					.requestMatchers(HttpMethod.POST, "/api/**").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/**").permitAll()
					.requestMatchers(HttpMethod.PUT, "/api/**").permitAll()
					.anyRequest().authenticated();
			return http.build();
		}
*/

		@Bean
		public WebMvcConfigurer corsConfigurer() {
			return new WebMvcConfigurer() {
				@Override
				public void addCorsMappings(CorsRegistry registry) {
					registry.addMapping("/api/**")
							.allowedOrigins(
									"http://localhost:4200",      // Para desarrollo en Angular
									"http://localhost:8100",      // Para desarrollo en Ionic
									"capacitor://localhost",      // Para aplicaciones con Capacitor
									"http://localhost"
							)
							.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Solo una línea para métodos permitidos
							.allowedHeaders("*")
							.allowCredentials(true); // Permite enviar cookies o cabeceras de autenticación
				}
			};
		}
	}
}
//areas, editoriales, tipos,autores
