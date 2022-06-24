package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	ImdbDAO dao;
	Graph<Actor, DefaultWeightedEdge>grafo;
	List<Actor> actors;
	Simulatore s;
	
	public Model() {
		this.dao = new ImdbDAO();
	}
	public List<String> getGenres(){
		return this.dao.getAllGenres();
	}
	public void creaGrafo(String genere) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.actors = this.dao.listAllActorsGenre(genere);
		Map<Integer, Actor> mActors = new TreeMap<>();
		for(Actor a : actors) {
			mActors.put(a.getId(), a);
		}
		Graphs.addAllVertices(this.grafo, actors);
		
		List<Adiacenza> adiacenze  = this.dao.getAdiacenze(genere, mActors);
		
		for(Adiacenza a : adiacenze) {
			Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		}
		
		System.out.println("GRAFO CREATO\n# VERTICI : "+this.grafo.vertexSet().size()+"\n# ARCHI : "+this.grafo.edgeSet().size());
		
	}
	public List<Actor> getActors(){
		return this.actors;
	}
	public Graph<Actor, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	public List<Actor> getSimili(Actor a){
		List<Actor> result;
		
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.grafo);
		result = new ArrayList<>(ci.connectedSetOf(a));
		
		result.remove(a);
		Collections.sort(result, new Comparator<Actor>() {

			@Override
			public int compare(Actor o1, Actor o2) {
				// TODO Auto-generated method stub
				return  o1.lastName.compareTo(o2.lastName);
			}
			
		});
		
		return result; 
	}
	
	public void attivaSimulazione(int giorni) {
		s = new Simulatore(this.grafo, giorni);
		s.init();
		s.run();
	}
	public int getPause() {
		return this.s.getPause();
	}
	public Collection <Actor> getIntervistati(){
		return this.s.getIntervistati();
	}
	
}
