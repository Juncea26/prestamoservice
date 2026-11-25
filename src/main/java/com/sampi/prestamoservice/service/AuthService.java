package com.sampi.prestamoservice.service;

import com.sampi.prestamoservice.dto.AuthResponse;
import com.sampi.prestamoservice.dto.LoginRequest;
import com.sampi.prestamoservice.dto.RegisterRequest;
import com.sampi.prestamoservice.entity.Usuario;
import com.sampi.prestamoservice.repository.UsuarioRepository;
import com.sampi.prestamoservice.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        String email = req.getEmail().trim();
        if (usuarioRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una cuenta registrada con el correo: " + email
            );
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(req.getNombre().trim());
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(passwordEncoder.encode(req.getPassword()));
        nuevoUsuario.setRol("USER");

        Usuario guardado = usuarioRepository.save(nuevoUsuario);
        String jwt = jwtService.generateToken(guardado);

        return new AuthResponse(jwt, guardado.getNombre(), guardado.getEmail());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        String email = req.getEmail().trim();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, req.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Credenciales inválidas"
                ));

        String jwt = jwtService.generateToken(usuario);
        return new AuthResponse(jwt, usuario.getNombre(), usuario.getEmail());
    }
}
