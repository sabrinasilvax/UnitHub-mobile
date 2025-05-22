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

