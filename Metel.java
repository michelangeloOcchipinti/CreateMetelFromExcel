package application;

import javafx.application.*;
import javafx.stage.*;
import javafx.stage.FileChooser;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.io.*;
import javafx.event.*;
import javafx.geometry.*;
import java.awt.Desktop;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.Iterator;



public class Metel extends Application implements EventHandler<ActionEvent>{
	
	private Desktop desktop = Desktop.getDesktop();
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
	private FileChooser fileChooser = new FileChooser();
	private File fileExcel;
	private PrintWriter fileMetel;
	private FileInputStream fIP;
	private XSSFWorkbook workbook;
	static XSSFRow row;
	
	

	
	
	public static void main(String args[]) {
		
		launch(args);
		
	}
	
	@Override
	public void start(Stage myStage) {
		
		lbl_instruction = new Label();
		lbl_instruction.setText("\n	Benvenuti nel programma Create Metel di Bot Lighting Srl.\n"
				+ "\n        Il file Excel dovrà avere le seguenti colonne:\n"
				+ "\n        • Codice prodotto\n"
				+ "        • EAN\n"
				+ "        • Descrizione\n"
				+ "        • Q'tà massima ordine\n"
				+ "        • Prezzo al rivenditore\n"
				+ "        • Stato prodotto\n"
				+ "        • Famiglia di sconto\n"
				+ "        • Electrocod (campo vuoto)\n"
				+ "        • Barcode (campo vuoto)\n"
				+ "        • Qualificatore Barcode (campo vuoto)\n");
		
		lbl_date_start = new Label("Inserisci la data inizio catalogo:");
		date_start = new TextField();
		date_start.setText(null);
		date_start.setPromptText("DDMMAAAA");
		date_start.setMinWidth(160);
		
		lbl_num_catalogue = new Label("Inserisci il numero di catalogo:");
		num_catalogue = new TextField();
		num_catalogue.setText(null);
		num_catalogue.setPromptText("Tre cifre es: 021");
		num_catalogue.setMinWidth(160);
		
		lbl_brand = new Label("Scegli il produttore:");
		
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
		
		btn_take_excel = new Button("Aggiungi Excel");
		btn_make_metel = new Button("Crea Metel");
		btn_make_metel.setOnAction(this);
		btn_take_excel.setMinWidth(160);
		btn_make_metel.setMinWidth(160);
		btn_take_excel.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				fileExcel = fileChooser.showOpenDialog(myStage);
			}
		});
		
		FlowPane paneForTextFieldsAndLabels = new FlowPane(Orientation.HORIZONTAL,
				20, 5, lbl_date_start, lbl_num_catalogue, lbl_brand, date_start, num_catalogue,  bot, kai, btn_take_excel, btn_make_metel);
		paneForTextFieldsAndLabels.setMaxWidth(1000);
		paneForTextFieldsAndLabels.setMinWidth(500);
		
		paneForTextFieldsAndLabels.setMargin(bot, new Insets(0));
		paneForTextFieldsAndLabels.setMargin(kai, new Insets(0,0,0,-10));
		paneForTextFieldsAndLabels.setMargin(btn_take_excel, new Insets(20,0,0,-2));
		paneForTextFieldsAndLabels.setMargin(btn_make_metel, new Insets(20,0,0,2));
		
		lbl_confirmation = new Label("Inizia inserendo i dati e il percorso del file Excel. In seguito clicca sul tasto Crea Metel.");
		
		
		BorderPane mainPane = new BorderPane();
		mainPane.setTop(instruction);
		mainPane.setLeft(paneForTextFieldsAndLabels);
		mainPane.setBottom(lbl_confirmation);
		mainPane.setAlignment(lbl_confirmation, Pos.BOTTOM_LEFT);
		mainPane.setMargin(lbl_confirmation, new Insets(0,0,50,25));
		mainPane.setMargin(paneForTextFieldsAndLabels, new Insets(25));
		
		
		
		Scene scene1 = new Scene(mainPane, 520, 500);
		
		btn_take_excel.requestFocus();
		
		myStage.setScene(scene1);
		myStage.show();
	}
	
	public void handle(ActionEvent e) {
		
		
		 try {
			 
         	if (num_catalogue.getText()!=null && date_start.getText()!=null && (bot.isSelected() || kai.isSelected())) {
         		try {
         			fileMetel = new PrintWriter("metel.TXT");
         			}
         			catch(FileNotFoundException f) {
         				lbl_confirmation.setText("Non è stato possibile creare o scrivere nel file Metel");
         			}
         			try {
         			 fIP = new FileInputStream(fileExcel.getPath());
         			}
         			catch(FileNotFoundException g) {
         				lbl_confirmation.setText("Non è stato possibile creare o scrivere nel file Excel");
         			}
         			try {
         				workbook = new XSSFWorkbook(fIP);
         				}
         				catch(IOException g) {
         					lbl_confirmation.setText("Non è stato possibile creare o scrivere nel file Excel");
         				} 
         			XSSFSheet spreadsheet = workbook.getSheetAt(0);
         			Row heading = spreadsheet.getRow(0);
         		fileMetel = new PrintWriter(new BufferedWriter(new FileWriter("metel.TXT", true)));
         		fileMetel.print("LISTINO METEL       BTL01733730285BTL   "+date_start.getText()+date_start.getText()+"LISTINO GENERALE                                                     "
         						+num_catalogue.getText()+"                                                                                                         ");
         		fileMetel.flush();
         		fileMetel.println();
         		
         	
         	Iterator < Row > rowIterator = spreadsheet.iterator();
         	while (rowIterator.hasNext()) {
         		row = (XSSFRow) rowIterator.next();
         		if (bot.isSelected()) {
         			fileMetel.print("BTL");
         		}
         		else {
         			fileMetel.print("KBT");
         		}
         		/*if (String.valueOf(row.getCell(0)).length()<14){
         			
         		}*/
         		fileMetel.print(String.valueOf(row.getCell(0))+String.valueOf(row.getCell(1)+String.valueOf(row.getCell(2))+String.valueOf(row.getCell(3))+String.valueOf(row.getCell(4))+String.valueOf(row.getCell(5))+String.valueOf(row.getCell(6))+String.valueOf(row.getCell(7))));
         		fileMetel.flush();
         		fileMetel.println();
         		
         	}
         	lbl_confirmation.setText("File Metel.TXT creato correttamente");
         	}
         	
         	else {
         		lbl_confirmation.setText("Mancano la data, il numero di catalogo oppure non è stato selezionato il fornitore");
         		
         	}
         	
		 }
		 catch(IOException h) {
         	System.out.println(h);
         }
		 
		 fileMetel.close();

	}
	
			
		         
	      }
	
	
