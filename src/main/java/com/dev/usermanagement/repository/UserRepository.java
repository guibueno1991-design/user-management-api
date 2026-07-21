package com.dev.usermanagement.repository;

import com.dev.usermanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // Método para buscar usuário pelo nome no banco de dados (usado no Login)
    Optional<User> findByUsername(String username);
    
    // Método para verificar se um username já existe antes de cadastrar
    boolean existsByUsername(String username);
}