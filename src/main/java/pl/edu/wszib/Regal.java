package pl.edu.wszib;

public class Regal {
    private static int regId = 0;
    private int Id;

    private float x,y,z,m = 0;
    private static int regCount = 0;

    private int przesla;
    private Przeslo[] przeslaList;

    public Regal(float wys, float szer, float gleb){
        this.setId();
        this.setWymiary(wys,szer,gleb);
    }

    //
    public Regal(float x, float y, float z, int iloscPrzesel){
        this.setId();
        this.setWymiary(x,y,z);
        przeslaList = new Przeslo[iloscPrzesel];
        this.przesla = iloscPrzesel;

        for (int i = 0; i < iloscPrzesel; i++) {
            Przeslo prz = new Przeslo(x, y, z);
            this.przeslaList[i] = prz;
            prz.addToRegal(this);
        }
    }

    public Regal(Przeslo ...prz){
        this.setId();
        float x=0,y=0,z=0,w=0;

        for (Przeslo p : prz) {
            //Stała wysokość
            //Szerokość i waga
            y += p.przesloWymiary()[1];
            w += p.przesloWymiary()[3];

            //Wysokość i głębokość
            x = p.przesloWymiary()[0];
            z = p.przesloWymiary()[2];
            this.addPrzeslo(p);
        }

        this.setWymiary(x,y,z);
        this.setWaga(w);
    }

    //UStawia ID
    public void setId(){
        this.Id = Regal.regCount++;
    }

    //Ustawia wymiary
    public void setWymiary(float wys, float szer, float gleb){
        this.x = wys;
        this.y = szer;
        this.z = gleb;
    }

    //Ustawia wagę
    public void setWaga(float w){
        this.m += w;
    }

    //Dodaje do regału przęsło z argumentu metody
    public void addPrzeslo(Przeslo prz){
        if (!prz.isInRegal()) {
            if (this.przesla == 0) {
                przeslaList = new Przeslo[this.przesla + 1];
            } else if (this.przesla > 0) {
                Przeslo[] tempArr = new Przeslo[this.przesla + 1];
                for (int i = 0; i < przeslaList.length; i++) {
                    tempArr[i] = przeslaList[i];
                }
                przeslaList = tempArr;
            }
            przeslaList[this.przesla++] = prz;
            prz.addToRegal(this);
        } else {
            System.out.println("Nie można dodać slotu o id " + prz.getId() + " do tego przęsła. SLot jest już przypisany do innego przęsła!");
        }
    }
    //Dodaje produkt do pierwszego wolnego miejsca w regale, w którym jest miejsce
    public void addProduct(Produkt p){
        if(!p.checkSlot()) {
            int isAdded=0;
            for(Przeslo przeslo : this.przeslaList) {
                for (Slot slot : przeslo.getSlotsList()) {
                    if (p.productSpace() <= slot.getSlotFreeSpace()) {
                        slot.addProdukt(p);
                        przeslo.countProducts();
                        this.setWaga(p.getWaga());
                        isAdded++;
                        break;
                    }
                }
                if(isAdded==1) break;
            }
            if(isAdded==0) System.out.println("Brak miejsca na slotach w tym regale o id "+this.Id);
        } else {
            System.out.println("Produkt znajduje się już na półce!");
        }
    }
    // Pobiera produkt z regału i usuwa go ze slotu.
    public Produkt getProduct(int id){
        if(przeslaList!=null) {
            for (Przeslo prz : przeslaList) {
                for (Slot sl : prz.getSlotsList()) {
                    for (Produkt p : sl.getProduktList()) {
                        if (p.getId() == id) {
                            this.setWaga(-p.getWaga());
                            p.inSlot.delProduct(p.getId());
                            prz.countProducts();
                            return p;
                        }
                    }
                }
            }
        }
        System.out.println("Nie ma produktu o id " + id + " w regale " + this.Id);
        return null;
    }



    @Override
    public String toString(){
        String s = "";
        s+= "REGAL\n";
        s+= "==========================\n";
        s+= "ID: "+this.Id + "\n";
        s+="--------------------------\n";
        s+="Regal sklada się z : " + this.przesla + (this.przesla == 1 ? " przesla\n" : " przesel\n");
        if(this.przeslaList != null) {
            for (Przeslo prz : this.przeslaList) {
                s += "\tPrzeslo ID: "+prz.getId()+" | Ilość slotow: " + (prz.getSlotsList().length>0 ? prz.getSlotsList().length : 0) + " | Ilośc przedmiotów w przęśle: "+ prz.getProductNum() +"\n";
                for (Slot sl : prz.getSlotsList()) {
                    s += "\t\tSlot ID: " + sl.getId()+" | Liczba przedmiotów: "+sl.getProductNum()+"\n";
                    for(Produkt p : sl.getProduktList()){
                        s+="\t\t\tProdukt ID: "+p.getId()+" | Nazwa: "+p.getName()+"\n";
                    }
                }
            }
        }
        s+="--------------------------\n";
        s+="Ilosc przeseł: "+(this.przeslaList!=null ? this.przeslaList.length : 0)+"\n";
        int x =0;
        for(Przeslo prz : this.przeslaList){
            x += prz.getSlotsList().length;
        };
        s+="Ilość slotow: " + x + "\n";
        int y = 0;
        for(Przeslo prz : this.przeslaList){
            for(Slot sl : prz.getSlotsList()){
                y+=sl.getProduktList().length;
            }
        }
        s+="Ilość przedmiotów: " + y + "\n";
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

