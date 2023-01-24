# Como Rodar

Primeiro, entre na pasta raiz do projeto. Depois, verifique se a sua variável de ambiente JAVA_HOME está apontando para um JDK 17, que é a versão que o projeto usa e o Maven (gerenciador de dependências e pacotes que roda o projeto) utiliza essa variável pra saber qual a versão do java.

No Linux, utilize o comando `export JAVA_HOME=<caminho_da_sua_jdk17>`.
No Windows, pode procurar alterar sua variável de ambiente pelas configurações do sistema, há vários tutoriais na internet.
Se você usa Mac, o mesmo.

Após ter a sua variável JAVA_HOME configurada, você precisa buildar o projeto. Pode utilizar o seguinte comando, na raiz do projeto: `./mvnw clean package -DskipTests`.

Se tiver buildado corretamente, agora você precisa buildar a imagem docker do projeto. Primeiro, instale o docker e o docker-compose no seu computador. Após isso, utilize o comando: `docker build -t bantads_gerente .`

Após buildar a imagem, é necessário executá-la, criando o container. Nesse sentido, rode o comando de build e depois o comando para erguer os containeres: `docker-compose build && docker-compose up -d`.

Agora os containeres estão rodando e você pode utilizar o sistema apontando para `http://localhost:8081/gerente`. Divirta-se!
