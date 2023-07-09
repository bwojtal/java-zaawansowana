package pl.edu.wszib;

public class Main {
    public static void main(String[] args) {

        System.out.println("########## REGAL TEST ##########");
        Produkt testreg_p1 = new Produkt("Bluza", 10,60,30,10);
        Produkt testreg_p2 = new Produkt("Spodnie", 5,5,5,10);
        Produkt testreg_p3 = new Produkt("Buty", 5,5,5,10);

        Przeslo testreg_prz1 = new Przeslo(30,60,30,3,"poziom");
        Przeslo testreg_prz2 = new Przeslo(30,60,30,3,"poziom");
        Przeslo testreg_prz3 = new Przeslo(30,60,30,3,"poziom");


        Regal testreg_reg1 = new Regal(testreg_prz1, testreg_prz2, testreg_prz3);

        System.out.println(testreg_reg1.toString());

        System.out.println("\n\t//DODAWANIE DO REGAŁU");
        testreg_reg1.addProduct(testreg_p1);
        testreg_reg1.addProduct(testreg_p2);
        testreg_reg1.addProduct(testreg_p3);


        System.out.println(testreg_reg1.toString());

        //Pobieranie produktu
        System.out.println("\n\t//POBIERANIE Z REGAŁU");
        testreg_reg1.getProduct(2);
        testreg_reg1.getProduct(36);

        System.out.println(testreg_reg1.toString());
    }
}