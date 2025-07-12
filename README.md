# PRJ_28_49746_50751

**Catalogo do Museu de Física do ISEL**  
Aplicação web para divulgação do património científico do Departamento de Física do ISEL, com catálogo de instrumentos históricos, glossário técnico e quizzes interativos.

## Índice

1. [Visão Geral](#visão-geral)  
2. [Tecnologias](#tecnologias)  
3. [Estrutura do Repositório](#estrutura-do-repositório)  
4. [Pré-requisitos](#pré-requisitos)  
5. [Instalação e Execução](#instalação-e-execução)  
6. [Template Configurável](#template-configurável)  
7. [Aplicação Móvel](#aplicação-móvel)  
8. [Testes e Validação](#testes-e-validação)  
9. [Autores e Agradecimentos](#autores-e-agradecimentos)  

---

## Visão Geral

Este projeto visa disponibilizar online uma aplicação web que exibe:

- **Catálogo de instrumentos históricos** (base de dados PostgreSQL + Spring Boot).  
- **Glossário técnico** dinâmico.  
- **Quizzes interativos** carregados de JSON.  
- **Deploy containerizado** (Docker) e servido por NGINX com HTTPS (Let’s Encrypt).  
- **Template genérico** para adaptação rápida a outros museus.  
- **Aplicação Android** de divulgação (Jetpack Compose).

## Tecnologias

- **Backend**: Java 11 + Spring Boot  
- **Frontend**: Angular + Bootstrap  
- **Containerização**: *Docker* (e compatível com Podman em desenvolvimento)  
- **Web server / Proxy**: NGINX  
- **TLS/HTTPS**: Certbot (Let’s Encrypt)  
- **CI/LFS**: Git LFS (para assets grandes)  
- **Mobile**: Android Native (Jetpack Compose)

## Estrutura do Repositório
/
├── 00_Planeamento # Documentos de planeamento
├── 01_Analise # Análise de requisitos e contexto
├── 02_Desenho # Diagramas e especificações de desenho
├── 03_Implementacao
│ └── Museu # Código-fonte do projeto Museu
│ ├── src/
│ │ └── main/
│ │ └── webapp/
│ │ ├── content/ # JSONs, textos, imagens e vídeos
│ │ ├── images/ # UI assets (capa, cartões)
│ │ └── video/ # Vídeo de apresentação
│ └── Dockerfiles, pom.xml, etc.
├── 04_Teste # Plano e resultados de validação
└── README.md # Este arquivo


## Pré-requisitos

- Java 11 JDK  
- Maven  
- Node.js + npm  
- Docker & Docker Compose  
- Git + Git LFS  
- (Opcional) Android Studio para app móvel

## Instalação e Execução

1. **Clone o repositório**  
   ```bash
   git clone https://github.com/TomasRoberto123/PRJ_28_49746_50751.git
   cd PRJ_28_49746_50751/03_Implementacao/Museu


