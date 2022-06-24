package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulatore {
/*SOLO UN'INVERTISTA AL GIORNO E OVVIAMENTE UN ATTORE NON PUO' ESSERE INTERVISTATO PIU' DI UNA VOLTA */
	
	//DATI IN INGRESSO
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private int giorni; 
	
	//DATI IN USCITA
	private int numeoPause;
	private Map<Integer, Actor> intervistati; //associo al giorno l'intervistato
	
	//STATO DEL MONDO
	List<Actor> daIntervistare; //sarebbe il vertexSet del grafo
	
	//CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;

	public Simulatore(Graph<Actor, DefaultWeightedEdge> grafo, int giorni) {
		super();
		this.grafo = grafo;
		this.giorni = giorni;
	}
	
	public void init() {
		this.daIntervistare = new ArrayList<>(this.grafo.vertexSet());
		this.numeoPause = 0;
		this.intervistati = new HashMap<>();
	}
	public void run() {
		for(int i = 1; i<=this.giorni; i++) {
			//MI TROVO NEL GIORNO i,  VEDIAMO COSA DEVO FARE...
			Random rand = new Random();
			//CASO 1: INTERVISTO IL PRIMO ATTORE OPPURE E' IL GIORNO  LA PAUSA 
			if(i == 0 || !intervistati.containsKey(i-1)) {
				Actor intervistato = daIntervistare.get(rand.nextInt(daIntervistare.size())); //oppure si potrebbe calcolare l'indice con MAth.rand()*1
			
				//aggiungo alla lista degli intervistati
				intervistati.put(i, intervistato);
				//rimuovo dalla lista da intervistare
				daIntervistare.remove(intervistato);
				
				System.out.println("Giorno: "+1+" intervistato casualmente: "+intervistato.toString());
				continue;
				
			}
			//CASO INTERVISTA 2 DELLO STESSO GENENRE PER 2 GIORNI DI FILA
			if(i>=3 && intervistati.containsKey(i-1) && intervistati.containsKey(i-2)) {
				if(intervistati.get(i-1).gender.equals(intervistati.get(i-2).gender)) {
					
					double p = Math.random()*1;
					if(p <= 0.9) { //PAUSA
						this.numeoPause ++;
						System.out.println("Giorno: "+ i +" giorno di pausa");
						continue;
					}
				}
			}
			//CALCOLO IL PROSSIMO ATTORE RACCOMANDATO
			double p = Math.random()*1; 
			if( p <= 0.6) {
				//SCELGO CASUALMENTE
				Actor intervistato = daIntervistare.get(rand.nextInt(daIntervistare.size())); 
				
				intervistati.put(i, intervistato);
		
				daIntervistare.remove(intervistato);
				
				System.out.println("Giorno: "+1+" intervistato casualmente: "+intervistato.toString());
				continue;
			}else {
				//ME LO FACCIO CONSIGLIARE
				Actor precedente = intervistati.get(i-1);
				Actor prossimo = consigliato(precedente);
				if(prossimo != null) {
					intervistati.put(i, prossimo);
					daIntervistare.remove(prossimo);
					System.out.println("Giorno: "+i+"Intervistato sotto consiglio: "+ prossimo.toString());
					continue;
				}else {
					Actor intervistato = daIntervistare.get(rand.nextInt(daIntervistare.size())); 
					intervistati.put(i, intervistato);
					daIntervistare.remove(intervistato);
					System.out.println("Giorno: "+1+" intervistato casualmente: "+intervistato.toString());
					continue;
				}			
			}
		}
	}
	
	private Actor consigliato(Actor precedente) {
		
		List<Actor> vicini = Graphs.neighborListOf(this.grafo, precedente);
		if(vicini.size() == 0) {
			return null;
		}
		List<Actor> consigliati = new ArrayList<>();
		int best =  0;
	
		for(Actor a  : vicini) {
			if(daIntervistare.contains(a)) {
				
			int peso = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(precedente, a));
			if(peso == best) {
				consigliati.add(a);
			}
			if(peso > best) {
				consigliati = new ArrayList<>();
				consigliati.add(a);
				best = peso;
			}
			}
		}
		if(consigliati.size()>1)
			return null; //NON SONO IN GRADO DI DEFINIRE UN CONSIGLIATO QUINDI SCELGO RANDOMICAMENTE
		else
			return consigliati.get(0);
	}
		
	public int getPause() {
		return this.numeoPause;
	}
	public Collection<Actor> getIntervistati(){
		return this.intervistati.values();
	}
	
}
