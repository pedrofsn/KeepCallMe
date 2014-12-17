package keep.call.me.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import keep.call.me.InterfaceJson;
import keep.call.me.MainActivity;
import keep.call.me.Model.TelefoneModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TelefoneDownloadJson extends AsyncTask<String, Void, ArrayList<TelefoneModel>> {

	private Context ctx;
	private int status;

	public TelefoneDownloadJson(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		MainActivity.p.setVisibility(View.VISIBLE);
	}

	@Override
	protected ArrayList<TelefoneModel> doInBackground(String... params) {

		return postData(params[0]);
	}

	@Override
	protected void onPostExecute(ArrayList<TelefoneModel> result) {
		super.onPostExecute(result);

		InterfaceJson acessoJson = (InterfaceJson) ctx;

		if (status == 200) {
			acessoJson.setJsonNaLista(result);
			acessoJson.setSalvarJson(result);
		} else {
			acessoJson.onErroDownloadJson();
		}

		if (MainActivity.p != null && MainActivity.p.getVisibility() == View.VISIBLE) {
			MainActivity.p.setVisibility(View.GONE);
		}

	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (MainActivity.p != null && MainActivity.p.getVisibility() == View.VISIBLE) {
			MainActivity.p.setVisibility(View.GONE);
		}
	}

	public ArrayList<TelefoneModel> postData(String string) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(string);

		ArrayList<TelefoneModel> data = null;

		try {
			HttpResponse response = httpclient.execute(httppost);

			HttpEntity i = response.getEntity();

			status = response.getStatusLine().getStatusCode();

			if (status == 200) {

				InputStream meuInputStream = i.getContent();

				Reader reader = new InputStreamReader(meuInputStream);

				data = new Gson().fromJson(reader, new TypeToken<ArrayList<TelefoneModel>>() {
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
