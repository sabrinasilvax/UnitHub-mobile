
# UnitHub Mobile

Aplicativo Android desenvolvido em **Java** com o objetivo de facilitar a gestão e participação em eventos universitários.

> Link da API: https://github.com/herbertgabriel/unithub-api

## 🎓 Informações Acadêmicas

- **Instituição:** Universidade Tiradentes  
- **Curso:** Análise e Desenvolvimento de Sistemas  
- **Período:** 5º Período  
- **Disciplina:** Desenvolvimento Mobile  
- **Professor:** Diógenes Carvalho Matias  

## 🎯 Objetivo

Desenvolver um aplicativo que permita aos estudantes visualizar, inscrever-se e gerenciar eventos universitários de maneira prática e intuitiva.

## 🚀 Tecnologias Utilizadas

- **Java**
- **Android SDK**
- **Retrofit** (Comunicação com API)
- **OkHttp**
- **Espresso** (Testes de interface)

## 🛠️ Como Executar o App

- Ter instalado:
  - **Java 17**
  - **Android SDK**  
- Importar o projeto no **Android Studio**.
- Configurar um emulador ou dispositivo físico Android.

## 👥 Integrantes do Grupo

| Nome | RA |
|---|---|
| Abraão Vinicio Araújo da Silva | 1251307059 |
| Carlos Geraldo Ferreira de Lima Filho | 1221307514 |
| Elaine Cristina Florencio Carvalho | 1242304808 |
| Felipe Vinicius Viana Melo | 1241302690 |
| Herbert Gabriel Almeida Cruz | 1251304424 |
| Paloma Maria Cezário da Silva | 1242305014 |
| Sabrina Guilherme da Silva | 1242303496 |

## 📁 Estrutura de Pastas

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

## ✅ Testes Automatizados

Testes implementados para validar o fluxo completo e funcionalidades críticas.

### 🔹 Funcionalidades Testadas

- Cadastro de usuários
- Login e autenticação
- Recuperação de senha
- Gestão de perfil
- Inscrição e cancelamento em eventos
- Integração com a API

## 🧪 Tipos de Testes

### 1. Testes de Interface (UI)

- **Ferramenta:** Espresso (AndroidX Test)

**Testes Implementados:**

- `CadastroActivityTest` — Validação de campos obrigatórios e senhas.
- `LoginActivityTest` — Testa autenticação, erros de formulário e redirecionamentos.
- `RecuperarSenhaActivityTest` — Valida fluxo de recuperação de senha.
- `FluxoCompletoInstrumentadoTest` — Teste end-to-end: cadastro → login → perfil → eventos.

### 2. Testes de API

- **Ferramentas:** Retrofit, OkHttpClient, JUnit

**Testes Implementados:**

- `ApiClientTest` — Configuração do Retrofit, interceptors (JWT) e tratamento de erro 401.

## 📝 Casos de Teste

### Cadastro (`CadastroActivityTest`)

| Cenário | Ação | Resultado Esperado |
|---|---|---|
| Campos obrigatórios vazios | Clicar em "Cadastrar" sem preencher dados | Campos exibem erro de validação |
| Senhas não coincidem | Preencher senha e confirmação diferentes | Erro no campo de confirmação |

### Login (`LoginActivityTest`)

| Cenário | Ação | Resultado Esperado |
|---|---|---|
| E-mail inválido | Inserir "emailinvalido" | Mensagem "E-mail inválido" |
| Senha curta | Inserir senha com 3 caracteres | Mensagem "Mínimo 6 caracteres" |
| Credenciais incorretas | Usar dados inválidos | Mantém na tela de login |

### Fluxo Completo (`FluxoCompletoInstrumentadoTest`)

- Cadastro → redireciona para login
- Recuperação de senha → valida estado do botão
- Login → exibe FeedActivity
- Logout → limpa token e redireciona
- Perfil → bloqueia senhas curtas, valida troca de senha
- Eventos → inscrição e cancelamento

### API (`ApiClientTest`)

| Cenário | Resultado Esperado |
|---|---|
| Configuração do Retrofit | Base URL correta: `https://unithub-3a018275aeb8.herokuapp.com/` |
| Interceptor | Header `Authorization: Bearer [token]` adicionado |
| Erro 401 | Token removido e redirecionamento para login (simulado) |

## 🎯 Dados de Teste

```java
private static final String EMAIL = "herbert.gabriel@souunit.com.br";
private static final String SENHA = "123456";
```

- **Token mock:** `token_teste` (armazenado em SharedPreferences)

## 📦 Dependências

- **AndroidX Test** — Espresso, JUnit4
- **Hamcrest** — Matchers avançados
- **Retrofit 2 + OkHttp** — Comunicação e testes de API

