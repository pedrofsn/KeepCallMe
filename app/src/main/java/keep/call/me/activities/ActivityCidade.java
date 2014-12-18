package keep.call.me.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;

import keep.call.me.R;
import keep.call.me.RecyclerItemClickListener;
import keep.call.me.adapter.AdapterCidade;
import keep.call.me.model.Estado;

public class ActivityCidade extends ActionBarActivity implements RecyclerItemClickListener.OnItemClickListener {

    private Estado estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        estado = (Estado) getIntent().getExtras().getSerializable("estado");

        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.e("teste", "Passou: " + estado.getSigla() + " - " + estado.getEstado() + " - " + estado.getListaCidades());

        recyclerView.setAdapter(new AdapterCidade(estado.getListaCidades()));

        recyclerView.playSoundEffect(SoundEffectConstants.CLICK);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}