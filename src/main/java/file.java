

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.json.simple.JSONObject;

public class file {

public static int length(File File) {
	
	int c = 0;
	
	 try {
			
			Scanner Reader = new Scanner(File);
			
			while(Reader.hasNext()) {Reader.next();++c;}
		
			Reader.close();

		} catch (FileNotFoundException e) {System.out.println("File not found");}
	 
	 return c;
}	
public static void write(File File, String Content) {

		try {
			
			if(	!file.newFile(File, Content)) {
		PrintWriter writer = new PrintWriter(File);
		writer.print(Content);
		writer.close();
		
			}
		} catch (FileNotFoundException e) {}
	}

public static void add(File File, String Content, int position) {
	
	StringBuffer sb = new StringBuffer();
	StringBuffer sb1 = new StringBuffer();

	  try {
			
			Scanner Reader = new Scanner(File);
			
			int n = 0;
			while(n <= position) {sb.append(" " + Reader.next()); ++n;}
			while(Reader.hasNext()) {sb1.append(" " + Reader.next());}
		
			Reader.close();

		} catch (FileNotFoundException e) {}
	  
	  write(File, sb.toString() + " " + Content + sb1);
	 
}

public static void addLast(File File, String Content) {
	
	StringBuffer sb = new StringBuffer();

	  try {
			
			Scanner Reader = new Scanner(File);
			
			while(Reader.hasNext()) {sb.append(" " + Reader.next());}
		
			Reader.close();

		} catch (FileNotFoundException e) {}
	  
	  write(File, sb.toString() + " " + Content);
	 
}

public static void addAddress(File File, String address) {
	StringBuffer sb = new StringBuffer();

	  try {
			
			Scanner Reader = new Scanner(File);
			
			while(Reader.hasNext()) {sb.append(" " + Reader.next());}
		
			Reader.close();

		} catch (FileNotFoundException e) {}
	  
	  write(File, sb.toString() + ":" + address);
}

public static void addFirst(File File, String Content) {
	
	StringBuffer sb = new StringBuffer();

	  try {
			
			Scanner Reader = new Scanner(File);
			
			while(Reader.hasNext()) {sb.append(" " + Reader.next());}
		
			Reader.close();

		} catch (FileNotFoundException e) {}
	  
	  write(File, Content + " " + sb.toString());
	 
}
public static boolean newFile(File File, String Content) {
		
	boolean result = false;
		try {
			if(File.createNewFile()) {
				PrintWriter writer = new PrintWriter(File);
				writer.print(Content);
				writer.close();	
				result = true;
			}
			else {result = false;}
		} catch (IOException e) {}
		
		return result;
		
	}

public static boolean newFile(File File, JSONObject jsonObject) {
	
	boolean result = false;
		try {
			if(File.createNewFile()) {
				PrintWriter writer = new PrintWriter(File);
				writer.print(jsonObject);
				writer.close();	
				result = true;
			}
			else {result = false;}
		} catch (IOException e) {}
		
		return result;
		
	}

public static String read(File File, int position) {

	int n = 0;
	String output = "r";
	try {
		
		Scanner Reader = new Scanner(File);
		do{output = Reader.next();n++;} while(n <= position) ;
		Reader.close();

	} catch (FileNotFoundException e) {System.out.println("File not found");}
	
	return output;
	
}

public static Double readDouble(File File, int position) {

	int n = 0;
	String output = "r";
	try {
		
		Scanner Reader = new Scanner(File);
		do{output = Reader.next();++n;} while(n <= position) ;
		Reader.close();

	} catch (FileNotFoundException e) {System.out.println("File not found");}
	
	double output1 = Double.parseDouble(output);
	return output1;
	
}
	
public static int readInteger(File File, int position) {

	

	return file.readDouble(File, position).intValue();
	
}
public static void rename(File oldFile, File newFile) {
	
	oldFile.renameTo(newFile);
}

public static String readAll(File File) {
	
	StringBuffer sb = new StringBuffer();

	  try {
			
			Scanner Reader = new Scanner(File);
			
			while(Reader.hasNext()) {sb.append(" " + Reader.next());}
		
			Reader.close();

		} catch (FileNotFoundException e) {}
	  
	  return sb.toString();
	  
}

public static boolean newFolder(File File) {
	
	if(!File.exists()) {
		
		if(File.mkdir()) {return true;}
		else {return false;}
	}
	else {return false;}
}
}