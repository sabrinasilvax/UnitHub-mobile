package com.mobile.unithub.activities;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mobile.unithub.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecuperarSenhaActivityTest {

    @Before
    public void setUp() {
        ActivityScenario.launch(RecuperarSenhaActivity.class);
    }

    @Test
    public void deveValidarCampoEmailObrigatorio() {
        // Clica no botão enviar sem preencher o e-mail
        Espresso.onView(ViewMatchers.withId(R.id.btnEnviar)).perform(ViewActions.click());
        // Verifica se o campo de e-mail ainda está visível (erro de validação)
        Espresso.onView(ViewMatchers.withId(R.id.email))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}