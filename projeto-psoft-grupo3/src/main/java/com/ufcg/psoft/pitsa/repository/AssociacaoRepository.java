package com.ufcg.psoft.pitsa.repository;

import com.ufcg.psoft.pitsa.model.Associacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AssociacaoRepository extends JpaRepository<Associacao, Long> {

    @Query("SELECT a FROM Associacao a WHERE a.entregadorId = :entregadorId AND a.estabelecimentoId = :estabelecimentoId")
    Optional<Associacao> findByEntregadorIdAndEstabelecimentoId(@Param("entregadorId") Long entregadorId, @Param("estabelecimentoId") Long estabelecimentoId);

}
