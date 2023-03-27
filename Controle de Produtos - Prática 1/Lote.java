public class Lote {

   private Long id;

   private Produto produto;

   private int numeroDeItens;

   public Lote(Long id, Produto produto, int numeroDeItens) {

      this.id = id;

      this.produto = produto;

      this.numeroDeItens = numeroDeItens;

   }

   public Long getId() {
      return id;
   }

   public int getNumeroDeItens() {
      return numeroDeItens;
   }

   public Produto getProduto() {
      return produto;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setNumeroDeItens(int numeroDeItens) {
      this.numeroDeItens = numeroDeItens;
   }

   public void setProduto(Produto produto) {
      this.produto = produto;
   }

}
