package semestralka;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * Trida zajistujici zpracovani vstupnich dat. Na zaklade informaci ziskanych ze
 * vstupniho souboru rozhodne o dalsich informacich potrebnych pri praci s
 * temito daty.
 * 
 * @author Martin Zlamal
 */
public class DataParser {
    /**
     * Pokud je hodnota <code>addOriEdge = true</code>, potom se jedna o
     * orientovany graf.
     */
    private boolean addOriEdge = false;
    /** Cislo z intervalu <1, 4> informujici o variante grafu. */
    private final int varianta;
    /** Startovni vrchol. Vrchol ze ktereho se bude prohledavat. */
    private final String start;
    /** <code>LinkedList</code> obsahujici vrcholy. */
    private final LinkedList<String> vrcholy = new LinkedList<String>();
    /** <code>LinkdeList</code> obsahujici hrany. */
    private final LinkedList<String> hrany = new LinkedList<String>();

    /**
     * Konstruktor dataParseru zajistujici cele zpracovani dat vstupujicich ze
     * scanneru.
     * 
     * @param scan
     *            scanner ze ktereho bude trida zpracovavat data
     * @param soubor
     *            boolean hodnota nesoucí informaci o druhu vstupujících dat
     *            tzn. z klávesnice, nebo ze souboru
     */
    public DataParser(Scanner scan, boolean soubor) {
	this.varianta = scan.nextInt();
	if (!soubor) {
	    System.out.print("Startovni vrchol: ");
	}
	this.start = scan.next();
	if (!soubor) {
	    System.out.println("Hrany (ukonceni vykricnikem): ");
	}
	for (; scan.hasNext() && !scan.hasNext("!");) {
	    String tmp = scan.next();
	    if (tmp.contains("=")) {
		hrany.add(tmp);
	    } else if (tmp.contains(">")) {
		this.addOriEdge = true;
		hrany.add(tmp);
	    } else if (tmp.contains("<")) {
		this.addOriEdge = true;
		String[] pole = tmp.split("<");
		pole = swap(pole, 0, 1);
		hrany.add(pole[0] + ">" + pole[1]);
	    }
	    String[] pole = tmp.split("[=<>]+");
	    vrchol(pole[0], pole[1]);
	}
    }

    /**
     * Getr orientovane hrany.
     * 
     * @return boolean hodnota obsahujici informaci o orientaci grafu
     */
    public boolean getAddOriEdge() {
	return this.addOriEdge;
    }

    /**
     * Getr varianty.
     * 
     * @return cislo z intervalu <1, 4> informujici o variante grafu
     */
    public int getVarianta() {
	return this.varianta;
    }

    /**
     * Getr startovniho vrcholu.
     * 
     * @return startovni vrchol
     */
    public String getStart() {
	return this.start;
    }

    /**
     * Getr LinkedListu hran.
     * 
     * @return LinkedList hran
     */
    public LinkedList<String> getHrany() {
	return this.hrany;
    }

    /**
     * Getr LinkedListu vrcholů.
     * 
     * @return LinkedList vrcholů
     */
    public LinkedList<String> getVrcholy() {
	return this.vrcholy;
    }

    /**
     * Zamenuje prvky v poli podle jejich indexu.
     * 
     * @param pole
     *            pole ve kterem chceme nejake dva prvky prohodit
     * @param left
     *            index leveho vrcholu urceneho k prohozeni
     * @param right
     *            index praveho vrcholu urceneho k prohozeni
     * @return pole, ktere ma dva prvky prohozene oproti poli vstupujicimu
     */
    private String[] swap(String[] pole, int left, int right) {
	String tmp = pole[right];
	pole[right] = pole[left];
	pole[left] = tmp;
	return pole;
    }

    /**
     * Prida dva vrcholy do LinkedListu najednou. Pridava je pouze za
     * predpokladu, že LinkedList tyto vrcholy neobsahuje.
     * 
     * @param vrchol_1
     *            prvni vrchol pro pridani do LinkedListu vrcholy
     * @param vrchol_2
     *            druhy vrchol pro pridani do LinkedListu vrcholy
     */
    private void vrchol(String vrchol_1, String vrchol_2) {
	if (!vrcholy.contains(vrchol_1)) {
	    vrcholy.add(vrchol_1);
	}
	if (!vrcholy.contains(vrchol_2)) {
	    vrcholy.add(vrchol_2);
	}
    }
}