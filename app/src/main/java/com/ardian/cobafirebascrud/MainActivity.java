package com.ardian.cobafirebascrud;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterMahasiswaRecyclerView.FirebaseDataListener
{
    //variabel fields
    private Toolbar mToolbar;
    private FloatingActionButton mFloatingActionButton;
    private EditText Nim;
    private EditText Nama;
    private EditText Semester;
    private EditText Ipk;
    private RecyclerView mRecyclerView;
    private AdapterMahasiswaRecyclerView mAdapter;
    private ArrayList<Mahasiswa> daftarMahasiswa;

    //variabel yang merefers ke Firebase Database
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //android toolbar
        setupToolbar(R.id.toolbar);


        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseApp.initializeApp(this);
        // mengambil referensi ke Firebase Database
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference("mahasiswa");
        mDatabaseReference.child("data_mahasiswa").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                daftarMahasiswa = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()){
                    Mahasiswa mahasiswa = mDataSnapshot.getValue(Mahasiswa.class);
                    mahasiswa.setKey(mDataSnapshot.getKey());
                    daftarMahasiswa.add(mahasiswa);
                }
                //set adapter RecyclerView
                mAdapter = new AdapterMahasiswaRecyclerView(MainActivity.this, daftarMahasiswa);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){
                // TODO: Implement this method
                Toast.makeText(MainActivity.this, databaseError.getDetails()+" "+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }

        });


        //FAB (FloatingActionButton) tambah barang
        mFloatingActionButton = (FloatingActionButton)findViewById(R.id.tambah_barang);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //tambah barang
                dialogTambahMahasiswa();
            }
        });
    }




    /* method ketika data di klik
     */
    @Override
    public void onDataClick(final Mahasiswa mahasiswa, int position){
        //aksi ketika data di klik
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Aksi");

        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){
                dialogUpdateMahasiswa(mahasiswa);
            }
        });
        builder.setNegativeButton("HAPUS", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){
                hapusDataBarang(mahasiswa);
            }
        });
        builder.setNeutralButton("BATAL", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){
                dialog.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }



    //setup android toolbar
    private void setupToolbar(int id){
        mToolbar = (Toolbar)findViewById(id);
        setSupportActionBar(mToolbar);
    }



    //dialog tambah barang / alert dialog
    private void dialogTambahMahasiswa(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Data Mahasiswa");
        View view = getLayoutInflater().inflate(R.layout.layout_tambah_mhs, null);

        Nim = (EditText)view.findViewById(R.id.nim_mhs);
        Nama = (EditText)view.findViewById(R.id.nama_mhs);
        Semester = (EditText)view.findViewById(R.id.semester_mhs);
        Ipk = (EditText)view.findViewById(R.id.ipk_mhs);

        builder.setView(view);

        //button simpan barang / submit barang
        builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){

                String nimMhs = Nim.getText().toString();
                String namaMhs = Nama.getText().toString();
                String semesterMhs = Semester.getText().toString();
                String ipkMhs = Ipk.getText().toString();

                if(!nimMhs.isEmpty() && !namaMhs.isEmpty() && !semesterMhs.isEmpty() && !ipkMhs.isEmpty()){
                    submitDataMahasiswa(new Mahasiswa(nimMhs, namaMhs, semesterMhs,ipkMhs));
                }
                else {
                    Toast.makeText(MainActivity.this, "Data harus di isi!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //button kembali / batal
        builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }



    //dialog update mahasiswa / update data mahasiswa
    private void dialogUpdateMahasiswa(final Mahasiswa mahasiswa){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Data Mahasiswa");
        View view = getLayoutInflater().inflate(R.layout.layout_edit_mhs, null);

        Nim = (EditText)view.findViewById(R.id.nim_mhs_edit);
        Nama = (EditText)view.findViewById(R.id.nama_mhs_edit);
        Semester = (EditText)view.findViewById(R.id.semester_mhs_edit);
        Ipk = (EditText)view.findViewById(R.id.ipk_mhs_edit);

        Nim.setText(mahasiswa.getNim());
        Nama.setText(mahasiswa.getNama());
        Semester.setText(mahasiswa.getSemester());
        Ipk.setText(mahasiswa.getIpk());
        builder.setView(view);

        //final Mahasiswa mBarang = (Mahasiswa)getIntent().getSerializableExtra("
        if (mahasiswa != null){
            builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id){
                    mahasiswa.setNim(Nim.getText().toString());
                    mahasiswa.setNama(Nama.getText().toString());
                    mahasiswa.setSemester(Semester.getText().toString());
                    mahasiswa.setIpk(Ipk.getText().toString());
                    updateDataMahasiswa(mahasiswa);
                }
            });
        }
        builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();

    }


    /**
     * submit data mahasiswa
     * ini adalah kode yang digunakan untuk mengirimkan data ke Firebase Realtime Database
     * set onSuccessListener yang berisi kode yang akan dijalankan
     * ketika data berhasil ditambahkan
     */
    private void submitDataMahasiswa(Mahasiswa mahasiswa){
        mDatabaseReference.child("data_mahasiswa").push().setValue(mahasiswa).addOnSuccessListener(this, new OnSuccessListener<Void>(){
            @Override
            public void onSuccess(Void mVoid){
                Toast.makeText(MainActivity.this, "Data mahasiswa berhasil di simpan !", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * update/edit data mahasiswa
     * ini adalah kode yang digunakan untuk mengirimkan data ke Firebase Realtime Database
     * set onSuccessListener yang berisi kode yang akan dijalankan
     * ketika data berhasil ditambahkan
     */
    private void updateDataMahasiswa(Mahasiswa mahasiswa){
        mDatabaseReference.child("data_mahasiswa").child(mahasiswa.getKey()).setValue(mahasiswa).addOnSuccessListener(new OnSuccessListener<Void>(){
            @Override
            public void onSuccess(Void mVoid){
                Toast.makeText(MainActivity.this, "Data berhasil di update !", Toast.LENGTH_LONG).show();
            }
        });
    }
    /**
     * hapus data mahasiswa
     * ini kode yang digunakan untuk menghapus data yang ada di Firebase Realtime Database
     * set onSuccessListener yang berisi kode yang akan dijalankan
     * ketika data berhasil dihapus
     */
    private void hapusDataBarang(Mahasiswa mahasiswa){
        if(mDatabaseReference != null){
            mDatabaseReference.child("data_mahasiswa").child(mahasiswa.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>(){
                @Override
                public void onSuccess(Void mVoid){
                    Toast.makeText(MainActivity.this,"Data berhasil di hapus !", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}