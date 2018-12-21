package azisalvriyanto.uinsunankalijaga.Adapter;

import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        String status;

        if ("masuk".equals(riwayat.getJenis())) {
            status = "Berhasil diverifikasi.";

        }
        else {
            if (riwayat.getStatus().matches("")) {
                status = "Sedang diproses.";
            }
            else {
                if ("sedang".equals(riwayat.getJenis())) {
                    status = riwayat.getStatus()+" diproses.";
                }
                else if ("berhasil".equals(riwayat.getJenis())) {
                    status = "Berhasil diverifikasi.";
                }
                else if ("gagal".equals(riwayat.getJenis())){
                    status = "Presensi ditolak.";
                }
                else {
                    status = "Sedang diproses.";
                }
            }
        }
        String url = "http://presensi-pegawai.msftrailers.co.za/riwayat_status/gagal.png";

        holder.a_friwayat_jenis.setText(capitalize(riwayat.getJenis()));
        holder.a_friwayat_status.setText(status);
        holder.a_friwayat_waktu.setText(riwayat.getWaktu());
        holder.a_friwayat_tanggal.setText(riwayat.getTanggal());
        //holder.a_friwyat_status_gambar.setBackground(R.mipmap.ic_gagal);

        /*Glide.with(ModelRiwayat)
                .load(url)
                .placeholder(R.mipmap.ic_gagal)
                .into(holder.a_friwyat_status_gambar);*/

        holder.a_friwayat_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View contentView = View.inflate(view.getContext(), R.layout.l_menu_friwayat_detail, null);

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
                bottomSheetDialog.setContentView(contentView);
                bottomSheetDialog.show();


                /*AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new Fragment();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.l_menu_friwayat_list, myFragment)
                        .addToBackStack(null).commit();*/

                //listener.onItemClick(v, mViewHolder.getPosition());

                /*Intent intent = new Intent(view.getContext(), AMenuFRiwayatDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("friwayat_detail_waktu", riwayat.getWaktu());
                view.getContext().startActivity();*/

                //BottomSheetDialogFragment bottomSheetDialogFragment = new BottomsheetDialog();
                //bottomSheetDialogFragment.show(((FragmentActivity)view.getContext()).getSupportFragmentManager(), bottomSheetDialogFragment.getTag());


                Log.d(TAG, "sukses | "+riwayat.getID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView a_friwayat_jenis, a_friwayat_status, a_friwayat_waktu, a_friwayat_tanggal, a_friwayat_detail_nama;
        public RelativeLayout a_friwayat_detail;
        public ImageView a_friwyat_status_gambar;

        public CustomViewHolder(View view) {
            super(view);
            a_friwayat_jenis    = view.findViewById(R.id.l_friwayat_jenis);
            a_friwayat_status   = view.findViewById(R.id.l_friwayat_status);
            a_friwayat_waktu    = view.findViewById(R.id.l_friwayat_waktu);
            a_friwayat_tanggal  = view.findViewById(R.id.l_friwayat_tanggal);
            a_friwayat_detail   = view.findViewById(R.id.l_menu_friwayat_list);
            a_friwyat_status_gambar = view.findViewById(R.id.l_friwayat_status_img);
            a_friwayat_detail_nama = view.findViewById(R.id.l_friwayat_detail_nama);
        }
    }




}
