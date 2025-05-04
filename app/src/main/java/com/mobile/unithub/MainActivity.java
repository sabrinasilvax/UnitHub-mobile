package com.mobile.unithub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.mobile.unithub.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura a Toolbar corretamente (modificado)
        Toolbar toolbar = findViewById(R.id.toolbar); // Acessa diretamente pelo ID
        setSupportActionBar(toolbar);

        // Configuração do Navigation Drawer
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Define os destinos de nível superior
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_seus_dados,
                R.id.nav_notificacoes,
                R.id.nav_inscricoes)
                .setOpenableLayout(drawer)
                .build();

        // Configura o NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);

        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Configura o listener do NavigationView
        navigationView.setNavigationItemSelectedListener(this);

        // Verifica se veio da LoginActivity
        checkIntentExtras();
    }

    private void checkIntentExtras() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ACCESS_TOKEN")) {
            String token = intent.getStringExtra("ACCESS_TOKEN");
            Log.d("MainActivity", "Token recebido: " + token);
            // Aqui você pode usar o token para outras operações
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla o menu da Toolbar (se houver)
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Manipula cliques nos itens da Toolbar
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Manipula cliques nos itens do Navigation Drawer
        if (item.getItemId() == R.id.nav_sair) {
            logout();
            return true;
        }

        // Para outros itens, use o Navigation Component
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    private void logout() {
        // Limpa os dados de autenticação
        getSharedPreferences("AuthPrefs", MODE_PRIVATE).edit().clear().apply();

        // Redireciona para a LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Manipula o botão de navegação (hamburger)
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        // Fecha o drawer se estiver aberto
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}