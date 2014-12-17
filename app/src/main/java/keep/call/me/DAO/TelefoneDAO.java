package keep.call.me.DAO;

import java.text.Normalizer;
import java.util.ArrayList;

import keep.call.me.Model.TelefoneModel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TelefoneDAO {
	public static final String TABELA_TELEFONES = "Telefone";
	public static final String COLUNA_ID = "id";
	public static final String COLUNA_DDD = "ddd";
	public static final String COLUNA_CIDADE = "cidade";
	public static final String COLUNA_UF = "uf";
	public static final String COLUNA_REGIAO = "regiao";

	private int id;
	private String ddd;
	private String cidade;
	private String uf;
	private String regiao;

	public static final String SCRIPT_CRIACAO_TABELA_TELEFONES = "CREATE TABLE " + TABELA_TELEFONES + " (" + COLUNA_ID + " INTEGER PRIMARY KEY, " + COLUNA_DDD + " TEXT, " + COLUNA_CIDADE + " TEXT," + COLUNA_UF + " TEXT," + COLUNA_REGIAO + " TEXT)";

	public static final String SCRIPT_DELECAO_TABELA = "DROP TABLE IF EXISTS " + TABELA_TELEFONES;

	private SQLiteDatabase dataBase = null;

	private static TelefoneDAO instance;

	public static TelefoneDAO getInstance(Context context) {
		if (instance == null)
			instance = new TelefoneDAO(context);
		return instance;
	}

	private TelefoneDAO(Context context) {
		PersistenceHelper persistenceHelper = PersistenceHelper.getInstance(context);
		dataBase = persistenceHelper.getWritableDatabase();
	}

	public void insert(ArrayList<TelefoneModel> telefone) {
		for (TelefoneModel elemento : telefone) {
			ContentValues values = gerarValoresDeTelefones(elemento);
			dataBase.insert(TABELA_TELEFONES, null, values);
		}

	}

	public void insert(TelefoneModel telefone) {
		ContentValues values = gerarValoresDeTelefones(telefone);
		dataBase.insert(TABELA_TELEFONES, null, values);

	}

	public ArrayList<TelefoneModel> recuperarTodosEmArray() {
		String queryReturnAll = "SELECT * FROM " + TABELA_TELEFONES;
		Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
		ArrayList<TelefoneModel> array = construirPorCursor(cursor);

		return array;
	}

	public ArrayList<TelefoneModel> recuperarTodosEmArrayFiltrandoPorUf(String ufDesejado) {
		String queryReturnAll = "SELECT * FROM " + TABELA_TELEFONES + " WHERE " + COLUNA_UF + " = " + "'" + ufDesejado + "';";
		Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
		ArrayList<TelefoneModel> array = construirPorCursor(cursor);

		return array;
	}

	public ArrayList<TelefoneModel> pesquisar(String texto, String ufDesejado) {
		texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
		texto = texto.replaceAll("[^\\p{ASCII}]", "");

		String queryReturnAll = "SELECT * FROM " + TABELA_TELEFONES + " WHERE " + COLUNA_CIDADE + " LIKE " + "'" + texto + "%'" + " AND " + COLUNA_UF + " = " + "'" + ufDesejado + "';";

		Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
		ArrayList<TelefoneModel> array = construirPorCursor(cursor);
		return array;
	}

	public ArrayList<TelefoneModel> pesquisar(String texto) {
		texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
		texto = texto.replaceAll("[^\\p{ASCII}]", "");

		String queryReturnAll = "SELECT * FROM " + TABELA_TELEFONES + " WHERE " + COLUNA_CIDADE + " LIKE " + "'" + texto + "%'";

		Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
		ArrayList<TelefoneModel> array = construirPorCursor(cursor);
		return array;
	}

	public ArrayList<TelefoneModel> getCidadesArrayListByUf(String ufDesejado) {
		String query = "SELECT * FROM " + TABELA_TELEFONES + " WHERE " + COLUNA_UF + " = " + "'" + ufDesejado + "';";

		ArrayList<TelefoneModel> arrayListTelefonesConstruidos = construirPorCursor(dataBase.rawQuery(query, null));

		return arrayListTelefonesConstruidos;
	}

	public ArrayList<TelefoneModel> getTelefoneModelByCidade(String cidade) {
		String query = "SELECT " + COLUNA_ID + " FROM " + TABELA_TELEFONES + " WHERE " + COLUNA_CIDADE + " = " + cidade;

		ArrayList<TelefoneModel> arrayListTelefonesConstruidos = construirPorCursor(dataBase.rawQuery(query, null));

		return arrayListTelefonesConstruidos;
	}

	public void delete(TelefoneModel telefone) {

		String[] valoresParaSubstituir = { String.valueOf(telefone.getId()) };
		dataBase.delete(TABELA_TELEFONES, COLUNA_ID + " =  ?", valoresParaSubstituir);
	}

	public void deletaPorUf(String uf) {
		String[] valoresParaSubstituir = { uf };
		// dataBase.delete(TABELA_TELEFONES, COLUNA_UF + " =  ?",
		// valoresParaSubstituir);
		dataBase.delete(TABELA_TELEFONES, COLUNA_UF + "=" + "'" + uf + "'", null);

	}

	public void edit(TelefoneModel telefone) {
		ContentValues valores = gerarValoresDeTelefones(telefone);

		String[] valoresParaSubstituir = { String.valueOf(telefone.getId()) };

		dataBase.update(TABELA_TELEFONES, valores, COLUNA_ID + " = ?", valoresParaSubstituir);
	}

	private ArrayList<TelefoneModel> construirPorCursor(Cursor cursor) {

		ArrayList<TelefoneModel> array = new ArrayList<TelefoneModel>();
		if (cursor == null) {
			return array;
		}

		try {

			if (cursor.moveToFirst()) {
				do {

					id = cursor.getInt(cursor.getColumnIndex(COLUNA_ID));
					cidade = cursor.getString(cursor.getColumnIndex(COLUNA_CIDADE));
					ddd = cursor.getString(cursor.getColumnIndex(COLUNA_DDD));
					uf = cursor.getString(cursor.getColumnIndex(COLUNA_UF));
					regiao = cursor.getString(cursor.getColumnIndex(COLUNA_REGIAO));

					array.add(new TelefoneModel(id, cidade, ddd, uf, regiao));

				} while (cursor.moveToNext());
			}

		} finally {
			cursor.close();
		}
		return array;
	}

	private ContentValues gerarValoresDeTelefones(TelefoneModel telefone) {
		ContentValues values = new ContentValues();
		values.put(COLUNA_CIDADE, telefone.getCidade());
		values.put(COLUNA_DDD, telefone.getDdd());
		values.put(COLUNA_UF, telefone.getUf());
		values.put(COLUNA_REGIAO, telefone.getRegiao());

		return values;
	}

	public void fecharConexao() {
		if (dataBase != null && dataBase.isOpen())
			dataBase.close();
	}

	public boolean isBancoVazio() {
		Cursor cur = dataBase.query(TABELA_TELEFONES, new String[] { COLUNA_ID, COLUNA_DDD, COLUNA_CIDADE, COLUNA_UF, COLUNA_REGIAO }, null, null, null, null, null);

		if (cur != null) {
			cur.moveToFirst();

			if (cur.getCount() == 0)
				return true;
		}
		return false;
	}
}