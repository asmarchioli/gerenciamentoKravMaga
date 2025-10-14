package com.alexandre.gerenciamentoKravMaga.service;

import com.alexandre.gerenciamentoKravMaga.model.Aluno;
import com.alexandre.gerenciamentoKravMaga.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {
    @Autowired
    private AlunoRepository alunoRepository;

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll(Sort.by("nome").ascending());
    }

    public Optional<Aluno> pesquisarPorId(Long id){
        return alunoRepository.findById(id);
    }

    public Optional<Aluno> buscarPorCPF(String cpf){
        return alunoRepository.findByCpf(cpf);
    }
    public Optional<Aluno> buscarPorEmail(String email){
        return alunoRepository.findByEmailIgnoreCase(email);
    }

    public Optional<Aluno> buscarPorTelefone(String telefone){
        return alunoRepository.findByTelefone(telefone);
    }

    public List<Aluno> pesquisarPorCPF(String cpf) {
        return alunoRepository.findByCpf(cpf)
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }

    public List<Aluno> pesquisarPorEmail(String email) {
        return alunoRepository.findByEmailIgnoreCase(email)
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }

    public List<Aluno> pesquisarPorTelefone(String telefone) {
        return alunoRepository.findByTelefone(telefone)
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }

    public List<Aluno> pesquisarPorNome(String nome){
        return alunoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public void salvar(Aluno aluno){
        alunoRepository.save(aluno); //save cria e atualiza, caso já exista id de aluno
    }

    public void deletarPorId(Long id){
        alunoRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return alunoRepository.existsById(id);
    }
}
