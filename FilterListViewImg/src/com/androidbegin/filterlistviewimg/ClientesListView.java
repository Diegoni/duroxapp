package com.androidbegin.filterlistviewimg;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class ClientesListView extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Clientes> cliente = null;
	private ArrayList<Clientes> arraylist;

	public ClientesListView(Context context,
			List<Clientes> cliente) {
		mContext = context;
		this.cliente = cliente;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Clientes>();
		this.arraylist.addAll(cliente);
	}

	public class ViewHolder {
		TextView nombre;
		TextView direccion;
		ImageView imagen;
	}

	@Override
	public int getCount() {
		return cliente.size();
	}

	@Override
	public Clientes getItem(int position) {
		return cliente.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.listview_item, null);
			// Locate the TextViews in listview_item.xml
			holder.nombre = (TextView) view.findViewById(R.id.nombre);
			holder.direccion = (TextView) view.findViewById(R.id.direccion);
			
			// Locate the ImageView in listview_item.xml
			holder.imagen = (ImageView) view.findViewById(R.id.imagen);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// Set the results into TextViews
		holder.nombre.setText(cliente.get(position).getNombre());
		holder.direccion.setText(cliente.get(position).getDireccion());
		
		// Set the results into ImageView
		holder.imagen.setImageResource(cliente.get(position).getImagen());

		// Detecta que item de la lista le hicieron clic
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				// Send single item click data to SingleItemView Class
				Intent intent = new Intent(mContext, ClientesItemView.class);
				
				// Pass all data country
				intent.putExtra("nombre",
						(cliente.get(position).getNombre()));
				// Pass all data population
				intent.putExtra("direccion",
						(cliente.get(position).getDireccion()));
				// Pass all data flag
				intent.putExtra("imagen",
						(cliente.get(position).getImagen()));
				// Start SingleItemView Class
				mContext.startActivity(intent);
			}
		});

		return view;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		cliente.clear();
		if (charText.length() == 0) {
			cliente.addAll(arraylist);
		} else {
			for (Clientes wp : arraylist) {
				if (wp.getNombre().toLowerCase(Locale.getDefault())
						.contains(charText)) {
					cliente.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}
