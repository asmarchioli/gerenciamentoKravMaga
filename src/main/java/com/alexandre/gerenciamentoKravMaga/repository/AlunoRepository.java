package com.alexandre.gerenciamentoKravMaga.repository;

import com.alexandre.gerenciamentoKravMaga.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    //Método mágico do o Sp. Dt. JPA
    List<Aluno> findByNomeContainingIgnoreCase(String nome);

    Optional<Aluno> findByCpf(String cpf);

    Optional<Aluno> findByEmailIgnoreCase(String email);
}
