package com.cecilio0.dicoformas.utils;

import java.io.*;

public final class DatUtil {
	public static void writeObjectToFile(Object obj, String filename) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(filename);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(obj);
	}
	
	public static Object readObjectFromFile(String filename) throws IOException, ClassNotFoundException {
		File file = new File(filename);
		
		if (!file.exists())
			return null;
		
		FileInputStream fileIn = new FileInputStream(file);
		ObjectInputStream objectIn = new ObjectInputStream(fileIn);
		return objectIn.readObject();
	}
}
