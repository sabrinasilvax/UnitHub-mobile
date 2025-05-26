# UnitHub Mobile

Este projeto é um aplicativo Android desenvolvido em Java, com o objetivo de facilitar a gestão e participação em eventos universitários.

## Estrutura de Pastas

Abaixo está um resumo da estrutura principal do código-fonte e o significado de cada pasta:

```
app/src/main/java/com/mobile/unithub/
│
├── activities/
│   ├── CadastroActivity.java
│   ├── DetalhesEventosActivity.java
│   ├── FeedActivity.java
│   ├── InscricoesActivity.java
│   ├── LoginActivity.java
│   ├── PerfilActivity.java
│   └── RecuperarSenhaActivity.java
│
├── adapters/
│   ├── FeedAdapter.java
│   ├── ImageAdapter.java
│   ├── ImageCarouselAdapter.java
│   └── InscricoesAdapter.java
│
├── api/
│   ├── ApiClient.java
│   ├── ApiService.java
│   ├── AuthInterceptor.java
│   ├── requests/
│   │   ├── CadastroRequest.java
│   │   ├── LoginRequest.java
│   │   └── ...
│   └── responses/
│       └── ...
│
├── components/
│   ├── AppBarMenu.java
│   └── PaginationView.java
│
└── exceptions/
    └── ErrorResponse.java
```


## 🗂️ Descrição das Pastas

- **activities/**  
  Contém as principais telas do aplicativo. Cada arquivo representa uma Activity, como login, cadastro, feed de eventos, perfil do usuário, entre outras.

- **adapters/**  
  Adaptadores para RecyclerView e carrosséis. Responsáveis por conectar dados às views.

- **api/**  
  Gerencia a comunicação com a API:
  - `ApiClient.java`: Configuração do Retrofit.
  - `ApiService.java`: Interface com os endpoints.
  - `AuthInterceptor.java`: Intercepta requisições para adicionar autenticação.
  - `requests/`: Modelos de dados enviados para a API.
  - `responses/`: Modelos de dados recebidos da API.

- **components/**  
  Componentes visuais reutilizáveis como AppBar e paginação.

- **exceptions/**  
  Classes para tratamento de erros e respostas da API.

---

## ✅ Testes Automatizados

Testes implementados para validar o fluxo completo e funcionalidades críticas do aplicativo.

### 🔹 Funcionalidades Testadas:

- Cadastro de usuários
- Login e autenticação
- Recuperação de senha
- Gestão de perfil
- Inscrição e cancelamento de inscrição em eventos
- Integração com a API

---

## 🧪 Tipos de Testes

### 1. Testes de Interface (UI)

**Ferramenta:** Espresso (AndroidX Test)

**Cobertura:**

- **CadastroActivityTest:**  
  Validação de campos obrigatórios e senhas.

- **LoginActivityTest:**  
  Testa autenticação, erros de formulário e redirecionamentos.

- **RecuperarSenhaActivityTest:**  
  Valida fluxo de recuperação de senha.

- **FluxoCompletoInstrumentadoTest:**  
  Teste end-to-end (cadastro → login → perfil → eventos).

---

### 2. Testes de API

**Ferramentas:** Retrofit + OkHttpClient + JUnit

**Cobertura:**

- **ApiClientTest:**  
  Valida configuração do Retrofit, interceptors (JWT) e tratamento de erro 401.

---

## 📝 Casos de Teste

### 1. Cadastro (CadastroActivityTest)

| Cenário                   | Ação                                                   | Resultado Esperado                           |
| ------------------------  | ------------------------------------------------------ | -------------------------------------------- |
| Campos obrigatórios vazios| Clicar em "Cadastrar" sem preencher dados              | Campos permanecem visíveis (erro de validação)|
| Senhas não coincidem      | Preencher senha e confirmação com valores diferentes   | Erro no campo confirmarSenha                 |

---

### 2. Login (LoginActivityTest)

| Cenário                   | Ação                           | Resultado Esperado             |
| ------------------------  | ------------------------------ | ------------------------------ |
| E-mail inválido           | Inserir "emailinvalido"        | Erro "E-mail inválido"         |
| Senha curta               | Inserir senha com 3 caracteres | Erro "Mínimo 6 caracteres"     |
| Credenciais incorretas    | Usar usuário/senha inválidos   | Mantém na tela de login        |

---

### 3. Fluxo Completo (FluxoCompletoInstrumentadoTest)

- **Cadastro:** Preenche dados válidos e verifica redirecionamento para login.
- **Recuperação de Senha:** Envia e-mail e valida estado do botão (enabled/disabled).
- **Login:** Autentica e verifica exibição do FeedActivity.
- **Logout:** Remove token das SharedPreferences e redireciona para login.
- **Perfil:**  
  - Bloqueia alteração de senha com menos de 6 caracteres.  
  - Altera senha duas vezes para validar troca.
- **Eventos:**  
  - Inscreve-se em um evento via FeedActivity.  
  - Cancela inscrição via InscricoesActivity.

---

### 4. API (ApiClientTest)

| Cenário                   | Resultado Esperado                                   |
| ------------------------  | ---------------------------------------------------- |
| Configuração do Retrofit  | Base URL correta: `https://unithub-3a018275aeb8.herokuapp.com/` |
| Interceptor de autenticação | Adiciona header `Authorization: Bearer [token]`  |
| Erro 401 (não autorizado) | Remove token e redireciona para login (simulado)      |

---

## 🎯 Dados de Teste

private static final String EMAIL = "herbert.gabriel@souunit.com.br";
private static final String SENHA = "123456";
Token mock: token_teste (armazenado em SharedPreferences).

##📦 Dependências
- AndroidX Test: Espresso, JUnit4

- Hamcrest: Matchers avançados

- Retrofit 2 + OkHttp: Testes de API

## ⚠️ Observações Importantes
- Thread.sleep(): Usado para simular espera por respostas assíncronas (pode ser substituído por IdlingResource).

- SharedPreferences: Limpeza do token após logout e em erros 401.

- RecyclerView: Manipula ações em itens dinâmicos, como clicar no primeiro evento da lista.

## 🚀 Tecnologias Principais
- Java

- Android SDK

- Retrofit

- Espresso

- OkHttp
