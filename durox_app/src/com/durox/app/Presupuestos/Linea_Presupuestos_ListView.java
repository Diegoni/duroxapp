package com.durox.app.Presupuestos;



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
import android.widget.TextView;
import android.view.View.OnLongClickListener;


public class Linea_Presupuestos_ListView extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Linea_Presupuestos> linea_presupuestos = null;
	private ArrayList<Linea_Presupuestos> arraylist;

	public Linea_Presupuestos_ListView(Context context, List<Linea_Presupuestos> linea_presupuestos) {
		mContext = context;
		this.linea_presupuestos = linea_presupuestos;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Linea_Presupuestos>();
		this.arraylist.clear();
		this.arraylist.addAll(linea_presupuestos);
	}

	public class ViewHolder {
		TextView producto;
		TextView cantidad;
		TextView precio;
		TextView moneda;
		TextView total;
	}
	
	public int getCount() {
		return linea_presupuestos.size();
	}

	public Linea_Presupuestos getItem(int position) {
		return linea_presupuestos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	@SuppressLint("InflateParams")
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.linea_presupuestos_listviewitem, null);
			
			// Locate the TextViews in listview_item.xml
			holder.producto = (TextView) view.findViewById(R.id.linea_presupuesto_producto);
			holder.cantidad = (TextView) view.findViewById(R.id.linea_presupuesto_cantidad);
			holder.precio = (TextView) view.findViewById(R.id.linea_presupuesto_precio);
			holder.total = (TextView) view.findViewById(R.id.linea_presupuesto_total);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		// Set the results into TextViews
		holder.producto.setText(linea_presupuestos.get(position).getProducto());
		holder.cantidad.setText(linea_presupuestos.get(position).getCantidad());
		holder.precio.setText(linea_presupuestos.get(position).getMoneda()+" "+linea_presupuestos.get(position).getPrecio());
		holder.total.setText(linea_presupuestos.get(position).getPorDefecto()+" "+linea_presupuestos.get(position).getTotal());
		
		
		// Detecta que item de la lista le hicieron clic
		view.setOnLongClickListener(new OnLongClickListener() {
		    public boolean onLongClick(View arg0) {
		    	Intent intent = new Intent(mContext, Presupuestos_Create.class);
				
				// Pasamos toda la informacion
				intent.putExtra("id_linea", (linea_presupuestos.get(position).getIdLinea()));
				intent.putExtra("nombre", (linea_presupuestos.get(position).getcNombre()));
				intent.putExtra("id_visita", (linea_presupuestos.get(position).getcIdVisita()));
				intent.putExtra("id", (linea_presupuestos.get(position).getid()));
				intent.putExtra("id_presupuesto", (linea_presupuestos.get(position).getid_presupuesto()));
				
				// Start SingleItemView Class
				mContext.startActivity(intent);
				
				return false;
			}
		});

		return view;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		linea_presupuestos.clear();
		if (charText.length() == 0) {
			linea_presupuestos.addAll(arraylist);
		} else {
			for (Linea_Presupuestos wp : arraylist) {
				if (wp.getProducto().toLowerCase(Locale.getDefault()).contains(charText)) {
					linea_presupuestos.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
	
}
