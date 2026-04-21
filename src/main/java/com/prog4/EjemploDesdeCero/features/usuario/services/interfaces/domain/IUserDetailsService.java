package com.prog4.EjemploDesdeCero.features.usuario.services.interfaces.domain;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserDetailsService extends UserDetailsService {
    UserDetails loadUserByUsername(String username);
}
