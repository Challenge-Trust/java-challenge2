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
| Pontos | `GET/POST /api/pontos` · `GET/PUT/DELETE /api/pontos/{id}` |
| Passagens | `GET/POST /api/tickets` · `GET/PUT/DELETE /api/tickets/{id}` |
| CO₂ | `GET/POST /api/co2` · `GET/PUT/DELETE /api/co2/{id}` |
| Autenticação | `POST /api/auth/login` · `POST /api/auth/recuperar-senha` |
| Saúde | `GET /health` |

Erros sempre no formato `{ "status", "erro", "mensagem", "timestamp" }`.

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

## Links

- Front-end: <https://github.com/Challenge-Trust/Challenge-FrontEnd>
- API publicada: _a definir (Render)_
- Front publicado: _a definir (Vercel)_
