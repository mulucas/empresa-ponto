package com.ponto.repository;

import com.ponto.model.RegistroPonto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Long> {
	List<RegistroPonto> findByCpfColaborador(String cpfColaborador);
}