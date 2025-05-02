package com.mobile.unithub;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.mobile.unithub.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configuração da Toolbar
        setSupportActionBar(binding.navView);

        // Inicialização dos componentes
        drawer = binding.drawerLayout;
        navigationView = binding.navView;

        // Configuração dos itens de navegação
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_seus_dados,
                R.id.nav_notificacoes,
                R.id.nav_inscricoes)
                .setOpenableLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        // Configuração da navegação
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Configuração do listener personalizado para o item "Sair"
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_sair) {
                handleLogout();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            return NavigationUI.onNavDestinationSelected(item, navController);
        });

        // Configura o ícone de menu (hamburger) na Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menuu);
        }
    }

    private void setSupportActionBar(NavigationView navView) {
    }

    private void handleLogout() {
        // Implemente sua lógica de logout aqui
        Toast.makeText(this, "Saindo da conta...", Toast.LENGTH_SHORT).show();
        // Exemplo: startActivity(new Intent(this, LoginActivity.class));
        // finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla o menu de opções da Toolbar (se necessário)
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Manipula o clique no ícone de navegação (hamburger ou seta)
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        // Fecha o drawer se estiver aberto
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}