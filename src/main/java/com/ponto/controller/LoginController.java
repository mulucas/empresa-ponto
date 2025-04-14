package com.ponto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ponto.model.Colaborador;
import com.ponto.repository.ColaboradorRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@GetMapping("/")
	public String exibirFormularioCpf() {
		return "operacoes/login";
	}

	@PostMapping("/processar-cpf")
	public String processarCpf(@RequestParam("cpf") String cpf, Model model, HttpSession session) {
		return colaboradorRepository.findByCpf(cpf).map(colaborador -> {
			session.setAttribute("cpf", cpf);
			model.addAttribute("cpf", cpf);
			model.addAttribute("acessoAdmin", colaborador.getAcessoAdmin());
			return "operacoes/tela-inicial";
		}).orElseGet(() -> {
			model.addAttribute("erro", "Colaborador não encontrado.");
			return "operacoes/login";
		});
	}

}