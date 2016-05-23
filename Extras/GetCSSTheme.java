package web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class GetCSSTheme {
		static String dir;
	public static void main (String[] args) {
		dir = new File(GetCSSTheme.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
		dir = dir.replace("%20", " ");
		getThemeComponents(dir + "\\css.txt");
		test();
	}
	
	public static void test() {
		StringBuilder result = new StringBuilder();
		result.append("TEST");
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "\\new_css.txt"), "utf-8"))) {
			writer.append(result);
			if (writer != null) {
				writer.close();
			}
		} catch (Exception e) {
			// This should never happen
			e.printStackTrace();
		}
	}
	
	public static void getThemeComponents(String file) {
		StringBuilder result = new StringBuilder();
		StringBuilder component = new StringBuilder();
		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    while ((line = br.readLine()) != null) {
		       // process the line.
		    	if (!line.contains("}")) {
		    		component.append(line);
		    		continue;
		    	}
		    	if (line.trim().equals("}")) {
		    		component.append("}");
		    	} else {
		    	component.append(line.split("}")[0]).append("}");
		    	}
		    	String section = component.toString();
		    	if (!section.contains("{")) {
//		    		System.out.println("{ not found\n" + section);
		    		result.append(section);
		    	} else {
		    		String section2 = section.split("\\{")[1];
		    		if ((section2.contains("#") && section2.contains("box")) || section2.contains("color") || section2.contains("background:") || section2.contains("background-color")) {
		    			result.append(section);
		    		}
		    	}
		    	component = new StringBuilder();
		    	if (!line.trim().equals("}")) component.append(line.split("}")[1]);
		   
		    }
		
		} catch (IOException e) {
			System.out.println("ERROR :" + e + "\n" + line);
		}
		
		try {
			File newFile = new File(dir, "css_new.txt");
			OutputStreamWriter f = new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8");
			f.append(result);
			f.close();
//			writer.append(result);
//			if (writer != null) {
//				writer.close();
//			}
		} catch (Exception e) {
			// This should never happen
			e.printStackTrace();
		}
	}
}
