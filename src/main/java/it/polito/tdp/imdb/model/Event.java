package it.polito.tdp.imdb.model;

public class Event {
	public enum EventType{
		PRIMO_GIORNO,
		GIORNO_GENERICO,
		PAUSA
	}
	
	private EventType type;
	private Actor actor; //se sono in pausa lo considero null
}
