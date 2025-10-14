package com.alexandre.gerenciamentoKravMaga.controller;

import com.alexandre.gerenciamentoKravMaga.model.Aluno;
import com.alexandre.gerenciamentoKravMaga.model.Faixa;
import com.alexandre.gerenciamentoKravMaga.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/alunos")
public class AlunoController {
    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping
    public String listarAlunos(@RequestParam(name="tipo_busca", required=false, defaultValue="nome") String tipo_busca,
                                @RequestParam(name="termo_busca", required=false) String termo_busca,
                                Model model){
        List<Aluno> alunos;
        if (termo_busca != null && !termo_busca.trim().isEmpty()) {
            alunos = switch (tipo_busca) {
                            case "cpf" -> alunoService.pesquisarPorCPF(termo_busca);
                            case "email" -> alunoService.pesquisarPorEmail(termo_busca);
                            case "telefone" -> alunoService.pesquisarPorTelefone(termo_busca);
                            default -> alunoService.pesquisarPorNome(termo_busca);
            };
        } else {
            alunos = alunoService.listarTodos();
        }
        model.addAttribute("alunos", alunos);
        model.addAttribute("termo_busca", termo_busca);
        model.addAttribute("tipo_busca", tipo_busca);
        return "alunos/lista";
    }

    @GetMapping("/novo")
    public String mostrarFormularioNovo(Model model){
        model.addAttribute("aluno", new Aluno());
        model.addAttribute("faixas", Faixa.values());
        return "alunos/form";
    }

    @PostMapping
    public String cadastrarAluno(@Valid @ModelAttribute Aluno aluno,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes ra){
        //VALIDAÇÃO DE UNICIDADE PARA CPF
        Optional<Aluno> alunoExistenteCpf = alunoService.buscarPorCPF(aluno.getCpf());
        if (alunoExistenteCpf.isPresent() && !alunoExistenteCpf.get().getId().equals(aluno.getId())) {
            result.rejectValue("cpf", "error.aluno", "CPF já cadastrado no sistema.");
        }

        //VALIDAÇÃO DE UNICIDADE PARA TELEFONE
        Optional<Aluno> alunoExistenteTelefone = alunoService.buscarPorTelefone(aluno.getTelefone());
        if (alunoExistenteTelefone.isPresent() && !alunoExistenteTelefone.get().getId().equals(aluno.getId())) {
            result.rejectValue("telefone", "error.aluno", "Telefone já cadastrado no sistema.");
        }

        //VALIDAÇÃO DE UNICIDADE PARA E-MAIL
        Optional<Aluno> alunoExistenteEmail = alunoService.buscarPorEmail(aluno.getEmail());
        if (alunoExistenteEmail.isPresent() && !alunoExistenteEmail.get().getId().equals(aluno.getId())) {
            result.rejectValue("email", "error.aluno", "E-mail já cadastrado no sistema.");
        }

        if (result.hasErrors()) {
            model.addAttribute("faixas", Faixa.values());
            return "alunos/form";
        }
        alunoService.salvar(aluno);
        ra.addFlashAttribute("msgSucesso", "Aluno cadastrado com sucesso!");
        return "redirect:/alunos";
    }

    @GetMapping("/editar/{id}")
    public String abrirEdicao(@PathVariable Long id, Model model, RedirectAttributes ra){
        Optional<Aluno> aluno = alunoService.pesquisarPorId(id);
        if (aluno.isPresent()) {
            model.addAttribute("aluno", aluno.get());
            model.addAttribute("faixas", Faixa.values());
            return "alunos/form";
        } else {
            ra.addFlashAttribute("msgErro", "Aluno não encontrado!");
            return "redirect:/alunos";
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletarAluno(@PathVariable Long id, RedirectAttributes ra){
        if (!alunoService.existsById(id)){
            ra.addFlashAttribute("msgErro", "Aluno não encontrado!");
        } else {
            alunoService.deletarPorId(id);
            ra.addFlashAttribute("msgSucesso", "Aluno excluído com sucesso!");
        }
        return "redirect:/alunos";
    }
}
