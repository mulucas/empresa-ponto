package com.ponto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AjustePonto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long registroPontoId;

    @Column(nullable = false)
    private LocalDateTime dataHoraAnterior;

    @Column(nullable = false)
    private LocalDateTime dataHoraNova;

    @Column(nullable = false, length = 20)
    private String tipoRegistroAnterior;

    @Column(nullable = false, length = 20)
    private String tipoRegistroNovo;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Colaborador colaboradorAdmin;

    @Column(nullable = false)
    private LocalDateTime dataHoraAjuste;

    @Column(length = 255)
    private String observacaoAnterior;

    @Column(length = 255)
    private String observacaoNova;

}