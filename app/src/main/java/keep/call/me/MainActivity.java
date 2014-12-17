package keep.call.me;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import keep.call.me.Adapter.ListagemListAdapter;
import keep.call.me.Controller.ListagemDownloadJson;
import keep.call.me.Controller.VersaoDownloadJson;
import keep.call.me.DAO.ListagemDAO;
import keep.call.me.DAO.TelefoneDAO;
import keep.call.me.Model.Estado;
import keep.call.me.Model.ListagemModel;
import keep.call.me.Model.VersaoModel;

public class MainActivity extends ActionBarActivity implements InterfaceJson, Runnable, OnItemClickListener {
    private ListView lista;

    private String urlDropbox = "https://www.dropbox.com/s/4vg9qf3naxwo9wj/listagem.json?dl=0";
    private String urlVersao = "https://dl.dropboxusercontent.com/u/9889747/Keep%20Call%20Me%20-%20json/versao.json";

    private String PREF_NAME = "KeepCallMeVersao";

    private ListagemDAO dataBaseListagem;
    private TelefoneDAO dataBaseTelefone;

    private ProgressDialog progress;
    public static ProgressBar p = null;

    public static final int VERSAO = 1;

    private SharedPreferences preferences;

    private Editor editor;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lista = (ListView) findViewById(R.id.lista);
        p = (ProgressBar) findViewById(R.id.progress);
        Button button = (Button) findViewById(R.id.button);
/*
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }

        // actionBar = getSupportActionBar();
        // actionBar.setIcon(R.drawable.ic_launcher);
*/
        final String pegou2 = Utils.lerArquivo("json.json", this);

        procedimentos(urlDropbox);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                Estado[] estado = gson.fromJson(pegou2, Estado[].class);
                for (Estado e : estado) {
                    Log.e("teste", ">> " + e.getEstado());
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        procedimentos(urlDropbox);
    }

    @Override
    public void run() {
        lista.setAdapter(new ListagemListAdapter(getApplicationContext(), dataBaseListagem.recuperarTodosEmArray(), getApplicationContext()));

        ((BaseAdapter) lista.getAdapter()).notifyDataSetChanged();

        lista.setOnItemClickListener(this);

        if (p != null && p.getVisibility() == View.VISIBLE) {
            p.setVisibility(View.GONE);
        }
    }

    @Override
    public void setJsonNaLista(ArrayList<?> arrayList) {
        lista.setAdapter(new ListagemListAdapter(getApplicationContext(), arrayList, getApplicationContext()));

        ((BaseAdapter) lista.getAdapter()).notifyDataSetChanged();

        if (p != null && p.getVisibility() == View.VISIBLE) {
            p.setVisibility(View.GONE);
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public void setSalvarJson(ArrayList<?> arrayList) {
        ArrayList<ListagemModel> array = (ArrayList<ListagemModel>) arrayList;

        int tamanhoListagemBaixada = arrayList.size();
        int tamanhoListagemBanco;
        // boolean salvar = false;
        // int i = 0;

        if (!dataBaseListagem.isBancoVazio()) {
            tamanhoListagemBanco = dataBaseListagem.recuperarTodosEmArray().size();
            ArrayList<ListagemModel> doBanco = dataBaseListagem.recuperarTodosEmArray();
            int maior = 0;
            int menor = 0;
            // int contador = 0;

            if (tamanhoListagemBaixada >= tamanhoListagemBanco) {
                maior = tamanhoListagemBaixada;
                menor = tamanhoListagemBanco;
            }

            // int[] valoresParaSalvar = new int[maior];

            // Atualiza itens
            for (int a = 0; a < maior; a++) {
                for (int b = 0; b < menor; b++) {
                    // Encontra as novas vers�es

                    if (array.get(a).getVersao() > doBanco.get(b).getVersao()) {

                        if (dataBaseTelefone.recuperarTodosEmArrayFiltrandoPorUf(array.get(a).getUf()).size() > 0) {

                            dataBaseTelefone.deletaPorUf(array.get(b).getUf());
                        }

                    }

                    // if (array.get(a).getVersao() !=
                    // doBanco.get(b).getVersao()) {
                    // contador++;
                    // if (contador >= b) {
                    // if (array.get(a) != doBanco.get(b)) {
                    // i++;
                    // valoresParaSalvar[i] = a;
                    // salvar = true;
                    // }
                    // }
                    // }

                }

            }

            // int temp = 0;
            // if (salvar) {
            // for (int a = 0; a < maior; a++) {
            // if (valoresParaSalvar[a] > 0) {
            // if (temp != valoresParaSalvar[a]) {
            // dataBaseListagem.salvar(array
            // .get(valoresParaSalvar[a]));
            // Toast.makeText(getApplicationContext(), "NOVO: " +
            // array.get(valoresParaSalvar[a]), Toast.LENGTH_SHORT).show();
            // Log.e("TESTE", "SALVAR = " + array.get(valoresParaSalvar[a]));
            // }
            // }
            // }
            // }

            do {

                for (int i = 0; i < dataBaseListagem.recuperarTodosEmArray().size(); i++) {
                    dataBaseListagem.deletar(dataBaseListagem.recuperarTodosEmArray().get(i));
                }

            } while (dataBaseListagem.recuperarTodosEmArray().size() > 0);

            if (dataBaseListagem.recuperarTodosEmArray().size() == 0) {
                dataBaseListagem.salvar((ArrayList<ListagemModel>) arrayList);
            }

        } else {
            dataBaseListagem.salvar((ArrayList<ListagemModel>) arrayList);
        }

        if (p != null && p.getVisibility() == View.VISIBLE) {
            p.setVisibility(View.GONE);
        }
    }

    @Override
    public void procedimentos(String link) {
        dataBaseListagem = ListagemDAO.getInstance(getApplicationContext());
        dataBaseTelefone = TelefoneDAO.getInstance(getApplicationContext());

        // Banco vazio
        if (dataBaseListagem.isBancoVazio()) {

            // Tem internet
            if (isConectado()) {
                p.setVisibility(View.VISIBLE);
                new VersaoDownloadJson(this).execute(urlVersao);
                // N�o tem internet
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.problema_conexao), Toast.LENGTH_LONG).show();

            }

            // Banco populado
        } else {
            new Thread(this).run();
        }
    }

    @Override
    public void onErroDownloadJson() {
        Toast.makeText(this, getString(R.string.ops_ocorreu_um_erro), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkVersion(VersaoModel result) {
        salvarVersao(result.getVersao());
        p.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String regiao = ((ListagemModel) lista.getItemAtPosition(position)).getRegiao();
        String uf = ((ListagemModel) lista.getItemAtPosition(position)).getUf();
        String link = ((ListagemModel) lista.getItemAtPosition(position)).getLink();

        Intent meuIntent = new Intent(MainActivity.this, Telefones.class);
        meuIntent.putExtra("uf", uf.trim());
        meuIntent.putExtra("regiao", regiao.trim());
        meuIntent.putExtra("link", link.trim());
        startActivity(meuIntent);

        MainActivity.this.overridePendingTransition(R.drawable.push_right_in, R.drawable.push_right_out);
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

    public void click() {
        if (isConectado()) {
            new VersaoDownloadJson(MainActivity.this).execute(urlVersao);
        }
    }

    public void salvarVersao(int versaoOnline) {
        int versaoAtual = 0;

        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        boolean contains = preferences.contains("versao");

        if (contains)
            versaoAtual = preferences.getInt("versao", 1);


        if (versaoAtual >= versaoOnline) {
            Toast.makeText(getApplicationContext(), "Seus dados já estão atualizados.", Toast.LENGTH_SHORT).show();
        } else {
            ListagemDownloadJson downloadJson = new ListagemDownloadJson(this);
            downloadJson.execute(urlDropbox);

            lista.setOnItemClickListener(this);

            editor = preferences.edit();
            editor.putInt("versao", versaoOnline);
            editor.commit();
        }
    }

    public void savePreferences() {

    }

    @Override
    public boolean isConectado() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

	/*
     * @Override public boolean onOptionsItemSelected(MenuItem item) { switch
	 * (item.getItemId()) { case R.id.atualizar: if (isConectado()) { new
	 * VersaoDownloadJson(MainActivity.this).execute(urlVersao); } else {
	 * Toast.makeText(MainActivity.this, getString(R.string.precisa_internet),
	 * Toast.LENGTH_SHORT).show(); } break; case R.id.delete: if
	 * (!dataBaseListagem.isBancoVazio()) { for (int i = 0; i <
	 * dataBaseListagem.recuperarTodosEmArray() .size(); i++) {
	 * dataBaseListagem.deletar(dataBaseListagem
	 * .recuperarTodosEmArray().get(i)); } } if
	 * (!dataBaseTelefone.isBancoVazio()) { for (int i = 0; i <
	 * dataBaseTelefone.recuperarTodosEmArray() .size(); i++) {
	 * dataBaseTelefone.delete(dataBaseTelefone
	 * .recuperarTodosEmArray().get(i)); } }
	 * 
	 * preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
	 * 
	 * editor = preferences.edit(); editor.putInt("versao", 0); editor.commit();
	 * break; default: break; }
	 * 
	 * return super.onOptionsItemSelected(item); }
	 */

}