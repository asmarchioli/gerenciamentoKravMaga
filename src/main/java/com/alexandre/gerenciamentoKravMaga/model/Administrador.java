package com.alexandre.gerenciamentoKravMaga.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "administradores")
public class Administrador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="login", length=50, nullable=false, unique=true)
    private String login;
    @Column(name="senha", length=100, nullable=false)
    private String senha;
}