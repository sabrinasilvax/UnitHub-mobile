package com.mobile.unithub;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mobile.unithub.activities.FeedActivity;
import com.mobile.unithub.activities.LoginActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.intent.Intents.intended;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AppBarMenuTest {

    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        Intents.init();
        // Inicia a FeedActivity que contém o AppBarMenu
        ActivityScenario.launch(FeedActivity.class);
        // Salva um token de teste nas SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        prefs.edit().putString("ACCESS_TOKEN", "token_teste").apply();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void deveAcionarMenuELogoutCorretamente() {
        // Abre o menu lateral clicando no ícone do menu
        Espresso.onView(ViewMatchers.withId(R.id.icon_menu)).perform(ViewActions.click());
        // Clica no item "Sair" do menu de navegação
        Espresso.onView(ViewMatchers.withText("Sair")).perform(ViewActions.click());
        // Verifica se a LoginActivity foi chamada
        intended(IntentMatchers.hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void deveLimparDadosDeAutenticacaoAoSair() {
        // Abre o menu lateral e clica em "Sair"
        Espresso.onView(ViewMatchers.withId(R.id.icon_menu)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Sair")).perform(ViewActions.click());
        // Verifica se o token foi removido das SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("ACCESS_TOKEN", null);
        assertNull(token);
    }
}