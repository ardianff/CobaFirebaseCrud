package com.ardian.cobafirebascrud;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterMahasiswaRecyclerView extends RecyclerView.Adapter<AdapterMahasiswaRecyclerView.ItemBarangViewHolder>
{
    private Context context;
    private ArrayList<Mahasiswa> daftarMahasiswa;
    private FirebaseDataListener listener;

    public AdapterMahasiswaRecyclerView(Context context, ArrayList<Mahasiswa> daftarMahasiswa){
        this.context = context;
        this.daftarMahasiswa = daftarMahasiswa;
        this.listener = (FirebaseDataListener)context;
    }

    @Override
    public ItemBarangViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // TODO: Implement this method
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mhs, parent, false);
        ItemBarangViewHolder holder = new ItemBarangViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemBarangViewHolder holder, final int position)
    {
        // TODO: Implement this method
        holder.nim.setText("NIM : "+ daftarMahasiswa.get(position).getNim());
        holder.nama.setText("Nama : "+ daftarMahasiswa.get(position).getNama());
        holder.semester.setText("Semester   : "+ daftarMahasiswa.get(position).getSemester());
        holder.ipk.setText("IPK   : "+ daftarMahasiswa.get(position).getIpk());

        holder.view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                listener.onDataClick(daftarMahasiswa.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        // TODO: Implement this method
        return daftarMahasiswa.size();
    }


    //interface data listener
    public interface FirebaseDataListener {
        void onDataClick(Mahasiswa mahasiswa, int position);
    }
    public class ItemBarangViewHolder extends RecyclerView.ViewHolder
    {
        public TextView nim;
        public TextView nama;
        public TextView semester;
        public TextView ipk;
        public View view;

        public ItemBarangViewHolder(View view){
            super(view);

            nim = (TextView)view.findViewById(R.id.tv_nim_mhs);
            nama = (TextView)view.findViewById(R.id.tv_nama_mhs);
            semester = (TextView)view.findViewById(R.id.tv_semester_mhs);
            ipk = (TextView)view.findViewById(R.id.tv_ipk_mhs);
            this.view = view;
        }
    }
}