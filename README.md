# Museu Digital Template

Este repositório contém o template para criação de museus digitais, baseado em JHipster (Spring Boot + Angular) e Docker, permitindo fácil configuração e deployment.

## Estrutura do Repositório

```
.
├── _RELATORIO/                     Documentação final em PDF e fontes LaTeX
├── src/                            Código-fonte Spring Boot (backend)
├── src/main/webapp/                Frontend Angular gerado pelo JHipster
│   ├── content/
│   │   ├── config-museu/           Configuração dinâmica (JSON)
│   │   │   └── museum-config.json  
│   │   ├── images/                 Assets de UI (capa, cartões, etc.)
│   │   ├── data/
│   │   │   ├── texts/              Textos estáticos (.txt, .csv)
│   │   │   └── images/             Imagens de conteúdo
│   │   └── video/                  Vídeos de apresentação
├── src/main/docker/                Docker Compose files
│   └── app.yml                     Compose para desenvolvimento
├── .env.example                    Exemplo de variáveis de ambiente
├── docker-compose.override.yml     Overrides locais (opcional)
└── README.md                       Este arquivo
```

## Pré-requisitos

- [Java 11+](https://adoptopenjdk.net/)
- [Maven](https://maven.apache.org/)
- [Node.js e npm](https://nodejs.org/)
- [Angular CLI](https://angular.io/cli) (opcional para desenvolvimento local)
- [Docker](https://www.docker.com/) e [Docker Compose](https://docs.docker.com/compose/)

## Configuração

1. Copie o exemplo de ambiente:

   ```bash
   cp .env.example .env
   ```

2. Edite o arquivo `.env` para definir:
   - `DB_HOST`, `DB_PORT`, `DB_NAME`
   - `MUSEUM_NAME`
   - Outros parâmetros conforme necessário.

3. (Opcional) Atualize `src/main/webapp/content/config-museu/museum-config.json` para alterar títulos e legendas de **navbar** e **footer**.

## Como Executar

### Usando Docker Compose

```bash
docker-compose --env-file .env -f src/main/docker/app.yml up -d --build
```

- Backend: `localhost:8080`
- Frontend: `localhost:8080` (via NGINX ou diretamente)

Para derrubar os containers:

```bash
docker-compose -f src/main/docker/app.yml down
```

### Desenvolvimento Local

#### Backend

```bash
cd src
mvn -DskipTests clean package
./mvnw
```

#### Frontend

```bash
cd src/main/webapp
npm install
ng serve --open
```

## Personalização do Museu

1. **Textos**  
   Edite o JSON em `content/config-museu/museum-config.json`:
   ```json
   {
     "navbar": { "museumName": "Nome do Museu" },
     "footer": {
       "title": "Nome do Museu",
       "quote": "...",
       "citation": "– Autor",
       "copyright": ""
     }
   }
   ```

2. **Imagens e Vídeos**  
   Substitua arquivos em:
   - `content/images/` (capa, cartões)
   - `content/video/` (apresenta.mp4, video_capa.png)
   - `content/data/images/` (imagens de conteúdo)

3. **Textos de Conteúdo**  
   Altere ou adicione `.txt` ou `.csv` em `content/data/texts/`.

## Documentação

- Relatório final em PDF e fontes LaTeX dentro de `_RELATORIO/`.
- Slides e demais artefatos podem ser acrescentados conforme convém.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

