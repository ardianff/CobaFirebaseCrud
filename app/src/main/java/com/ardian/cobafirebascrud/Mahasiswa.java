package com.ardian.cobafirebascrud;


public class Mahasiswa
{
    private String key;
    private String nim;
    private String nama;
    private String semester;
    private String ipk;


    public Mahasiswa() {

    }
    public void setKey(String key){
        this.key = key;
    }

    public String getKey(){
        return key;
    }

    public void setNim(String nim){
        this.nim = nim;
    }

    public String getNim(){
        return nim;
    }

    public void setNama(String nama){
        this.nama = nama;
    }

    public String getNama(){
        return nama;
    }

    public void setSemester(String semester){
        this.semester = semester;
    }

    public String getSemester(){
        return semester;
    }

    public Mahasiswa(String NIM, String NAMA, String SEMESTER, String IPK){
        nim = NIM;
        nama = NAMA;
        semester = SEMESTER;
        ipk = IPK;
    }

    public String getIpk() {
        return ipk;
    }

    public void setIpk(String ipk) {
        this.ipk = ipk;
    }
}