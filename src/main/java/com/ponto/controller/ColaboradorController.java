package com.ponto.controller;

import com.ponto.model.Colaborador;
import com.ponto.repository.ColaboradorRepository; // Assumindo que você tem um repositório

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/colaboradores")
public class ColaboradorController {

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@GetMapping("/cadastro")
	public String exibirFormularioCadastro(Model model, HttpSession session) {
		Optional.ofNullable((String) session.getAttribute("cpf")).flatMap(colaboradorRepository::findByCpf)
				.map(Colaborador::getAcessoAdmin)
				.ifPresent(acessoAdmin -> model.addAttribute("acessoAdmin", acessoAdmin));

		model.addAttribute("colaborador", new Colaborador());
		return "colaborador/tela-cadastro-colaborador";
	}

	@PostMapping("/salvar")
	public String salvarColaborador(@Valid Colaborador colaborador, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("colaborador", colaborador);
			return "colaborador/cadastro-colaborador"; // Retorna ao formulário com erros
		}

		// Lógica para salvar o colaborador no banco de dados
		colaboradorRepository.save(colaborador);
		model.addAttribute("mensagem", "Colaborador cadastrado com sucesso!");
		return "colaborador/cadastro-colaborador-sucesso"; // Redireciona para uma página de sucesso
	}

	@GetMapping("/editar/{id}")
	public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
		Colaborador colaborador = colaboradorRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("ID de colaborador inválido: " + id));
		model.addAttribute("colaborador", colaborador);
		return "colaborador/editar-colaborador";
	}

	@PostMapping("/atualizar/{id}")
	public String atualizarColaborador(@PathVariable Long id, @Valid Colaborador colaborador, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			colaborador.setId(id);
			return "colaborador/editar-colaborador";
		}
		colaboradorRepository.save(colaborador);
		model.addAttribute("mensagem", "Colaborador atualizado com sucesso!");
		return "colaborador/cadastro-colaborador-sucesso"; // Ou uma página de sucesso de edição específica
	}

	@GetMapping("/listar")
	public String listarColaboradores(Model model, HttpSession session) {

		List<Colaborador> colaboradores = colaboradorRepository.findAll();
		model.addAttribute("colaboradores", colaboradores);
		Optional.ofNullable((String) session.getAttribute("cpf")).flatMap(colaboradorRepository::findByCpf)
				.map(Colaborador::getAcessoAdmin)
				.ifPresent(acessoAdmin -> model.addAttribute("acessoAdmin", acessoAdmin));
		return "colaborador/listar-colaboradores"; // Ou "listar-colaboradores" se você mover o arquivo
	}

	@GetMapping("/excluir/{id}")
	public String excluirColaborador(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			colaboradorRepository.deleteById(id);
			redirectAttributes.addFlashAttribute("mensagem", "Colaborador excluído com sucesso!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", "Erro ao excluir colaborador.");
		}
		return "redirect:/colaboradores/listar";
	}
}