package printers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Printer
{
	public static void printToFile(byte[] arr, String fileName)
	{
		try
		{
			PrintWriter pw = new PrintWriter(fileName, "UTF-8");

			for (int i = 0; i < arr.length; i++)
				pw.write(String.valueOf(arr[i]) + "\n");

			pw.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void printToFile(double[] arr, String fileName)
	{
		try
		{
			PrintWriter pw = new PrintWriter(fileName, "UTF-8");

			for (int i = 0; i < arr.length; i++)
				pw.write(String.valueOf(arr[i]) + "\n");

			pw.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void printToFile(float[] arr, String fileName)
	{
		try
		{
			PrintWriter pw = new PrintWriter(fileName, "UTF-8");

			for (int i = 0; i < arr.length; i++)
				pw.write(String.valueOf(arr[i]) + "\n");

			pw.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
}
