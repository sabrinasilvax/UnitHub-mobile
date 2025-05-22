package com.mobile.unithub.activities;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mobile.unithub.activities.RecuperarSenhaActivity;
import com.mobile.unithub.activities.ToastMatcher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.mobile.unithub.R;

@RunWith(AndroidJUnit4.class)
public class RecuperarSenhaActivityTest {

    @Before
    public void setUp() {
        ActivityScenario.launch(RecuperarSenhaActivity.class);
    }

    @Test
    public void deveValidarCampoDeEmail() {
        // Clica no botão sem preencher o e-mail
        Espresso.onView(ViewMatchers.withId(R.id.btnEnviar)).perform(ViewActions.click());
        // Verifica se a mensagem de erro aparece
        Espresso.onView(ViewMatchers.withText("Por favor, insira seu e-mail."))
                .inRoot(new ToastMatcher())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void deveEnviarSolicitacaoDeRecuperacaoDeSenha() {
        // Preenche o campo de e-mail
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText("teste@teste.com"), ViewActions.closeSoftKeyboard());
        // Clica no botão enviar
        Espresso.onView(ViewMatchers.withId(R.id.btnEnviar)).perform(ViewActions.click());
        // Verifica mensagem de sucesso (ajuste conforme o texto real)
        Espresso.onView(ViewMatchers.withText("As instruções de recuperação foram enviadas por e-mail."))
                .inRoot(new ToastMatcher())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void deveExibirMensagemDeErroConformeRespostaDaAPI() {
        // Preenche o campo de e-mail com valor que cause erro na API
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText("erro@teste.com"), ViewActions.closeSoftKeyboard());
        // Clica no botão enviar
        Espresso.onView(ViewMatchers.withId(R.id.btnEnviar)).perform(ViewActions.click());
        // Verifica mensagem de erro (ajuste conforme o texto real)
        Espresso.onView(ViewMatchers.withText("Erro ao enviar e-mail de recuperação."))
                .inRoot(new ToastMatcher())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}