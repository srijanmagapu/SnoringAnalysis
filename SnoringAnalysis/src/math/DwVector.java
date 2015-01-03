package math;
import java.util.Locale;

public class DwVector
{
	double[] vector;
	
	public double[] getVector()
	{
		return vector;
	}

	public DwVector(float... values)
	{
		this.vector = new double[values.length];
		for (int i = 0; i < values.length; i++)
		{
			this.vector[i] = values[i];
		}
	}

	public DwVector(double... values)
	{
		this.vector = new double[values.length];
		for (int i = 0; i < values.length; i++)
		{
			this.vector[i] = values[i];
		}
	}
	
	void print()
	{
		for (int i = 0; i < vector.length; i++)
		{
			System.out.printf(Locale.ENGLISH, "%+8.5f, ", vector[i]);
		}
		System.out.printf(Locale.ENGLISH, "\n");
	}

}