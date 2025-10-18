package com.alexandre.gerenciamentoKravMaga.repository;

import com.alexandre.gerenciamentoKravMaga.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByLoginAndSenha(String login, String senha);
}
