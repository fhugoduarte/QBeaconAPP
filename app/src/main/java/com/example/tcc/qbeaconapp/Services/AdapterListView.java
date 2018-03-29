package com.example.tcc.qbeaconapp.Services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tcc.qbeaconapp.Datas.ItemListView;
import com.example.tcc.qbeaconapp.Datas.ItemSuport;
import com.example.tcc.qbeaconapp.R;

import java.util.List;

/**
 * Created by hugoduarte on 29/03/18.
 */

public class AdapterListView extends BaseAdapter {

    private LayoutInflater mInflater;
    private List itens;

    public AdapterListView(Context context, List itens) {
        //Itens do listview
        this.itens = itens;
        //Objeto responsável por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Object getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ItemSuport itemSuport;
        if(view == null){
            view = mInflater.inflate(R.layout.item_list, null);

            itemSuport = new ItemSuport();
            itemSuport.textTitle = ((TextView) view.findViewById(R.id.textList));
            itemSuport.imgIcon = ((ImageView) view.findViewById(R.id.imageList));

            view.setTag(itemSuport);
        }else{
            itemSuport = (ItemSuport) view.getTag();
        }

        ItemListView item = (ItemListView) itens.get(position);
        itemSuport.textTitle.setText(item.getTexto());
        itemSuport.imgIcon.setImageResource(item.getIconId());

        return view;
    }


}
