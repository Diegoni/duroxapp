package com.durox.app.Vendedores;

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

public class Mensajes_ListView extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Mensajes> mensajes = null;
	private ArrayList<Mensajes> arraylist;

	public Mensajes_ListView(Context context, List<Mensajes> mensajes) {
		mContext = context;
		this.mensajes = mensajes;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Mensajes>();
		this.arraylist.addAll(mensajes);
	}

	public class ViewHolder {
		TextView asunto;
		TextView mensaje;
		TextView date_add;
		ImageView imagen;
	}

	public int getCount() {
		return mensajes.size();
	}

	public Mensajes getItem(int position) {
		return mensajes.get(position);
	}

	public long getItemId(int position) 
	{
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.mensajes_listviewitem, null);
	
			// Locate the TextViews in listview_item.xml
			holder.asunto = (TextView) view.findViewById(R.id.txt_Asunto);
			holder.mensaje = (TextView) view.findViewById(R.id.txt_mensaje);
			holder.date_add = (TextView) view.findViewById(R.id.txt_date);
			holder.imagen = (ImageView) view.findViewById(R.id.imagen);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.asunto.setText(mensajes.get(position).getAsunto());
		holder.mensaje.setText(mensajes.get(position).getMensaje());
		holder.date_add.setText(mensajes.get(position).getDate_add());
		holder.imagen.setImageResource(mensajes.get(position).getImagen());

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				Intent intent = new Intent(mContext, Mensajes_ItemView.class);
				
				intent.putExtra("id", (mensajes.get(position).getID()));
				intent.putExtra("asunto", (mensajes.get(position).getAsunto()));
				intent.putExtra("mensaje", (mensajes.get(position).getMensaje()));
				intent.putExtra("date_add", (mensajes.get(position).getDate_add()));
				intent.putExtra("id_origen", (mensajes.get(position).getIdorigen()));
				intent.putExtra("imagen", (mensajes.get(position).getImagen()));
		
				mContext.startActivity(intent);
				
			}
		});

		return view;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		mensajes.clear();
		if (charText.length() == 0) {
			mensajes.addAll(arraylist);
		} else {
			for (Mensajes wp : arraylist) {
				if (wp.getAsunto().toLowerCase(Locale.getDefault()).contains(charText)) {
					mensajes.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}
