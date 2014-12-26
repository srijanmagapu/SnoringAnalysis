package math;
import java.util.Locale;

public class DwVector
{
	float[] v;

	public DwVector(float... values)
	{
		this.v = new float[values.length];
		for (int i = 0; i < values.length; i++)
		{
			this.v[i] = values[i];
		}
	}

	void print()
	{
		for (int i = 0; i < v.length; i++)
		{
			System.out.printf(Locale.ENGLISH, "%+8.5f, ", v[i]);
		}
		System.out.printf(Locale.ENGLISH, "\n");
	}

}