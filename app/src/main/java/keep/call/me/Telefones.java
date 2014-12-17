package keep.call.me;

import java.util.ArrayList;

import keep.call.me.Adapter.TelefoneListAdapter;
import keep.call.me.Controller.TelefoneDownloadJson;
import keep.call.me.DAO.TelefoneDAO;
import keep.call.me.Model.TelefoneModel;
import keep.call.me.Model.VersaoModel;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class Telefones extends ActionBarActivity implements InterfaceJson, Runnable, OnItemClickListener {
	private ListView lista;
	static String dddRecebido;
	private String linkRecebido;
	private String regiaoRecebido;
	private String ufRecebido;
	private TelefoneDAO dataBase;
	private TelefoneListAdapter adapterLista;
	private ActionBar actionBar;
	private ProgressBar p;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		//actionBar.setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
		// actionBar.setIcon(R.drawable.ic_launcher);

		p = (ProgressBar) findViewById(R.id.progress);

		getCoisas();

		lista = (ListView) findViewById(R.id.lista);
 
		procedimentos(linkRecebido);

	}

	@Override
	public void run() {
		adapterLista = new TelefoneListAdapter(getApplicationContext(), dataBase.getCidadesArrayListByUf(ufRecebido));
		lista.setOnItemClickListener(this); 
		lista.setAdapter(adapterLista);
		if (p != null && p.getVisibility() == View.VISIBLE) {
			p.setVisibility(View.GONE);
		}
	}

	@Override
	public void setJsonNaLista(ArrayList<?> arrayList) {
		adapterLista = new TelefoneListAdapter(getApplicationContext(), arrayList);
		lista.setOnItemClickListener(this);
		lista.setAdapter(adapterLista);
		if (p != null && p.getVisibility() == View.VISIBLE) {
			p.setVisibility(View.GONE);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setSalvarJson(ArrayList<?> arrayList) {
		// ArrayList<TelefoneModel> arrayTemp = new ArrayList<TelefoneModel>();
		for (int i = 0; i < arrayList.size(); i++) {
			((TelefoneModel) arrayList.get(i)).setUf(ufRecebido);
			((TelefoneModel) arrayList.get(i)).setRegiao(regiaoRecebido);
			// arrayTemp.add((TelefoneModel) arrayList.get(i));
		}
		dataBase.insert((ArrayList<TelefoneModel>) arrayList); // arrayTemp);
	}

	@Override
	public void onErroDownloadJson() {
		finish();
		Toast.makeText(getApplicationContext(), getString(R.string.problema_link), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void checkVersion(VersaoModel result) {
	}

	@Override
	public void procedimentos(String link) {

		// alertDialog = new AlertDialog.Builder(this).create();

		dataBase = TelefoneDAO.getInstance(getApplicationContext());
		// Tem Internet
		if (isConectado()) {

			// N�o tem este DDD no banco
			if (dataBase.getCidadesArrayListByUf(ufRecebido).size() <= 0) {
				new TelefoneDownloadJson(this).execute(link);
			} else {
				// Tem o DDD no banco
				new Thread(this).run();
			}
		}
		// N�o tem internet e nem o DDD no banco / N�o tem nada
		else if (dataBase.getCidadesArrayListByUf(ufRecebido).size() > 0) {
			// N�o tem internet e tem o DDD no banco
			new Thread(this).run();

		} else {
			Toast.makeText(getApplicationContext(), getString(R.string.problema_conexao), Toast.LENGTH_LONG).show();
			finish();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		dddRecebido = ((TelefoneModel) lista.getItemAtPosition(position)).getDdd();
		//
		// alertDialog.setTitle(getString(R.string.efetuar_ligacao));
		// alertDialog.setMessage(getString(R.string.mensagem_dialog));
		// alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
		// getString(R.string.inserir_numero), new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		//
		// }
		// });
		//
		// alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
		// getString(R.string.lista_de_contatos), new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		//
		// }
		// });
		//
		// // Set the Icon for the Dialog
		// alertDialog.setIcon(R.drawable.ic_device_access_call);
		// alertDialog.show();

        FragmentManager fm = getSupportFragmentManager();
		Dialog editNameDialog = new Dialog();
		editNameDialog.show(fm, "fragment_edit_name");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			Uri uri = data.getData();

			if (uri != null) {
				Cursor c = null;
				try {
					c = getContentResolver().query(uri, new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE }, null, null, null);

					if (c != null && c.moveToFirst()) {
						String number = c.getString(0);
						int type = c.getInt(1);
						showSelectedNumber(type, number);
					}
				} finally {
					if (c != null) {
						c.close();
					}
				}
			}
		}
	}

	public void showSelectedNumber(int type, final String number) {
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:".concat(dddRecebido).concat(number)));
		startActivity(callIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu_telefones, menu);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			int atual = 0;
			int antigo = 0;

			@Override
			public boolean onQueryTextChange(String texto) {
				antigo = atual;
				if (texto != null && texto.length() >= 0) {
					if (!dataBase.isBancoVazio()) {
						lista.setAdapter(new TelefoneListAdapter(Telefones.this, dataBase.pesquisar(texto, ufRecebido)));
						atual = texto.length();
					}

					if (antigo <= antigo && texto.length() <= 1) {
						if (!dataBase.isBancoVazio())
							lista.setAdapter(new TelefoneListAdapter(getApplicationContext(), dataBase.recuperarTodosEmArrayFiltrandoPorUf(ufRecebido)));
					}

				}

				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String texto) {

				return false;
			}
		});

		return super.onCreateOptionsMenu(menu);
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

	public void getCoisas() {
		Intent receptor = getIntent();
		if (receptor != null) {
			ufRecebido = receptor.getStringExtra("uf");
			regiaoRecebido = receptor.getStringExtra("regiao");
			linkRecebido = receptor.getStringExtra("link");
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Telefones.this.overridePendingTransition(R.drawable.push_right_out, R.drawable.push_right_in);
	}
}