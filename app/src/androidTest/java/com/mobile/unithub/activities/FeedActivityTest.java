package com.mobile.unithub.activities;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mobile.unithub.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FeedActivityTest {

    @Before
    public void setUp() {
        ActivityScenario.launch(FeedActivity.class);
    }

    @Test
    public void deveCarregarEExibirListaDeEventos() {
        // Aguarda o carregamento e verifica se o RecyclerView está visível
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        // Opcional: Verifica se há pelo menos um item na lista
        // Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
        //         .check(new RecyclerViewItemCountAssertion(1));
    }

    @Test
    public void deveExibirMensagemSeNaoHouverEventos() {
        // Simule a resposta da API sem eventos (mock ou ambiente de teste)
        // Verifica se a mensagem aparece
        Espresso.onView(ViewMatchers.withText("Nenhuma publicação encontrada."))
                .inRoot(new ToastMatcher())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void deveTratarFalhasDeConexao() {
        // Simule falha de conexão (ex: desligue a internet ou use mock)
        // Verifica se a mensagem de erro aparece
        Espresso.onView(ViewMatchers.withText("Erro ao carregar o feed:"))
                .inRoot(new ToastMatcher())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}