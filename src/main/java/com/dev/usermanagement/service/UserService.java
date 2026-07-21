package com.dev.usermanagement.service;

import com.dev.usermanagement.dto.LoginDTO;
import com.dev.usermanagement.dto.RegisterDTO;
import com.dev.usermanagement.model.User;
import com.dev.usermanagement.repository.UserRepository;
import com.dev.usermanagement.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Método para cadastrar novo usuário
    public User register(RegisterDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new RuntimeException("Usuário já existe!");
        }

        // Criptografa a senha antes de salvar no banco
        String encryptedPassword = passwordEncoder.encode(dto.password());
        
        // Se não informar a role, define como ROLE_USER por padrão
        String role = (dto.role() != null && !dto.role().isBlank()) ? dto.role() : "ROLE_USER";

        User newUser = new User(dto.username(), encryptedPassword, role);
        return userRepository.save(newUser);
    }

    // Método para realizar o Login e retornar o Token JWT
    public String login(LoginDTO dto) {
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        // Verifica se a senha enviada corresponde à senha criptografada no banco
        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new RuntimeException("Senha incorreta!");
        }

        // Gera e retorna o token JWT
        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }

    // Listar todos os usuários (apenas para testes/admin)
    public List<User> findAll() {
        return userRepository.findAll();
    }
}