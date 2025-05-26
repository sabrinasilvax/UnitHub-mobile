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

## O que significa cada pasta

- **activities/**  
  Contém as telas principais do aplicativo (Activities do Android). Cada arquivo representa uma tela, como login, cadastro, feed de eventos, perfil do usuário, etc.

- **adapters/**  
  Contém adaptadores para RecyclerView e outros componentes de lista/carrossel. Eles são responsáveis por conectar os dados às views de listas, carrosséis de imagens, etc.

- **api/**  
  Centraliza toda a comunicação com a API do backend.
  - **ApiClient.java**: Configuração do Retrofit e do cliente HTTP.
  - **ApiService.java**: Interface com os endpoints da API.
  - **AuthInterceptor.java**: Intercepta requisições para adicionar autenticação.
  - **requests/**: Modelos de dados enviados para a API (ex: cadastro, login).
  - **responses/**: Modelos de dados recebidos da API (ex: eventos, cursos).

- **components/**  
  Componentes visuais reutilizáveis, como a barra de navegação superior (AppBarMenu) e a view de paginação.

- **exceptions/**  
  Classes para tratamento de erros e respostas de erro da API.

---

testes automatizados implementados para validar o fluxo completo e funcionalidades críticas do aplicativo UniHub, garantindo:

Cadastro de usuários

Login e autenticação

Recuperação de senha

Gestão de perfil

Inscrição em eventos

Integração com a API

2. Tipos de Testes
2.1 Testes de Interface (UI)
Ferramenta: Espresso (AndroidX Test)

Cobertura:

CadastroActivityTest: Validação de campos obrigatórios e senhas.

LoginActivityTest: Autenticação, erros de formulário e redirecionamentos.

RecuperarSenhaActivityTest: Fluxo de recuperação de senha.

FluxoCompletoInstrumentadoTest: Teste end-to-end (cadastro → login → perfil → eventos).

2.2 Testes de API
Ferramenta: Retrofit + OkHttpClient + JUnit

Cobertura:

ApiClientTest: Configuração do Retrofit, interceptors (JWT) e tratamento de erro 401.

3. Casos de Teste
3.1 Cadastro (CadastroActivityTest)
Cenário	Ação	Resultado Esperado
Campos obrigatórios vazios	Clicar em "Cadastrar" sem preencher dados	Campos permanecem visíveis (erro de validação)
Senhas não coincidem	Preencher senha e confirmação com valores diferentes	Erro no campo confirmarSenha
3.2 Login (LoginActivityTest)
Cenário	Ação	Resultado Esperado
E-mail inválido	Inserir emailinvalido	Erro "E-mail inválido"
Senha curta	Inserir senha com 3 caracteres	Erro "Mínimo 6 caracteres"
Credenciais incorretas	Usar usuário/senha inválidos	Mantém na tela de login
3.3 Fluxo Completo (FluxoCompletoInstrumentadoTest)
Cadastro: Preenche dados válidos e verifica redirecionamento para login.

Recuperação de Senha: Envia e-mail e valida estado do botão (enabled/disabled).

Login: Autentica e verifica exibição do FeedActivity.

Logout: Remove token das SharedPreferences e redireciona para login.

Perfil:

Bloqueia senha com menos de 6 caracteres.

Altera senha duas vezes (valida troca).

Eventos:

Inscreve-se em um evento via FeedActivity.

Cancela inscrição via InscricoesActivity.

3.4 API (ApiClientTest)
Cenário	Resultado Esperado
Configuração do Retrofit	Base URL correta (https://unithub-3a018275aeb8.herokuapp.com/)
Interceptor de autenticação	Adiciona header Authorization: Bearer [token]
Erro 401 (não autorizado)	Remove token e redireciona para login (simulado)
4. Dados de Teste
Usuário de teste:

java
private static final String EMAIL = "herbert.gabriel@souunit.com.br";  
private static final String SENHA = "123456";  
Token mock: token_teste (armazenado em SharedPreferences).

5. Dependências
AndroidX Test (Espresso, JUnit4)

Hamcrest (para matchers avançados)

Retrofit 2 + OkHttp (testes de API)

6. Observações
Thread.sleep(): Usado para simular espera por respostas assíncronas (substituível por IdlingResource).

SharedPreferences: Limpeza de token após logout e erro 401.

RecyclerView: Ações em itens dinâmicos (ex.: clique no primeiro evento da lista).

ajuste o readme.md