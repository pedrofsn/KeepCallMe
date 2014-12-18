package keep.call.me.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SoundEffectConstants;
import android.view.View;

import com.google.gson.Gson;

import keep.call.me.R;
import keep.call.me.RecyclerItemClickListener;
import keep.call.me.Utils;
import keep.call.me.adapter.AdapterEstado;
import keep.call.me.model.Estado;

public class ActivityEstado extends ActionBarActivity implements RecyclerItemClickListener.OnItemClickListener {

    private Estado[] arrayEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrayEstado = new Gson().fromJson(Utils.lerArquivo("json.json", this), Estado[].class);

        recyclerView.setAdapter(new AdapterEstado(arrayEstado));

        recyclerView.playSoundEffect(SoundEffectConstants.CLICK);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, ActivityCidade.class);
        intent.putExtra("estado", arrayEstado[position]);
        startActivity(intent);
    }
}