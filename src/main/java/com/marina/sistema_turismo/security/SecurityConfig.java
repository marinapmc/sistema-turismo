package com.marina.sistema_turismo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Configuração central do Spring Security
// Define quem pode acessar o quê, como o login funciona e como o logout é feito
// Usa a API moderna do Spring Security 6 com SecurityFilterChain (não mais WebSecurityConfigurerAdapter)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Bean do codificador de senhas — BCrypt é o padrão recomendado
    // Injeta automaticamente em qualquer lugar que precise de PasswordEncoder
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Expõe o AuthenticationManager para ser usado manualmente se necessário
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                // CSRF é uma proteção para formulários HTML — desativamos apenas nos endpoints REST,
                // pois clientes de API (Postman, mobile) não enviam o token CSRF
                .ignoringRequestMatchers(
                    "/clientes", "/clientes/**",
                    "/agencias", "/agencias/**",
                    "/pacotes", "/pacotes/**"
                )
            )
            .authorizeHttpRequests(auth -> auth
                // Arquivos estáticos e uploads são públicos (CSS, JS, imagens, PDFs)
                .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()

                // Página inicial e login são acessíveis por todos
                .requestMatchers("/", "/login", "/erro").permitAll()

                // Listagem e detalhe de pacotes são públicos (R4)
                .requestMatchers(HttpMethod.GET, "/pacotes", "/pacotes/*").permitAll()

                // Todos os endpoints REST são públicos (sem autenticação), conforme AA-2
                .requestMatchers(HttpMethod.GET,
                    "/clientes", "/clientes/**",
                    "/agencias", "/agencias/**",
                    "/pacotes", "/pacotes/**").permitAll()
                .requestMatchers(HttpMethod.POST,
                    "/clientes", "/agencias", "/pacotes/agencias/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/clientes/**", "/agencias/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/clientes/**", "/agencias/**").permitAll()

                // Área do admin: CRUD de clientes e agências
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Área da agência: cadastro e listagem dos próprios pacotes
                .requestMatchers("/agencia/**").hasRole("AGENCIA")

                // Área do cliente: comprar e ver histórico
                .requestMatchers("/cliente/**", "/compras/**").hasRole("CLIENTE")

                // Qualquer outra rota exige login
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")           // página de login personalizada
                .defaultSuccessUrl("/", true)  // após login, redireciona para "/"
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")         // após logout, volta para a home
                .invalidateHttpSession(true)   // encerra a sessão do usuário
                .deleteCookies("JSESSIONID")   // remove o cookie de sessão do navegador
                .permitAll()
            );

        return http.build();
    }
}
