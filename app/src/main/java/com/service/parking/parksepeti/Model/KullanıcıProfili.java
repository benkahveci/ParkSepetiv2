package com.service.parking.parksepeti.Model;


public class KullanıcıProfili {

    public String Email = " ";
    public String Telefon_no;
    public String Name;

    public KullanıcıProfili(String email, String Telefon_no, String name) {
        Email = email;
        Telefon_no = Telefon_no;
        Name = name;
    }

    public KullanıcıProfili(String Telefon_no, String name) {
        Telefon_no = Telefon_no;
        Name = name;
    }

    public KullanıcıProfili() {
    }
}
