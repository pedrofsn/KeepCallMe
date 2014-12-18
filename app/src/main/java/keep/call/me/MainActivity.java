package keep.call.me;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import keep.call.me.model.Estado;

public class MainActivity extends ActionBarActivity {

    private ListView lista;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lista = (ListView) findViewById(R.id.lista);
        progress = (ProgressBar) findViewById(R.id.progress);

        final String pegou2 = Utils.lerArquivo("json.json", this);

        Gson gson = new Gson();
        Estado[] estado = gson.fromJson(pegou2, Estado[].class);
        for (Estado e : estado) {
            Log.e("teste", ">> " + e.getEstado());
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
/*
        getMenuInflater().inflate(R.menu.menu_listagem, menu);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.pesquisar_cidade));

		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			int atual = 0;
			int antigo = 0;

			@Override
			public boolean onQueryTextChange(String texto) {
				antigo = atual;
				if (texto != null && texto.length() >= 0) {
					if (!dataBaseTelefone.isBancoVazio()) {
						lista.setAdapter(new TelefoneListAdapter(MainActivity.this, dataBaseTelefone.pesquisar(texto)));
						atual = texto.length();

						if (p != null && p.getVisibility() == View.VISIBLE) {
							p.setVisibility(View.GONE);
						}
					}

					if (antigo <= antigo && texto.length() <= 1) {
						if (!dataBaseListagem.isBancoVazio())
							lista.setAdapter(new ListagemListAdapter(getApplicationContext(), dataBaseListagem.recuperarTodosEmArray(), getApplicationContext()));

						if (p != null && p.getVisibility() == View.VISIBLE) {
							p.setVisibility(View.GONE);
						}
					}

				}

				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String texto) {

				return false;
			}
		});
*/
        return super.onCreateOptionsMenu(menu);
    }


}