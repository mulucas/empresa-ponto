package com.ponto.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Colaborador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 14)
	private String cpf;

	@Column(nullable = false, length = 255)
	private String nome;

	@Column(length = 255, unique = true)
	private String email;

	@Column
	private LocalDate dataNascimento;

	@Column(length = 20)
	private String telefone;

	@Column(length = 255)
	private String endereco;

	@Column(length = 255)
	private String complemento;

	@Column(length = 100)
	private String bairro;

	@Column(length = 100)
	private String cidade;

	@Column(length = 2)
	private String estado;

	@Column(length = 9)
	private String cep;

	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime dataCadastro;

	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime dataAtualizacao;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
	private Boolean ativo;

	@NotNull(message = "O acesso administrativo deve ser informado.")
	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean acessoAdmin;

	@Column(length = 100)
	private String departamento;

	@Column(length = 100)
	private String cargo;

	@Column(length = 50, unique = true)
	private String matricula;

	@Column
	private LocalDateTime ultimoLogin;

	@Column(columnDefinition = "TEXT")
	private String observacoes;

}