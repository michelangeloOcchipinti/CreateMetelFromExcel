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
	private int first, second, third, fourth, fifth, sixt, seventh, TrueFalseOnCatalogue;

	
	
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
				+ "	• Stato\n"
				+ "	• Prezzo\n"
				+ "	• Cod_FW\n");
		
		lbl_date_start = new Label("Inserisci la data inizio catalogo:");
		date_start = new TextField();
		date_start.setPromptText("DDMMAAAA");
		date_start.setMinWidth(160);
		
		lbl_num_catalogue = new Label("Inserisci il numero di catalogo:");
		num_catalogue = new TextField();
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
				/*if (fileExcel!=null) {
					try {
					desktop.open(fileExcel);
					}
					catch(IOException h) {
						System.out.println("Impossibile aprire il file!");
					}
				}*/
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
		
		lbl_confirmation = new Label("Prova label");
		
		
		BorderPane mainPane = new BorderPane();
		mainPane.setTop(instruction);
		mainPane.setLeft(paneForTextFieldsAndLabels);
		mainPane.setBottom(lbl_confirmation);
		mainPane.setAlignment(lbl_confirmation, Pos.TOP_CENTER);
		mainPane.setMargin(lbl_confirmation, new Insets(0,0,100,0));
		mainPane.setMargin(paneForTextFieldsAndLabels, new Insets(25));
		
		
		
		Scene scene1 = new Scene(mainPane, 520, 500);
		
		btn_take_excel.requestFocus();
		
		myStage.setScene(scene1);
		myStage.show();
	}
	
	public void handle(ActionEvent e) {
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
		Iterator < Cell > rowZeroIterator = heading.cellIterator();
		int index = 0;
		while (rowZeroIterator.hasNext()) {
			Cell cellZero = rowZeroIterator.next();
			switch (String.valueOf(heading.getCell(index))) {
				case "Cod_Articolo":
					first = cellZero.getColumnIndex();
					break;
				case "Barcode":
					second = cellZero.getColumnIndex();	
					break;
				case "Descri_Articolo":
					third = cellZero.getColumnIndex();
					break;
				case "Pz_x_Conf":
					fourth = cellZero.getColumnIndex();
					break;
				case "Prezzo":
					fifth = cellZero.getColumnIndex();
					break;
				case "Stato":
					sixt = cellZero.getColumnIndex();
					break;
				case "Cod_FW":
					seventh = cellZero.getColumnIndex();
					break;
				case "CatalogoWeb":
					TrueFalseOnCatalogue = cellZero.getColumnIndex();
					break;
    			}
			index++;
			}	
		 try {
         	fileMetel = new PrintWriter(new BufferedWriter(new FileWriter("metel.TXT", true)));
         	if (num_catalogue!=null && date_start!= null) {
         		fileMetel.print("LISTINO METEL       BTL01733730285BTL   "+date_start.getText()+date_start.getText()+"LISTINO GENERALE                                                     "
         	+num_catalogue.getText()+"                                                                                                         ");
         		fileMetel.flush();
         		fileMetel.println();
         	}
         	else {
         		
         	}
         	Iterator < Row > rowIterator = spreadsheet.iterator();
         	while (rowIterator.hasNext()) {
         		row = (XSSFRow) rowIterator.next();
         		if (String.valueOf(row.getCell(TrueFalseOnCatalogue)).equals("TRUE")) {
         			System.out.println(String.valueOf(row.getCell(first))+String.valueOf(row.getCell(second)+String.valueOf(row.getCell(third))+String.valueOf(row.getCell(fourth))+String.valueOf(row.getCell(fifth))+String.valueOf(row.getCell(sixt))+String.valueOf(row.getCell(seventh))+String.valueOf(row.getCell(TrueFalseOnCatalogue))));
         			fileMetel.print(String.valueOf(row.getCell(first))+String.valueOf(row.getCell(second)+String.valueOf(row.getCell(third))+String.valueOf(row.getCell(fourth))+String.valueOf(row.getCell(fifth))+String.valueOf(row.getCell(sixt))+String.valueOf(row.getCell(seventh))+String.valueOf(row.getCell(TrueFalseOnCatalogue))));
         			fileMetel.flush();
         			fileMetel.println();
         		}
         	}
         	
		 }
		 catch(IOException h) {
         	System.out.println(h);
         }
		 
		 fileMetel.close();

	}
	
			
	/*Iterator < Row > rowIterator = spreadsheet.iterator();
		
		 while (rowIterator.hasNext()) {
	         row = (XSSFRow) rowIterator.next();
	         Iterator < Cell >  cellIterator = row.cellIterator();
	         int index=0;
	         String[] cells = new String[6];
	         while ( cellIterator.hasNext()) {
	            Cell cell = cellIterator.next();
	            Row intestazione = spreadsheet.getRow(0);
	            //lbl_confirmation.setText(cell.getStringCellValue()+"\n");
	            try {
	            	fileMetel = new PrintWriter(new BufferedWriter(new FileWriter("metel.TXT", true)));
	            	String cellaIntestazione = String.valueOf(intestazione.getCell(index));
	            	switch(cellaIntestazione) {
	            		case "Cod_Articolo":
	            			if (cell.getCellTypeEnum() == CellType.STRING) {
	            				cells[0]=cell.getStringCellValue();
	            			} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
	            				cells[0]=String.valueOf(cell.getNumericCellValue());
	            			}
	            			break;
	            		case "Barcode":
	            			if (cell.getCellTypeEnum() == CellType.STRING) {
	            				cells[1]=cell.getStringCellValue();
	            			} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
	            				cells[1]=String.valueOf(cell.getNumericCellValue());
	            			}
	            			break;
	            		case "Descri_Articolo":
	            			if (cell.getCellTypeEnum() == CellType.STRING) {
	            				cells[2]=cell.getStringCellValue();
	            			} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
	            				cells[2]=String.valueOf(cell.getNumericCellValue());
	            			}
	            			break;
	            		case "Pz_x_Conf":
	            			if (cell.getCellTypeEnum() == CellType.STRING) {
	            				cells[3]=cell.getStringCellValue();
	            			} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
	            				cells[3]=String.valueOf(cell.getNumericCellValue());
	            			}
	            			break;
	            		case "Prezzo":
	            			if (cell.getCellTypeEnum() == CellType.STRING) {
	            				cells[4]=cell.getStringCellValue();
	            			} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
	            				cells[4]=String.valueOf(cell.getNumericCellValue());
	            			}
	            			break;
	            		case "Famiglia":
	            			if (cell.getCellTypeEnum() == CellType.STRING) {
	            				cells[5]=cell.getStringCellValue();
	            			} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
	            				cells[5]=String.valueOf(cell.getNumericCellValue());
	            			}
	            			break;
	            		}

	            	fileMetel.flush();
	            	
	            }
	            catch(IOException h) {
	            	System.out.println(h);
	            }
	            index++;
	            }
	         index=0;
	         for (int i=0; i<cells.length;i++) {
         		fileMetel.print(cells[i]);
         	}
	         System.out.println();
	         fileMetel.println();
	         fileMetel.close();
	         }*/
		
	         
	      }
	
	
