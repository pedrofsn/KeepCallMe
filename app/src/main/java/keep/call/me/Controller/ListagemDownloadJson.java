package keep.call.me.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import keep.call.me.InterfaceJson;
import keep.call.me.MainActivity;
import keep.call.me.Model.ListagemModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ListagemDownloadJson extends AsyncTask<String, Void, ArrayList<ListagemModel>> {

	private Context ctx;
	private int status;

	public ListagemDownloadJson(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		MainActivity.p.setVisibility(View.VISIBLE);
	}

	@Override
	protected ArrayList<ListagemModel> doInBackground(String... params) {

		return postData(params[0]);
	}

	@Override
	protected void onPostExecute(ArrayList<ListagemModel> result) {
		super.onPostExecute(result);

		InterfaceJson acessoJson = (InterfaceJson) ctx;

		if (status == 200) {
			Log.e("TESTE", "" + result.size());
			MainActivity.p.setVisibility(View.VISIBLE);
			acessoJson.setJsonNaLista(result);
			acessoJson.setSalvarJson(result);

		} else {
			MainActivity.p.setVisibility(View.GONE);
			acessoJson.onErroDownloadJson();
		}

	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (MainActivity.p != null && MainActivity.p.getVisibility() == View.VISIBLE) {
			MainActivity.p.setVisibility(View.GONE);
		}
	}

	public ArrayList<ListagemModel> postData(String string) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(string);

		ArrayList<ListagemModel> data = null;

		try {
			HttpResponse response = httpclient.execute(httppost);

			HttpEntity i = response.getEntity();

			status = response.getStatusLine().getStatusCode();

			if (status == 200) {

				InputStream meuInputStream = i.getContent();

				Reader reader = new InputStreamReader(meuInputStream);

				data = new Gson().fromJson(reader, new TypeToken<ArrayList<ListagemModel>>() {
				}.getType());
			} else {
			}

		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (Exception e) {
		}

		return data;
	}

}
