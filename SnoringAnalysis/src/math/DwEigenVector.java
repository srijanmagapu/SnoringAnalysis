package math;

import java.util.Locale;


public class DwEigenVector implements Comparable<DwEigenVector>
{
	double eval;
	double[] evec;

	public DwEigenVector(double[] evec, double eval)
	{
		this.evec = evec;
		this.eval = eval;
	}

	void print()
	{
		System.out.printf(Locale.ENGLISH, "%+8.5f,     ", eval);
		for (int i = 0; i < evec.length; i++)
		{
			System.out.printf(Locale.ENGLISH, "%+8.5f, ", evec[i]);
		}
		System.out.printf(Locale.ENGLISH, "\n");
	}

	public int compareTo(DwEigenVector o)
	{
		if (eval < o.eval) return +1;
		if (eval > o.eval) return -1;
		return 0;
	}
}
