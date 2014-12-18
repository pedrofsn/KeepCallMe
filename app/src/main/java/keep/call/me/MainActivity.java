package keep.call.me;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SoundEffectConstants;
import android.view.View;

import com.google.gson.Gson;

import keep.call.me.adapter.AdapterEstado;
import keep.call.me.model.Estado;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String pegou2 = Utils.lerArquivo("json.json", this);

        Gson gson = new Gson();
        Estado[] estado = gson.fromJson(pegou2, Estado[].class);

        AdapterEstado adapterEstado = new AdapterEstado(estado);

        recyclerView.setAdapter(adapterEstado);

        recyclerView.playSoundEffect(SoundEffectConstants.CLICK);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }


}