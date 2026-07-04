# sistema-turismo-cliente-web (T8)

Aplicação Cliente REST, independente, que consome a API REST de **Clientes** exposta pelo
projeto [sistema-turismo](../sistema-turismo) (T7). Usa `RestClient` + Spring MVC + Thymeleaf.
Este projeto **não acessa banco de dados** — toda persistência acontece do lado do projeto T7,
via chamadas HTTP.

## Arquitetura

```
[Navegador] -> [sistema-turismo-cliente-web : 8081] --RestClient(HTTP)--> [sistema-turismo : 8080] -> [PostgreSQL]
```

## Pré-requisitos

- Java 17+
- Maven 3.9+
- O projeto **sistema-turismo (T7)** rodando e acessível (por padrão em `http://localhost:8080`),
  com o PostgreSQL configurado conforme o README daquele projeto.

## Como executar

1. Suba primeiro a API (projeto `sistema-turismo`, T7):
   ```
   cd ../sistema-turismo
   ./mvnw spring-boot:run
   ```
   Confirme que está respondendo em `http://localhost:8080/clientes`.

2. Em outro terminal, suba este projeto (Cliente, T8):
   ```
   cd sistema-turismo-cliente-web
   ./mvnw spring-boot:run
   ```

3. Acesse `http://localhost:8081/clientes` no navegador.

## Configuração

- `server.port=8081` — porta deste projeto (diferente da API, que usa 8080).
- `api.sistema-turismo.base-url=http://localhost:8080` — endereço da API T7. Se a API rodar em
  outra porta/host, altere essa propriedade em `src/main/resources/application.properties`.

## Operações disponíveis (CRUD completo)

| Ação             | Rota neste projeto (Cliente)     | Endpoint chamado na API (T7)  |
|------------------|-----------------------------------|--------------------------------|
| Listar           | `GET /clientes`                  | `GET /clientes`                |
| Criar            | `GET /clientes/novo` + `POST /clientes` | `POST /clientes`         |
| Editar           | `GET /clientes/{id}/editar` + `POST /clientes/{id}` | `PUT /clientes/{id}` |
| Excluir          | `POST /clientes/{id}/excluir`    | `DELETE /clientes/{id}`        |

## Observações

- Os endpoints `/clientes/**` da API (T7) são públicos (`permitAll`), então este cliente não
  precisa se autenticar para consumi-los.
- O campo "Senha" precisa ser preenchido também ao editar um cliente: a API (`PUT /clientes/{id}`)
  valida o campo como obrigatório (`@NotBlank`) mesmo que a lógica de negócio só recodifique a
  senha quando um valor novo é enviado.
- Erros retornados pela API (ex: CPF/e-mail duplicado, dados inválidos) são exibidos na tela de
  formulário ou na listagem, conforme a operação.
