package boraldan.shop.config;

import boraldan.shop.service.PersonDetailsService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * класс SecurityConfig представляет конфигурацию безопасности Spring Security для веб-приложения
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    // Сервис для получения информации о пользователях
    private final PersonDetailsService detailsService;
    /**
     * Конфигурация менеджера аутентификации.
     * @param http Конфигурация HTTP-безопасности
     * @return Менеджер аутентификации
     * @throws Exception Исключение, которое может возникнуть при конфигурации менеджера аутентификации
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }
    /**
     * Создание провайдера аутентификации DAO.
     * @return Провайдер аутентификации DAO
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(detailsService);
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
        return daoAuthenticationProvider;
    }
    /**
     * Получение кодировщика паролей.
     * @return Кодировщик паролей BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * Конфигурация цепочки фильтров безопасности.
     * @param http Конфигурация HTTP-безопасности
     * @return Цепочка фильтров безопасности
     * @throws Exception Исключение, которое может возникнуть при конфигурации цепочки фильтров безопасности
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/item/**", "/person/**").hasAnyRole("ADMIN")
                        .requestMatchers("/checkout", "/thankyou").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/public/**", "/auth/**", "/**").permitAll()
                        .anyRequest().hasAnyRole("USER", "ADMIN"))
                .formLogin(login -> login
                        .loginPage("/auth/login")
                        .permitAll()
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/auth/account", false)
                        .failureUrl("/auth/login?error"))
                .exceptionHandling(except -> except
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("http://localhost:8080/auth/login");
                        }))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login"))
                .sessionManagement((session) -> session
                        .invalidSessionUrl("/")
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                );
        return http.build();
    }

}
