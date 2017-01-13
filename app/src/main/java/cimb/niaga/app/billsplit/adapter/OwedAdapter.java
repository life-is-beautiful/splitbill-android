package cimb.niaga.app.billsplit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cimb.niaga.app.billsplit.R;
import cimb.niaga.app.billsplit.fragment.FragmentOwed;
import cimb.niaga.app.billsplit.fragment.HomeFragment;

/**
 * Created by Denny on 1/10/2017.
 */

public class OwedAdapter extends BaseAdapter {
    private Activity activity;

    public OwedAdapter(Activity act) {
        this.activity = act;
    }

    @Override
    public int getCount() {
        return FragmentOwed.owed_event.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ListHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_event, null);
            holder = new ListHolder();

            convertView.setTag(holder);
        }else{
            holder = (ListHolder) convertView.getTag();
        }

        holder.txt_bill            = (TextView) convertView.findViewById(R.id.txt_bill);
        holder.txt_totalprice    = (TextView) convertView.findViewById(R.id.txt_totalprice);

        holder.txt_bill.setText(FragmentOwed.owed_event.get(position));
        holder.txt_totalprice.setText(FragmentOwed.owed_price.get(position));


        convertView.setTag(holder);

        return convertView;
    }

    class ListHolder
    {
        TextView txt_bill, txt_totalprice;
    }
}
