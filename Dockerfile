FROM ubuntu:latest AS build

RUN apt-get update
# o -y no final é para ele dar sim em todas as perguntas
RUN apt-get install openjdk-17-jdk -y

#aqui ele copia todo o conteudo  do diretorio da construção local para o diretório da imagem
# que está sendo construida
COPY . .

#instala o maven
RUN apt-get install maven -y
#gera os .jar
RUN mvn clean install

EXPOSE 8080

# Esse /target/todolist-1.0.0.jar deve ser gerado ao rodar o comando mvn clean install
# Esse nome vem lá do pom.xml <artifactId>todolist</artifactId>
# <version>1.0.0</version>
COPY --from=build /target/todolist-1.0.0.jar app.jar

#quando estamos construindo uma aplicação .jar, para executar essa aplicação passamos
# java -jar target/todolist-1.0.0.jar
# e então ele inicia a aplicação
ENTRYPOINT [ "java", "-jar", "app.jar" ]