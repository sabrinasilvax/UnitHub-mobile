package com.mobile.unithub.activities;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.mobile.unithub.R;
import com.mobile.unithub.activities.ToastMatcher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CadastroActivityTest {

    @Before
    public void setUp() {
        ActivityScenario.launch(CadastroActivity.class);
    }

    @Test
    public void deveValidarTodosOsCamposObrigatorios() {
        Espresso.onView(ViewMatchers.withId(R.id.btnCadastre)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Digite seu nome completo"))
                .inRoot(new ToastMatcher())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void deveExibirErroSeAsSenhasNaoCoincidirem() {
        Espresso.onView(ViewMatchers.withId(R.id.NomeCompleto)).perform(ViewActions.replaceText("Usuário Teste"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.numero)).perform(ViewActions.replaceText("12345"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.replaceText("teste@teste.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.senha)).perform(ViewActions.replaceText("senha123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.confirmarSenha)).perform(ViewActions.replaceText("senha456"), ViewActions.closeSoftKeyboard());
        // Se necessário, selecione um curso aqui
        Espresso.onView(ViewMatchers.withId(R.id.btnCadastre)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("As senhas não coincidem"))
                .inRoot(new ToastMatcher())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void deveCadastrarUsuarioComDadosValidos() {
        // Preenche todos os campos corretamente
        Espresso.onView(ViewMatchers.withId(R.id.NomeCompleto)).perform(ViewActions.typeText("Usuário Teste"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.numero)).perform(ViewActions.typeText("12345"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText("teste@teste.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.senha)).perform(ViewActions.typeText("senha123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.confirmarSenha)).perform(ViewActions.typeText("senha123"), ViewActions.closeSoftKeyboard());
        // Simula seleção de curso (ajuste conforme necessário)
        // Espresso.onView(ViewMatchers.withId(R.id.spinnerCursos)).perform(...);
        Espresso.onView(ViewMatchers.withId(R.id.btnCadastre)).perform(ViewActions.click());

        // Verifica mensagem de sucesso (ajuste conforme o texto real)
        Espresso.onView(ViewMatchers.withText("Cadastro realizado com sucesso! Agora faça o login."))
                .inRoot(new ToastMatcher())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void deveExibirMensagemDeErroEmCasoDeFalhaNoCadastro() {
        // Preencha os campos com dados que causem erro na API (exemplo: e-mail já cadastrado)
        Espresso.onView(ViewMatchers.withId(R.id.NomeCompleto)).perform(ViewActions.typeText("Usuário Teste"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.numero)).perform(ViewActions.typeText("12345"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText("emailja@cadastrado.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.senha)).perform(ViewActions.typeText("senha123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.confirmarSenha)).perform(ViewActions.typeText("senha123"), ViewActions.closeSoftKeyboard());
        // Simula seleção de curso (ajuste conforme necessário)
        // Espresso.onView(ViewMatchers.withId(R.id.spinnerCursos)).perform(...);
        Espresso.onView(ViewMatchers.withId(R.id.btnCadastre)).perform(ViewActions.click());

        // Verifica mensagem de erro (ajuste conforme o texto real)
        Espresso.onView(ViewMatchers.withText("Falha ao realizar cadastro"))
                .inRoot(new ToastMatcher())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}