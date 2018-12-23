package azisalvriyanto.uinsunankalijaga.Adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayatData;
import azisalvriyanto.uinsunankalijaga.R;

import static org.apache.commons.lang3.StringUtils.capitalize;

public class AdapterRiwayat extends RecyclerView.Adapter<AdapterRiwayat.CustomViewHolder> {
    private List<ModelRiwayatData> data;

    //DARI SINI
    private static final String TAG = "RecyclerViewAdapter";
    private Activity context;
    //SAMPE SINI

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
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        final ModelRiwayatData riwayat = data.get(position);
        final String status;
        Drawable status_gambar;

        if (riwayat.getStatus().matches("")) {
            status = "Sedang diproses.";
            status_gambar = holder.itemView.getResources().getDrawable(R.mipmap.ic_sedang);
        }
        else {
            if ("sedang".equals(riwayat.getStatus())) {
                status = riwayat.getStatus()+" diproses.";
                status_gambar = holder.itemView.getResources().getDrawable(R.mipmap.ic_sedang);
            }
            else if ("sukses".equals(riwayat.getStatus())) {
                status = "Berhasil diverifikasi.";
                status_gambar = holder.itemView.getResources().getDrawable(R.mipmap.ic_sukses);
            }
            else if ("gagal".equals(riwayat.getStatus())){
                status = "Presensi ditolak.";
                status_gambar = holder.itemView.getResources().getDrawable(R.mipmap.ic_gagal);
            }
            else {
                status = "Sedang diproses.";
                status_gambar = holder.itemView.getResources().getDrawable(R.mipmap.ic_sedang);
            }
        }

        holder.a_friwayat_jenis.setText(capitalize(riwayat.getJenis()));
        holder.a_friwayat_status.setText(status);
        holder.a_friwayat_waktu.setText(riwayat.getWaktu());
        holder.a_friwayat_tanggal.setText(riwayat.getTanggal());
        holder.a_friwayat_status_gambar.setImageDrawable(status_gambar);

        holder.a_friwayat_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View contentView = View.inflate(view.getContext(), R.layout.l_menu_friwayat_detail, null);

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
                bottomSheetDialog.setContentView(contentView);

                TextView a_friwayat_detail_jenis        = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_jenis);
                TextView a_friwayat_detail_status       = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_status);
                TextView a_friwayat_detail_waktu        = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_waktu);
                //ImageView a_friwayat_detail_foto         = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_foto);
                TextView a_friwayat_detail_nama         = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_nama);
                TextView a_friwayat_detail_nip          = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_nip);
                TextView a_friwayat_detail_tanggal      = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_tanggal);
                TextView a_friwayat_detail_latitude     = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_latitude);
                TextView a_friwayat_detail_longitude    = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_longitude);
                TextView l_friwayat_detail_keterangan   = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_keterangan);
                TextView l_friwayat_detail_berkas       = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_berkas);

                a_friwayat_detail_jenis.setText(capitalize(riwayat.getJenis()));
                a_friwayat_detail_status.setText(status);
                a_friwayat_detail_waktu.setText(riwayat.getWaktu());
                a_friwayat_detail_tanggal.setText(riwayat.getTanggal());
                a_friwayat_detail_nama.setText(riwayat.getID());
                a_friwayat_detail_nip.setText(riwayat.getID());
                a_friwayat_detail_latitude.setText(riwayat.getLatitude());
                a_friwayat_detail_longitude.setText(riwayat.getLongitude());
                l_friwayat_detail_keterangan.setText(riwayat.getKeterangan());
                l_friwayat_detail_berkas.setText(riwayat.getBerkas());

                //String data_foto_url = "http://presensi-pegawai.msftrailers.co.za/foto/"+riwayat.getFoto();
                //loadImageFromUrl(data_foto_url, a_friwayat_detail_foto);

                bottomSheetDialog.show();
            }
        });

        /*Glide.with(holder.itemView)
            //.asBitmap()
            .load(url)
            .into(holder.a_friwyat_status_gambar);*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView a_friwayat_jenis, a_friwayat_status, a_friwayat_waktu, a_friwayat_tanggal, a_friwayat_detail_nama;
        public RelativeLayout a_friwayat_detail;
        public ImageView a_friwayat_status_gambar;

        public CustomViewHolder(View view) {
            super(view);
            a_friwayat_jenis            = view.findViewById(R.id.l_friwayat_jenis);
            a_friwayat_status           = view.findViewById(R.id.l_friwayat_status);
            a_friwayat_waktu            = view.findViewById(R.id.l_friwayat_waktu);
            a_friwayat_tanggal          = view.findViewById(R.id.l_friwayat_tanggal);
            a_friwayat_detail           = view.findViewById(R.id.l_menu_friwayat_list);
            a_friwayat_detail_nama      = view.findViewById(R.id.l_friwayat_detail_nama);
            a_friwayat_status_gambar    = view.findViewById(R.id.l_friwayat_status_img);
        }
    }
}
