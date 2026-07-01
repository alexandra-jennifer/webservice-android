package com.example.webservice_app;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {

    private Context context;
    private List<Mahasiswa> listMahasiswa;
    private OnItemClickListener listener;

    private final int[] CARD_COLORS = {
            Color.parseColor("#FF6B6B"),
            Color.parseColor("#4ECDC4"),
            Color.parseColor("#FFD93D"),
            Color.parseColor("#6BCB77"),
            Color.parseColor("#4D96FF"),
            Color.parseColor("#FF922B")
    };

    public interface OnItemClickListener {
        void onItemClick(Mahasiswa mahasiswa);
    }

    public MahasiswaAdapter(Context context, List<Mahasiswa> list, OnItemClickListener listener) {
        this.context       = context;
        this.listMahasiswa = list;
        this.listener      = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_mahasiswa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mahasiswa mhs = listMahasiswa.get(position);

        // Ambil warna berdasarkan posisi (bergantian)
        int color = CARD_COLORS[position % CARD_COLORS.length];

        // Warna strip kiri dan avatar
        holder.viewAccent.setBackgroundColor(color);
        holder.viewAvatar.setBackgroundColor(color);

        // Avatar
        String urlFoto = APIConfig.URL_UPLOADS + mhs.getFoto();
        com.bumptech.glide.Glide.with(context)
                .load(urlFoto)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE) // BARIS BARU (Matikan simpanan memori HP)
                .skipMemoryCache(true) // BARIS BARU (Matikan simpanan sementara)
                .into(holder.ivFotoItem);

        // Isi data teks
        holder.tvNama.setText(mhs.getNama());
        holder.tvNIM.setText(mhs.getNim());
        holder.tvJurusan.setText(mhs.getJurusan());
        holder.tvAlamat.setText(mhs.getAlamat());

        // Klik item → buka form edit
        holder.itemView.setOnClickListener(v -> listener.onItemClick(mhs));
    }

    @Override
    public int getItemCount() {
        return listMahasiswa.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View viewAccent, viewAvatar;
        TextView tvInitial, tvNama, tvNIM, tvJurusan, tvAlamat;
        ImageView ivFotoItem;

        ViewHolder(View v) {
            super(v);
            viewAccent = v.findViewById(R.id.viewAccent);
            viewAvatar = v.findViewById(R.id.viewAvatar);
            tvNama     = v.findViewById(R.id.tvNama);
            tvNIM      = v.findViewById(R.id.tvNIM);
            tvJurusan  = v.findViewById(R.id.tvJurusan);
            tvAlamat   = v.findViewById(R.id.tvAlamat);
            ivFotoItem = itemView.findViewById(R.id.ivFotoItem);
        }
    }
}