package com.alexandre.gerenciamentoKravMaga.controller;

import com.alexandre.gerenciamentoKravMaga.model.Aluno;
import com.alexandre.gerenciamentoKravMaga.model.Faixa;
import com.alexandre.gerenciamentoKravMaga.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/alunos")
public class AlunoController {
    @Autowired
    private AlunoService alunoService;

    @GetMapping
    public String listarAlunos(Model model){
        model.addAttribute("alunos", alunoService.listarTodos());
        return "alunos/lista";
    }

    @GetMapping("/novo")
    public String mostrarFormularioNovo(Model model){
        model.addAttribute("alunos", new Aluno());
        model.addAttribute("faixas", Faixa.values());
        return "alunos/form";
    }

    @PostMapping()
    public String cadastrarAluno(@Valid @ModelAttribute Aluno aluno,
                                  BindingResult erros,
                                  Model model,
                                  RedirectAttributes ra){
        if (erros.hasErrors()) {
            model.addAttribute("faixas", Faixa.values());
            return "alunos/form";
        }
        alunoService.salvar(aluno);
        ra.addFlashAttribute("msgSucesso", "Aluno cadastrado com sucesso!");
        return "redirect:/alunos";
    }

    @GetMapping("editar/{id}")
    public String abrirEdicao(@PathVariable Long id, Model model, RedirectAttributes ra){
        if(!(alunoService.existsById(id))){
            ra.addFlashAttribute("msgErro", "Aluno não encontrado!");
            return "redirect:/alunos";
        }
        model.addAttribute("aluno", alunoService.pesquisarPorId(id).get());
        model.addAttribute("faixas", Faixa.values());
        return "alunos/form";
    }

    @GetMapping("deletar/{id}")
    public String deletarAluno(@PathVariable Long id, RedirectAttributes ra){
        if (!alunoService.existsById(id)){
            ra.addFlashAttribute("msgErro", "Aluno não encontrar!");
        } else {
            alunoService.deletarPorId(id);
            ra.addFlashAttribute("msgSucesso", "Aluno excluído com sucesso!");
        }
        return "redirect:/alunos";
    }


}
