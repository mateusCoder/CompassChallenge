# CompassChallenge

<div align="center">
  <a href="https://www.java.com/pt-BR/" target="_blank" rel="noreferrer" rel="noopener">
    <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java"/>
  </a>
  <a href="https://spring.io/" target="_blank" rel="noreferrer" rel="noopener">
    <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring"/>
  </a>
  <a href="https://git-scm.com/" target="_blank" rel="noreferrer" rel="noopener">
    <img src="https://img.shields.io/badge/Git-E34F26?style=for-the-badge&logo=git&logoColor=white" alt="Git"/>
  </a>
  <a href="https://www.microsoft.com/pt-br/windows/?r=1" target="_blank" rel="noreferrer" rel="noopener">
  <img src="https://img.shields.io/badge/Windows-017AD7?style=for-the-badge&logo=windows&logoColor=white" alt="Windows"/>
</div></br>



> O Compass Challenge é um desafio proposto para testar os conhecimentos do aluno, o projeto é formado por dois microserviços que conversam fazendo com que ocorra a criação e pagamento de pedidos.

## ⚙️ Funcionalidades

- [x] O Microserviço Order permite cadastrar:
    - [x] Produtos informando:
    - Nome
    - Descrição
    - preço
    - [x] Pedidos informando:
    - CPF
    - Nomes dos Produtos
    - Quantidade dos Produtos
- [x] O Microserviço Pagamento permite consultar:
    - [x] Pagamentos informando:
    - ID
    - CPF

## 📈 Ajustes e melhorias

O projeto possui suporte as seguintes tarefas:

- [x] Mensageria com RabbitMQ
- [x] Documentação com Swagger
- [x] Observabilidade com Grafana


## 💻 Pré-requisitos

Antes de começar, verifique se você atendeu aos seguintes requisitos:

* Ter instalado a  linguagem `Java JDK 17 LTS`.
* Ter instalado a IDE `IntelliJIDEA` ou `Eclipse Spring`.
* Ter instalado o subsistema `WSL 2`.
* Ter instalado a plataforma de virtualização `docker v20.10.17`.
* Ter instalado o orquestrador de containers`docker-compose v2.7.0`.
* Ter uma máquina `Windows 10` ou `11`.

## 🚀 Instalação

Passo-a-passo que informa o que você deve executar para ter um ambiente de desenvolvimento em execução.


><div align="center"> Clone do Projeto </div>
```
# Abra o terminal bash

# Clone este repositório
$ git clone https://github.com/mateusCoder/CompassChallenge.git

# Abra a pasta compass-challenge
```


><div align="center"> Executando o docker-compose </div>
```
# Com o wsl 2 configurado com docker e docker-compose
# Abra um shell do linux e execute para iniciar o docker daemon
$ sudo dockerd

# Abra outro shell do linux e execute para executar o docker-compose
$ docker-compose up
```


><div align="center"> Login RabbitMQ </div>
```
# Faça o login no rabbitmq, acesse http://localhost:15672 e insira:
$ login: rabbitmq
$ senha: rabbitmq

# Com o Rabbitmq rodando
# Abra a pasta ms-order e execute a aplicação em modo de desenvolvimento
# Abra a pasta ms-payment e execute a aplicação em modo de desenvolvimento
```


><div align="center"> Configurando o Banco de Dados </div>
```
# Faça o login no pgAdmin, acesse http://localhost:15432 e insira:
$ login: admin@admin.com
$ senha: root

# Crie um novo servidor

# Crie dois novos bancos de dados com os nomes: 
$ order
$ payment
```


><div align="center"> Executando a Aplicação </div>

```
# Abra a pasta ms-order e execute a aplicação em modo de desenvolvimento
# Abra a pasta ms-payment e execute a aplicação em modo de desenvolvimento

# A aplicação ms-order será aberta na porta:8080 - acesse http://localhost:8080/v1
# A aplicação ms-payment será aberta na porta:8090 - acesse http://localhost:8090/v1
```

## 📃 Swagger ##

Para acessar a documentação Compass Challenge: 
1. []()Verifique que os microserviços Order e Payment estão sendo executados
2. Para visualizar a documentação, acesse: [Ms-Order](http://localhost:8080/swagger-ui/index.html#/)
3. Para visualizar a documentação, acesse: [Ms-Payment](http://localhost:8090/swagger-ui/index.html#/)


## 📌 EndPoints

><div align="center"> Microserviço Order </div>

| Entidade | Método | EndPoint                  | Description                                                     |
|----------|--------|---------------------------|-----------------------------------------------------------------|
| Produto  | GET    | /v1/products              | Lista todos os produtos cadastrados                             |
| Produto  | POST   | /v1/products              | Cadastra um novo produto                                        |
| Produto  | PUT    | /v1/products/{product_id} | Atualiza o cadastro de um produto existente pelo ID             |
| Pedido   | POST   | /v1/orders                | Cadastra um novo pedido                                         |
| Pedido   | GET    | /v1/orders/{order_id}     | Detalha o cadastro de um pedido existente pelo ID               |
| Pedido   | GET    | /v1/orders/{order_number} | Detalha o cadastro de um pedido existente pelo número do Pedido |
| Pedido   | GET    | /v1/orders/{order_cpf}    | Lista todos os pedidos cadastrados pelo cpf                     |


><div align="center"> Microserviço Payment </div>
| Entidade  | Método | EndPoint                                       | Description                                   |
|-----------|--------|------------------------------------------------|-----------------------------------------------|
| Pagamento | GET    | /v1/payments/{payment_id}/client/{payment_cpf} | Detalha um pagamento cadastrado pelo ID e CPF |


><div align="center"> Observabilidade Grafana </div>
| URL                      | Description                          |
|--------------------------|--------------------------------------|
| http://localhost:3000/   | Interface de observabilidade Grafana |


><div align="center"> Banco de Dados pgAdmin </div>
| URL                             | Description                            |
|---------------------------------|----------------------------------------|
| http://localhost:15432/browser/ | Interface do banco de dados PostgreSQL |


><div align="center"> Documentação Swagger </div>
| URL                                           | Description             |
|-----------------------------------------------|-------------------------|
| http://localhost:8080/swagger-ui/index.html#/ | Documentação Ms-Order   |
| http://localhost:8090/swagger-ui/index.html#/ | Documentação Ms-Payment |


><div align="center"> Mensageria RabbitMQ </div>
| URL                       | Description                                                                |
|---------------------------|----------------------------------------------------------------------------|
| http://localhost:15672/#/ | Interface de controle de filas e exchanges com dashboards de processamento |


## 🕷️ Testes
Foram usadas as seguintes tecnologias e ferramentas para Testes da API:
* [JUnit 5](https://junit.org/junit5/docs/current/user-guide/) - Framework de Testes
* [Mockito](https://site.mockito.org/) - Estrutura de Testes

## 🚧🛠️ Tecnologias e Ferramentas

Foram usadas as seguintes tecnologias e ferramentas para a construção da API:
* [Git](https://git-scm.com/) - Ferramenta de Versionamento de Código
* [Java](https://www.java.com/pt-BR/) - Linguagem de Programação
* [SpringBoot](https://spring.io/) - FrameWork Java
* [H2](https://www.h2database.com/html/main.html) - Banco de Dados para Testes Unitários e de Integração
* [PostgreSQL](https://www.postgresql.org/) - Banco de Dados para Produção
* [Swagger](https://swagger.io/tools/swagger-editor/) - Documentação
* [RabbitMq](https://www.rabbitmq.com/) - Mensageria
* [Grafana](https://grafana.com/) - Observabilidade
* [Docker](https://www.docker.com/) - Plataforma de virtualização
* [Postman](https://www.postman.com/) - Plataforma da API
* [IntelliJIDEA](https://www.jetbrains.com/pt-br/idea/) - IDE (Ambiente de desenvolvimento integrado)
* [Windows](https://www.microsoft.com/pt-br/windows/?r=1) - Sistema Operacional


## 👨‍💻 Desenvolvedor

<table align="center">
    <td align="center">
      <a href="https://github.com/mateusCoder">
        <img src="https://avatars.githubusercontent.com/u/76978080?v=4" width="100px;" alt="Foto do Mateus Cardoso"/><br>
        <sub>
          <div align="center">
            <b>Mateus Cardoso</b></br></br>
            <a href="https://www.linkedin.com/in/mateus-cardoso-de-moraes/" target="_blank" rel="noreferrer" rel="noopener">
              <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" alt="Linkedin"/>
            </a></br>
            <a href="mailto:mateus.moraes0507@gmail.com" target="_blank" rel="noreferrer" rel="noopener">
              <img src="https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white" alt="Gmail"/></br>
            </a>
          </div>
        </sub>
      </a>
    </td>
</table>

## 🎬📽 Apresentação

A gravação da apresentação do desafio está disponível no <a href=" " target="_blank" rel="noreferrer" rel="noopener"> link </a>.



## ❤️ Apoio

Agradeço a empresa por todo apoio prestado neste projeto.

<sub>
  <div>
    <a href="https://compass.uol/pt/" target="_blank" rel="noreferrer" rel="noopener">
      <img src="https://github.com/mateusCoder/ReadMeTemplate/blob/main/compass.uol.png" alt="compass.uol" width="300px;"/><br>
    </a></br>
  </div>
</sub>
</br>

[⬆ Voltar ao topo](#CompassChallenge)<br>
