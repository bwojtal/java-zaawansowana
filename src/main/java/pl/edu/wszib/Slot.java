package pl.edu.wszib;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Slot {
    private static int slotCount = 0;

    private int Id;

    private float maxWys, maxSz, maxGleb;

    private float freeSpace;

    private float totalWaga;
    private String uklad = "poziom";
    private int produkty = 0;
    private Produkt[] productsList;
    private Przeslo inPrzeslo;




    public Slot(){
        this.setId();
        this.setWymiary(0,0,0);
        this.setSpace();
    }

    public Slot(float x, float y, float z){
        this.setId();
        this.setWymiary(x,y,z);
        this.setSpace();
    }

    public void setId(){
        this.Id = Slot.slotCount++;
    }
    public void setWymiary(float x, float y, float z){
        this.maxWys = x;
        this.maxSz = y;
        this.maxGleb = z;
    }
    //Pobiera wartość wagi łącznej wagi slotu
    public float getWaga() {
        return this.totalWaga;
    }

    //Getter dzięki któremu otrzymujemy wymiary slotu w tablicy [0] = wys, [1] = szer, [2] = gleb, [3] = waga
    public float[] slotWymiary(){
        float[] wym = {this.maxWys, this.maxSz, this.maxGleb, this.totalWaga};
        return wym;
    }

    //Dodaje produkty na podstawie parametru typu Produkt
    public void addProdukt(Produkt ... p){
        for(Produkt addP: p) {
            float[] wymiary = addP.prodWymiary();
            float productSpace = addP.productSpace();
            if (freeSpace >= productSpace && this.maxWys>=wymiary[0] && this.maxSz>=wymiary[1] && this.maxGleb>=wymiary[2]) {
                if (!addP.checkSlot()) {
                    int id = addP.getId();
                    if (this.produkty == 0) {
                        productsList = new Produkt[this.produkty + 1];
                    } else if (this.produkty > 0) {
                        Produkt[] tempArr = new Produkt[this.produkty + 1];
                        for (int i = 0; i < productsList.length; i++) {
                            tempArr[i] = productsList[i];
                        }
                        productsList = tempArr;
                    }
                    productsList[this.produkty] = addP;
                    this.produkty++;
                    this.freeSpace -= productSpace;
                    this.setWaga(wymiary[3]);
                    addP.addToSlot(this);
                    System.out.println("Pomyślnie dodano produkt "+addP.getName()+" o id "+addP.getId()+" do slotu "+this.Id+(this.inPrzeslo!=null?" w przęśle "+this.inPrzeslo.getId():""));
                } else {
                    System.out.println("Nie można dodać przedmiotu do slotu ponieważ jest już przypisany do slotu o id: " + addP.getSlot().getId());
                }
            } else {
                System.out.println("Nie można ułożyć produktu " + addP.getName()+" na obecnym slocie. Za mało miejsca!\n");
            }
        }
    }

    void setWaga(float w){
        this.totalWaga+=w;
    }


    //Zwraca id slota
    public int getId(){
        return this.Id;
    }


    //Usuwa produkt na podstawie obiektu
    public void delProduct(Produkt p) {

        if (this.productsList != null) {
            int check = 0;
            for(Produkt checkP:productsList){
                if(checkP==p) check=1;
            }

            if(check == 1) {
                //Dynamicznie alokowana tablica w celu zmniejszenia jej długości
                Produkt newArr[] = new Produkt[productsList.length - 1];
                for (int i = 0, k = 0; i < productsList.length; i++) {
                    if (productsList[i] == p) {
                        float[] wymiary = p.prodWymiary();
                        float productSpace = wymiary[0] * wymiary[1] * wymiary[2];
                        this.freeSpace += productSpace;
                        this.setWaga(-wymiary[3]);
                        this.produkty--;
                        p.removeFromSlot();
                        System.out.println("Pomyslnie usunięto produkt o id: "+p.getId()+"\n");
                        check = 1;
                        continue;
                    }
                    newArr[k++] = productsList[i];
                }
                productsList = newArr;
            }else{
                System.out.println("Nie ma takiego produktu w slocie!\n");
            }
        }
    }

    //Usuwa produkt na podstawie jego id
    public void delProduct(int pId) {
        if (this.productsList != null) {
            int check = 0;
            for(Produkt checkP:productsList){
                if(checkP.getId()==pId) check=1;
            }

            if(check == 1) {
                //Dynamicznie alokowana tablica w celu zmniejszenia jej długości
                Produkt newArr[] = new Produkt[productsList.length - 1];
                int i = 0;
                for (Produkt pr : productsList) {
                    if (pId == pr.getId()) {
                        float[] wymiary = pr.prodWymiary();
                        float productSpace = wymiary[0] * wymiary[1] * wymiary[2];
                        this.freeSpace += productSpace;
                        this.setWaga(-wymiary[3]);
                        this.produkty--;
                        if(inPrzeslo!=null) inPrzeslo.countProducts();
                        pr.removeFromSlot();

                        System.out.println("Pomyslnie usunięto produkt o id: "+pId+"\n");

                        continue;
                    }
                    newArr[i++] = pr;
                }
                productsList = newArr;
            } else{
                System.out.println("Nie ma takiego produktu w slocie!\n");
            }
        }
    }


    //Przypisuje do jakiego przęsła jest przypisany slot
    public void addToPrzeslo(Przeslo prz){
        this.inPrzeslo = prz;
    }

    //Zwraca TRUE jeżeli slot jest już w jakimś przęśle
    public boolean isInPrzeslo(){
        if(this.inPrzeslo != null) return true;
        else {
            return false;
        }
    }

    //Ustawia początkową wolną przestreń na podstawie maksymalnej objętości slotu
    public void setSpace(){
        this.freeSpace = this.maxGleb*this.maxWys*this.maxSz;
    }

    //Pobiera ile pozostało objętościowo wolnego miejsca w slocie
    public float getSlotFreeSpace(){
        return this.freeSpace;
    }

    //Pobiera ile slot objętościowo posiada miejsca
    public float getSlotTotalSpace(){
        return this.maxWys*this.maxSz*this.maxGleb;
    }

    //Pokazuje ilość produktów w slocie
    public int getProductNum(){
        if(this.productsList == null) return 0;
        else{
            return this.productsList.length;
        }
    }

    public String getUklad(){
        return this.uklad;
    }
    public void setUklad(String u){
        this.uklad = u;
    }

    public Produkt[] getProduktList(){
        if(this.productsList != null){
            Produkt[] newProduktList = new Produkt[this.productsList.length];
            for(int i =0; i<this.productsList.length; i++){
                newProduktList[i] = this.productsList[i];
            }
            return newProduktList;
        } else {
            Produkt[] newProduktList = {};
            return newProduktList;
        }
    }


    @Override
    public String toString(){
        String s= "Slot ID: " +this.Id +"\n";
        s+="==========================\n";
        s+="Ilosc produktow w slocie: "+this.produkty + "\n";
        if(this.productsList!=null){
            for(Produkt p : productsList){
                s+="\tID: "+p.getId()+" | Nazwa: "+p.getName()+" | Objętość: "+p.getObjetosc()+"cm3 | Waga: "+p.getWaga()+"\n";
            }
        }
        s+="--------------------------\n";
        s+="Pojemność slotu: "+this.freeSpace+"/"+this.getSlotTotalSpace()+"\n";
        s+="--------------------------\n";
        s+="Wymiary:\n\twys:"+this.maxWys+"\n\tszer:"+this.maxSz+"\n\tgleb:"+this.maxGleb+"\n";
        s+="--------------------------\n";
        s+="Waga:"+this.totalWaga+"kg\n";
        s+="--------------------------\n";
        s+="Pozostała wolna przestrzeń:"+this.freeSpace+"cm2\n";
        s+="==========================\n\n";
        return s;
    }

}
