package com.marina.sistema_turismo.security;

import com.marina.sistema_turismo.model.Admin;
import com.marina.sistema_turismo.model.Agencia;
import com.marina.sistema_turismo.model.Cliente;
import com.marina.sistema_turismo.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

// Adaptador que traduz o Usuario para o formato do Spring Security 
public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String email;
    private final String senha;
    private final String role;
    private final Usuario usuario; // guardamos o objeto original para uso nos controllers

    public UserDetailsImpl(Usuario usuario) {
        this.usuario = usuario;
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.senha = usuario.getSenha();

        // Determina o papel com base no tipo real do objeto
        // Hibernate retorna Admin, Agencia ou Cliente
        if (usuario instanceof Admin) {
            this.role = "ROLE_ADMIN";
        } else if (usuario instanceof Agencia) {
            this.role = "ROLE_AGENCIA";
        } else if (usuario instanceof Cliente) {
            this.role = "ROLE_CLIENTE";
        } else {
            this.role = "ROLE_USER";
        }
    }

    // Permite que os controllers acessem o objeto
    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public String getRole() { return role; }

    // Métodos exigidos pela interface UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override public String getPassword() { return senha; }
    @Override public String getUsername() { return email; } // no Spring Security, username é o e-mail aqui

    // sempre retornam true pois não tem lógica de bloqueio de conta
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
