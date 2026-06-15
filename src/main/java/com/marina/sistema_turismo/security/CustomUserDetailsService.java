package com.marina.sistema_turismo.security;

import com.marina.sistema_turismo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

// Serviço chamado pelo Spring Security no momento do login
// Dado um e-mail, retorna o UserDetails correspondente para que o Spring verifique a senha
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    // E-mail do admin lido do application.properties
    @Value("${app.admin.email}")
    private String adminEmail;

    // Hash da senha do admin gerado uma única vez ao iniciar a aplicação
    private final String adminPasswordHash;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository,
                                    PasswordEncoder passwordEncoder,
                                    @Value("${app.admin.senha}") String adminSenha) {
        this.usuarioRepository = usuarioRepository;
        // Codifica a senha do admin em BCrypt  na inicialização
        this.adminPasswordHash = passwordEncoder.encode(adminSenha);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Verifica primeiro se é o admin 
        if (adminEmail.equalsIgnoreCase(email)) {
            return User.builder()
                    .username(adminEmail)
                    .password(adminPasswordHash)
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                    .build();
        }

        // Busca na tabela "usuarios" e retorna a subclasse real (Agencia ou Cliente)
        var usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        // Envolve o usuario no nosso adaptador que o Spring Security usa
        return new UserDetailsImpl(usuario);
    }
}
