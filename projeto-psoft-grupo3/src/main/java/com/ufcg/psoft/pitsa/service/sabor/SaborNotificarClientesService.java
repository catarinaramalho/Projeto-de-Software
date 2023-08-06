package com.ufcg.psoft.pitsa.service.sabor;

import java.util.List;

@FunctionalInterface
public interface SaborNotificarClientesService {
    List<String> notificarClientes(Long saborId);
}
