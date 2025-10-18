package com.alexandre.gerenciamentoKravMaga.service;

import com.alexandre.gerenciamentoKravMaga.model.Administrador;
import com.alexandre.gerenciamentoKravMaga.repository.AdministradorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdministradorService {
    private final AdministradorRepository administradorRepository;

    public AdministradorService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    public Optional<Administrador> autenticar(String login, String senha) {
        return administradorRepository.findByLoginAndSenha(login, senha);
    }



}
