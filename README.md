# teste
Projeto para Ler, Armazenas e Calcular Dados

### Construção

Para construir o projeto com o Maven, seguir os passos abaixo:
* Navegar para o diretório onde encotra-se projeto (mesmo diretótio onde o arquivo pom.xml encontra-se)
* Executar o comando abaixo:

```shell
mvn clean package
```

* O Arquivo teste-1.0.2.jar poderá ser encontrado na parta target, que será criada após a execução do comando

O comando irá baixar todas as dependências do projeto e criar um diretório *target* com os artefatos construídos, que incluem o arquivo jar do projeto. Além disso, serão executados os testes unitários, e se algum falhar, o Maven exibirá essa informação no console.

### Execução
Para rodar a aplicação, seguir os passos abaixo:
* Colocar o arquivo teste-1.0.1.jar, criado na contrução, na pasta onde desejar. Se quiser manter na pasta target, pode.
* Criar a diretório massa, no mesmo diretório onde encontra-se o arquivo .jar.
* Copiar para um diretório massa, criado no passo acima, os 4 arquivos que devem ser carregados.
* Executar o comando abaixo:

```shell
java -jar teste-1.0.2.jar
```


### Informações Adicionais
* Acesso ao swagger com o endpoint disponibilizado para o cálculo: <a>http://localhost:8080/teste/swagger-ui.html</a>