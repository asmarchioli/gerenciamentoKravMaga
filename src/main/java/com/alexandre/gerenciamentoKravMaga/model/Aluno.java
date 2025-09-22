package com.alexandre.gerenciamentoKravMaga.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="O nome é obrigatório!")
    @Size(max=100, message="O nome deve ter até 100 caracteres!")
    private String nome;

    @NotBlank(message="O CPF é obrigatório!")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "Formato de CPF inválido. Use XXX.XXX.XXX-XX")
    @Column(name="cpf", length=14, nullable=false, unique=true)
    private String cpf;

    @NotBlank(message="A faixa é obrigatória!")
    @Enumerated(EnumType.STRING)
    private Faixa faixa;

    @Size(max=15, message="O telefone deve ter até 15 caracteres!")
    @NotBlank(message="O telefone é obrigatório!")
    @Column(name="telefone", length=15, nullable=false, unique=true)
    private String telefone;

    @Size(max=15, message="O e-mail deve ter até 100 caracteres!")
    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O formato do e-mail é inválido.")
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "turma", length = 20, nullable = false)
    @NotBlank(message = "A turma é obrigatória.")
    private String turma;

    private String endereco;

    @Past
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Future
    @Column(name = "data_prox_graduacao")
    private LocalDate dataProxGraduacao;

}
