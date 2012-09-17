package semestralka;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Prohledavani grafu (orientovaneho i neorientovaneho) do hloubky (DFS) a do
 * sirky (BFS) pro jeho reprezentaci matici sousednosti.
 * 
 * @author Martin Zlamal
 */
public class MaticeSousednosti {
    /** Hodnota jeste nenavstiveneho vrcholu. */
    private final int FRESH = 0;
    /** Hodnota vrcholu navstiveneho, ne vsak uzavreneho. */
    private final int OPENED = 1;
    /** Hodnota uzavreneho vrcholu. */
    private final int CLOSED = 2;

    /** Privatni trida reprezentujici vrchol. */
    private class Vrchol {
	/** Uchovava hodnotu klice, resp. hodnotu vrcholu. */
	private final String klic;
	/** Uchovava stav. FRESH=0, OPENED=1, CLOSED=2 */
	private int stav;

	/**
	 * Konstruktor vrcholu zajistujici inicializaci klice a stavu vrcholu.
	 * 
	 * @param klic
	 *            hodnota klice, resp. hodnota vrcholu
	 * @param stav
	 *            stav vrcholu, FRESH=0, OPENED=1, CLOSED=2
	 */
	public Vrchol(String klic, int stav) {
	    this.klic = klic;
	    this.stav = stav;
	}

	/**
	 * Getr klice vrcholu.
	 * 
	 * @return hodnota klice
	 */
	public String getKlic() {
	    return klic;
	}

	/**
	 * Getr stavu vrcholu.
	 * 
	 * @return stav vrcholu
	 */
	public int getStav() {
	    return stav;
	}

	/**
	 * Setr stavu vrcholu.
	 * 
	 * @param stav
	 *            pripustne pouze FRESH=0, OPENED=1, CLOSED=2
	 */
	public void setStav(int stav) {
	    if (stav == OPENED || stav == CLOSED || stav == FRESH) {
		this.stav = stav;
	    }
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	    return this.getKlic() + " ";
	}

    }

    /** Privatni trida reprezentujici graf. */
    private class Graf {
	/** Index prvku pro pole <code>vrcholy</code>. */
	private int index = 0;
	/** Pole vrcholu. */
	private final Vrchol[] vrcholy;
	/** Matice uchovavajici si informaci o existenci hran. */
	private final String[][] matice;
	/**
	 * Startovni velikost. Ve skutecnosti si uchovava hodnotu
	 * <code>vrcholy.size()</code>
	 */
	private final int start;

	/**
	 * Konstruktor grafu zajistujici inicializaci pole vrcholu a matice.
	 * Zaroven pole vrcholu naplni hodnotou <code>null</code>.
	 * 
	 * @param start
	 *            startovni velikost
	 */
	public Graf(int start) {
	    this.start = start;
	    this.matice = new String[start][start];
	    this.vrcholy = new Vrchol[start];
	    for (int index = 0; index < vrcholy.length; index++) {
		vrcholy[index] = null;
	    }
	}

	/**
	 * Pridani vrcholu tzn. zmena hodnoty <code>null</code> na objekt typu
	 * Vrchol.
	 * 
	 * @param klic
	 *            hodnota vrcholu
	 */
	public void addNode(String klic) {
	    Vrchol tmp = new Vrchol(klic, FRESH);
	    vrcholy[index++] = tmp;
	}

	/**
	 * Pridani orientovane hrany grafu.
	 * 
	 * @param start
	 *            startovni vrchol ze ktereho bude hrana vychazet
	 * @param end
	 *            koncovy vrchol do ktereho bude hrana prichazet
	 */
	public void addOriEdge(String start, String end) {
	    for (int index = 0; index < this.start; index++) {
		if (vrcholy[index].getKlic().equals(start)) {
		    for (int j = 0; j < this.start; j++) {
			if (vrcholy[j].getKlic().equals(end)) {
			    matice[index][j] = "" + 1;
			}
		    }
		}
	    }
	}

	/**
	 * Pridani neorientovane hrany grafu. Ockoliv prichazi pouze pocatecni a
	 * koncový vrchol, je treba zajistit propojeni hrany v obou smerech.
	 * 
	 * @param start
	 *            startovni vrchol ze ktereho bude hrana vychazet
	 * @param end
	 *            koncový vrchol do ktereho bude hrana prichazet
	 */
	public void addNoriEdge(String start, String end) {
	    for (int index = 0; index < this.start; index++) {
		if (vrcholy[index].getKlic().equals(start)) {
		    for (int j = 0; j < this.start; j++) {
			if (vrcholy[j].getKlic().equals(end)) {
			    matice[index][j] = "" + 1;
			    matice[j][index] = "" + 1;
			}
		    }
		}
	    }
	}

	/**
	 * Hlavni metoda zajistujici prohledavani grafu (orientovaneho i
	 * neorientovaneho) do sirky (BFS).
	 * 
	 * @param start
	 *            pocatecni index ze ktereho se zacne graf prohledavat
	 * @param file
	 *            <code>PrintWriter</code> pro tisknuti do souboru
	 */
	public void BFS(String start, PrintWriter file) {
	    int pozice = -1;
	    for (int index = 0; index < vrcholy.length; index++) {
		pozice = vrcholy[index].getKlic().equals(start) ? index : -1;
		if (pozice == index) {
		    break;
		}
	    }
	    if (pozice == -1) {
		System.out.println("\nVrchol \"" + start + "\" se nepodařilo najít!");
		System.exit(1);
	    }
	    vrcholy[pozice].setStav(OPENED);
	    Queue<Integer> fronta = new LinkedList<Integer>();
	    fronta.add(pozice);
	    while (!fronta.isEmpty()) {
		int tmp = fronta.poll();
		for (int index = 0; index < matice.length; index++) {
		    if (matice[tmp][index] == "1") {
			if (vrcholy[index].getStav() == FRESH) {
			    vrcholy[index].setStav(OPENED);
			    fronta.add(index);
			}
		    }

		}
		vrcholy[tmp].setStav(CLOSED);
		if (!fronta.isEmpty()) {
		    System.out.print(vrcholy[tmp] + ", ");
		    file.print(vrcholy[tmp] + ", ");
		} else {
		    System.out.println(vrcholy[tmp]);
		    file.println(vrcholy[tmp]);
		}
	    }
	}

	/**
	 * Hlavni metoda zajistujici prohledavani grafu (orientovaneho i
	 * neorientovaneho) do hloubky (DFS).
	 * 
	 * @param start
	 *            pocatecni index ze ktereho se zacne graf prohledavat
	 * @param file
	 *            <code>PrintWriter</code> pro tisknuti do souboru
	 */
	public void DFS(String start, PrintWriter file) {
	    int pozice = -1;
	    for (int index = 0; index < vrcholy.length; index++) {
		pozice = vrcholy[index].getKlic().equals(start) ? index : -1;
		if (pozice == index) {
		    break;
		}
	    }
	    if (pozice == -1) {
		System.out.println("\nVrchol \"" + start + "\" se nepodařilo najít!");
		System.exit(1);
	    }
	    vrcholy[pozice].setStav(OPENED);
	    Stack<Integer> zasobnik = new Stack<Integer>();
	    zasobnik.add(pozice);
	    while (!zasobnik.isEmpty()) {
		int tmp = zasobnik.pop();
		for (int index = 0; index < matice.length; index++) {
		    if (matice[tmp][index] == "1") {
			if (vrcholy[index].getStav() == FRESH) {
			    vrcholy[index].setStav(OPENED);
			    zasobnik.add(index);
			}
		    }

		}
		vrcholy[tmp].setStav(CLOSED);
		if (!zasobnik.isEmpty()) {
		    System.out.print(vrcholy[tmp] + ", ");
		    file.print(vrcholy[tmp] + ", ");
		} else {
		    System.out.println(vrcholy[tmp]);
		    file.println(vrcholy[tmp]);
		}
	    }
	}
    }

    /**
     * Inicializacni metoda zajistujici obsluhu grafu a spravne vkladani vrcholu
     * a hran grafu.
     * 
     * @param dataParser
     *            objekt dataParseru nesouci hrany, vrcholy a informace o
     *            orientovani grafu, variante a startovnim vrcholu
     */
    public void init(DataParser dataParser) {
	LinkedList<String> hrany = dataParser.getHrany();
	LinkedList<String> vrcholy = dataParser.getVrcholy();
	boolean addOriEdge = dataParser.getAddOriEdge();
	int varianta = dataParser.getVarianta();
	String start = dataParser.getStart();

	Graf graf = new Graf(vrcholy.size());
	while (!vrcholy.isEmpty()) {
	    graf.addNode(vrcholy.poll());
	}

	while (!hrany.isEmpty()) {
	    String[] pole = hrany.poll().split("[=<>]+");
	    if (addOriEdge) {
		graf.addOriEdge(pole[0], pole[1]);
	    } else {
		graf.addNoriEdge(pole[0], pole[1]);
	    }
	}
	PrintWriter file = null;
	try {
	    file = new PrintWriter(new FileWriter("vystup.txt"));
	} catch (IOException e) {
	    e.printStackTrace();
	}
	if (varianta == 3) {
	    System.out.print("\nBFS(" + start + "): ");
	    file.print("BFS(" + start + "): ");
	    graf.BFS(start, file);
	} else if (varianta == 4) {
	    System.out.print("\nDFS(" + start + "): ");
	    file.print("DFS(" + start + "): ");
	    graf.DFS(start, file);
	}
	file.close();
    }
}