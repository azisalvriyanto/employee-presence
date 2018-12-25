package azisalvriyanto.uinsunankalijaga.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import azisalvriyanto.uinsunankalijaga.Api.ApiClient;
import azisalvriyanto.uinsunankalijaga.Api.ApiService;
import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayatData;
import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayatDetail;
import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayatDetailData;
import azisalvriyanto.uinsunankalijaga.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static org.apache.commons.lang3.StringUtils.capitalize;

public class AdapterRiwayat extends RecyclerView.Adapter<AdapterRiwayat.CustomViewHolder> {
    private List<ModelRiwayatData> data;
    Context mContext;

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
                status = "Sedang diproses.";
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
            public void onClick(final View view) {
                View contentView = View.inflate(view.getContext(), R.layout.l_menu_friwayat_detail, null);

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
                bottomSheetDialog.setContentView(contentView);

                final TextView a_friwayat_detail_jenis        = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_jenis);
                final TextView a_friwayat_detail_status       = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_status);
                final TextView a_friwayat_detail_waktu        = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_waktu);
                final ImageView a_friwayat_detail_foto        = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_foto);
                final TextView a_friwayat_detail_nama         = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_nama);
                final TextView a_friwayat_detail_nip          = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_nip);
                final TextView a_friwayat_detail_tanggal      = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_tanggal);
                final TextView a_friwayat_detail_latitude     = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_latitude);
                final TextView a_friwayat_detail_longitude    = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_longitude);
                final TextView l_friwayat_detail_keterangan   = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_keterangan);
                final TextView l_friwayat_detail_berkas       = bottomSheetDialog.findViewById(R.id.l_friwayat_detail_berkas);

                //retrofit
                final Retrofit apiClient = ApiClient.getClient();
                final ApiService apiService = apiClient.create(ApiService.class);

                Call<ModelRiwayatDetail> call = apiService.riwayat_detail(riwayat.getID());
                call.enqueue(new Callback<ModelRiwayatDetail>() {
                    @Override
                    public void onResponse(Call<ModelRiwayatDetail> call, Response<ModelRiwayatDetail> response) {
                        if (response.isSuccessful()) {
                            try {
                                if (response.body().getStatus().equals("sukses")) {
                                    ModelRiwayatDetailData data = response.body().getData();
                                    String keterangan;
                                    Glide.with(holder.itemView)
                                        .asBitmap()
                                        .load("http://presensi-pegawai.msftrailers.co.za/foto/"+data.getFoto())
                                        .into(a_friwayat_detail_foto);


                                    if (data.getKeterangan() != null && data.getKeterangan().isEmpty()) {
                                        keterangan = "Tidak ada keterangan.";
                                    }
                                    else {
                                        keterangan = data.getKeterangan();
                                    }

                                    a_friwayat_detail_jenis.setText(capitalize(data.getJenis()));
                                    a_friwayat_detail_status.setText(status);
                                    a_friwayat_detail_waktu.setText(data.getWaktu());
                                    a_friwayat_detail_tanggal.setText(data.getTanggal());
                                    a_friwayat_detail_nama.setText(data.getNama());
                                    a_friwayat_detail_nip.setText(data.getNIP());
                                    a_friwayat_detail_latitude.setText(data.getLatitude());
                                    a_friwayat_detail_longitude.setText(data.getLongitude());
                                    l_friwayat_detail_keterangan.setText(capitalize(keterangan));
                                    l_friwayat_detail_berkas.setText(data.getBerkas());
                                } else {
                                    Toast.makeText(holder.itemView.getContext(), "Detail riwayat tidak ditemukan.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(holder.itemView.getContext(), "Sambugan internet gagal.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(holder.itemView.getContext(), "Kredensial tidak valid.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelRiwayatDetail> call, Throwable t) {
                        Toast.makeText(holder.itemView.getContext(), "Sambugan internet gagal.", Toast.LENGTH_SHORT).show();

                        Log.e("TAG", "=======onFailure: " + t.toString());
                        t.printStackTrace();
                    }
                });

                bottomSheetDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView a_friwayat_jenis, a_friwayat_status, a_friwayat_waktu, a_friwayat_tanggal;
        public RelativeLayout a_friwayat_detail;
        public ImageView a_friwayat_status_gambar;

        public CustomViewHolder(View view) {
            super(view);
            a_friwayat_jenis            = view.findViewById(R.id.l_friwayat_jenis);
            a_friwayat_status           = view.findViewById(R.id.l_friwayat_status);
            a_friwayat_waktu            = view.findViewById(R.id.l_friwayat_waktu);
            a_friwayat_tanggal          = view.findViewById(R.id.l_friwayat_tanggal);
            a_friwayat_detail           = view.findViewById(R.id.l_menu_friwayat_list);
            a_friwayat_status_gambar    = view.findViewById(R.id.l_friwayat_status_img);
        }
    }
}
