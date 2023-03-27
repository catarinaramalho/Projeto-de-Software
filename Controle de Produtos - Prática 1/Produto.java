
public class Produto {

   private Long id;

   private String nome;

   private double preco;

   private String codigoBarra;

   private String fabricante;

   public Produto(Long id, String nome, double preco, String codigoBarra, String fabricante) {

      this.id = id;

      this.nome = nome;

      this.preco = preco;

      this.codigoBarra = codigoBarra;

      this.fabricante = fabricante;

   }

   public String getCodigoBarra() {
      return codigoBarra;
   }

   public String getFabricante() {
      return fabricante;
   }

   public Long getId() {
      return id;
   }

   public String getNome() {
      return nome;
   }

   public double getPreco() {
      return preco;
   }

   public void setCodigoBarra(String codigoBarra) {
      this.codigoBarra = codigoBarra;
   }

   public void setFabricante(String fabricante) {
      this.fabricante = fabricante;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public void setPreco(double preco) {
      this.preco = preco;
   }

}
