package com.ponto.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

//@Entity
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
    private Optional<Colaborador> colaboradorAdmin;

    @Column(nullable = false)
    private LocalDateTime dataHoraAjuste;

    @Column(length = 255)
    private String observacaoAnterior;

    @Column(length = 255)
    private String observacaoNova;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRegistroPontoId() {
		return registroPontoId;
	}

	public void setRegistroPontoId(Long registroPontoId) {
		this.registroPontoId = registroPontoId;
	}

	public LocalDateTime getDataHoraAnterior() {
		return dataHoraAnterior;
	}

	public void setDataHoraAnterior(LocalDateTime dataHoraAnterior) {
		this.dataHoraAnterior = dataHoraAnterior;
	}

	public LocalDateTime getDataHoraNova() {
		return dataHoraNova;
	}

	public void setDataHoraNova(LocalDateTime dataHoraNova) {
		this.dataHoraNova = dataHoraNova;
	}

	public String getTipoRegistroAnterior() {
		return tipoRegistroAnterior;
	}

	public void setTipoRegistroAnterior(String tipoRegistroAnterior) {
		this.tipoRegistroAnterior = tipoRegistroAnterior;
	}

	public String getTipoRegistroNovo() {
		return tipoRegistroNovo;
	}

	public void setTipoRegistroNovo(String tipoRegistroNovo) {
		this.tipoRegistroNovo = tipoRegistroNovo;
	}

	public Optional<Colaborador> getColaboradorAdmin() {
		return colaboradorAdmin;
	}

	public void setColaboradorAdmin(Optional<Colaborador> colaboradorAdmin2) {
		this.colaboradorAdmin = colaboradorAdmin2;
	}

	public LocalDateTime getDataHoraAjuste() {
		return dataHoraAjuste;
	}

	public void setDataHoraAjuste(LocalDateTime dataHoraAjuste) {
		this.dataHoraAjuste = dataHoraAjuste;
	}

	public String getObservacaoAnterior() {
		return observacaoAnterior;
	}

	public void setObservacaoAnterior(String observacaoAnterior) {
		this.observacaoAnterior = observacaoAnterior;
	}

	public String getObservacaoNova() {
		return observacaoNova;
	}

	public void setObservacaoNova(String observacaoNova) {
		this.observacaoNova = observacaoNova;
	}
    
}