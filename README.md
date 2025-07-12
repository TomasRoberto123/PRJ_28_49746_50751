# Catalogo do Museu de Física do ISEL
Aplicação web para divulgação do património científico do Departamento de Física do ISEL, com catálogo de instrumentos históricos, glossário técnico e quizzes interativos.

## Estrutura do Repositório

```
.
├── 00_Planeamento      
├── 01_Analise          
├── 02_Desenho          # Diagramas 
├── 03_Implementacao
│   └── Museu           # Código-fonte do projeto Museu
│       ├── src/
│       │   └── main/
│       │       └── webapp/
│       │           ├── content/       # JSON, textos, imagens e vídeos           
│       └── Dockerfiles, pom.xml, etc.
├── 04_Teste            # Plano e resultados de validação
└── README.md           # Este ficheiro
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
   Substitua ficheiros em:
   - `content/images/` (capa, cartões)
   - `content/video/` (apresenta.mp4, video_capa.png)
   - `content/data/images/` (imagens de conteúdo)

3. **Textos de Conteúdo**  
   Altere ou adicione `.txt` ou `.csv` em `content/data/texts/`.


## Aplicação Móvel
Projeto Android em 03_Implementacao/AppQuiz.

 - Carrega perguntas de JSON dos assets.

 - Quiz interativo, categorias e integração para abrir o site do museu.

 - Jetpack Compose + Navigation Compose.

## Documentação

- Relatório final em PDF e fontes LaTeX dentro de `_RELATORIO/`.

## Autores e Agradecimentos
Tomas Roberto (49746)

Daniela Gomes (50751)

Orientadores: Prof. Rui Jesus, Prof. Carlos Gonçalves

Agradecemos também ao ISEL, DEETC e às nossas famílias pelo apoio.

