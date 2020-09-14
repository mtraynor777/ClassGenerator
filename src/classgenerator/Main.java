package classgenerator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Main--
 * Prototype for a program that will read class names and instance fields from an excel file, generate the Java code for a basic class,
 * and output that code as text so the user may copy and paste the code into a text editor of their chosing.
 * @author Michael Traynor
 *
 */
public class Main {
	
	/**
	 * printClass--
	 * This method will process the information regarding the class, such as the name of the class and instance fields,
	 * and will generate the text for a class of the specified name, including a parameterized constructor, getters, setters,
	 * and a toString method (formatted the same as the auto-generated toString method in eclipse)
	 * @param name - name of the class
	 * @param type - an array containing the data types of the instance fields for the class
	 * @param vnames - an array containing the names of the instance fields for the class (should directly correspond with 
	 * data types in type)
	 * @param num - a counter for the number of instance fields
	 */
	public static void printClass(String name, String[] type, String[] vnames, int num) {
		
		//parameterized constructor
		String constructor = "public " + name.substring(0, 1).toUpperCase() + name.substring(1) + "("; //parameterized constructor header
		System.out.println("public class " + name.substring(0, 1).toUpperCase() + name.substring(1) + " {" + "\n"); //class header
		for (int i = 0; i<num; i++) { //this loop adds the parameters for the parameterized constructor and prints the instance fields
			System.out.println("	private " + type[i] + " " + vnames[i] + ";");
			constructor += (type[i] + " " + vnames[i]);
			if (i != num - 1)
				constructor += ", ";
		}
		constructor += ") {";
		System.out.println("\n	" + constructor);
		for (int i =0; i < num; i++) {
			System.out.println("		this." + vnames[i] + " = " + vnames[i] + ";");
		}
		System.out.println("	}");
		
		System.out.println("\n");
		
		//getter methods
		for (int i = 0; i < num; i++) { //the loop outputs the Java code for the getter methods
			System.out.println("	public " + type[i] + " get" + vnames[i].substring(0,1).toUpperCase() + vnames[i].substring(1) + "() {");
			System.out.println("		return " + vnames[i] + ";");
			System.out.println("	}" + "\n");
		}
		
		//setter methods
		for (int i = 0; i < num; i++) { //the loop outputs the Java code for the setter methods
			System.out.println("	public void set" + vnames[i].substring(0,1).toUpperCase() + vnames[i].substring(1) + "(" + type[i] + " newValue) {");
			System.out.println("		this." + vnames[i] + " = newValue;");
			System.out.println("	}" + "\n");
		}
		
		//toString
		char ch = '"';
		String toString =  "	public String toString() {" + "\n" + "		return " + ch + name.substring(0, 1).toUpperCase() + name.substring(1) +  " [";
		for (int i = 0; i <num; i++) { //loop adds code with correct formatting for all the instance fields of the class
			toString += (vnames[i] + "=" + ch + " + " + vnames[i]);
			if (i != num-1)
				toString +=  (" + " + ch + ", ");
		}
		toString += (" + " + ch + "]" + ch + ";");
		toString += ("\n" + "	}");
		System.out.println(toString); //outputs the finished toString method
		
		System.out.println("}"); //closing the class
		
	}

	/**
	 * main--
	 * the main method will use a BufferedReader object to read in from the test.csv file and will
	 * pass relevant information to the printClass method
	 * @param args, command line arguments
	 * @throws FileNotFoundException if test.csv is not found or if there is a problem reading the file
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		String name = ""; //holds the name of the class
		String[] type = new String[10]; //holds the data types of each instance field
		String[] vnames = new String[10]; //holds the name of each instance field (should directly correspond with data type in type)
		int num = 0; //counts the number of instance fields
		
		BufferedReader csvReader = new BufferedReader(new FileReader("test.csv"));
		String row; //line read in by csvReader will referred to here
		try {
			csvReader.readLine(); //skipping first line (this line only specifies to the user where data type and var names should be in test.csv)
			row = csvReader.readLine();
			String[] data = row.split(",");
			name = data[1]; 
			while ((row = csvReader.readLine()) != null) { //the while loop will read data from test.csv until it reaches a blank line
				data = row.split(",");
				if (data[0].equals("class_name")) { //if the user specifies for a new class to be created
					printClass(name, type, vnames, num);
					System.out.println("\n"); //a newline will be printed to separate each class
					name = data[1]; 
					row = csvReader.readLine(); //moving csvReader to next line for first instance field of new class
					data = row.split(",");
					num = 0; //resetting necessary variables for next class
					type = new String[10]; 
					vnames = new String[10];
				}
				type[num] = data[0];
				vnames[num] = data[1];
				num++;
			}

			printClass(name, type, vnames, num); //if the end of the file has been reached, printClass is called to print the code for the last class
		    csvReader.close(); 

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
