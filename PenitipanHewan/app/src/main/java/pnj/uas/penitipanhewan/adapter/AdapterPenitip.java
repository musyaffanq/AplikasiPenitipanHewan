package pnj.uas.penitipanhewan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import pnj.uas.penitipanhewan.R;
import pnj.uas.penitipanhewan.model.ModelPenitip;

public class AdapterPenitip extends ArrayAdapter<ModelPenitip> {
    Context context;
    int resource;

    public AdapterPenitip(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    class Holder {
        TextView txtNama, txtTelepon, txtHewan;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent){
        Holder holder;

        if(convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            holder.txtNama = convertView.findViewById(R.id.txtNama);
            holder.txtTelepon = convertView.findViewById(R.id.txtTelepon);
            holder.txtHewan = convertView.findViewById(R.id.txtHewan);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.txtNama.setText("Nama : "+getItem(position).getNama());
        holder.txtTelepon.setText("Telepon : "+getItem(position).getTelepon());
        holder.txtHewan.setText("Hewan : "+getItem(position).getHewan());

    return convertView;
    }

}
