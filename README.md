# Sistema de Turismo — T7

Sistema web para compra/venda de pacotes turísticos, desenvolvido com Spring MVC, Spring Data JPA, Spring Security e Thymeleaf.

## Tecnologias

- Java 17
- Spring Boot 3.5.14
- Spring MVC + Thymeleaf
- Spring Data JPA + Hibernate
- Spring Security (BCrypt)
- PostgreSQL
- Maven

---

## Banco de dados

**SGBD:** PostgreSQL 15+

**Nome do banco:** `turismo`

### Scripts SQL necessários

Execute os comandos abaixo antes de iniciar a aplicação:

```sql
-- 1. Criar o banco de dados
CREATE DATABASE turismo;
```

> Não é necessário criar tabelas manualmente. O Hibernate cria e atualiza o schema automaticamente (`ddl-auto=update`) ao iniciar a aplicação.

### Tabelas criadas automaticamente pelo Hibernate

| Tabela               | Descrição                                      |
|----------------------|------------------------------------------------|
| `usuarios`           | Tabela base — armazena id, email, senha (hash) |
| `admins`             | Subtabela de `usuarios` — sem campos próprios  |
| `agencias`           | Subtabela de `usuarios` — cnpj, nome, descrição |
| `clientes`           | Subtabela de `usuarios` — cpf, nome, telefone, sexo, data de nascimento |
| `pacotes_turisticos` | Pacotes cadastrados pelas agências (inclui vagas disponíveis) |
| `pacote_fotos`       | Caminhos das fotos de cada pacote              |
| `compras`            | Registros de compra de pacotes por clientes    |

A herança entre `usuarios`, `admins`, `agencias` e `clientes` usa a estratégia `JOINED` do JPA: cada registro de admin, agência ou cliente tem uma linha em `usuarios` (com o id compartilhado) e uma linha na respectiva subtabela.

### Configuração de conexão (`application.properties`)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/turismo
spring.datasource.username=postgres
spring.datasource.password=postgres
```

---

## Usuários pré-cadastrados

Na **primeira execução**, o sistema popula automaticamente os usuários abaixo via `DataInitializer` — não é necessário rodar nenhum script de seed manual. O admin é criado de forma idempotente (toda vez que a aplicação sobe, caso ainda não exista); agência, cliente e pacotes de exemplo só são criados se o banco estiver totalmente vazio.

| Tipo    | E-mail              | Senha      | Papel        | Tabelas envolvidas |
|---------|---------------------|------------|--------------|-----------------|
| Admin   | admin@sistema.com   | admin123   | ROLE_ADMIN   | `usuarios` + `admins` |
| Agência | agencia@teste.com   | agencia123 | ROLE_AGENCIA | `usuarios` + `agencias` |
| Cliente | cliente@teste.com   | cliente123 | ROLE_CLIENTE | `usuarios` + `clientes` |

As credenciais do admin (e-mail e senha) vêm de `app.admin.email`/`app.admin.senha` em `application.properties` — a senha é codificada em BCrypt antes de salvar.

### Papéis e permissões

| Papel         | Acesso permitido |
|---------------|-----------------|
| `ROLE_ADMIN`  | CRUD de clientes (`/admin/clientes/**`) e CRUD de agências (`/admin/agencias/**`) |
| `ROLE_AGENCIA`| Cadastrar e listar os próprios pacotes (`/agencia/pacotes/**`) |
| `ROLE_CLIENTE`| Comprar pacotes (`/compras/**`) e ver histórico de compras (`/cliente/pacotes`) |
| Público       | Listar e detalhar pacotes (`/pacotes`, `/pacotes/{id}`) e REST API |

---

## Como executar

### Pré-requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL 15+ rodando em `localhost:5432`

### Passos

```bash
# 1. Criar o banco (apenas na primeira vez)
psql -U postgres -c "CREATE DATABASE turismo;"

# 2. Iniciar a aplicação
mvn spring-boot:run
```

Acesse: `http://localhost:8080`

---

## Funcionalidades

| Requisito | Descrição | Acesso |
|-----------|-----------|--------|
| R1 | CRUD de clientes | Admin |
| R2 | CRUD de agências | Admin |
| R3 | Cadastro de pacotes com fotos e roteiro PDF | Agência |
| R4 | Listagem pública com filtros (destino, agência, data) | Público |
| R5 | Compra de pacote + e-mail de confirmação + link de videoconferência | Cliente |
| R6 | Histórico de compras do cliente | Cliente |
| R7 | Pacotes da agência com filtro "apenas vigentes" | Agência |
| R8 | Internacionalização PT/EN (`?lang=pt` / `?lang=en`) | Todos |
| R9 | Validação de formulários com mensagens de erro | Todos |
| R10 | Controle de estoque: vagas limitadas por pacote, decremento atômico, bloqueio quando esgota | Cliente / Agência |

---

## REST API 

Todos os endpoints retornam JSON e **não requerem autenticação**.

```
GET    /clientes                → lista todos os clientes
GET    /clientes/{id}           → busca cliente por id
POST   /clientes                → cria cliente (corpo JSON)
PUT    /clientes/{id}           → atualiza cliente (corpo JSON)
DELETE /clientes/{id}           → remove cliente

GET    /agencias                → lista todas as agências
GET    /agencias/{id}           → busca agência por id
POST   /agencias                → cria agência (corpo JSON)
PUT    /agencias/{id}           → atualiza agência (corpo JSON)
DELETE /agencias/{id}           → remove agência

GET    /pacotes                 → lista todos os pacotes
GET    /pacotes/clientes/{id}   → pacotes comprados pelo cliente
POST   /pacotes/agencias/{id}   → cria pacote para a agência (corpo JSON, exige "vagasDisponiveis")
GET    /pacotes/agencias/{id}   → pacotes da agência
GET    /pacotes/destinos/{nome} → pacotes filtrados por destino
```

Erros de validação (`@Valid`) retornam `400` com o detalhe de cada campo:
```json
{"status": 400, "mensagem": "Dados inválidos.", "erros": {"cpf": "CPF deve conter exatamente 11 dígitos numéricos."}}
```

---

## Aplicação Cliente REST 

A pasta [`sistema-turismo-cliente-web/`](sistema-turismo-cliente-web/) contém um segundo projeto
Maven, independente deste, que consome o CRUD de clientes exposto pela REST API acima usando
`RestClient` + Spring MVC + Thymeleaf. Ele não acessa o banco de dados diretamente — toda
persistência passa pela API deste projeto.

Veja o passo a passo de execução (como subir os dois projetos juntos, portas usadas, etc.) no
[README da pasta](sistema-turismo-cliente-web/README.md).

---

## Uploads

Arquivos salvos em `./uploads/` (relativo ao diretório de execução):

- Fotos dos pacotes: `./uploads/fotos/`
- Roteiros em PDF: `./uploads/roteiros/`

Os arquivos são servidos diretamente pelo Spring Boot via `spring.web.resources.static-locations`,
publicamente em `/fotos/**` e `/roteiros/**` (sem necessidade de login, já que a listagem de
pacotes também é pública).

---

## Internacionalização

O idioma é trocado pelo parâmetro `?lang=` em qualquer URL:

- Português (padrão): `?lang=pt`
- Inglês: `?lang=en`

---

## E-mail de confirmação de compra

Ao comprar um pacote, o sistema envia um e-mail com os dados da compra e o link da videoconferência (foi usado o Jitsi Meet, uma vez que não precisa de API Key). Configure as variáveis de ambiente:

```bash
export MAIL_USER=seu-email@gmail.com
export MAIL_PASS=sua-senha-de-app
```

Se as variáveis não estiverem definidas, o envio de e-mail é ignorado sem interromper a compra.
