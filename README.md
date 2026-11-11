# Go Market API

API REST para a plataforma Go Market, voltada ao gerenciamento de usuários, lojas, produtos, categorias, pedidos e cupons. A aplicação foi desenvolvida com Java 21 e Spring Boot 3.4.4, usa MySQL para persistência, Spring Data JPA e autenticação stateless com JWT. Também oferece upload e entrega de arquivos e imagens.

## Requisitos

- Java 21 e Maven 3.9+ para execução local
- MySQL 8.4+
- Docker e Docker Compose (alternativa para iniciar a API e o banco)

## Configuração e execução

A aplicação lê as credenciais e chaves das variáveis de ambiente. Para executar com Docker Compose, crie um arquivo `.env` na raiz:

```env
MYSQL_ROOT_PASSWORD=senha_root
MYSQL_PASSWORD=senha_do_banco
JWT_SECRET_KEY=chave_secreta_jwt
CRYPTO_SECRET_KEY=chave_secreta_criptografia
```

Inicie os serviços:

```bash
docker compose up --build
```

A API ficará disponível em `http://localhost:8080` e o MySQL na porta `3306`.

Para executar fora do Docker, configure `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET_KEY` e `CRYPTO_SECRET_KEY`, e inicie com:

```bash
./mvnw spring-boot:run
```

No Windows, use `mvnw.cmd spring-boot:run`.

## Autenticação

O token JWT deve ser enviado no cabeçalho `Authorization: Bearer <token>` para as rotas protegidas. Pela configuração do Spring Security, `/api/auth/**`, `/api/files/**` e `/api/hello-world` são caminhos liberados; as demais rotas exigem autenticação. A API é stateless.

## Rotas disponíveis

Todas as rotas abaixo têm o prefixo `http://localhost:8080`. `{id}`, `{uuid}`, `{pcId}` e `{code}` são parâmetros de caminho.

### Verificação — `/api/hello-world`

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/api/hello-world` | Retorna `Hello World` sem exigir autenticação. |

### Autenticação — `/api/auth`

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/api/auth/sign-up` | Registra um usuário. |
| `POST` | `/api/auth/sign-in` | Autentica com `username` e `password`. |
| `GET` | `/api/auth/me` | Retorna os dados do usuário atual. |

### Usuários — `/api/users`

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/api/users` | Lista usuários; aceita o parâmetro opcional `where`. |
| `GET` | `/api/users/{id}` | Busca usuário por ID. |
| `PUT` | `/api/users/{id}` | Atualiza usuário. |
| `DELETE` | `/api/users/{id}` | Rota declarada, mas ainda não implementada (retorna erro de operação não suportada). |

### Lojas — `/api/stores`

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/api/stores` | Cria loja; recebe `multipart/form-data` com `data` e arquivo opcional `file`. |
| `GET` | `/api/stores` | Lista lojas; aceita o parâmetro opcional `where`. |
| `GET` | `/api/stores/{id}` | Busca loja por ID. |
| `GET` | `/api/stores/dashboard-metrics` | Retorna métricas do painel. |
| `GET` | `/api/stores/{id}/dashboard-metrics` | Retorna métricas do painel de uma loja. |
| `PUT` | `/api/stores/{id}` | Atualiza loja; recebe `multipart/form-data` com `data` e arquivo opcional `file`. |
| `DELETE` | `/api/stores/{id}` | Exclui loja. |

### Produtos — `/api/products`

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/api/products` | Cria produto; recebe `multipart/form-data` com `data` e arquivos opcionais `files`. |
| `GET` | `/api/products` | Lista produtos; aceita o parâmetro opcional `where`. |
| `GET` | `/api/products/featured` | Lista produtos em destaque; aceita o parâmetro opcional `where`. |
| `GET` | `/api/products/{id}` | Busca produto por ID. |
| `PUT` | `/api/products/{id}` | Atualiza produto; recebe `multipart/form-data` com `data` e arquivos opcionais `files`. |
| `PUT` | `/api/products/mark-as-featured` | Atualiza destaque para uma lista de IDs de produtos enviada no corpo. |
| `DELETE` | `/api/products/{id}` | Exclui produto. |

### Comentários de produtos — `/api/products/{id}`

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/api/products/{id}/comment` | Cria comentário para um produto. |
| `GET` | `/api/products/{id}/comment` | Lista os comentários do produto. |
| `PUT` | `/api/products/{id}/comment/{pcId}` | Atualiza comentário do produto. |

### Pedidos — `/api/orders`

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/api/orders` | Cria pedido. |
| `GET` | `/api/orders` | Lista pedidos; aceita o parâmetro opcional `where`. |
| `GET` | `/api/orders/{id}` | Busca pedido por ID. |
| `GET` | `/api/orders/{id}/update-status` | Atualiza o status do pedido. |

### Cupons — `/api/cupons`

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/api/cupons` | Cria cupom. |
| `GET` | `/api/cupons` | Lista cupons; requer o parâmetro `where`. |
| `GET` | `/api/cupons/{id}` | Busca cupom por ID. |
| `GET` | `/api/cupons/use/{code}` | Aplica/usa cupom pelo código. |
| `PUT` | `/api/cupons/{id}` | Atualiza cupom. |

### Categorias — `/api/categories`

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/api/categories` | Cria categoria. |
| `GET` | `/api/categories` | Lista categorias; aceita o parâmetro opcional `where`. |
| `GET` | `/api/categories/{id}` | Rota declarada, mas ainda não implementada (retorna erro de operação não suportada). |
| `PUT` | `/api/categories/{id}` | Rota declarada, mas ainda não implementada (retorna erro de operação não suportada). |
| `DELETE` | `/api/categories/{id}` | Rota declarada, mas ainda não implementada (retorna erro de operação não suportada). |

### Arquivos — `/api/files`

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/api/files/{uuid}` | Recupera arquivo pelo UUID e retorna seu conteúdo com o tipo MIME armazenado. |

## Observações

- Os endpoints que recebem imagens ou arquivos usam `multipart/form-data`; os campos estão indicados na tabela de cada rota.
- As listagens marcadas com `where` aceitam filtros definidos pela API.
- O tamanho máximo configurado para upload é de 10 MB por arquivo e por requisição.
- As rotas de exclusão de usuário e de leitura, atualização e exclusão de categoria estão mapeadas, mas seus métodos ainda não foram implementados.
