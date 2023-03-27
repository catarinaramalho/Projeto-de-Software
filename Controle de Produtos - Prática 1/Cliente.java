public class Cliente {

   private Long id;

   private Long cpf;

   private String nome;

   private Integer idade;

   private String endereco;

   public Cliente(Long id, Long cpf, String nome, Integer idade, String endereco) {

      this.id = id;

      this.cpf = cpf;

      this.nome = nome;

      this.idade = idade;

      this.endereco = endereco;

   }

   public String getNome() {
      return nome;
   }

   public Long getCpf() {
      return cpf;
   }

   public Integer getIdade() {
      return idade;
   }

   public String getEndereco() {
      return endereco;
   }

   public Long getId() {
      return id;
   }

   public void setCpf(Long cpf) {
      this.cpf = cpf;
   }

   public void setEndereco(String endereco) {
      this.endereco = endereco;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setIdade(Integer idade) {
      this.idade = idade;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

}
