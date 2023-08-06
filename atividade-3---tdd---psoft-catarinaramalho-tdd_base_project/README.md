# Mercado Fácil

Um supermercado da cidade de Campina Grande precisa de um sistema que gerencie o estoque e venda de produtos na sua loja. Neste sistema, o administrador deve obter uma visão geral e o controle sobre o funcionamento do supermercado, por exemplo, ele deve poder adicionar novos produtos, acompanhar quantas unidades do produto estão disponíveis, alterar preços, ser notificado sobre eventos críticos, gerenciar as vendas e oferecer alguns serviços personalizados para o cliente.

## User Stories já implementadas

- Eu, como administrador, gostaria de adicionar um novo produto no sistema,
  informando seu nome, código de barra, fabricante e preço;
- Eu, como administrador, gostaria de consultar o nome, código de barra,
  preço e fabricante de cada produto do supermercado;
- Eu, como administrador, gostaria de modificar atributos de um determinado
  produto no sistema;
- Eu, como administrador, gostaria de remover um determinado produto do sistema;
- Eu, como administrador, gostaria de listar produtos cadastrados no sistema
- Eu, como administrador, gostaria de adicionar um novo cliente no sistema,
  informando seu nome, cpf, idade e endereço;
- Eu, como administrador, gostaria de consultar o nome, cpf,
  idade e endereço de cada cliente do supermercado;
- Eu, como administrador, gostaria de modificar atributos de um determinado
  cliente no sistema;
- Eu, como administrador, gostaria de remover um determinado cliente do sistema;
- Eu, como administrador, gostaria de listar clientes cadastrados no sistema
- Eu, como administrador, gostaria de criar lotes associados aos produtos,
- Eu, como administrador, gostaria de listar lotes cadastrados no sistema

## Estrutura básica

- Um projeto: MercadoFacil;
- Controllers que implementam os endpoints da API Rest (VersionController, ClienteControlle, ProdutoController e LoteController).
- Três repositórios são utilizados: ClienteRepository, ProdutoRepository e LoteRepository, que são responsáveis por manipular as entidades Cliente, Produto e Lote em um banco de dados em memória;
- O modelo é composto pelas classes Cliente.java, Produto.java e Lote.java que podem ser
  encontradas no pacote model;
- O pacote exceptions guarda as classes de exceções que podem ser levantadas
  dentro do sistema;
- Não há implementação de frontend, mas o projeto fornece uma interface de acesso à API via swagger.

## Objetivo Prática 2
 - Revisão de TDD.
