package com.example.prova_mobol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private LinearLayout ly1, ly2, ly3, ly4, ly5, ly6, ly7, ly8, ly9;
    private Button btnMesa1, btnMesa2, btnMesa3, btnMesa4, btnMesa5, btnMesa6, btnMesa7, btnMesa8, btnMesa9;
    private Button btnSair, btnLiberarMesa, btnReservarTodasMesas, btnSalvarOperacao, btnConfiguracao;
    private EditText editNumeroMesa;

    String corLivre, corOcupado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("props", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String ultimasMesasReservadas = sharedPreferences.getString("mesas_reservadas", "");

        editNumeroMesa = findViewById(R.id.editNumeroMesa);

        corLivre = sharedPreferences.getString("corLivre", "Azul");
        corOcupado = sharedPreferences.getString("corOcupado", "Vermelho");

        ly1 = findViewById(R.id.ly1);
        ly2 = findViewById(R.id.ly2);
        ly3 = findViewById(R.id.ly3);
        ly4 = findViewById(R.id.ly4);
        ly5 = findViewById(R.id.ly5);
        ly6 = findViewById(R.id.ly6);
        ly7 = findViewById(R.id.ly7);
        ly8 = findViewById(R.id.ly8);
        ly9 = findViewById(R.id.ly9);

        btnMesa1 = findViewById(R.id.mesa1);
        btnMesa2 = findViewById(R.id.mesa2);
        btnMesa3 = findViewById(R.id.mesa3);
        btnMesa4 = findViewById(R.id.mesa4);
        btnMesa5 = findViewById(R.id.mesa5);
        btnMesa6 = findViewById(R.id.mesa6);
        btnMesa7 = findViewById(R.id.mesa7);
        btnMesa8 = findViewById(R.id.mesa8);
        btnMesa9 = findViewById(R.id.mesa9);

        btnLiberarMesa = findViewById(R.id.btnLiberarMesa);
        btnSalvarOperacao = findViewById(R.id.btnSalvarOperacao);
        btnReservarTodasMesas = findViewById(R.id.btnReservarTodasMesas);
        btnConfiguracao = findViewById(R.id.btnConfiguracao);
        btnSair = findViewById(R.id.btnSair);

        ArrayList<String> mesasReservadas = new ArrayList<String>();
        Map<Button, LinearLayout> mapaLayouts = new HashMap<Button, LinearLayout>();

        mapaLayouts.put(btnMesa1, ly1);
        mapaLayouts.put(btnMesa2, ly2);
        mapaLayouts.put(btnMesa3, ly3);
        mapaLayouts.put(btnMesa4, ly4);
        mapaLayouts.put(btnMesa5, ly5);
        mapaLayouts.put(btnMesa6, ly6);
        mapaLayouts.put(btnMesa7, ly7);
        mapaLayouts.put(btnMesa8, ly8);
        mapaLayouts.put(btnMesa9, ly9);

        Intent it = new Intent(MainActivity.this, LoginActivity.class);

        for (Button btn : mapaLayouts.keySet()) {
            LinearLayout ly = mapaLayouts.get(btn);
            int cor = ContextCompat.getColor(getApplicationContext(), getCor(corLivre));
            ly.setBackgroundColor(cor);
        }

        if (!ultimasMesasReservadas.isEmpty()) {
            String[] mesasReservadasArray = ultimasMesasReservadas.split(",");
            for (String mesa : mesasReservadasArray) {
                for (Button btn : mapaLayouts.keySet()) {
                    LinearLayout ly = mapaLayouts.get(btn);
                    if (mesa.equals(getResources().getResourceEntryName(ly.getId()))) {
                        int cor = ContextCompat.getColor(getApplicationContext(), getCor(corOcupado));
                        ly.setBackgroundColor(cor);
                        btn.setEnabled(false);
                        mesasReservadas.add(mesa);
                    }
                }
            }
        }

        for (Button btn : mapaLayouts.keySet()) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout ly = mapaLayouts.get(btn);
                    int cor = ContextCompat.getColor(getApplicationContext(), getCor(corOcupado));
                    ly.setBackgroundColor(cor);
                    mesasReservadas.add(getResources().getResourceEntryName(ly.getId()));

                    btn.setEnabled(false);
                }
            });
        }

        btnLiberarMesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numeroMesa = editNumeroMesa.getText().toString();
                if (numeroMesa.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Campo número mesa precisa estar preenchido", Toast.LENGTH_SHORT).show();
                    return;
                }

                int indexMesa = mesasReservadas.indexOf("ly" + numeroMesa);

                if (indexMesa == -1) {
                    Toast.makeText(MainActivity.this, "Mesa não reservada. A mesa " + numeroMesa + " encontra-se habilitada para reserva", Toast.LENGTH_LONG).show();
                    return;
                }

                for (Button btn : mapaLayouts.keySet()) {
                    LinearLayout ly = mapaLayouts.get(btn);
                    if (getResources().getResourceEntryName(ly.getId()).equals("ly" + numeroMesa)) {
                        int cor = ContextCompat.getColor(getApplicationContext(), getCor(corLivre));
                        ly.setBackgroundColor(cor);
                        mesasReservadas.remove(getResources().getResourceEntryName(ly.getId()));
                        btn.setEnabled(true);
                    }
                }

            }
        });

        btnSalvarOperacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String mesaReservada : mesasReservadas) {
                    stringBuilder.append(mesaReservada).append(",");
                }
                editor.putString("mesas_reservadas", stringBuilder.toString());
                editor.putString("corLivre", corLivre);
                editor.putString("corOcupado", corOcupado);
                editor.apply();
            }
        });

        btnReservarTodasMesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mesasReservadas.size() == 9) {
                    Toast.makeText(MainActivity.this, "Operação inválida. Todas as mesas já possuem reserva", Toast.LENGTH_LONG).show();
                    return;
                }
                for (Button btn : mapaLayouts.keySet()) {
                    LinearLayout ly = mapaLayouts.get(btn);
                    int corVermelha = ContextCompat.getColor(getApplicationContext(), getCor(corOcupado));
                    ly.setBackgroundColor(corVermelha);
                    mesasReservadas.add(getResources().getResourceEntryName(ly.getId()));
                    btn.setEnabled(false);
                }
            }
        });

        btnConfiguracao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] colors = {"Verde / Vermelho", "Azul Escuro / Laranja", "Rosa / Amarelo"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Escolha o par de cores, \nDisponivel / Ocupado");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            corLivre = "Verde";
                            corOcupado = "Vermelho";

                            for (Button btn : mapaLayouts.keySet()) {
                                LinearLayout ly = mapaLayouts.get(btn);
                                int cor = ContextCompat.getColor(getApplicationContext(), getCor(corLivre));
                                ly.setBackgroundColor(cor);
                            }

                            for (String mesa : mesasReservadas) {
                                for (Button btn : mapaLayouts.keySet()) {
                                    LinearLayout ly = mapaLayouts.get(btn);
                                    if (mesa.equals(getResources().getResourceEntryName(ly.getId()))) {
                                        int cor = ContextCompat.getColor(getApplicationContext(), getCor(corOcupado));
                                        ly.setBackgroundColor(cor);
                                    }
                                }
                            }
                        } else if (which == 1) {
                            corLivre = "Azul Escuro";
                            corOcupado = "Laranja";

                            for (Button btn : mapaLayouts.keySet()) {
                                LinearLayout ly = mapaLayouts.get(btn);
                                int cor = ContextCompat.getColor(getApplicationContext(), getCor(corLivre));
                                ly.setBackgroundColor(cor);
                            }

                            for (String mesa : mesasReservadas) {
                                for (Button btn : mapaLayouts.keySet()) {
                                    LinearLayout ly = mapaLayouts.get(btn);
                                    if (mesa.equals(getResources().getResourceEntryName(ly.getId()))) {
                                        int cor = ContextCompat.getColor(getApplicationContext(), getCor(corOcupado));
                                        ly.setBackgroundColor(cor);
                                    }
                                }
                            }
                        } else if (which == 2) {
                            corLivre = "Rosa";
                            corOcupado = "Amarelo";

                            for (Button btn : mapaLayouts.keySet()) {
                                LinearLayout ly = mapaLayouts.get(btn);
                                int cor = ContextCompat.getColor(getApplicationContext(), getCor(corLivre));
                                ly.setBackgroundColor(cor);
                            }

                            for (String mesa : mesasReservadas) {
                                for (Button btn : mapaLayouts.keySet()) {
                                    LinearLayout ly = mapaLayouts.get(btn);
                                    if (mesa.equals(getResources().getResourceEntryName(ly.getId()))) {
                                        int cor = ContextCompat.getColor(getApplicationContext(), getCor(corOcupado));
                                        ly.setBackgroundColor(cor);
                                    }
                                }
                            }
                        }
                    }
                });
                builder.show();
            }
        });

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.apply();
                startActivity(it);
            }
        });
    }

    public int getCor(String cor) {
        switch(cor) {
            case "Azul":
                return R.color.blue;
            case "Azul Escuro":
                return R.color.darkBlue;
            case "Vermelho":
                return R.color.red;
            case "Laranja":
                return R.color.orange;
            case "Verde":
                return R.color.green;
            case "Rosa":
                return R.color.pink;
            case "Amarelo":
                return R.color.yellow;
            default:
                return R.color.purple;
        }
    }

}