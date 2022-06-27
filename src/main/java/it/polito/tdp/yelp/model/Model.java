package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao = new YelpDao();
	private List<Business> locali = new ArrayList<>();
	private Graph<Business, DefaultWeightedEdge> grafo;
	private List<Business> percorsoMigliore;
	
	
	
	public String creaGrafo(String citta) {
		this.grafo = new SimpleWeightedGraph<Business, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.locali = dao.getBusinessByCity(citta);
		Graphs.addAllVertices(this.grafo, this.locali);
		
		for(Business b1 : this.locali) {
			for(Business b2 : this.locali) {
				if(!b1.equals(b2)) {
					Double latB1 = b1.getLatitude();
					Double lngB1 =b1.getLongitude();
					Double latB2 =b2.getLatitude();
					Double lngB2= b2.getLongitude();
					Double peso = LatLngTool.distance(new LatLng(latB1,lngB1), 
							new LatLng(latB2, lngB2), 
							LengthUnit.KILOMETER);

							Graphs.addEdge(this.grafo, b1, b2, peso);
				}
			}
		}
		return "#VERTICI: "+this.grafo.vertexSet().size()+
				" #ARCHI: "+this.grafo.edgeSet().size();
		//System.out.println(this.grafo.vertexSet().size());
		//System.out.println(this.grafo.edgeSet().size());
	}
	public List<String> getCitta() {
		
		return this.dao.getCity();
	}
	public Set<Business> getVertici() {
		//System.out.println(this.grafo.vertexSet().size());
		return this.grafo.vertexSet();
	}
	public String getLocaleLontano(Business partenza) {
		Double peso = 0.0;
		Business localeDistante=null;
		List<Business> vicini = Graphs.neighborListOf(this.grafo, partenza);
		for(Business vicino : vicini) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(vicino, partenza))>peso) {
				peso = this.grafo.getEdgeWeight(this.grafo.getEdge(vicino, partenza));
				localeDistante = vicino;
			}
		}
		return localeDistante+" "+peso;
	}
	public String trovaPercorso(Business partenza, Business arrivo, String soglia) {
		this.percorsoMigliore = new LinkedList<>();
		List<Business> parziale = new LinkedList<>();
		parziale.add(partenza);
		
		cerca(arrivo, parziale,soglia); 		//il primo nodo è sicuramente la sorgente
		return this.percorsoMigliore+"\n#NODI ATTRAVERSATI: "
		+(this.percorsoMigliore.size());
		
	}

private void cerca(Business arrivo, List<Business> parziale, String soglia) {
	
	Business ultimo = parziale.get(parziale.size()-1) ;
	Integer s = Integer.parseInt(soglia);
	
	// caso terminale: ho trovato l'arrivo
	if(ultimo.equals(arrivo)) {
		if(this.percorsoMigliore==null) {
			this.percorsoMigliore = new ArrayList<>(parziale) ;
			return ;
		} else if( parziale.size() > this.percorsoMigliore.size() ) {
			// NOTA: per calcolare i percorsi più lunghi, basta
			// mettere > nell'istuzione precedente
			this.percorsoMigliore = new ArrayList<>(parziale) ;
			return ;
		} else {
			return ;
		}
	}
	
	// generazione dei percorsi
	// cerca i successori di 'ultimo'
	for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(ultimo)) {
		if(this.grafo.getEdgeSource(e).getStars()>s) {
			// vai
			
			Business prossimo = Graphs.getOppositeVertex(this.grafo, e, ultimo) ;
			
			if(!parziale.contains(prossimo)) { // evita i cicli
				parziale.add(prossimo);
				cerca(arrivo, parziale,soglia);
				parziale.remove(parziale.size()-1) ;
			}
		}
	}	
}
}
