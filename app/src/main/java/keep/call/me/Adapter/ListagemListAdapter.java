package keep.call.me.Adapter;

import java.util.ArrayList;
import java.util.List;

import keep.call.me.R;
import keep.call.me.DAO.TelefoneDAO;
import keep.call.me.Model.ListagemModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListagemListAdapter extends BaseAdapter {
	private ArrayList<?> listData;
	private LayoutInflater layoutInflater;
	private ViewHolder holder;
	private TelefoneDAO databaseTelefone;
	private Context ctx;

	public ListagemListAdapter(Context context, List<? extends Object> listData, Context ctx) {
		this.listData = (ArrayList<?>) listData;
		this.layoutInflater = LayoutInflater.from(context);
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {

			databaseTelefone = TelefoneDAO.getInstance(ctx);

			convertView = layoutInflater.inflate(R.layout.adapter_listagem, null);

			holder = new ViewHolder();
			holder.regiao = (TextView) convertView.findViewById(R.id.regiao);
			holder.uf = (TextView) convertView.findViewById(R.id.uf);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.regiao.setText(((ListagemModel) listData.get(position)).getRegiao());
		holder.uf.setText(((ListagemModel) listData.get(position)).getUf());

		if (databaseTelefone.recuperarTodosEmArrayFiltrandoPorUf(((ListagemModel) listData.get(position)).getUf()).size() > 0) {
			holder.icon.setVisibility(View.GONE);
		}

		return convertView;
	}

	static class ViewHolder {
		TextView regiao;
		TextView uf;
		ImageView icon;
	}

}
