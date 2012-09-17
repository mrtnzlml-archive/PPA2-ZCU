package semestralka;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Prohledavani grafu (orientovaneho i neorientovaneho) do hloubky (DFS) a do
 * sirky (BFS) pro jeho reprezentaci seznamem sousednoti.
 * 
 * @author Martin Zlamal
 */
public class SeznamSousednosti {
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
	/** Uchovává odkaz na okolní sousedy. */
	private Soused sousedi;

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
	    this.sousedi = null;
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

	/**
	 * Getr sousednich vrcholu (sousedu).
	 * 
	 * @return sousední vrcholy
	 */
	public Soused getSousedi() {
	    return sousedi;
	}

	/**
	 * Setr sousedu.
	 * 
	 * @param sousedi
	 *            objekt sousedu sousedici s vrcholem
	 */
	public void setSousedi(Soused sousedi) {
	    if (sousedi != null) {
		this.sousedi = sousedi;
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

    /**
     * Privatni trida reprezentujici sousedy. Ve spojeni s dalsimi sousedy tvoři
     * spojovy seznam pro jednotlivy vrchol.
     */
    private class Soused {
	/** Uchovava hodnotu klice, resp. hodnotu vrcholu. */
	private final String vrchol;
	/** Odkaz na dalsi polozku spojoveho seznamu sousedu. */
	private Soused dalsi;

	/**
	 * Konstruktor souseda zajistujici inicializaci klice a odkazu na další
	 * položku spojového seznamu.
	 * 
	 * @param vrchol
	 *            hodnota klice, resp. hodnota vrcholu
	 */
	public Soused(String vrchol) {
	    this.vrchol = vrchol;
	    this.dalsi = null;
	}

	/**
	 * Getr klice vrcholu.
	 * 
	 * @return hodnota klice resp. hodnota vrcholu
	 */
	public String getVrchol() {
	    return vrchol;
	}

	/**
	 * Getr dalsich sousedu ze spojoveho seznamu.
	 * 
	 * @return odkaz na dalsi polozku spojoveho seznamu sousedu
	 */
	public Soused getDalsi() {
	    return dalsi;
	}

	/**
	 * Setr sousedu. Vklada souseda na misto dalsi do seznamu sousednoti.
	 * 
	 * @param dalsi
	 *            objekt typu Soused
	 */
	public void setDalsi(Soused dalsi) {
	    if (dalsi != null) {
		this.dalsi = dalsi;
	    }
	}
    }

    /** Privatni trida reprezentujici graf. */
    private class Graf {
	/** Index prvku pro pole <code>vrcholy</code>. */
	private int index = 0;
	/** Pole vrcholu. */
	private final Vrchol[] vrcholy;

	//private final int start;

	/**
	 * Konstruktor grafu zajistujici inicializaci pole vrcholu. Zaroven pole
	 * vrcholu naplni hodnotou <code>null</code>.
	 * 
	 * @param start
	 *            startovni velikost
	 */
	public Graf(int start) {
	    //this.start = start;
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
	 *            koncový vrchol do ktereho bude hrana prichazet
	 */
	public void addOriEdge(String start, String end) {
	    int pozice = indexVrcholu(start);
	    Soused sou = new Soused(end);
	    sou.setDalsi(vrcholy[pozice].getSousedi());
	    vrcholy[pozice].setSousedi(sou);
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
	    int pozice = indexVrcholu(start);
	    Soused sou1 = new Soused(end);
	    sou1.setDalsi(vrcholy[pozice].getSousedi());
	    vrcholy[pozice].setSousedi(sou1);
	    pozice = indexVrcholu(end);
	    Soused sou2 = new Soused(start);
	    sou2.setDalsi(vrcholy[pozice].getSousedi());
	    vrcholy[pozice].setSousedi(sou2);
	}

	/**
	 * Nalezne podle klice v poli vrcholu prislusny objekt a vrati jeho
	 * index v poli.
	 * 
	 * @param klic
	 *            klic objektu u kterého potřebujeme znát jeho index v poli
	 *            vrcholů
	 * @return pozice objektu v poli vrcholu
	 */
	public int indexVrcholu(String klic) {
	    int pozice = -1;
	    for (int index = 0; index < vrcholy.length; index++) {
		if (vrcholy[index].getKlic().equals(klic)) {
		    pozice = index;
		    break;
		}
	    }
	    return pozice;
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
	    int pozice = indexVrcholu(start);
	    if (pozice == -1) {
		System.out.println("\nVrchol \"" + start + "\" se nepodařilo najít!");
		System.exit(1);
	    }
	    vrcholy[pozice].setStav(OPENED);
	    Queue<Integer> fronta = new LinkedList<Integer>();
	    fronta.add(pozice);
	    while (!fronta.isEmpty()) {
		int tmp = fronta.poll();
		for (Soused act = vrcholy[tmp].getSousedi(); act != null; act = act.getDalsi()) {
		    pozice = indexVrcholu(act.getVrchol());
		    if (vrcholy[pozice].getStav() == FRESH) {
			vrcholy[pozice].setStav(OPENED);
			fronta.add(pozice);
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
	    int pozice = indexVrcholu(start);
	    if (pozice == -1) {
		System.out.println("\nVrchol \"" + start + "\" se nepodařilo najít!");
		System.exit(1);
	    }
	    vrcholy[pozice].setStav(OPENED);
	    Stack<Integer> zasobnik = new Stack<Integer>();
	    zasobnik.add(pozice);
	    while (!zasobnik.isEmpty()) {
		int tmp = zasobnik.pop();
		for (Soused act = vrcholy[tmp].getSousedi(); act != null; act = act.getDalsi()) {
		    pozice = indexVrcholu(act.getVrchol());
		    if (vrcholy[pozice].getStav() == FRESH) {
			vrcholy[pozice].setStav(OPENED);
			zasobnik.add(pozice);
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
	if (varianta == 1) {
	    System.out.print("\nBFS(" + start + "): ");
	    file.print("BFS(" + start + "): ");
	    graf.BFS(start, file);
	} else if (varianta == 2) {
	    System.out.print("\nDFS(" + start + "): ");
	    file.print("DFS(" + start + "): ");
	    graf.DFS(start, file);
	}
	file.close();
    }
}