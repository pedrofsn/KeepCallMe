package keep.call.me.Adapter;

import java.util.ArrayList;
import java.util.List;

import keep.call.me.R;
import keep.call.me.Model.TelefoneModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class TelefoneListAdapter extends BaseAdapter implements Filterable {

	private ArrayList<?> listData;
	private LayoutInflater layoutInflater;
	private ViewHolder holder;
	private List<?> listaOriginal;
	private Filter planetFilter;

	public TelefoneListAdapter(Context context,  List<? extends Object> listData) {
		this.listData = (ArrayList<?>) listData;
		this.layoutInflater = LayoutInflater.from(context);
		this.listaOriginal = listData;
	}

	@Override
	public Filter getFilter() {
		if (planetFilter == null)
			planetFilter = new PlanetFilter();

		return planetFilter;
	}

	public void resetData() {
		listData = (ArrayList<?>) listaOriginal;
	}

	@Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.adapter_telefone, null);
			holder = new ViewHolder();
			holder.cidade = (TextView) convertView.findViewById(R.id.cidade);
			holder.ddd = (TextView) convertView.findViewById(R.id.ddd);
			holder.uf = (TextView) convertView.findViewById(R.id.uf);
			holder.regiao = (TextView) convertView.findViewById(R.id.regiao);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.cidade.setText(((TelefoneModel) listData.get(position)).getCidade());
		holder.ddd.setText(((TelefoneModel) listData.get(position)).getDdd());
		holder.uf.setText(((TelefoneModel) listData.get(position)).getUf());
		holder.regiao.setText(((TelefoneModel) listData.get(position)).getRegiao());

		return convertView;
	}

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class PlanetFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			// We implement here the filter logic
			if (constraint == null || constraint.length() == 0) {
				// No filter implemented we return all the list
				results.values = listaOriginal;
				results.count = listaOriginal.size();
			} else {
				// We perform filtering operation
				ArrayList<TelefoneModel> nPlanetList = new ArrayList<TelefoneModel>();

				for (Object p : listData) {
					if (((TelefoneModel) p).getCidade().toUpperCase().startsWith(constraint.toString()))
						nPlanetList.add((TelefoneModel) p);
				}

				results.values = nPlanetList;
				results.count = nPlanetList.size();

			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {

			// Now we have to inform the adapter about the new list filtered
			if (results.count == 0)
				notifyDataSetInvalidated();
			else {
				listData = (ArrayList<TelefoneModel>) results.values;
				notifyDataSetChanged();
			}

		}

	}

	static class ViewHolder {
		TextView ddd;
		TextView cidade;
		TextView uf;
		TextView regiao;
	}
}
