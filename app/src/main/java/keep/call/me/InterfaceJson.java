package keep.call.me;

import java.util.ArrayList;

import keep.call.me.Model.VersaoModel;

public interface InterfaceJson {
	public void setJsonNaLista(ArrayList<?> arrayList);

	public void setSalvarJson(ArrayList<?> arrayList);

	public boolean isConectado();

	public void onErroDownloadJson();

	public void procedimentos(String link);

	public void checkVersion(VersaoModel result);

}
