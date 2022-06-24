/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;
	boolean grafoCreato = false;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	if(this.grafoCreato == false) {
    		txtResult.setText("DEVI PRIMA CREARE IL GRAFO");
    	}else {
    		Actor a = this.boxAttore.getValue();
    		if(a == null) {
    			txtResult.setText("SELEZIONA UN ATTORE");
    		}else {
    			//trovo gli attori simili
    			List<Actor> actors = this.model.getSimili(a);
    			for(Actor aa : actors) {
    				txtResult.appendText("\n"+aa.toString());
    			}
    		}
    		
    		
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String genere = this.boxGenere.getValue();
    	if(genere.equals(null)) {
    		txtResult.setText("SELEZIONA UN GENERE");
    	}else {
    		this.model.creaGrafo(genere);
    		Graph<Actor, DefaultWeightedEdge>grafo = this.model.getGrafo();
    		
    		txtResult.setText("GRAFO CREATO:\n");
    		txtResult.appendText("# VERTICI :"+ grafo.vertexSet().size() );
    		txtResult.appendText("\n# ARCHI :"+ grafo.edgeSet().size() );
    		
    		this.grafoCreato = true;
    		
    		this.boxAttore.getItems().clear();
    		this.boxAttore.getItems().addAll(this.model.getActors());
    	}
 
    
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	if(this.grafoCreato==false) {
    		txtResult.setText("DEVI PRIMA CREARE IL GRAFO");
    	}else {
    		String n = txtGiorni.getText();
    		try {
    			int giorni = Integer.parseInt(n);
    			this.model.attivaSimulazione(giorni);
    			
    			int pause = this.model.getPause();
    			List<Actor> intervistati = (List<Actor>) this.model.getIntervistati();
    			txtResult.setText("NUMERO DI PAUSE : "+pause);
    			for(Actor a : intervistati) {
    				txtResult.appendText("\n"+a.toString());
    			}
    		}catch(NumberFormatException e) {
    			e.printStackTrace();
    			txtResult.setText("DEVI INSERIRE UN NUMERO");
    		}
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.boxGenere.getItems().clear();
    	this.boxGenere.getItems().addAll(this.model.getGenres());
    }
}
