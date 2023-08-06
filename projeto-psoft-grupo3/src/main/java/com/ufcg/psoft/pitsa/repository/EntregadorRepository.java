package com.ufcg.psoft.pitsa.repository;

import com.ufcg.psoft.pitsa.model.Entregador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntregadorRepository extends JpaRepository<Entregador, Long> {
}
