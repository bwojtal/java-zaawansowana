package pl.edu.wszib;
import javax.management.DescriptorKey;

public class Przeslo {
    private static int przeslaCount = 0;
    private float x,y,z,m = 0;
    private int Id;

    private int sloty = 0;
    private int productsInPrzeslo = 0;

    private Regal inRegal;
    private Slot[] slotsList;

    //Konstruktor przyjmujący jako argumenty slot/y na podstawie których ma zostać stworzone przęsło
    public Przeslo(Slot ...s){
        this.setId();
        float x=0,y=0,z=0,w=0;

        if(s.length<=3&&s.length>0) {
            for (Slot sl : s) {
                if(sl.getUklad()=="pion") {
                    x = sl.slotWymiary()[0];
                    y += sl.slotWymiary()[1];
                    z = sl.slotWymiary()[2];
                    w += sl.slotWymiary()[3];
                    this.addSlot(sl);
                }else if(sl.getUklad()=="poziom"){
                    x += sl.slotWymiary()[0];
                    y = sl.slotWymiary()[1];
                    z = sl.slotWymiary()[2];
                    w += sl.slotWymiary()[3];
                    this.addSlot(sl);
                }
            }
        }else {
            for (int i=0; i<3; i++) {
                if(s[i].getUklad()=="pion") {
                    x = s[i].slotWymiary()[0];
                    y += s[i].slotWymiary()[1];
                    z = s[i].slotWymiary()[2];
                    w += s[i].slotWymiary()[3];
                    this.addSlot(s[i]);
                } else if(s[i].getUklad()=="poziom"){
                    x += s[i].slotWymiary()[0];
                    y = s[i].slotWymiary()[1];
                    z = s[i].slotWymiary()[2];
                    w += s[i].slotWymiary()[3];
                    this.addSlot(s[i]);
                }
            }
            System.out.println("Lista slotów była większa niż 3, dodano 3 pierwsze sloty!\n");
        }
        this.setWymiary(x,y,z);
        this.setWaga(w);
    }

    public Przeslo(float x, float y, float z){
        this.setId();
        this.setWymiary(x,y,z);
        slotsList = new Slot[3];
        this.sloty = 3;
        for (int i = 0; i < 3; i++) {

            float m = x / 3;
            Slot s = new Slot(m, y, z);
            this.slotsList[i] = s;
            s.addToPrzeslo(this);

        }
    }
    public Przeslo(float x, float y, float z, int iloscSlotow, String uklad){
        this.setId();
        this.setWymiary(x,y,z);
        if(iloscSlotow>=1&&iloscSlotow<=3) {
            slotsList = new Slot[iloscSlotow];
            this.sloty = iloscSlotow;
            for (int i = 0; i < iloscSlotow; i++) {
                if(uklad == "pion"){
                    float m = y/iloscSlotow;
                    Slot s = new Slot(x, m, z);
                    s.setUklad("pion");
                    this.slotsList[i] = s;
                    s.addToPrzeslo(this);
                } else if(uklad == "poziom"){
                    float m = x/iloscSlotow;
                    Slot s = new Slot(m, y, z);
                    this.slotsList[i] = s;
                    s.addToPrzeslo(this);
                }
            }
        }else if(iloscSlotow>3||iloscSlotow<1){
            System.out.println("Zła ilość slotów! Wybierz pomiędzy 1 a 3");
        } else if(uklad!="poziom"||uklad!="pion"){
            System.out.println("Nie można utworzyć slotów! Wybrano zły układ, wybierz \"pion\" lub \"poziom\"");
        }
    }

    //Ustawia ID przęsłą
    public void setId(){
        this.Id = Przeslo.przeslaCount++;
    }

    //Ustawa wymiary przęsłą
    public void setWymiary(float wys, float szer, float gleb){
        this.x = wys;
        this.y = szer;
        this.z=gleb;
    }
    //Zwraca tablicę, w której znajdują się wymiary przęsła [0] = wys, [1] = szer, [2] = gleb, [3] = waga
    public float[] przesloWymiary(){
        float[] wym = {this.x, this.y, this.z, this.m};
        return wym;
    }

    //Dodaje wagę do łącznej wagi przęsłą
    public void setWaga(float waga){
        this.m = +waga;
    }

    //Dodaje produkt do pierwszego wolnego, mającego wystarczająco miejsca slotu w przęśle
    public void addProduct(Produkt p){
        if(!p.checkSlot()) {
            int isAdded=0;
            for (Slot slot: this.slotsList) {
                if(p.productSpace()<=slot.getSlotFreeSpace()){
                    slot.addProdukt(p);
                    this.countProducts();
                    this.setWaga(slot.getWaga());
                    isAdded++;
                    break;
                }
            }
            if(isAdded==0) System.out.println("Brak miejsca na slotach w przesle nr "+this.Id);
        } else {
            System.out.println("Produkt znajduje się już na półce!");
        }
    }

    //Pobiera dane produktu w przęśle (jeżeli jest) i zwraca ten produkt. Czy ta funkcja ma też usuwać produkt?
    public Produkt getProduct(int produktId){
        for(Slot sl : slotsList){

//            if (sl.productsList!=null) {
            if (sl.getProduktList()!=null) {

                for (Produkt p : sl.getProduktList()) {
                    if (p.getId() == produktId) {
                        System.out.println("Znalazlem produkt \n"+"\tProdukt ID: " + produktId + " ---> " + "Slot ID: "+sl.getId()+"\n\tNazwa: " + p.getName()+"\n" );
                        //Czy ta metoda ma usuwać przedmiot ze slotu?
                        //p.inSlot.delProduct(p.getId());
                        return p;
                    }
                }
            }


        }
        System.out.println("Nie ma produktu o ID: "+produktId+ " w tym przesle!\n");
        return null;
    }

    //Zwraca ile jest produktów w przęśle
    public int getProductNum(){
        return this.productsInPrzeslo;
    }

    //Dodaje slot do przęsła
    public void addSlot(Slot s){
        if(this.sloty<3){
            if (!s.isInPrzeslo()) {
                if (this.sloty == 0) {
                    slotsList = new Slot[this.sloty + 1];
                } else if (this.sloty > 0) {
                    Slot[] tempArr = new Slot[this.sloty + 1];
                    for (int i = 0; i < slotsList.length; i++) {
                        tempArr[i] = slotsList[i];
                    }
                    slotsList = tempArr;
                }
                slotsList[this.sloty] = s;
                this.sloty++;
                System.out.println("Pomyślnie dodano slot ID "+s.getId()+" do przęsła o ID " + this.Id);
                s.addToPrzeslo(this);
            } else{
                System.out.println("Nie można dodać slotu o id "+s.getId()+" do tego przęsła. SLot jest już przypisany do innego przęsła!");
            }
        } else {
            System.out.println("Nie można dodać slotu! Max ilość slotów to 3.");
        }
    }


    //Zwraca ile jest produktów w przęśle
    public int countProducts(){
        int x=0;
        for(Slot sl : slotsList){
            x+=sl.getProductNum();
        }
        this.productsInPrzeslo = x;
        return x;
    }

    //Pokazuje produkty w przęśle
    public void showProducts(){
        if(productsInPrzeslo == 0) System.out.println("Brak produktów w przęśle!");
        else{
            System.out.println("Produkty w przęśle ID "+this.Id);
            for(Slot sl: slotsList){
                System.out.println("W slocie o ID: " + sl.getId() + " znajduje się: ");
                for(Produkt p : sl.getProduktList()){
                    System.out.println("\tID: " + p.getId() + " | Nazwa: " + p.getName() + " | Waga: " + p.prodWymiary()[3] +"kg");
                }
            }
        }
    }

    //Funkcja sprawdzająca czy przęsło nie posiada błędów w układzie
    public Boolean isOk(){
        float sumWys=0, sumSzer=0, sumGleb=0;

        for(Slot sl: slotsList){
            if(sl.getUklad()=="poziom") {
                sumWys += sl.slotWymiary()[0];
                sumSzer = sl.slotWymiary()[1];
                sumGleb = sl.slotWymiary()[2];
            } else if(sl.getUklad()=="pion"){
                sumWys = sl.slotWymiary()[0];
                sumSzer += sl.slotWymiary()[1];
                sumGleb = sl.slotWymiary()[2];
            }
        }

        if(sumWys == this.x && sumSzer == this.y && sumGleb == this.z) {
            System.out.println("Przęsło jest poprawne.");
            return true;
        }
        else {
            System.out.println("Niepoprawne przęsło!");
            return false;
        }
    }

    //Dodaje przęsło do regału
    public void addToRegal(Regal regal) {
        this.inRegal = regal;
    }

    //Zwraca TRUE jeżeli przęsło jest już w jakimś regale
    public boolean isInRegal() {
        if(inRegal!=null) return true;
        else {
            return false;
        }
    }

    //Pobiera ID przęsła
    public int getId() {
        return this.Id;
    }

    public Slot[] getSlotsList(){
        if(this.slotsList != null){
            Slot[] newSlotsList = new Slot[this.slotsList.length];

            for(int i =0; i<this.slotsList.length; i++){
                newSlotsList[i] = this.slotsList[i];
            }

            return newSlotsList;
        } else {
            Slot[] newSlotsList = {};
            return newSlotsList;
        }
    }

    @Override
    public String toString(){
        String s = "";
        s+= "PRZESLO\n";
        s+= "==========================\n";
        s+= "ID: "+this.Id + "\n";
        s+="--------------------------\n";
        s+="Ilosc slotow w przesle: "+this.sloty+"\n";
        if(slotsList != null) {
            for (Slot sl : this.slotsList) {
                int p = (sl.getProduktList() != null ? sl.getProduktList().length : 0);
                s += "\tSlot ID: "+sl.getId()+" | Ilość przedmiotów w slocie: "+ p +" | Pozostała wolna przestrzeń "+ sl.getSlotFreeSpace() + "/" + sl.getSlotTotalSpace() + "\n";
            }
        }
        s+="--------------------------\n";
        s+="Ilosc produktow w przesle: "+this.countProducts()+"\n";
        s+="--------------------------\n";
        s+="Wymiary: \n";
        s+="\tWys: "+this.x + " cm\n";
        s+="\tSzer: "+this.y + " cm\n";
        s+="\tGleb: "+this.z + " cm\n";
        s+="\tWaga: "+this.m+" kg\n";
        s+= "==========================\n";
        return s;
    }
}

