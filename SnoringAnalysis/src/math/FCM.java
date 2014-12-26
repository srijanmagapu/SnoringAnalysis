package math;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FCM
{

	private static final String FILE_DATA_IN = "data_in.txt";
	private static final String FILE_PAR = "parameters.txt";
	private static final String FILE_CENTER = "center.txt";
	private static final String FILE_MATRIX = "matrix.txt";
	private static final int SIZE = 10;

	public int numpattern;
	public int dimension;
	public int cata;
	public int maxcycle;
	public double m;
	public double limit;

	public FCM()
	{
		super();
	}

	public FCM(int numpattern, int dimension, int cata, int maxcycle, double m, double limit)
	{
		this.numpattern = numpattern;
		this.dimension = dimension;
		this.cata = cata;
		this.maxcycle = maxcycle;
		this.m = m;
		this.limit = limit;
	}

	/**
	 * Get parameters from FILE_PAR
	 * Each parameter in separate line:
	 * 1. numpattern
	 * 2. dimension
	 * 3. cata
	 * 4. m
	 * 5. maxcycle
	 * 6. limit
	 * @return
	 */
	public boolean getPar()
	{

		
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(FILE_PAR));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		
		String line = null;
		for (int i = 0; i < 6; i++)
		{
			try
			{
				line = br.readLine();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			switch (i)
			{
			case 0:
				numpattern = Integer.valueOf(line);
				break;
			case 1:
				dimension = Integer.valueOf(line);
				break;
			case 2:
				cata = Integer.valueOf(line);
				break;
			case 3:
				m = Double.valueOf(line);
				break;
			case 4:
				maxcycle = Integer.valueOf(line);
				break;
			case 5:
				limit = Double.valueOf(line);
				break;
			}
		}

		return true;
	}

	/**
	 * Get pattern from FILE_DATA_IN.
	 * Separated by comma ','
	 * @param pattern array for returned values
	 * @return always true
	 */
	public boolean getPattern(double[] pattern)
	{

		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(FILE_DATA_IN));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		String line = null;
		String regex = ",";
		int index = 0;
		while (true)
		{
			try
			{
				line = br.readLine();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			if (line == null) break;

			String[] split = line.split(regex);
			for (int i = 0; i < split.length; i++)
				pattern[index++] = Double.valueOf(split[i]);
		}

		return true;
	}

	
	public boolean FCM_fun(double[] pattern, int dimension, int numpattern, int cata, double m, int maxcycle, double limit, double[] umatrix, double[] rescenter, double result)
	{

		int j, i, k, t, count;
		int n_cycle;
		int n_selnum;
		int flagtemp;
		double f_temp, lastv, delta, t_temp;
		double[] v1 = new double[SIZE];
		double[] v2 = new double[SIZE];
		double min_dis = 0.001;

		if (cata >= numpattern || m <= 1) return false;

		for (j = 0; j < cata; j++)
		{
			t_temp = Math.random();
			n_selnum = (int) (Math.random() * numpattern / cata + j * numpattern / cata);
			n_selnum = j * numpattern / cata;

			while (n_selnum * dimension > numpattern * dimension)
				n_selnum -= numpattern;

			for (i = 0; i < dimension; i++)
				rescenter[j * dimension + i] = pattern[n_selnum * dimension + i];
		}

		n_cycle = 0;
		lastv = 0;

		do
		{
			for (i = 0; i < numpattern; i++)
			{
				flagtemp = 0;
				count = 0;
				for (j = 0; j < cata; j++)
				{
					f_temp = 0;
					for (t = 0; t < cata; t++)
					{
						for (k = 0; k < dimension; k++)
						{
							v1[k] = pattern[i * dimension + k];
							v2[k] = rescenter[t * dimension + k];
						}

						if (distance(v1, v2, dimension) > min_dis)
						{
							f_temp += Math.pow(distance(v1, v2, dimension), -2 / (m - 1));
						}
						else
						{
							flagtemp = 1;
						}
					}

					for (k = 0; k < dimension; k++)
					{
						v1[k] = pattern[i * dimension + k];
						v2[k] = rescenter[j * dimension + k];
					}

					if (flagtemp == 1)
					{
						umatrix[j * numpattern + i] = 0;
						flagtemp = 0;
					}

					if (distance(v1, v2, dimension) > min_dis)
					{
						double shit1 = distance(v1, v2, dimension);
						double shit2 = Math.pow(shit1, -2 / (m - 1)) / f_temp;
						int shit3 = j * numpattern + i;
						umatrix[shit3] = shit2;
					}
					else
					{
						count++;
						umatrix[j * numpattern + i] = -1;
					}
				}// end for j

				if (count > 0)
				{
					for (j = 0; j < cata; j++)
					{
						if (umatrix[j * numpattern + i] == -1)
						{
							umatrix[j * numpattern + i] = 1 / (double) (count);
						}
						else umatrix[j * numpattern + i] = 0;
					}
				}
			}// end for i

			f_temp = objectfun(umatrix, rescenter, pattern, cata, numpattern, dimension, m);
			delta = Math.abs(f_temp - lastv);
			lastv = f_temp;

			for (i = 0; i < cata; i++)
			{
				for (j = 0; j < dimension; j++)
				{
					f_temp = 0;
					for (k = 0; k < numpattern; k++)
					{
						f_temp += Math.pow(umatrix[i * numpattern + k], m) * pattern[k * dimension + j];
					}
					rescenter[i * dimension + j] = f_temp;
					f_temp = 0;
					for (k = 0; k < numpattern; k++)
					{
						f_temp += Math.pow(umatrix[i * numpattern + k], m);
					}
					rescenter[i * dimension + j] /= f_temp;
				}
			}
			n_cycle++;

		}
		while (n_cycle < maxcycle && delta > limit);

		return true;

	}

	/**
	 * Euclidean distance between two vectors
	 * @param v1
	 * @param v2
	 * @param dimension
	 * @return
	 */
	public double distance(double v1[], double v2[], double dimension)
	{
		int i;
		double result;

		result = 0;
		for (i = 0; i < dimension; i++)
		{
			result += (v1[i] - v2[i]) * (v1[i] - v2[i]);
		}

		result = Math.sqrt(result);

		return result;
	}



	public double objectfun(double u[], double v[], double x[], int c, int pattern, int dimension, double m)
	{
		int i, j, k;
		double[] v1 = new double[SIZE];
		double[] v2 = new double[SIZE];
		double object;

		object = 0;
		for (i = 0; i < c; i++)
		{
			for (j = 0; j < pattern; j++)
			{

				for (k = 0; k < dimension; k++)
				{
					v1[k] = x[j * dimension + k];
					v2[k] = v[i * dimension + k];
				}

				object += Math.pow(u[i * pattern + j], m) * distance(v1, v2, dimension) * distance(v1, v2, dimension);
			}
		}

		return object;
	}


	
	public void runFCM()
	{

		double[] pattern = new double[numpattern * dimension];
		double[] umatrix = new double[numpattern * cata];
		double[] rescenter = new double[cata * dimension];
		double result = 0;

		getPattern(pattern);

		FCM_fun(pattern, dimension, numpattern, cata, m, maxcycle, limit, umatrix, rescenter, result);

		Export(umatrix, rescenter);
	}


	public void Export(double[] umatrix, double[] rescenter)
	{
		String str = null;
		String tab = "  ";

		try
		{
			FileWriter matrixFileWriter = new FileWriter(FILE_MATRIX);
			
			//numpattern - amount of rows
			for (int i = 0; i < numpattern; i++)
			{
				str = "";
				//cata - amount of columns
				for (int j = 0; j < cata; j++)
				{
					str += umatrix[j * numpattern + i] + tab;
				}
				str += "\n";
				matrixFileWriter.write(str);
			}

			matrixFileWriter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		try
		{
			FileWriter centerFileWriter = new FileWriter(FILE_CENTER);

			for (int i = 0; i < cata; i++)
			{
				str = "";
				for (int j = 0; j < dimension; j++)
				{
					str += rescenter[i * dimension + j] + tab;
				}
				str += "\n";
				centerFileWriter.write(str);
			}

			centerFileWriter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public static void main(String[] args)
	{
		FCM fcm = new FCM();
		fcm.getPar();
		fcm.runFCM();
	}
}