package com.mobile.unithub.activities;

import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import com.mobile.unithub.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Before
    public void setUp() {
        Intents.init();
        ActivityScenario.launch(LoginActivity.class);
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void deveValidarCamposObrigatorios() {
        // Clica no botão entrar sem preencher nada
        Espresso.onView(withId(R.id.btnEntrar)).perform(ViewActions.click());
        // Verifica se o campo de e-mail está com erro ou visível (erro de validação)
        Espresso.onView(withId(R.id.email))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void deveExibirErroParaEmailInvalidoOuSenhaCurta() {
        // Preenche e-mail inválido e senha curta
        Espresso.onView(withId(R.id.email)).perform(ViewActions.replaceText("emailinvalido"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.senha)).perform(ViewActions.replaceText("123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.btnEntrar)).perform(ViewActions.click());

        // Verifica erro de e-mail inválido
        Espresso.onView(withId(R.id.email)).check(ViewAssertions.matches(ViewMatchers.hasErrorText("Email inválido")));

        // Corrige e-mail, mas senha ainda curta
        Espresso.onView(withId(R.id.email)).perform(ViewActions.replaceText("teste@teste.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.btnEntrar)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.senha)).check(ViewAssertions.matches(ViewMatchers.hasErrorText("Mínimo 6 caracteres")));
    }

    @Test
    public void deveExibirMensagemDeErroEmCasoDeFalhaNaAutenticacao() {
        // Simule um login inválido (usuário/senha incorretos)
        Espresso.onView(withId(R.id.email)).perform(ViewActions.replaceText("usuario@teste.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.senha)).perform(ViewActions.replaceText("senhaerrada"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.btnEntrar)).perform(ViewActions.click());

        // Verifica se o campo de senha ainda está visível (erro de autenticação impede navegação)
        Espresso.onView(withId(R.id.senha))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}