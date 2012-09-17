package semestralka;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Prohledavani grafu (orientovaneho i neorientovaneho) do hloubky (DFS) a do
 * sirky (BFS) pro jeho reprezentace seznamem sousednosti a matici sousednosti.
 * 
 * @author Martin Zlamal
 * @version 1.0
 */
public class Main {

    /**
     * Hlavni metoda main, ktera zprostredkovava obsluhu trid a dataParseru.
     * 
     * @param args
     *            pole argumentu, ocekava nazev souboru, kde jsou vstupni data
     */
    public static void main(String[] args) {
	boolean soubor = false;
	Scanner scan = null;
	if (args.length != 0) {
	    try {
		scan = new Scanner(new File(args[0]));
		soubor = true;
	    } catch (IOException exc) {
		System.err.println("Soubor \"" + args[0] + "\" nebyl nalezen!");
		exc.printStackTrace();
	    }
	} else {
	    scan = new Scanner(System.in);
	}
	System.out.println("Vyberte prosim jednu z nasledujicich variant:");
	System.out.println("1) Prohledavani do sirky seznamem sousednosti - SS_BFS");
	System.out.println("2) Prohledavani do hloubky seznamem sousednosti - SS_DFS");
	System.out.println("3) Prohledavani do sirky matici sousednosti - MS_BFS");
	System.out.println("4) Prohledavani do hloubky matici sousednosti - MS_DFS");
	System.out.print("Varianta: ");

	DataParser dataParser = new DataParser(scan, soubor);
	int varianta = dataParser.getVarianta();
	if (args.length != 0) {
	    System.out.println(varianta);
	}

	switch (varianta) {
	case 1:
	case 2:
	    SeznamSousednosti tmp1 = new SeznamSousednosti();
	    tmp1.init(dataParser);
	    break;
	case 3:
	case 4:
	    MaticeSousednosti tmp2 = new MaticeSousednosti();
	    tmp2.init(dataParser);
	    break;
	default:
	    System.out.println("\nTuto variantu nemam naprogramovanou!");
	}
    }
}