package com.prog4.EjemploDesdeCero.features.usuario.services.impl.domain;

import org.springframework.stereotype.Service;

import com.prog4.EjemploDesdeCero.features.usuario.repositories.IUserRepository;
import com.prog4.EjemploDesdeCero.features.usuario.services.interfaces.domain.IUserDetailsService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsService implements IUserDetailsService {

    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
    
}
