package com.ponto.controller;

import com.ponto.enums.TipoRegistro;
import com.ponto.model.RegistroPonto;
import com.ponto.repository.RegistroPontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class RegistroPontoController {

    @Autowired
    private RegistroPontoRepository registroPontoRepository;

    @GetMapping("/registrar-ponto")
    public String exibirPaginaRegistroPonto(Model model) {
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
}