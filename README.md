# App de Previsão do Tempo em Java

## Visão Geral do Projeto

Este projeto é um aplicativo simples de linha de comando em Java que permite ao usuário digitar o nome de uma cidade e consultar o clima atual e a previsão para os próximos dias utilizando a API Open-Meteo.

O aplicativo funciona em duas etapas:
1. Busca a latitude e longitude da cidade usando a API de geocodificação do Open-Meteo.
2. Usa essas coordenadas para consultar o clima atual e a previsão dos próximos dias.

O objetivo do projeto é praticar conceitos básicos de:
- entrada do usuário
- requisições HTTP em Java
- consumo de API pública
- tratamento de erros
- organização de saída no terminal

---

## Funcionalidades

- Recebe o nome de uma cidade digitado pelo usuário
- Consulta coordenadas geográficas da cidade
- Busca a temperatura atual
- Exibe previsão para os próximos 4 dias
- Exibe dados de forma organizada no terminal
- Trata erros (cidade não encontrada ou falha na API)

---

## Instalação

### Pré-requisitos

Antes de executar o projeto, você precisa ter:

- Java 11 ou superior instalado
- Um terminal ou editor como VS Code
- Conexão com a internet para acessar a API Open-Meteo

### Configuração

Salve o arquivo `Main.java` dentro do seu projeto (ex: `src/main/java/com/weatherapp`).

---

## Como Executar

No terminal, navegue até a pasta do projeto e compile o programa:

```bash
javac -d bin src/main/java/com/weatherapp/Main.java
