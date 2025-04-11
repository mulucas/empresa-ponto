package com.ponto.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import com.ponto.enums.TipoRegistro;

@Entity
public class RegistroPonto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 14)
    private String cpfColaborador;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoRegistro tipoRegistro;

    @Column(length = 255)
    private String observacao;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime dataCadastro;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpfColaborador() {
		return cpfColaborador;
	}

	public void setCpfColaborador(String cpfColaborador) {
		this.cpfColaborador = cpfColaborador;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	public TipoRegistro getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(TipoRegistro tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

}