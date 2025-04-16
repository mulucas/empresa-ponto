package com.ponto.controller.ponto;

import com.ponto.enums.TipoRegistro;
import com.ponto.model.AjustePonto;
import com.ponto.model.Colaborador;
import com.ponto.model.RegistroPonto;
import com.ponto.repository.AjustePontoRepository;
import com.ponto.repository.ColaboradorRepository;
import com.ponto.repository.RegistroPontoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
public class RegistroPontoController {

    @Autowired
    private RegistroPontoRepository registroPontoRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private AjustePontoRepository ajustePontoRepository;

    @GetMapping("/registrar-ponto")
    public String exibirPaginaRegistroPonto(Model model, HttpSession session) {
        String cpf = (String) session.getAttribute("cpf");
        if (cpf == null) {
            return "redirect:/";
        }
        model.addAttribute("cpf", cpf);
        return "ponto/registrar-ponto";
    }

    @PostMapping("/registrar-ponto")
    public String registrarPonto(@RequestParam("cpfColaborador") String cpfColaborador,
                                 @RequestParam("tipoRegistro") TipoRegistro tipoRegistro,
                                 @RequestParam("dataHora") String dataHoraString,
                                 Model model) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime dataHora = LocalDateTime.parse(dataHoraString, formatter);

            RegistroPonto registroPonto = new RegistroPonto();
            registroPonto.setCpfColaborador(cpfColaborador);
            registroPonto.setTipoRegistro(tipoRegistro);
            registroPonto.setDataHora(dataHora);
            registroPontoRepository.save(registroPonto);
            model.addAttribute("mensagem", "Ponto registrado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("mensagem", "Erro ao registrar o ponto: " + e.getMessage());
        }
        return "registrar-ponto";
    }

    @GetMapping("/ponto/ajustar-ponto/{id}")
    public String exibirFormularioAjuste(@PathVariable Long id, Model model) {
        RegistroPonto registroPonto = registroPontoRepository.findById(id).orElse(null);
        if (registroPonto == null) {
            return "redirect:/ponto/historico-pontos";
        }
        model.addAttribute("registroPonto", registroPonto);
        return "ponto/ajustar-ponto";
    }

    @PostMapping("/ponto/ajustar-ponto/{id}")
    public String ajustarPonto(@PathVariable Long id, @RequestParam("dataHora") String dataHoraString,
                               @RequestParam("tipoRegistro") TipoRegistro tipoRegistro, HttpSession session) {

        RegistroPonto registroPonto = registroPontoRepository.findById(id).orElse(null);
        if (registroPonto == null) {
            return "redirect:/ponto/historico-pontos";
        }

        LocalDateTime dataHoraNova = LocalDateTime.parse(dataHoraString, DateTimeFormatter.ISO_DATE_TIME);

        String cpfAdmin = (String) session.getAttribute("cpf");
        Colaborador colaboradorAdmin = colaboradorRepository.findByCpf(cpfAdmin).orElse(null);

        AjustePonto ajustePonto = new AjustePonto();
        ajustePonto.setRegistroPontoId(id);
        ajustePonto.setDataHoraAnterior(registroPonto.getDataHora());
        ajustePonto.setDataHoraNova(dataHoraNova);
        ajustePonto.setTipoRegistroAnterior(registroPonto.getTipoRegistro().name());
        ajustePonto.setTipoRegistroNovo(tipoRegistro.name());
        ajustePonto.setColaboradorAdmin(colaboradorAdmin);
        ajustePonto.setDataHoraAjuste(LocalDateTime.now());
        ajustePontoRepository.save(ajustePonto);

        registroPonto.setDataHora(dataHoraNova);
        registroPonto.setTipoRegistro(tipoRegistro);
        registroPontoRepository.save(registroPonto);

        Colaborador colaborador = colaboradorRepository.findByCpf(registroPonto.getCpfColaborador()).orElse(null);
        if (colaborador != null) {
            return "redirect:/ponto/ajustar-pontos/colaborador/" + colaborador.getId();
        } else {
            return "redirect:/ponto/historico-pontos";
        }
    }

    @GetMapping("/ponto/ajustes/colaborador/{idColaborador}")
    public String listarAjustesDePonto(@PathVariable Long idColaborador, Model model) {
        Colaborador colaborador = colaboradorRepository.findById(idColaborador).orElse(null);
        if (colaborador == null) {
            return "redirect:/ponto/historico-pontos";
        }
        List<AjustePonto> ajustes = ajustePontoRepository.findByRegistroPontoIdIn(
                registroPontoRepository.findByCpfColaborador(colaborador.getCpf()).stream()
                        .map(RegistroPonto::getId)
                        .toList()
        );
        model.addAttribute("ajustes", ajustes);
        model.addAttribute("colaborador", colaborador);
        return "ponto/ajustes-ponto";
    }

    // Método para exibir a página de listagem de registros de ponto (usado para redirecionamento)
    @GetMapping("/ponto/ajustar-pontos")
    public String listarRegistrosPontoRedirecionamento(HttpSession session) {
        Long colaboradorIdAjustado = (Long) session.getAttribute("colaboradorIdAjustado");
        if (colaboradorIdAjustado != null) {
            return "redirect:/ponto/ajustar-pontos/colaborador/" + colaboradorIdAjustado;
        }
        return "redirect:/ponto/historico-pontos";
    }

    @GetMapping("/ponto/ajustar-pontos/colaborador/{idColaborador}")
    public String exibirFormularioAjustePontos(@PathVariable Long idColaborador, Model model, HttpSession session) {
        Colaborador colaborador = colaboradorRepository.findById(idColaborador).orElse(null);
        if (colaborador == null) {
            return "redirect:/ponto/historico-pontos";
        }
        List<RegistroPonto> registros = registroPontoRepository.findByCpfColaborador(colaborador.getCpf());
        model.addAttribute("registros", registros);
        model.addAttribute("cpf", colaborador.getCpf());
        model.addAttribute("idColaborador", idColaborador);

        session.setAttribute("colaboradorIdAjustado", idColaborador);
        return "ponto/ajustar-pontos";
    }

    @GetMapping("/ponto/adicionar-ponto")
    public String exibirFormularioAdicionarPonto() {
        return "ponto/adicionar-ponto";
    }

    @PostMapping("/ponto/adicionar-ponto")
    public String adicionarPonto(@RequestParam("cpfColaborador") String cpfColaborador,
                                 @RequestParam("dataHora") String dataHoraString,
                                 @RequestParam("tipoRegistro") TipoRegistro tipoRegistro) {
        LocalDateTime dataHora = LocalDateTime.parse(dataHoraString, DateTimeFormatter.ISO_DATE_TIME);

        RegistroPonto registroPonto = new RegistroPonto();
        registroPonto.setCpfColaborador(cpfColaborador);
        registroPonto.setDataHora(dataHora);
        registroPonto.setTipoRegistro(tipoRegistro);
        registroPontoRepository.save(registroPonto);

        Colaborador colaborador = colaboradorRepository.findByCpf(cpfColaborador).orElse(null);
        if (colaborador != null) {
            return "redirect:/ponto/ajustar-pontos/colaborador/" + colaborador.getId();
        } else {
            return "redirect:/ponto/historico-pontos";
        }
    }

    @GetMapping("/ponto/remover-ponto/{id}")
    public String removerPonto(@PathVariable Long id) {
        RegistroPonto registroPonto = registroPontoRepository.findById(id).orElse(null);
        if (registroPonto != null) {
            Colaborador colaborador = colaboradorRepository.findByCpf(registroPonto.getCpfColaborador()).orElse(null);
            registroPontoRepository.deleteById(id);
            if (colaborador != null) {
                return "redirect:/ponto/ajustar-pontos/colaborador/" + colaborador.getId();
            } else {
                return "redirect:/ponto/historico-pontos";
            }
        }
        return "redirect:/ponto/historico-pontos";
    }

    @GetMapping("/ponto/historico-pontos")
    public String exibirHistoricoPontos(Model model) {
        List<RegistroPonto> registros = registroPontoRepository.findAll();
        model.addAttribute("registros", registros);
        return "ponto/historico-pontos";
    }
}