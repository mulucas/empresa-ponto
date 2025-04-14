package com.ponto.controller.ponto;

import com.ponto.enums.TipoRegistro;
import com.ponto.model.AjustePonto;
import com.ponto.model.Colaborador;
import com.ponto.model.RegistroPonto;
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

@Controller
public class RegistroPontoController {

    @Autowired
    private RegistroPontoRepository registroPontoRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @GetMapping("/registrar-ponto")
    public String exibirPaginaRegistroPonto(Model model, HttpSession session) {
        String cpf = (String) session.getAttribute("cpf");
        if (cpf == null) {
            return "redirect:/login";
        }
        model.addAttribute("cpf", cpf);
        return "registrar-ponto";
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

        LocalDateTime dataHora = LocalDateTime.parse(dataHoraString, DateTimeFormatter.ISO_DATE_TIME);

        String cpfAdmin = (String) session.getAttribute("cpf");
        //Colaborador colaboradorAdmin = colaboradorRepository.findByCpf(cpfAdmin);

        AjustePonto ajustePonto = new AjustePonto();
        ajustePonto.setRegistroPontoId(id);
        ajustePonto.setDataHoraAnterior(registroPonto.getDataHora());
        ajustePonto.setDataHoraNova(dataHora);
        ajustePonto.setTipoRegistroAnterior(registroPonto.getTipoRegistro().name());
        ajustePonto.setTipoRegistroNovo(tipoRegistro.name());
        //ajustePonto.setColaboradorAdmin(colaboradorAdmin);
        ajustePonto.setDataHoraAjuste(LocalDateTime.now());
        //ajustePontoRepository.save(ajustePonto);

        registroPonto.setDataHora(dataHora);
        registroPonto.setTipoRegistro(tipoRegistro);
        registroPontoRepository.save(registroPonto);

        // Armazenar o CPF na sessão
        session.setAttribute("cpfColaboradorAjustado", registroPonto.getCpfColaborador());

        // Redirecionar para a URL genérica
        return "redirect:/ponto/ajustar-pontos";
    }

    // Método para exibir a página de listagem de registros de ponto
    @GetMapping("/ponto/ajustar-pontos")
    public String listarRegistrosPonto(HttpSession session, Model model) {

        // Recuperar o CPF da sessão
        String cpfColaborador = (String) session.getAttribute("cpfColaboradorAjustado");

        // Remover o CPF da sessão para evitar reutilização indevida
        session.removeAttribute("cpfColaboradorAjustado");

        if (cpfColaborador == null) {
            // Lógica para lidar com o caso em que o CPF não está na sessão
            // Por exemplo, redirecionar para uma página de erro ou exibir uma mensagem
            return "redirect:/ponto/historico-pontos";
        }

        // Lógica para buscar os registros de ponto do colaborador com o CPF recuperado
        List<RegistroPonto> registrosPonto = registroPontoRepository.findByCpfColaborador(cpfColaborador);

        model.addAttribute("registrosPonto", registrosPonto);
        model.addAttribute("cpfColaborador", cpfColaborador);

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

        return "redirect:/ponto/historico-pontos";
    }

    @GetMapping("/ponto/remover-ponto/{id}")
    public String removerPonto(@PathVariable Long id) {
        registroPontoRepository.deleteById(id);
        return "redirect:/ponto/historico-pontos";
    }

    @GetMapping("/ponto/historico-pontos")
    public String exibirHistoricoPontos(Model model) {
        List<RegistroPonto> registros = registroPontoRepository.findAll();
        model.addAttribute("registros", registros);
        return "ponto/historico-pontos";
    }

    @GetMapping("/ponto/ajustar-pontos/{cpf}")
    public String exibirFormularioAjustePontos(@PathVariable String cpf, Model model) {
        List<RegistroPonto> registros = registroPontoRepository.findByCpfColaborador(cpf);
        model.addAttribute("registros", registros);
        model.addAttribute("cpf", cpf);
        return "ponto/ajustar-pontos";
    }
}