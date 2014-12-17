package keep.call.me.DAO;

import keep.call.me.MainActivity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PersistenceHelper extends SQLiteOpenHelper {
	public static final String NOME_BANCO = "BancoKeepCallMe";

	private static PersistenceHelper instance;

	private PersistenceHelper(Context context) {
		super(context, NOME_BANCO, null, MainActivity.VERSAO);
	}

	public static PersistenceHelper getInstance(Context context) {
		if (instance == null)
			instance = new PersistenceHelper(context);
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(ListagemDAO.SCRIPT_CRIACAO_TABELA_LISTAGEM);
		db.execSQL(TelefoneDAO.SCRIPT_CRIACAO_TABELA_TELEFONES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL(ListagemDAO.SCRIPT_DELECAO_TABELA);
		db.execSQL(TelefoneDAO.SCRIPT_DELECAO_TABELA);
		onCreate(db);
	}

}
