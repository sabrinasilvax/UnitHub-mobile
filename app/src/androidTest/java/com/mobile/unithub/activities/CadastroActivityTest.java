package com.mobile.unithub.activities;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.mobile.unithub.R;

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
        // Verifica se o campo de nome ainda está visível (erro de validação impede navegação)
        Espresso.onView(ViewMatchers.withId(R.id.NomeCompleto))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void deveExibirErroSeAsSenhasNaoCoincidirem() {
        Espresso.onView(ViewMatchers.withId(R.id.NomeCompleto)).perform(ViewActions.replaceText("Usuário Teste"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.numero)).perform(ViewActions.replaceText("12345678"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.replaceText("teste@teste.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.senha)).perform(ViewActions.replaceText("senha123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.confirmarSenha)).perform(ViewActions.replaceText("senha456"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.btnCadastre)).perform(ViewActions.click());
        // Verifica se o campo de confirmação de senha ainda está visível (erro de validação)
        Espresso.onView(ViewMatchers.withId(R.id.confirmarSenha))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}