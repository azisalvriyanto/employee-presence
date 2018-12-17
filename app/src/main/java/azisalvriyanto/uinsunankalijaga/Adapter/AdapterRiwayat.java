package azisalvriyanto.uinsunankalijaga.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayatData;
import azisalvriyanto.uinsunankalijaga.R;

public class AdapterRiwayat extends RecyclerView.Adapter<AdapterRiwayat.CustomViewHolder> {
    private List<ModelRiwayatData> data;

    public AdapterRiwayat(List<ModelRiwayatData> data) {
        this.data = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.l_menu_friwayat_list, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ModelRiwayatData riwayat = data.get(position);
        holder.a_friwayat_jenis.setText(riwayat.getJenis());
        holder.a_friwayat_waktu.setText(riwayat.getWaktu());
        holder.a_friwayat_tanggal.setText(riwayat.getTanggal());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView a_friwayat_jenis, a_friwayat_waktu, a_friwayat_tanggal;

        public CustomViewHolder(View view) {
            super(view);
            a_friwayat_jenis    = (TextView) view.findViewById(R.id.l_friwayat_jenis);
            a_friwayat_waktu    = (TextView) view.findViewById(R.id.l_friwayat_waktu);
            a_friwayat_tanggal  = (TextView) view.findViewById(R.id.l_friwayat_tanggal);
        }
    }
}
