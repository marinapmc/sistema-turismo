package com.marina.sistema_turismo.security;

import com.marina.sistema_turismo.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Serviço chamado pelo Spring Security no momento do login
// Dado um e-mail, retorna o UserDetails correspondente para que o Spring verifique a senha
// Admin, agência e cliente são todos buscados do banco — nenhum usuário fica só em memória
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca na tabela "usuarios" e retorna a subclasse real (Admin, Agencia ou Cliente)
        var usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        // Envolve o usuario no nosso adaptador que o Spring Security usa
        return new UserDetailsImpl(usuario);
    }
}
