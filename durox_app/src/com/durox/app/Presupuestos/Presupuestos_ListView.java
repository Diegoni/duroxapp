package com.durox.app.Presupuestos;



import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.durox_app.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;


public class Presupuestos_ListView extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Presupuesto> presupuestos = null;
	private ArrayList<Presupuesto> arraylist;

	public Presupuestos_ListView(Context context, List<Presupuesto> presupuestos) {
		mContext = context;
		this.presupuestos = presupuestos;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Presupuesto>();
		this.arraylist.clear();
		this.arraylist.addAll(presupuestos);
	}

	public class ViewHolder {
		TextView nombre;
		TextView total;
		TextView estado;
		TextView fecha;
		TextView id;
		ImageView imagen;
	}
	
	public int getCount() {
		return presupuestos.size();
	}

	public Presupuesto getItem(int position) {
		return presupuestos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	
	@SuppressLint("InflateParams")
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.presupuestos_listviewitem, null);
			
			// Locate the TextViews in listview_item.xml
			holder.nombre = (TextView) view.findViewById(R.id.txt_pCliente);
			holder.id = (TextView) view.findViewById(R.id.txt_pBack);
			holder.total = (TextView) view.findViewById(R.id.txt_pTotal);
			holder.estado = (TextView) view.findViewById(R.id.txt_pEstado);
			holder.fecha = (TextView) view.findViewById(R.id.txt_pFecha);
			holder.imagen = (ImageView) view.findViewById(R.id.imagen);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.nombre.setText(presupuestos.get(position).getNombre());
		holder.id.setText(presupuestos.get(position).getID());
		holder.total.setText(presupuestos.get(position).getTotal());
		holder.estado.setText(presupuestos.get(position).getEstado());
		holder.fecha.setText(presupuestos.get(position).getFecha());
		holder.imagen.setImageResource(presupuestos.get(position).getImagen());

		
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, Presupuestos_ItemView.class);
					
				intent.putExtra("id", (presupuestos.get(position).getID()));
				intent.putExtra("nombre", (presupuestos.get(position).getNombre()));
				intent.putExtra("total", (presupuestos.get(position).getTotal()));
				intent.putExtra("imagen", (presupuestos.get(position).getImagen()));
				intent.putExtra("id_presupuesto", (presupuestos.get(position).getIDPresupuesto()));
					
				mContext.startActivity(intent);
				
			}
		});

		return view;
	}

	
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		presupuestos.clear();
		if (charText.length() == 0) {
			presupuestos.addAll(arraylist);
		} else {
			for (Presupuesto wp : arraylist)  {
				if (wp.getNombre().toLowerCase(Locale.getDefault()).contains(charText)){
					presupuestos.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
	
}
