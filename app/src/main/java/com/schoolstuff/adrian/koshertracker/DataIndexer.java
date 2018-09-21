package com.schoolstuff.adrian.koshertracker;

public class DataIndexer {
    String name;
    String city;
    String address;
    String type;
    int cert;
    String phone;
    public DataIndexer () { }
    public DataIndexer (String name,String city,String address,String type,int cert,String phone){
        this.name=name;
        this.city=city;
        this.address=address;
        this.type=type;
        this.cert=cert;
        this.phone=phone;
    }
    public String toString(){
        return name+" |"+city+" |"+address+" |"+type+" |"+cert+" |"+phone+"\n";
    }
}
