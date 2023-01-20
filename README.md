# Como Rodar

Primeiro, entre na pasta raiz do projeto. Depois, verifique se a sua variável de ambiente JAVA_HOME está apontando para um JDK 17, que é a versão que o projeto usa e o Maven (gerenciador de dependências e pacotes que roda o projeto) utiliza essa variável pra saber qual a versão do java.

No Linux, utilize o comando `export JAVA_HOME=<caminho_da_sua_jdk17>`.
No Windows, pode procurar alterar sua variável de ambiente pelas configurações do sistema, há vários tutoriais na internet.
Se você usa Mac, o mesmo.

Após ter a sua variável JAVA_HOME configurada, você precisa buildar o projeto. Pode utilizar o seguinte comando, na raiz do projeto: `./mvnw clean package -DskipTests`.

Se tiver buildado corretamente, agora você precisa buildar a imagem docker do projeto. Primeiro, instale o docker e o docker-compose no seu computador. Após isso, utilize o comando: `docker build -t bantads_gerente .`

Após buildar a imagem, é necessário executá-la, criando o container. Nesse sentido, rode o comando de build e depois o comando para erguer os containeres: `docker-compose build && docker-compose up -d`.

Agora os containeres estão rodando e você pode utilizar o sistema apontando para `http://localhost:8081/gerente`. Divirta-se!

## Observação

Na versão atual do sistema, você precisa alterar o endereço ip do container do MySQL no arquivo `Gerente/src/main/resources/application.properties`.

Isso acontece porque ainda não está implementado um docker network que conecta-se de maneria automática no ip dos containeres.

O que você precisa fazer, basicamente, é:
- Rodar os containeres, para poder ver com qual endereço ip o container do MySQL ficou.
- Alterar a seguinte linha:
`spring.datasource.url=jdbc:mysql://172.24.0.2:3306/bantads-gerenteallowPublicKeyRetrieval=true&useSSL=false&allowMultiQueries=true&serverTimezone=UTC`
- Perceba que o endereço ip `172.24.0.2` está sendo usado. Altere esse endereço para o endereço ip do seu container MySQL.

Agora, como saber o endereço ip do seu container? Recomendo instalar e utilizar o Portainer, um dashboard online onde você administra os containeres e imagens numa interface fácil de usar. Lá, você verá as informações do seu container.

Boa sorte!
