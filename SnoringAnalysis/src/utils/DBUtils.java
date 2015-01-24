package utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class DBUtils
{
	public static final String POINTS_WITH_MEMBERSHIP_FILENAME = "point_with_membership.csv";
	public static final String RAW_FEATURES_FILENAME = "raw_features.csv";
	public static final String PCA_MATRIX_FILENAME = "pca_matrix.csv";
	public static final String CLUSTERS_CENTERS_FILENAME = "cluster_centers.csv";
	
	public static void storePointsWithMembership(double[][] points, double[][] memberships)
	{
		try
		{
			ArrayList<String> row = new ArrayList<String>();
			List<String[]> rows = new ArrayList<String[]>();
			
			for( int i=0 ; i<points.length ; i++ )
			{
				for( double coord : points[i] )
				{
					row.add(Double.toString(coord));
				}
				for( double membership : memberships[i] )
				{
					row.add(Double.toString(membership));
				}
				rows.add(row.toArray(new String[0]));
				row.clear();
			}
			CSVWriter writer = new CSVWriter(new FileWriter(POINTS_WITH_MEMBERSHIP_FILENAME, true));
			writer.writeAll(rows);
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void storeRawFeatures(List<double[]> featureVectors)
	{
		writeToFile(RAW_FEATURES_FILENAME, featureVectors);
	}
	
	public static void storeRawFeatures(double[][] featureVectors)
	{
		writeToFile(RAW_FEATURES_FILENAME, featureVectors);
	}
	
	public static void storePCAMatrix(double[][] pca)
	{
		writeToFile(PCA_MATRIX_FILENAME, pca);
	}

	public static void storeClusterCenters(double[][] centers)
	{
		writeToFile(CLUSTERS_CENTERS_FILENAME, centers);
	}
	
	public double[][] loadPointsWithMembership()
	{
		return readArrayFromFile(POINTS_WITH_MEMBERSHIP_FILENAME);
	}
	
	public static double[][] loadClusterCenters()
	{
		return readArrayFromFile(CLUSTERS_CENTERS_FILENAME);
	}
	
	public static double[][] loadPCAMatrix()
	{
		return readArrayFromFile(PCA_MATRIX_FILENAME);
	}
	
	public static List<double[]> loadRawFeatures()
	{
		return readListFromFile(RAW_FEATURES_FILENAME);
	}
	
	private static double[][] readArrayFromFile(String fileName)
	{
		List<double[]> list = readListFromFile(fileName);
		double[][] matrix = new double[list.size()][];
		matrix = list.toArray(matrix);
		return matrix;
	}
	
	private static List<double[]> readListFromFile(String fileName)
	{
		List<double[]> values = new ArrayList<double[]>();
		CSVReader reader = null;
		try
		{
			reader = new CSVReader(new FileReader(fileName));
			
			String[] line;
		    while ((line = reader.readNext()) != null)
		    {
		    	double[] doubleLine = new double[line.length];
		    	for( int i=0 ; i<line.length ; i++ )
		    		doubleLine[i] = Double.valueOf(line[i]);
		    	
		    	values.add(doubleLine);
		    }
		    
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(reader != null)
			{
				try
				{
					reader.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return values;
	}
	
	private static void writeToFile(String fileName, double[][] table)
	{
		try
		{
			List<String[]> rows = new ArrayList<String[]>();
			
			for( double[] row : table )
			{
				String[] stringRow = new String[row.length];
				for( int i=0 ; i<stringRow.length ; i++ )
				{
					stringRow[i] = Double.toString(row[i]);
				}
				rows.add(stringRow);
			}
			CSVWriter writer = new CSVWriter(new FileWriter(fileName, !fileName.equals(PCA_MATRIX_FILENAME)));
			writer.writeAll(rows);
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void writeToFile(String fileName, List<double[]> table)
	{
		try
		{
			List<String[]> rows = new ArrayList<String[]>();
			
			for( double[] row : table )
			{
				String[] stringRow = new String[row.length];
				for( int i=0 ; i<stringRow.length ; i++ )
				{
					stringRow[i] = Double.toString(row[i]);
				}
				rows.add(stringRow);
			}
			CSVWriter writer = new CSVWriter(new FileWriter(fileName, true));
			writer.writeAll(rows);
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
