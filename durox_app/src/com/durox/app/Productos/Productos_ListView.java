package com.durox.app.Productos;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.durox_app.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class Productos_ListView extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Productos> productos = null;
	private ArrayList<Productos> arraylist;

	public Productos_ListView(Context context, List<Productos> productos) {
		mContext = context;
		this.productos = productos;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Productos>();
		this.arraylist.addAll(productos);
	}

	public class ViewHolder {
		TextView id_back;
		TextView nombre;
		TextView precio;
		TextView moneda;
		TextView codigo;
		ImageView imagen;
	}

	
	public int getCount() {
		return productos.size();
	}
	

	public Productos getItem(int position) {
		return productos.get(position);
	}
	

	public long getItemId(int position) {
		return position;
	}
	

	@SuppressLint("InflateParams")
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.productos_listviewitem, null);
	
			// Locate the TextViews in listview_item.xml
			holder.nombre = (TextView) view.findViewById(R.id.txt_producto);
			holder.id_back = (TextView) view.findViewById(R.id.txt_id_back);
			holder.precio = (TextView) view.findViewById(R.id.txt_precio);
			holder.moneda = (TextView) view.findViewById(R.id.txt_moneda);
			holder.codigo = (TextView) view.findViewById(R.id.txt_codigo);
			holder.imagen = (ImageView) view.findViewById(R.id.imagen);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		// Set the results into TextViews
		holder.nombre.setText(productos.get(position).getNombre());
		holder.id_back.setText(productos.get(position).getID());
		holder.precio.setText(productos.get(position).getPrecio());
		holder.moneda.setText(productos.get(position).getMoneda());
		holder.codigo.setText(productos.get(position).getCodigo());
		holder.imagen.setImageResource(productos.get(position).getImagen());

		// Detecta que item de la lista le hicieron clic
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				Intent intent = new Intent(mContext, Productos_ItemView.class);
				
				// Pasamos todos los datos de producto
				intent.putExtra("nombre", (productos.get(position).getNombre()));
				intent.putExtra("id", (productos.get(position).getID()));
				intent.putExtra("precio", (productos.get(position).getPrecio()));
				intent.putExtra("imagen", (productos.get(position).getImagen()));
		
				// Start SingleItemView Class
				mContext.startActivity(intent);
				
			}
		});

		return view;
	}

	// Filter Class
	public void filter(String charText, CharSequence filtro) {
		charText = charText.toLowerCase(Locale.getDefault());
		productos.clear();
		if (charText.length() == 0) {
			productos.addAll(arraylist);
		} else {
			for (Productos wp : arraylist) {
				
				if(filtro.equals("") || filtro.equals("nombre")){
					if (wp.getNombre().toLowerCase(Locale.getDefault()).contains(charText)) {
						productos.add(wp);
					}
				} else if(filtro.equals("id")){
					if (wp.getID().toLowerCase(Locale.getDefault()).contains(charText)) {
						productos.add(wp);
					}
				} else if(filtro.equals("codigo")){
					if (wp.getCodigo().toLowerCase(Locale.getDefault()).contains(charText)) {
						productos.add(wp);
					}
				}else {
					if (wp.getPrecio().toLowerCase(Locale.getDefault()).contains(charText)) {
						productos.add(wp);
					}
				}
			}
		}
		notifyDataSetChanged();
	}

}
