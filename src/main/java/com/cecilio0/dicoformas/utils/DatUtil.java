package com.cecilio0.dicoformas.utils;

import java.io.*;

public class DatUtil {
	public static void writeObjectToFile(Object obj, String filename) {
		try (FileOutputStream fileOut = new FileOutputStream(filename);
			 ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
			out.writeObject(obj);
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
	
	public static Object readObjectFromFile(String filename) {
		try (FileInputStream fileIn = new FileInputStream(filename);
			 ObjectInputStream in = new ObjectInputStream(fileIn)) {
			return in.readObject();
		} catch (IOException | ClassNotFoundException i) {
			i.printStackTrace();
			return null;
		}
	}
}
