/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnDistante"
    private Button btnDistante; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcolaPercorso"
    private Button btnCalcolaPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB1"
    private ComboBox<Business> cmbB1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB2"
    private ComboBox<Business> cmbB2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String citta = cmbCitta.getValue();
    	if(citta == null) {
    		txtResult.appendText("Seleziona una citta'!\n");
    		return;
    	}
    	
    	txtResult.clear();
    	txtResult.appendText("GRAFO CREATO!\n"+model.creaGrafo(citta));
    	cmbCitta.setDisable(true);
    	cmbB1.getItems().addAll(this.model.getVertici());
    	cmbB2.getItems().addAll(this.model.getVertici());
    	btnCreaGrafo.setDisable(true);
    	cmbB1.setDisable(false);
    	btnDistante.setDisable(false);
    	
    	
    }

    @FXML
    void doCalcolaLocaleDistante(ActionEvent event) {
    	Business locale = cmbB1.getValue();
    	if(locale == null) {
    		txtResult.appendText("\nSeleziona un locale!\n");
    		return;
    	}
    	txtResult.appendText("\n"+model.getLocaleLontano(locale));
    	cmbB2.setDisable(false);
    	txtX2.setDisable(false);
    	btnCalcolaPercorso.setDisable(false);
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {

    	Business localePreferito = cmbB2.getValue();
    	if (localePreferito == null) {
    		txtResult.appendText("\nSeleziona un locale preferito!\n");
    		return;
    	}
    	if (localePreferito ==cmbB1.getValue()) {
    		txtResult.appendText("\nSeleziona un locale preferito diverso da quello di partenza!\n");
    	return;
    			}
    	Integer s=0;
    	try {
    		s = Integer.parseInt(txtX2.getText());
    	} catch (NumberFormatException e) {
        	this.txtResult.clear();
    		txtResult.appendText("Formato N non corretto\n");
    		return;
    	}
    	
    	txtResult.appendText(("\n"+this.model.trovaPercorso(cmbB1.getValue(), localePreferito, txtX2.getText())));
    	
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDistante != null : "fx:id=\"btnDistante\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB1 != null : "fx:id=\"cmbB1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB2 != null : "fx:id=\"cmbB2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbCitta.getItems().addAll(this.model.getCitta());
    	cmbB1.setDisable(true);
    	cmbB2.setDisable(true);
    	txtX2.setDisable(true);
    	btnCalcolaPercorso.setDisable(true);
    	btnDistante.setDisable(true);
    	
    }
}
