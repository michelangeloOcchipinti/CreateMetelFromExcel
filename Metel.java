package application;

//Imported all needed library
import javafx.application.*;
import javafx.stage.*;
import javafx.stage.FileChooser;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import java.io.*;
import javafx.event.*;
import javafx.geometry.*;
import java.awt.Desktop;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.Iterator;


//Created a class that extends Application and implements EventHandler in order to handle events.
public class Metel extends Application implements EventHandler<ActionEvent>{
	
//Created all needed variables (GUI and not GUI)
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
	
	

	
//Started with main method as usual in Java	but it has only launch() method inside
	public static void main(String args[]) {
		
		launch(args);
		
	}
	
//Overrided original start() method for JavaFx
	@Override
	public void start(Stage myStage) {
		
//Started to use all GUI JavaFx elements and put them in the Panels and then in the Scenes and then in the Stage
		lbl_instruction = new Label();
		lbl_instruction.setText("\n	Benvenuti nel programma CreateMetelFromExcel® di Bot Lighting Srl.\n"
				+ "\n        Il file Excel dovrà avere le seguenti colonne:\n"
				+ "\n        • Codice prodotto\n"
				+ "        • EAN\n"
				+ "        • Descrizione\n"
				+ "        • Q'tà cartone\n"
				+ "        • Prezzo al rivenditore\n"
				+ "        • Stato prodotto\n"
				+ "        • Famiglia di sconto\n");
		
		lbl_date_start = new Label("Inserisci la data inizio catalogo:");
		date_start = new TextField();
		date_start.setText("");
		date_start.setPromptText("AAAAMMGG");
		date_start.setMinWidth(160);
		
		lbl_num_catalogue = new Label("Inserisci il numero di catalogo:");
		num_catalogue = new TextField();
		num_catalogue.setText("");
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
		lbl_confirmation.setId("information");
		
		BorderPane mainPane = new BorderPane();
		mainPane.setTop(instruction);
		mainPane.setLeft(paneForTextFieldsAndLabels);
		mainPane.setBottom(lbl_confirmation);
		mainPane.setAlignment(lbl_confirmation, Pos.BOTTOM_LEFT);
		mainPane.setMargin(lbl_confirmation, new Insets(0,0,140,25));
		mainPane.setMargin(paneForTextFieldsAndLabels, new Insets(25));
		mainPane.setId("cssMainPanel");
		
	
		Scene scene1 = new Scene(mainPane, 520, 500);
		scene1.getStylesheets().add("application/application.css");
		btn_take_excel.requestFocus();
		
		myStage.getIcons().add(new Image(Metel.class.getResourceAsStream("BotMetel.jpg")));
		
		myStage.setScene(scene1);
		myStage.show();
	}
	
//Used handle() method of EventHandler class.
	public void handle(ActionEvent e) {
		
		
		 try {

//Checked if text input areas are filled and if at least one brand is selected otherwise an error message will appears.
         	if (num_catalogue.getText().length()!=0 && date_start.getText().length()!=0 && (bot.isSelected() || kai.isSelected())) {
         		lbl_confirmation.setId("information");
         		try {

//Created the new metel.txt file with relative Stream
         			fileMetel = new PrintWriter("metel.TXT");
         			}
         			catch(FileNotFoundException f) {
         				lbl_confirmation.setText("Non è stato possibile creare o scrivere nel file Metel");
         			}
//Created a new object with path of Excel file.
         			try {
         			 fIP = new FileInputStream(fileExcel.getPath());
         			}
         			catch(FileNotFoundException g) {
         				lbl_confirmation.setText("Non è stato possibile creare o scrivere nel file Excel");
         			}
//Started to create objects for read from Excel file: workbook, spreadsheet and row all this using ApachePOI library.
         			try {
         				workbook = new XSSFWorkbook(fIP);
         				}
         				catch(IOException g) {
         					lbl_confirmation.setText("Non è stato possibile creare o scrivere nel file Excel");
         				} 
         			XSSFSheet spreadsheet = workbook.getSheetAt(0);
         			Row heading = spreadsheet.getRow(0);
//Created a PrintWriter object with metel.TXT in order to create a stream to write inside the txt file.	
         		fileMetel = new PrintWriter(new BufferedWriter(new FileWriter("metel.TXT", true)));
         		
//Started to write data inside the txt file.
         		if (bot.isSelected()) {
         			fileMetel.print("LISTINO METEL       BTL01733730285BTL   "+date_start.getText()+date_start.getText()+"LISTINO GENERALE                                                     "
     						+num_catalogue.getText()+"                                                                                                         ");
         		}
         		else {
         			fileMetel.print("LISTINO METEL       BTL01733730285KBT   "+date_start.getText()+date_start.getText()+"LISTINO GENERALE                                                     "
     						+num_catalogue.getText()+"                                                                                                         ");
         		}
         		fileMetel.flush();
         		fileMetel.println();
         		
 //Started to iterate inside the Excel file to extract data with an Iterator object      	
         	Iterator < Row > rowIterator = spreadsheet.iterator();
//Created loop in order to read and write continuously data till there will be no more rows.
         	while (rowIterator.hasNext()) {
         		DataFormatter df=new DataFormatter();
         		row = (XSSFRow) rowIterator.next();
         		if (bot.isSelected()) {
         			fileMetel.print("BTL");
         		}
         		else {
         			fileMetel.print("KBT");
         		}
         		String s1=df.formatCellValue(row.getCell(0));
//
         		if (s1.length()<16){
         			while (s1.length()<16){
         				s1=s1+" ";
         			}
         			
         		}
         		else {
         			s1=s1.substring(0, 15);
         		}
         		
         		fileMetel.print(s1);
         		
         		s1=df.formatCellValue(row.getCell(1));
         		if (s1.length()>1) {
         			if (s1.substring(1,2).equals(".")) {
         				s1=s1.substring(0,1)+s1.substring(2);
         			}
         		}
         		if (s1.length()<13){
         			while (s1.length()<13){
         				s1=s1+" ";
         			}
         			
         		}
         		else {
         			s1=s1.substring(0, 13);
         		}         		
         		fileMetel.print(s1);
         		
         		s1=df.formatCellValue(row.getCell(2));
         		if (s1.length()<43){
         			while (s1.length()<43){
         				s1=s1+" ";
         			}
         			
         		}
         		else {
         			s1=s1.substring(0, 43);
         		}         		
         		fileMetel.print(s1);
         		
         		s1=df.formatCellValue(row.getCell(3));
         		if (s1.length()<5){
         			while (s1.length()<5){
         				s1=s1+"0";
         			}
         			
         		}
         		else {
         			s1=s1.substring(0, 5);
         		}         		
         		fileMetel.print(s1+s1+s1+"999999"+"3");
         		
         		s1=df.formatCellValue(row.getCell(4));
         		if (s1.substring(1,2).equals(",")) {
         			s1=s1.substring(0,1)+s1.substring(2);
         		}
         		else if(s1.substring(2,3).equals(",")){
         			s1=s1.substring(0,2)+s1.substring(3);
         		}
         		else {
         			s1=s1.substring(0,3)+s1.substring(4);
         		}
         		
         		if (s1.length()<11){
         			while (s1.length()<11){
         				s1="0"+s1;
         			}
         			
         		}
         		else {
         			s1=s1.substring(0, 11);
         		}         		
         		fileMetel.print(s1+s1+"000001EURPCE0");
         		
         		s1=df.formatCellValue(row.getCell(5));
         		fileMetel.print(s1);
         		
         		s1=date_start.getText();
         		fileMetel.print(s1);
         		
         		s1=df.formatCellValue(row.getCell(7));
         		if (s1.length()<18){
         			while (s1.length()<18){
         				s1=s1+" ";
         			}
         			
         		}
         		else {
         			s1=s1.substring(0, 18);
         		}         		
         		fileMetel.print(s1+s1);
         		
         		s1="";
         		while (s1.length()<56) {
         			s1=s1+" ";
         		}
         		fileMetel.print(s1);
         		
         		
         		
         		//fileMetel.print(String.valueOf(row.getCell(6))+String.valueOf(row.getCell(7)));
         		fileMetel.flush();
         		fileMetel.println();
         		
         	}
         	lbl_confirmation.setText("File Metel.TXT creato correttamente");
         	
         	}
         	
         	else {
         		lbl_confirmation.setId("warningRed");
         		lbl_confirmation.setText("Mancano la data, il numero di catalogo oppure non è stato selezionato il fornitore");
         	}
         	
		 }
		 catch(IOException h) {
         	System.out.println(h);
         }
		 
		 fileMetel.close();
		 
		 
	}
	
			
		         
	      }
	
	
