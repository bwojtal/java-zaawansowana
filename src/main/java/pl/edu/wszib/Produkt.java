package pl.edu.wszib;
import java.io.FileWriter;
import java.util.Random;

public class Produkt {
    public static int nbId = 0;
    private int id;
    private String nazwa;
    private float cena;
    private float x;
    private float y;
    private float z;
    private float m;

    public Slot inSlot;

    public Produkt() {
        this.setId();
        this.nazwa = "Produkt";
        this.cena = 0;
        this.setCena(0);
        this.setWymiary(0, 0, 0);
        this.setWaga(0);
    }

    public Produkt(float wys, float szer, float gleb, float waga) {
        this.setId();
        this.nazwa = "Prod_" + this.id;
        this.setCena(0);
        setWymiary(wys, szer, gleb);
        setWaga(waga);
    }

    public Produkt(float wys, float szer, float gleb, float waga, float cena) {
        this.setId();
        this.nazwa = "Prod_" + this.id;
        this.setCena(cena);
        setWymiary(wys, szer, gleb);
        setWaga(waga);
    }

    public Produkt(String nazwa, float wys, float szer, float gleb, float waga) {
        this.nazwa = nazwa;
        this.setId();
        this.setCena(0);
        this.setWymiary(wys, szer, gleb);
        this.setWaga(waga);
    }

    public Produkt(String nazwa, float wys, float szer, float gleb, float waga, float cena) {
        this.nazwa = nazwa;
        this.setId();
        this.setCena(cena);
        this.setWymiary(wys, szer, gleb);
        this.setWaga(waga);
    }

    //Dyrektywa o świadomości przeładowania. Przeciążenia wewnątrz klasy, przeładowania (override) przy dziedziczeniu
    @Override
    public String toString() {
        //System.out.println("Witaj. \nTwoja paczka o nazwie "+this.nazwa+", numer zamówienia "+this.id+" kosztuje "+this.cena+"zł i waży "+this.m+"kg. \nWymiary: "+this.x+"x"+this.y+"x"+this.z+"mm");
//        String s= "Twoj podukt o nazwie "+this.nazwa+", numer zamowienia "+this.id+" kosztuje "+this.cena+"zl i wazy "+this.m+"kg. \nWymiary: "+this.x+"x"+this.y+"x"+this.z+"mm";
        String s = this.nazwa + "\n";
        s += "==========================\n";
        s += "Cena: " + this.cena + "zl\n";
        s += "--------------------------\n";
        s += "Wymiary:\n\twys:" + this.x + "\n\tszer:" + this.y + "\n\tgleb:" + this.z + "\n";
        s += "--------------------------\n";
        s += "Waga:" + this.m + "kg\n";
        s += "==========================\n\n";
        return s;
    }

    public void save() {
        try {
            FileWriter w = new FileWriter("prod_" + this.id + ".txt");
            w.write(this.nazwa + "\n");
            w.write(this.x + "\n");
            w.write(this.y + "\n");
            w.write(this.z + "\n");
            w.write(this.m + "\n");
            w.write(this.cena + "\n");
            w.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    public void pokazWymiary() {
        System.out.println("Wymiary paczki: " + this.x + "x" + this.y + "x" + this.z + "mm");
    }

    public void pokazWage() {
        System.out.println("Waga produktu: " + this.m + "kg");
    }

    public void pokazCene() {
        System.out.println("Cena produktu: " + this.cena + "zl");
    }

    public void setWymiary(float x, float y, float z) {

        if (x > 0 && x < 2000) {
            this.x = x;
        }
        if (y > 0 && y < 2000) {
            this.y = y;
        }
        if (z > 0 && z < 2000) {
            this.z = z;
        }

//        System.out.println("Ustawiono nowe wymiary prduktu");
//        pokazWymiary();
    }

    public void setWaga(float m) {
        if (m > 0 && m < 500) {
            this.m = m;
        }
//        System.out.println("Ustawiono nowa wage prduktu");
//        pokazWage();
    }

    public void setCena(float nowaCena) {
        if (nowaCena > 0) {
            this.cena = nowaCena;
        } else {
            this.cena = 0;
        }
//        System.out.println("Ustawiono nowa cene prduktu");
//        pokazCene();
    }

    public void setId() {
        this.id = Produkt.nbId++;
    }

    public float[] prodWymiary(){
        float[] wym = {this.x, this.y, this.z, this.m};
        return wym;
    }

    public float getObjetosc(){
        return this.x*this.y*this.z;
    }

    public float getWaga(){
        return this.m;
    }

    public float productSpace(){
        return this.x*this.y*this.z;
    }

    public int getId(){
        return this.id;
    }
    //    public void getName(){
//        System.out.println(this.nazwa);
//    }
    public String getName(){ return this.nazwa; }
    public void addToSlot(Slot s){
        if(this.inSlot == null) this.inSlot = s;
        else{
            System.out.println("Przedmiot już znajduje się w slocie! Najpierw usuń go z obecnego slotu");
        }
    }

    public void removeFromSlot(){
        this.inSlot = null;
    }

    public Slot getSlot(){
        return this.inSlot;
    }

    //Zwraca TRUE jeżeli produkt już jest przypisany do jakiegoś slotu
    public Boolean checkSlot(){
        return this.inSlot != null;
    }



}
