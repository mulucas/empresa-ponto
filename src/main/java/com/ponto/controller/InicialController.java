package com.ponto.controller;

import com.ponto.repository.ColaboradorRepository; // Importe o repositório
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicialController {

	@Autowired
	private ColaboradorRepository colaboradorRepository; 

	@GetMapping("/bater-ponto")
	public String exibirTelaBaterPontoRealtime(Model model, HttpSession session) {
		String cpf = (String) session.getAttribute("cpf");
		if (cpf != null) {
			model.addAttribute("cpf", cpf);
		}
		return "tela-bater-ponto";
	}

	@GetMapping("/menu-ponto")
	public String exibirMenuPonto(Model model, HttpSession session) {
		String cpf = (String) session.getAttribute("cpf");

		if (cpf == null) {
			return "redirect:/";
		}

		return colaboradorRepository.findByCpf(cpf).map(colaborador -> {
			model.addAttribute("cpf", cpf);
			model.addAttribute("acessoAdmin", colaborador.getAcessoAdmin());
			return "operacoes/tela-inicial";
		}).orElse("redirect:/");
	}

	@GetMapping("/sair")
	public String sair() {
		return "operacoes/tela-sair";
	}
}