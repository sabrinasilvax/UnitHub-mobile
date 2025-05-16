package com.mobile.unithub.components;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;
import com.mobile.unithub.R;
import com.mobile.unithub.activities.FeedActivity;
import com.mobile.unithub.activities.InscricoesActivity;
import com.mobile.unithub.activities.LoginActivity;
import com.mobile.unithub.activities.PerfilActivity;

public class AppBarMenu extends Toolbar {

    public AppBarMenu(Context context) {
        super(context);
        init(context);
    }

    public AppBarMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AppBarMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.app_bar_main, this, true);
        // Troca o ícone do menu para o correto
        ImageView iconMenu = findViewById(R.id.icon_menu);
        if (iconMenu != null) {
            iconMenu.setImageResource(R.drawable.ic_menuu);
        }
    }

    public void attachDrawer(final DrawerLayout drawerLayout, final Context context) {
        ImageView iconMenu = findViewById(R.id.icon_menu);
        if (iconMenu != null && drawerLayout != null) {
            iconMenu.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        final NavigationView navView = drawerLayout.getRootView().findViewById(R.id.nav_view);
        if (navView != null) {
            navView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                drawerLayout.closeDrawers();

                if (id == R.id.nav_seusdados) {
                    context.startActivity(new Intent(context, PerfilActivity.class));
                } else if (id == R.id.nav_feed) {
                    context.startActivity(new Intent(context, FeedActivity.class));
                } else if (id == R.id.nav_inscricoes) {
                    context.startActivity(new Intent(context, InscricoesActivity.class));
                } else if (id == R.id.nav_sair) {
                    // Remover token do SharedPreferences correto
                    SharedPreferences prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
                    // Ir para LoginActivity e limpar a pilha
                    prefs.edit().clear().apply();

                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
                return true;
            });
        }
    }
}
