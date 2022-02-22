package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
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
	private Graph<Business, DefaultWeightedEdge> grafo ;
	private List<Business> vertici ;
	private List<Business> percorsoBest;
	private Map<String, Business> verticiIdMap ;



	public List<String> getAllCities() {
		YelpDao dao = new YelpDao();
				return dao.getAllCities();
	}
	
	
	public String creaGrafo(String city) {
		this.grafo = new SimpleWeightedGraph<Business, DefaultWeightedEdge>(DefaultWeightedEdge.class) ;
		YelpDao dao = new YelpDao() ;
		this.vertici = dao.getBusinessByCity(city) ;
		this.verticiIdMap = new HashMap<>() ;
		for(Business b : this.vertici)
			this.verticiIdMap.put(b.getBusinessId(), b) ;
		
		
		Graphs.addAllVertices(this.grafo, this.vertici) ;
		
		
			
		
		
		/*for(Business v1 : this.grafo.vertexSet()) {
			for(Business v2 : this.grafo.vertexSet()) {
				if(!v1.equals(v2)) {
					if(this.grafo.getEdge(v1, v2) == null) {
						Double latMediaV1 = dao.getLatMedia(v1);
						Double latMediaV2 = dao.getLatMedia(v2);
						
						Double lonMediaV1 = dao.getLonMedia(v1);
						Double lonMediaV2 = dao.getLonMedia(v2);
						
						Double distanzaMedia = LatLngTool.distance(new LatLng(latMediaV1,lonMediaV1), 
																	new LatLng(latMediaV2, lonMediaV2), 
																	LengthUnit.KILOMETER);
						
						Graphs.addEdgeWithVertices(this.grafo, v1, v2, distanzaMedia);
						verticiDistanze.put(v2, distanzaMedia);
						System.out.println(distanzaMedia+"");
					}
				}
			}
			*/ 
			List<ArcoGrafo> archi = dao.calcolaArchi(city) ;
			for(ArcoGrafo arco : archi) {
				Graphs.addEdge(this.grafo,
						this.verticiIdMap.get(arco.getBusinessId1()),
						this.verticiIdMap.get(arco.getBusinessId2()), 
						arco.getPeso()) ;
			}
			
		
		
				
		
		return String.format("Grafo creato con %d vertici e %d archi\n",
				this.grafo.vertexSet().size(),
				this.grafo.edgeSet().size()) ;
}

	
	public String  getLontano(Business locale) {
		
		if(grafo==null)
			return null;
		Business lontano = null;
		Double distanzaMassima=0.0;

		
		for (Business b : this.grafo.vertexSet()) {
			if(!b.equals(locale)) {
				Double distanza = this.grafo.getEdgeWeight(this.grafo.getEdge(locale, b));
				
				if (distanza > distanzaMassima) {
					distanzaMassima =distanza;
					lontano = b;
				}
			}
		}
		
		return "Locale più distante=\n"+lontano+" "+distanzaMassima;
	}

	public List<Business> percorsoMigliore(Business partenza, Business arrivo, double soglia) {
		this.percorsoBest = null ;
		
		List<Business> parziale = new ArrayList<Business>() ;
		parziale.add(partenza) ;
		
		cerca(parziale, 1, arrivo, soglia) ;
		
		return this.percorsoBest ;
	}
	
private void cerca(List<Business> parziale, int livello, Business arrivo, double soglia) {
		
		Business ultimo = parziale.get(parziale.size()-1) ;
		
		// caso terminale: ho trovato l'arrivo
		if(ultimo.equals(arrivo)) {
			if(this.percorsoBest==null) {
				this.percorsoBest = new ArrayList<>(parziale) ;
				return ;
			} else if( parziale.size() > this.percorsoBest.size() ) {
				
				this.percorsoBest = new ArrayList<>(parziale) ;
				return ;
			} else {
				return ;
			}
		}
		
		// generazione dei percorsi
		// cerca i successori di 'ultimo'
		for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(ultimo)) {
			
				// vai
				
				Business prossimo = Graphs.getOppositeVertex(this.grafo, e, ultimo) ;
				
				if(!parziale.contains(prossimo) && prossimo.getStars()>soglia) { // evita i cicli
					parziale.add(prossimo);
					cerca(parziale, livello + 1, arrivo, soglia);
					parziale.remove(parziale.size()-1) ;
				}
			
		}	
	}
	
	public List<Business> getVertici() {
		return this.vertici ;
	}
}
