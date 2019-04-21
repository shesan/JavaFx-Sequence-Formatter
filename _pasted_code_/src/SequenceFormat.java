/*
Name: Shesan Govindasamy
*/

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SequenceFormat extends Application {

	public String seq = "";
	public String lengthval = "";
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("JavaFx Assignment 2");
		
		Label LabelInput= new Label("Enter input sequence");
		
		//Input InputArea
		TextArea InputArea = new TextArea();
		InputArea.setMinHeight(100);
		InputArea.setWrapText(true);
		
		Label Labelcharacter= new Label ("Number of characters per line?");

		//Length of sequence per line
		TextField CharCount= new TextField();
		CharCount.setMaxSize(200,50);
		CharCount.setText("60 by default");
		CharCount.setOnMouseClicked(e -> {
		CharCount.clear();
		}
		);
		
		//CheckBox values
		CheckBox SpaceCount, IndexVal;
		SpaceCount= new CheckBox("Add a space every 10 bases");
		IndexVal= new CheckBox("Add Starting number of sequence base to the left");
		
		//RadioButton values and grouping
		RadioButton UpCase, LowCase;
		final ToggleGroup group = new ToggleGroup();
		UpCase= new RadioButton("Print bases in uppercase");
		UpCase.setToggleGroup(group);
		LowCase= new RadioButton("Print bases in lowercase");
		LowCase.setSelected(true);
		LowCase.setToggleGroup(group);
		
		//Buttons
		Button Submit= new Button("Submit");
		Button Reset= new Button("Reset Input");
		
		TextArea OutputArea= new TextArea();
		OutputArea.setPrefHeight(1000);
		OutputArea.setDisable(true);
		
		//Save Button
		Button Save = new Button("Save Parameters");
		Save.setOnAction(e -> {
			SaveData data = new SaveData();
			data.CharCount = CharCount.getText();
			data.Space = SpaceCount.isSelected();
			data.Index = IndexVal.isSelected();
			data.Upper = UpCase.isSelected();
			data.Lower = LowCase.isSelected();

            try {
                ResourceManager.save(data, "setting.xml");
            }
            catch (Exception e1) {
				OutputArea.setText("Error Unable to save data.");
            }			
		});
		
		//Load Button
		Button Load = new Button("Load Parameters");
		Load.setOnAction(e -> {
			try {
                SaveData data = (SaveData) ResourceManager.load("setting.txt");
                CharCount.setText(data.CharCount);
                SpaceCount.setSelected(data.Space);
                IndexVal.setSelected(data.Index);
                UpCase.setSelected(data.Upper);
    			LowCase.setSelected(data.Lower);
            }
            catch (Exception e1) {
				OutputArea.setText("Error Unable to load data.");
            }
		});
		
		
		
		//What happens when you Submit the Submit button
		Submit.setOnAction(e -> 
		{
			//Check if the input text area's have a value in them
			if(InputArea.getText().isEmpty() || CharCount.getText().isEmpty()) {
				OutputArea.setText("Error! One or More TextFields are Blank");
				OutputArea.setFont(Font.font(40));
			}
			
			//Check if the CharCount text area has number values in them instead of alphabet. 
			else if (CharCount.getText().matches("[^0-9]*")) {
				OutputArea.setText("Enter Number into Character Count Input");
				OutputArea.setFont(Font.font(30));
			}
			
			else {
				
				//obtain values from InputArea, remove the newline characters, 
				seq = InputArea.getText();
				seq = seq.toLowerCase();
				seq = seq.replaceAll("[^acgtubdhvnwsmkry]", "");
				
				//obtain values from ChrCount length
				lengthval = CharCount.getText();
				lengthval = lengthval.replaceAll("[^0-9]", "");
				int length = Integer.parseInt(lengthval);
				
				
				//Prints out 60 characters then a newline (repeat) using length (?)
				String repeated = new String(new char[length]).replace("\0", "."); //creates "." times length to be used by replaceAll  
				seq  = seq.replaceAll(repeated, "$0\n"); //newline character addition to sequence
	
				//Prints out 10 characters then a space (repeat)
				if(SpaceCount.isSelected()) {
					String repeated1 = new String(new char[10]).replace("\0", "."); //creates "." times 10 to be used by replaceAll
					seq = seq.replaceAll(repeated1, "$0 "); //space character addition to sequence
				}
				
				//Prints out string in Uppercase 
				if(UpCase.isSelected()) {
					seq = seq.toUpperCase(); //Uppercase function
				}
				
				//Prints out string in Lowercase
				if(LowCase.isSelected()) {
					seq = seq.toLowerCase(); //Lowercase function
				}
				
				//Prints out the number to the left. This uses an array and not a string
				if(IndexVal.isSelected()) {
					String[] num_seq = seq.split("\n");
					seq = "";
					for(int i=0; i < num_seq.length; i++) {
						int len_value = length*i + 1;
						seq += (len_value + "\t" + num_seq[i]+ "\n");					
					}
				}
						
				//Prints out ACGT number in the string (subtracts from string and takes difference amount)
				String temp_seq = seq.toLowerCase().replaceAll("[^acgtubdhvnwsmkry]+", "");
	
					//Number of each base
				int countA = temp_seq.length() - temp_seq.replace("a", "").length();	
				int countC = temp_seq.length() - temp_seq.replace("c", "").length();
				int countG = temp_seq.length() - temp_seq.replace("g", "").length();
				int countT = temp_seq.length() - temp_seq.replace("t", "").length();
				int total = temp_seq.length();
				
					//Percentage of each base
				int percentA = (countA*100)/total;
				int percentC = (countC*100)/total;
				int percentG = (countG*100)/total;
				int percentT = (countT*100)/total;
				
				
				seq = "Base: Number of Occurance (Percentage of Occurance) \n" + "A's: " + countA + " (" + percentA + "%)" + " , " + "C's: " + countC + " (" + percentC + "%)" + " , " + "G's: " + countG + " (" + percentG + "%)" + " , " + "T's: " + countT + " (" + percentT + "%)" + "\n" + "Total Length : " + total + "\n\n" + seq;
				
				OutputArea.setDisable(false);
				
				
				OutputArea.setFont(Font.font("Monospaced", 15));
				OutputArea.setText(seq);
			
			}
		}
		);
		
		//Reset Button
		Reset.setOnAction(e ->
		{
			InputArea.setText("");
			CharCount.setText("60 by default");
			SpaceCount.setSelected(false);
			LowCase.setSelected(true);
			IndexVal.setSelected(false);
			OutputArea.setText("");
			OutputArea.setDisable(true);			
		}
		);
		
		//Layout
		VBox layout= new VBox(10);
		layout.setPadding(new Insets(10,20,20,20));

		layout.getChildren().addAll(LabelInput, InputArea, Labelcharacter, CharCount, SpaceCount, IndexVal, UpCase, LowCase, Submit, Reset, OutputArea, Save, Load);
		
		Scene scene= new Scene(layout, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}