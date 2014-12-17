package keep.call.me.DAO;

import java.util.ArrayList;

import keep.call.me.Model.ListagemModel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ListagemDAO {
	public static final String TABELA_LISTAGEM = "Listagem";
	public static final String COLUNA_ID = "id";
	public static final String COLUNA_CIDADE = "cidade";
	public static final String COLUNA_VERSAO = "versao";
	public static final String COLUNA_UF = "uf";
	public static final String COLUNA_LINK = "link";

	public static final String SCRIPT_CRIACAO_TABELA_LISTAGEM = "CREATE TABLE " + TABELA_LISTAGEM + "(" + COLUNA_ID + " INTEGER PRIMARY KEY," + COLUNA_CIDADE + " TEXT," + COLUNA_VERSAO + " TEXT," + COLUNA_UF + " TEXT," + COLUNA_LINK + " TEXT" + ")";

	public static final String SCRIPT_DELECAO_TABELA = "DROP TABLE IF EXISTS " + TABELA_LISTAGEM;

	private SQLiteDatabase dataBase = null;

	private static ListagemDAO instance;

	public static ListagemDAO getInstance(Context context) {
		if (instance == null)
			instance = new ListagemDAO(context);
		return instance;
	}

	private ListagemDAO(Context context) {
		PersistenceHelper persistenceHelper = PersistenceHelper.getInstance(context);
		dataBase = persistenceHelper.getWritableDatabase();
	}

	public void salvar(ArrayList<ListagemModel> objeto) {
		for (ListagemModel elemento : objeto) {
			ContentValues values = gerarValores(elemento);
			dataBase.insert(TABELA_LISTAGEM, null, values);
		}

	}

	public void salvar(ListagemModel objeto) {
		ContentValues values = gerarValores(objeto);
		dataBase.insert(TABELA_LISTAGEM, null, values);

	}

	public ArrayList<ListagemModel> recuperarTodosEmArray() {
		String queryReturnAll = "SELECT * FROM " + TABELA_LISTAGEM;
		Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
		ArrayList<ListagemModel> array = createItem(cursor);

		return array;
	}

	public void deletar(ListagemModel objeto) {
		String[] valoresParaSubstituir = { String.valueOf(objeto.getId()) };
		dataBase.delete(TABELA_LISTAGEM, COLUNA_ID + " =  ?", valoresParaSubstituir);
	}

	public void editar(ListagemModel objeto) {
		ContentValues valores = gerarValores(objeto);

		String[] valoresParaSubstituir = { String.valueOf(objeto.getId()) };

		dataBase.update(TABELA_LISTAGEM, valores, COLUNA_ID + " = ?", valoresParaSubstituir);
	}

	public void fecharConexao() {
		if (dataBase != null && dataBase.isOpen())
			dataBase.close();
	}

	private ArrayList<ListagemModel> createItem(Cursor cursor) {
		ArrayList<ListagemModel> array = new ArrayList<ListagemModel>();
		if (cursor == null)
			return array;

		try {

			if (cursor.moveToFirst()) {
				do {

					int indexID = cursor.getColumnIndex(COLUNA_ID);
					int indexCidade = cursor.getColumnIndex(COLUNA_CIDADE);
					int indexItem = cursor.getColumnIndex(COLUNA_VERSAO);
					int indexUf = cursor.getColumnIndex(COLUNA_UF);
					int indexLink = cursor.getColumnIndex(COLUNA_LINK);

					int id = cursor.getInt(indexID);
					String cidade = cursor.getString(indexCidade);
					int item = cursor.getInt(indexItem);
					String uf = cursor.getString(indexUf);
					String link = cursor.getString(indexLink);

					array.add(new ListagemModel(id, cidade, item, uf, link));

				} while (cursor.moveToNext());
			}

		} finally {
			cursor.close();
		}
		return array;
	}

	private ContentValues gerarValores(ListagemModel objeto) {
		ContentValues values = new ContentValues();
		values.put(COLUNA_CIDADE, objeto.getRegiao());
		values.put(COLUNA_VERSAO, objeto.getVersao());
		values.put(COLUNA_UF, objeto.getUf());
		values.put(COLUNA_LINK, objeto.getLink());

		return values;
	}

	public boolean isBancoVazio() {
		Cursor cur = dataBase.query(TABELA_LISTAGEM, new String[] { COLUNA_ID, COLUNA_CIDADE }, null, null, null, null, null);

		if (cur != null) {
			cur.moveToFirst();

			if (cur.getCount() == 0)
				return true;
		}
		return false;
	}
}
