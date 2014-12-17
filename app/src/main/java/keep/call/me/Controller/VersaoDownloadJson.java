package keep.call.me.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import keep.call.me.InterfaceJson;
import keep.call.me.MainActivity;
import keep.call.me.Model.VersaoModel;

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

public class VersaoDownloadJson extends AsyncTask<String, Void, VersaoModel> {

	private Context ctx;
	private int status;
	private InterfaceJson acessoJson;

	public VersaoDownloadJson(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		acessoJson = (InterfaceJson) ctx;
		MainActivity.p.setVisibility(View.VISIBLE);
	}

	@Override
	protected VersaoModel doInBackground(String... params) {
		return postData(params[0]);
	}

	@Override
	protected void onPostExecute(VersaoModel result) {
		super.onPostExecute(result);

		if (status == 200)
			acessoJson.checkVersion(result);
		else
			acessoJson.onErroDownloadJson();

		if (MainActivity.p != null && MainActivity.p.getVisibility() == View.VISIBLE)
			MainActivity.p.setVisibility(View.GONE);

	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (MainActivity.p != null && MainActivity.p.getVisibility() == View.VISIBLE)
			MainActivity.p.setVisibility(View.GONE);

	}

	public VersaoModel postData(String string) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(string);

		VersaoModel data = null;

		try {
			HttpResponse response = httpclient.execute(httppost);

			HttpEntity i = response.getEntity();

			status = response.getStatusLine().getStatusCode();

			if (status == 200) {

				InputStream meuInputStream = i.getContent();

				Reader reader = new InputStreamReader(meuInputStream);

				data = new Gson().fromJson(reader, new TypeToken<VersaoModel>() {
				}.getType());

			}

		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (Exception e) {
		}

		return data;
	}

}
