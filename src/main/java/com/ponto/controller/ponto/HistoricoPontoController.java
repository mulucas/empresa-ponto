package com.ponto.controller.ponto;

import com.ponto.model.RegistroPonto;
import com.ponto.repository.RegistroPontoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HistoricoPontoController {

    @Autowired
    private RegistroPontoRepository registroPontoRepository;

    @GetMapping("/historico-ponto")
    public String exibirHistoricoPonto(Model model, HttpSession session) {
        String cpf = (String) session.getAttribute("cpf");
        if (cpf == null) {
            return "redirect:/"; // Redireciona para o login se o CPF não estiver na sessão
        }

        List<RegistroPonto> registros = registroPontoRepository.findByCpfColaborador(cpf);
        model.addAttribute("registros", registros);
        return "ponto/historico-ponto";
    }
}