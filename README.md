# SoulUp API (TrustCorp)

API REST da solução **SoulUp**: o usuário assiste conteúdos, os minutos viram pontos
(180 min = 1 ponto) e os pontos viram passagens de transporte público (10 pontos = 1 passagem).
A API também calcula o CO₂ economizado ao trocar o carro pelo ônibus e o selo sustentável.

> Versão da Sprint 2 (console + H2): tag [`sprint-2`](https://github.com/Challenge-Trust/java-challenge2/tree/sprint-2).

## Tecnologias

- Java 21 · Spring Boot 3.5 · Maven (wrapper incluso)
- Oracle Database via JDBC puro (padrão DAO + ConnectionFactory, `PreparedStatement`)
- BCrypt (`spring-security-crypto`) para o hash das senhas
- Docker (build multi-stage) para o deploy

## Estrutura

```
src/main/java/br/com/fiap/soulup/
├── model      Usuario, Points, Ticket, CO2Economia (1:1 com as tabelas)
├── factory    ConnectionFactory
├── dao        CRUD JDBC de cada entidade
├── bo         regras de negócio e validações
├── resource   controllers REST
├── exception  exceções customizadas + GlobalExceptionHandler
├── config     CorsFilter
└── dto        objetos de entrada/saída do JSON
```

## Endpoints

| Recurso | Endpoints |
| --- | --- |
| Usuários | `GET/POST /api/usuarios` · `GET/PUT/DELETE /api/usuarios/{id}` |
| Dados do usuário | `GET /api/usuarios/{id}/pontos` · `/tickets` · `/co2` · `/selo` |
| Conversão | `POST /api/usuarios/{id}/minutos` (credita minutos e converte) |
| Pontos | `GET/POST /api/pontos` · `GET/PUT/DELETE /api/pontos/{id}` |
| Passagens | `GET/POST /api/tickets` · `GET/PUT/DELETE /api/tickets/{id}` |
| CO₂ | `GET/POST /api/co2` · `GET/PUT/DELETE /api/co2/{id}` |
| Autenticação | `POST /api/auth/login` · `POST /api/auth/recuperar-senha` |
| Saúde | `GET /health` |

Erros sempre no formato `{ "status", "erro", "mensagem", "timestamp" }`.
Tabela completa (corpo e status de cada endpoint), MER e diagrama de classes em
[`DOCUMENTACAO.md`](DOCUMENTACAO.md).

## Como rodar localmente

Pré-requisitos: JDK 21 e um banco Oracle (FIAP ou local via Docker).

1. Crie as tabelas executando o [`script.sql`](script.sql) no seu schema (SQL Developer).
2. Defina as variáveis de ambiente:

   | Variável | Exemplo |
   | --- | --- |
   | `DB_URL` | `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL` |
   | `DB_USER` | seu RM (ex.: `rm573351`) |
   | `DB_PASSWORD` | sua senha do Oracle |
   | `FRONT_URL` | URL do front na Vercel (opcional local) |

   No IntelliJ: *Run > Edit Configurations > Environment variables*.

3. Rode a aplicação:

   ```bash
   ./mvnw spring-boot:run        # Linux/macOS/Git Bash
   mvnw.cmd spring-boot:run      # Windows (cmd/PowerShell)
   ```

4. Teste: <http://localhost:8080/health>. O front (Vite) roda em `http://localhost:5173`,
   já liberado no CORS.

### Oracle local com Docker (opcional)

```bash
docker run -d --name soulup-oracle -p 1521:1521 \
  -e ORACLE_PASSWORD=oracle -e APP_USER=soulup -e APP_USER_PASSWORD=soulup \
  gvenzl/oracle-free:23-slim-faststart
# DB_URL=jdbc:oracle:thin:@localhost:1521/FREEPDB1  DB_USER=soulup  DB_PASSWORD=soulup
```

Usuários de exemplo do `script.sql`: CPF `52998224725` ou `11144477735`, senha `senha123`.

## Deploy no Render

1. Em <https://render.com>: **New → Web Service** e conecte este repositório.
2. **Language:** Docker (usa o `Dockerfile` multi-stage). **Instance type:** Free.
3. **Health Check Path:** `/health`.
4. **Environment Variables:** `DB_URL`, `DB_USER`, `DB_PASSWORD` e `FRONT_URL`
   (URL da Vercel, sem `/` no final). A porta vem da variável `PORT`, definida pelo Render.
5. Depois do deploy, use a URL `https://<servico>.onrender.com` como `VITE_API_URL` no front.

## Entrega (.zip sem a pasta target)

```powershell
# PowerShell, na pasta java-challenge2 — gera o zip só com os arquivos versionados
git archive --format=zip -o ..\java-challenge2.zip HEAD
```

Ou compacte manualmente `src/`, `pom.xml`, `mvnw`, `mvnw.cmd`, `.mvn/`, `Dockerfile`,
`script.sql`, `README.md` e `DOCUMENTACAO.md` — **sem** `target/` e `.idea/`.

## Links

- Repositório da API: <https://github.com/Challenge-Trust/java-challenge2>
- Repositório do front-end: <https://github.com/Challenge-Trust/Challenge-FrontEnd>
- API publicada: **[PREENCHER APÓS O DEPLOY NO RENDER]**
- Front publicado: **[PREENCHER APÓS O DEPLOY NA VERCEL]**

## Integrantes · Turma 1TDSPI · FIAP 2026

| Nome            | RM     | GitHub                                                            |
| --------------- | ------ | ----------------------------------------------------------------- |
| Henrique Soares | 573618 | [henriquesoaresserra-h](https://github.com/henriquesoaresserra-h) |
| Vinicius Soares | 573351 | [vinisl2510-sudo](https://github.com/vinisl2510-sudo)             |
| Nicolas Frazão  | 568780 | [Frazaomol](https://github.com/Frazaomol)                         |
| Cauã Bertini    | 570451 | [cauabertini](https://github.com/cauabertini)                     |
