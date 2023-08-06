package com.ufcg.psoft.pitsa.repository;

import com.ufcg.psoft.pitsa.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
