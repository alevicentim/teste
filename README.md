# teste
Projeto para Ler, Armazenas e Calcular Dados

### Construção

Para construir o projeto com o Maven, executar o comando abaixo, no diretório onde encontra-se o código fonte:

```shell
mvn clean package
```

O comando irá baixar todas as dependências do projeto e criar um diretório *target* com os artefatos construídos, que incluem o arquivo jar do projeto. Além disso, serão executados os testes unitários, e se algum falhar, o Maven exibirá essa informação no console.

### Execução
Para rodar a aplicação, seguir os passos abaixo:
* Copiar para um diretório chamado massa, presente no mesmo diretório em que encontra-se o arquivo .jar, os 4 arquivos que devem ser carregados.
* Executar o comando abaixo:

```shell
java -jar teste-1.0.1.jar
```


### Informações Adicionais
* Acesso ao banco de dados utilizado pela aplicação: <a>http://localhost:8080/teste/h2-console/</a> (User=<i>sa</i>, Password=<i></i>)

* Acesso ao swagger com o endpoint disponibilizado para o cálculo: <a>http://localhost:8080/teste/swagger-ui.html</a>