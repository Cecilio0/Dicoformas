package com.cecilio0.dicoformas.utils;

import java.io.*;

public final class DatUtil {
	public static void writeObjectToFile(Object obj, String filename) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(filename);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(obj);
	}
	
	public static Object readObjectFromFile(String filename) throws IOException, ClassNotFoundException {
		FileInputStream fileIn = new FileInputStream(filename);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		return in.readObject();
	}
}
