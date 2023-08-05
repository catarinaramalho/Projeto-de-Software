package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.dto.ProdutoNomePatchRequestDTO;
import com.ufcg.psoft.mercadofacil.dto.ProdutoPostPutRequestDTO;
import com.ufcg.psoft.mercadofacil.exception.ProdutoNaoExisteException;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoAlterarNomePadraoService implements ProdutoAlterarNomeService {
    @Autowired
    ProdutoRepository produtoRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public Produto alterarParcialmente(Long id, ProdutoNomePatchRequestDTO produtoNomePatchRequestDTO) {
        Produto produto = produtoRepository.findById(id).orElseThrow(ProdutoNaoExisteException::new);
        modelMapper.map(produtoNomePatchRequestDTO, produto);
        return produtoRepository.save(produto);
    }
}
