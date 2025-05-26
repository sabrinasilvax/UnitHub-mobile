package com.mobile.unithub.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mobile.unithub.R;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class FluxoCompletoInstrumentadoTest {

    private static final String EMAIL = "herbert.gabriel@souunit.com.br";
    private static final String SENHA = "123456";
    private static final String NOVA_SENHA = "654321";
    private static final String NOME = "Herbert Gabriel";
    private static final String NUMERO = "12345678";
    private static final String TAG = "FluxoCompletoTest";

    private Context context;

    @Before
    public void setUp() {
        Intents.init();
        context = ApplicationProvider.getApplicationContext();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void fluxoCompleto() throws InterruptedException {
        Log.d(TAG, "Iniciando fluxo de Cadastro");
        // 1. Cadastro
        ActivityScenario.launch(CadastroActivity.class);

        Espresso.onView(withId(R.id.NomeCompleto)).perform(ViewActions.replaceText(NOME), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.numero)).perform(ViewActions.replaceText(NUMERO), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.email)).perform(ViewActions.replaceText(EMAIL), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.senha)).perform(ViewActions.replaceText(SENHA), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.confirmarSenha)).perform(ViewActions.replaceText(SENHA), ViewActions.closeSoftKeyboard());

        // Seleciona o primeiro curso válido no spinner (posição 1, pois 0 é "Escolha seu curso")
        Espresso.onView(withId(R.id.spinnerCursos)).perform(ViewActions.click());
        Espresso.onData(Matchers.anything()).atPosition(1).perform(ViewActions.click());

        Espresso.onView(withId(R.id.btnCadastre)).perform(ViewActions.click());

        // Verifica se a Activity de login foi aberta (após cadastro bem-sucedido)
        Espresso.onView(withId(R.id.btnEntrar))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Log.d(TAG, "Cadastro realizado e tela de login exibida");

        // 2. Recuperar Senha
        Log.d(TAG, "Iniciando fluxo de Recuperação de Senha");
        ActivityScenario.launch(RecuperarSenhaActivity.class);

        Espresso.onView(withId(R.id.email)).perform(ViewActions.replaceText(EMAIL), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.btnEnviar)).perform(ViewActions.click());

        // Verifica se o botão foi desabilitado (durante requisição)
        Espresso.onView(withId(R.id.btnEnviar))
                .check(ViewAssertions.matches(ViewMatchers.isNotEnabled()));

        // Aguarda a resposta e verifica se o botão está habilitado novamente (simulação, pode ser necessário IdlingResource)
        Thread.sleep(2000);
        Espresso.onView(withId(R.id.btnEnviar))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()));
        Log.d(TAG, "Recuperação de senha testada");

        // 3. Login
        Log.d(TAG, "Iniciando fluxo de Login");
        ActivityScenario.launch(LoginActivity.class);

        // Preenche e tenta logar até conseguir acessar o feed
        boolean loggedIn = false;
        int tentativas = 0;
        while (!loggedIn && tentativas < 3) {
            tentativas++;
            Espresso.onView(withId(R.id.email)).perform(ViewActions.replaceText(EMAIL), ViewActions.closeSoftKeyboard());
            Espresso.onView(withId(R.id.senha)).perform(ViewActions.replaceText(SENHA), ViewActions.closeSoftKeyboard());
            Espresso.onView(withId(R.id.btnEntrar)).perform(ViewActions.click());
            Thread.sleep(1500); // Aguarda possível reload

            try {
                // Se o feed aparecer, login foi bem-sucedido
                Espresso.onView(withId(R.id.recyclerView))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
                loggedIn = true;
            } catch (Exception e) {
                // Se não, provavelmente voltou para login, tenta de novo
                Log.d(TAG, "Tentativa de login falhou, tentando novamente...");
            }
        }
        if (!loggedIn) {
            throw new AssertionError("Não foi possível fazer login após várias tentativas.");
        }
        Log.d(TAG, "Login realizado e Feed exibido");

        // --- AppBarMenuTest: Logout e limpeza de token ---
        Log.d(TAG, "Testando AppBarMenu (logout e limpeza de token)");
        // Salva um token de teste nas SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        prefs.edit().putString("ACCESS_TOKEN", "token_teste").apply();

        // Abre o menu lateral clicando no ícone do menu
        Espresso.onView(ViewMatchers.withId(R.id.icon_menu)).perform(ViewActions.click());
        // Clica no item "Sair da Conta" do menu de navegação
        Espresso.onView(ViewMatchers.withText("Sair da Conta")).perform(ViewActions.click());
        // Verifica se o token foi removido das SharedPreferences
        String token = prefs.getString("ACCESS_TOKEN", null);
        assertNull(token);
        Log.d(TAG, "Logout e limpeza de token testados com sucesso");

        // --- Fim do AppBarMenuTest ---

        // 4. Login novamente após logout
        Log.d(TAG, "Realizando novo login após logout");
        Espresso.onView(withId(R.id.email)).perform(ViewActions.replaceText(EMAIL), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.senha)).perform(ViewActions.replaceText(SENHA), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.btnEntrar)).perform(ViewActions.click());

        // Aguarda e verifica se está no feed
        Thread.sleep(1500);
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Log.d(TAG, "Novo login realizado e Feed exibido");

        // 5. Perfil - Testa senha menor que 6 caracteres
        Log.d(TAG, "Acessando tela de Perfil para testar senha inválida");
        ActivityScenario.launch(PerfilActivity.class);

        Espresso.onView(withId(R.id.senha)).perform(ViewActions.replaceText("123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.confirmarSenha)).perform(ViewActions.replaceText("123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.btnSalvar)).perform(ViewActions.click());
        // Verifica se o campo senha ainda está visível (erro de validação)
        Espresso.onView(withId(R.id.senha))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Log.d(TAG, "Senha menor que 6 caracteres não permitida");

        // 6. Perfil - Troca senha para 654321
        Log.d(TAG, "Alterando senha para 654321");
        Espresso.onView(withId(R.id.senha)).perform(ViewActions.replaceText(NOVA_SENHA), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.confirmarSenha)).perform(ViewActions.replaceText(NOVA_SENHA), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.btnSalvar)).perform(ViewActions.click());
        Thread.sleep(2000); // Aguarda requisição

        // 7. Perfil - Volta senha para 123456
        Log.d(TAG, "Voltando senha para 123456");
        Espresso.onView(withId(R.id.senha)).perform(ViewActions.replaceText(SENHA), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.confirmarSenha)).perform(ViewActions.replaceText(SENHA), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.btnSalvar)).perform(ViewActions.click());
        Thread.sleep(2000); // Aguarda requisição

        // 8. Inscrição em evento
        Log.d(TAG, "Acessando detalhes do primeiro evento do feed para inscrição");
        ActivityScenario.launch(FeedActivity.class);
        // Clica no botão "Ver detalhes" do primeiro item do RecyclerView
        Espresso.onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.button)));

        // Espera tela de detalhes abrir e tenta inscrever
        Thread.sleep(2000);
        Espresso.onView(withId(R.id.subscribeButton)).perform(ViewActions.click());
        Log.d(TAG, "Tentativa de inscrição realizada");

        // 9. Desinscrição do evento
        Log.d(TAG, "Acessando tela de inscrições para cancelar inscrição");
        ActivityScenario.launch(InscricoesActivity.class);
        Thread.sleep(2000);
        // Clica no botão de cancelar inscrição do primeiro item (ajuste o id se necessário)
        Espresso.onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                MyViewAction.clickChildViewWithId(R.id.button)));
        Log.d(TAG, "Tentativa de desinscrição realizada");
    }
}