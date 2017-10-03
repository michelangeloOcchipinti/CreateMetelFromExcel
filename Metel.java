package application;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.io.*;
import javafx.event.*;
import javafx.geometry.*;

public class Metel extends Application{
	
	private Label lbl_instruction;
	private Label lbl_date_start;
	private TextField date_start;
	private Label lbl_num_catalogue;
	private TextField num_catalogue;
	private Button btn_take_excel;
	private Button btn_make_metel;
	private Label lbl_confirmation;
	private Label lbl_brand;
	private RadioButton bot, kai;
	
	
	public static void main(String args[]) {
		
		launch(args);
		
	}
	
	@Override
	public void start(Stage myStage) {
		
		lbl_instruction = new Label();
		lbl_instruction.setText("\n	Benvenuti nel programma Create Metel di Bot Lighting Srl.\n\n"
				+ "	Il file excel che verrà caricato dovra' avere le seguenti colonne con i relativi titoli:\n\n "
				+ "	• Cod_Articolo\n"
				+ "	• Barcode\n"
				+ "	• Descri_Articolo\n"
				+ "	• Pz_x_Conf\n"
				+ "	• Prezzo\n"
				+ "	• Famiglia\n");
		
		lbl_date_start = new Label("Inserisci la data di decorrenza:");
		date_start = new TextField();
		date_start.setPromptText("Inserisci la data di decorrenza");
		date_start.setMinWidth(157);
		
		lbl_num_catalogue = new Label("Inserisci il numero di catalogo:");
		num_catalogue = new TextField();
		num_catalogue.setPromptText("Inserisci il numero consecutivo di catalogo");
		num_catalogue.setMinWidth(150);
		
		lbl_brand = new Label("Inserisci il produttore");
		
		HBox instruction = new HBox();
		instruction.getChildren().addAll(lbl_instruction);
		
		HBox labelsTextAreaFields = new HBox();
		labelsTextAreaFields.getChildren().addAll(lbl_date_start, lbl_num_catalogue);
		labelsTextAreaFields.setSpacing(20);
		labelsTextAreaFields.setMargin(lbl_date_start, new Insets(20,0,0,25));
		labelsTextAreaFields.setMargin(num_catalogue, new Insets(20,0,0,0));
		
		HBox textAreaFields = new HBox();
		textAreaFields.getChildren().addAll(date_start, num_catalogue);
		textAreaFields.setSpacing(20);
		textAreaFields.setMargin(date_start, new Insets(20,0,0,25));
		textAreaFields.setMargin(num_catalogue, new Insets(20,0,0,0));
		
		bot = new RadioButton("BOT");
		kai = new RadioButton("KAI");
		
		ToggleGroup brandRadioButton = new ToggleGroup();
		bot.setToggleGroup(brandRadioButton);
		kai.setToggleGroup(brandRadioButton);
		
		FlowPane paneForTextFieldsAndLabels = new FlowPane(Orientation.HORIZONTAL,
				20, 5, lbl_date_start, lbl_num_catalogue, lbl_brand, date_start, num_catalogue,  bot, kai);
		paneForTextFieldsAndLabels.setMaxWidth(2000);
		paneForTextFieldsAndLabels.setMinWidth(600);
		paneForTextFieldsAndLabels.setMargin(bot, new Insets(15));
		paneForTextFieldsAndLabels.setMargin(kai, new Insets(-25));
		
		BorderPane mainPane = new BorderPane();
		mainPane.setTop(instruction);
		mainPane.setLeft(paneForTextFieldsAndLabels);
		mainPane.setMargin(paneForTextFieldsAndLabels, new Insets(25));
		
		
		Scene scene1 = new Scene(mainPane, 520, 500);
		
		myStage.setTitle("Create Metel by Bot Lighting Srl");
		myStage.setScene(scene1);
		myStage.show();
	}
	
}