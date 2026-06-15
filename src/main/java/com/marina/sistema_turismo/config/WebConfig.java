package com.marina.sistema_turismo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import java.util.Locale;

// Configuração da internacionalização (i18n)
// Define o idioma padrão (português) e permite que o usuário mude o idioma pela URL com ?lang=en
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Define que o idioma fica salvo na sessão do usuário
    // Assim o idioma escolhido persiste enquanto a sessão estiver ativa
    @Bean
    LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(new Locale("pt", "BR")); // português como padrão
        return resolver;
    }

    // Interceptor que observa todas as requisições
    // Se a URL contém "?lang=en", troca o idioma para inglês; "?lang=pt" volta para português
    @Bean
    LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang"); // parâmetro que aciona a troca
        return interceptor;
    }

    // Registra o interceptor para que ele seja executado em todas as requisições
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
