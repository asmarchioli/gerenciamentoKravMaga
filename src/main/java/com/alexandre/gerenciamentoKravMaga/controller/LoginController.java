package com.alexandre.gerenciamentoKravMaga.controller;

import com.alexandre.gerenciamentoKravMaga.model.Administrador;
import com.alexandre.gerenciamentoKravMaga.service.AdministradorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {
    private final AdministradorService administradorService;

    public LoginController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        if (session.getAttribute("usuarioLogado") != null) {
            return "redirect:/alunos";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("login") String login,
                        @RequestParam("senha") String senha,
                        HttpSession session, Model model){

        Optional<Administrador> adminAutenticado = administradorService.autenticar(login, senha);

        if(adminAutenticado.isPresent()){
            Administrador admin = adminAutenticado.get();

            session.setAttribute("usuarioLogado", admin.getLogin());

            return "redirect:/alunos";
        } else {
            model.addAttribute("erro", "Usuário ou senha inválidos");
            return "login";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }
}
