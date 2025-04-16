package com.ponto.repository;

import com.ponto.model.AjustePonto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AjustePontoRepository extends JpaRepository<AjustePonto, Long> {
    List<AjustePonto> findByRegistroPontoIdIn(List<Long> registroPontoIds);
}