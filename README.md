# Awards-API
API RESTful para possibilitar a leitura da lista de indicados e vencedores
da categoria Pior Filme do Golden Raspberry Awards.

## Ferramentas
- JDK 21
- Intellij

## Instalação e execução da API
- repositório: https://github.com/AlexandreMMizuno/awardsApp
- no site GitHub: baixar o projeto em "Code" > Download ZIP
- descompactar em algum diretório de sua preferencia
- no Intellij: ir em File > open > "localizar o projeto baixado" > botão select folder
- navegar até o arquivo: awardsApp\src\main\java\com\api\Application.java
- clicar com o botão direito do mouse e ir na opção "run"

## EndPoints
- url para executar a API: GET http://localhost:8080/api/intervalo
- url para testar outro arquivo CSV: POST http://localhost:8080/api/arquivo (multipart/form-data)

## Explicação

- A API ao ser iniciada já carrega os dados do arquivo "movielist.csv" na base de dados h2, bastando somente chamar a url(GET) http://localhost:8080/api/intervalo para executar e verificar o retorno esperado.
- Para carregar novos arquivos .csv na base h2, foi criado um endpoint para possibilitar novos testes. Utilizar a url(POST) http://localhost:8080/api/arquivo, na requisição configurar como: Content-Type:"multipart/form-data", key:"arquivo", value:"diretorio do arquivo". Utilizado a ferramenta postman para facilitar a execução.
- Logo após carregar o novo arquivo .csv, executar novamente o endpoint http://localhost:8080/api/intervalo para verificar o retorno esperado.

## Teste integrado
- Para executar os testes integrados navegar até o arquivo: awardsApp\src\test\java\com\apiApplicationTests.java
- clicar com o botão direito do mouse e selecionar a opção "run"