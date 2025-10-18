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
import jakarta.servlet.http.HttpSession;

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
                                Model model, HttpSession session){
        if (termo_busca != null){
            session.setAttribute("termo_busca_sessao", termo_busca);
            session.setAttribute("tipo_busca_sessao", tipo_busca);
        }

        String tipo_busca_sessao = (String) session.getAttribute("tipo_busca_sessao");
        String termo_busca_sessao = (String) session.getAttribute("termo_busca_sessao");

        List<Aluno> alunos;
        if (termo_busca_sessao != null && !termo_busca_sessao.trim().isEmpty()) {
            alunos = switch (tipo_busca_sessao) {
                            case "cpf" -> alunoService.pesquisarPorCPF(termo_busca_sessao);
                            case "email" -> alunoService.pesquisarPorEmail(termo_busca_sessao);
                            case "telefone" -> alunoService.pesquisarPorTelefone(termo_busca_sessao);
                            default -> alunoService.pesquisarPorNome(termo_busca_sessao);
            };
        } else {
            alunos = alunoService.listarTodos();
        }
        model.addAttribute("alunos", alunos);
        model.addAttribute("termo_busca", termo_busca_sessao);
        model.addAttribute("tipo_busca", tipo_busca_sessao);
        return "alunos/lista";
    }

    @GetMapping("/novo")
    public String mostrarFormularioNovo(Model model){
        model.addAttribute("aluno", new Aluno());
        model.addAttribute("faixas", Faixa.values());
        return "alunos/form";
    }

    @GetMapping("/limpar-filtro")
    public String limparFiltro(HttpSession session){
        session.removeAttribute("tipo_busca_sessao");
        session.removeAttribute("termo_busca_sessao");
        return "redirect:/alunos";
    }

    @PostMapping
    public String cadastrarAluno(@Valid @ModelAttribute Aluno aluno,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes ra){
        alunoService.salvar(aluno, result);

        if (result.hasErrors()) {
            model.addAttribute("faixas", Faixa.values());
            return "alunos/form";
        }

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
