package math;

import java.util.Arrays;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 *
 * @author thomas diewald
 *
 *         date: 21.06.2013
 *
 *
 *         PCA - Principal Component Analysis
 *
 *         works for any number of dimension using: "Jama" for eigenvector
 *         calculation
 *
 */

public class PCA
{
	private int num_data = 0;
	private int dim_data = 0;

	private DwVector[] data; // input data-vectors
	private DwVector mean; // translation vector
	private Matrix cvmat; // (variance)-covariance-matrix

	private EigenvalueDecomposition edec; // eigenvalue decomposition (by Jama)
	private DwEigenVector[] evec; // list of eigenvectors and eigenvalues
	private Matrix emat; // eigenvector matrix (sorted colums by eigenvalues)

	public PCA()
	{
	}
	
	public PCA(float[] data)
	{
		this(new DwVector[] {new DwVector(data)});
	}
	
	public PCA(DwVector[] data)
	{
		setData(data);
	}
	
	public void setData(DwVector[] data)
	{
		this.data = data;
		this.num_data = data.length;
		this.dim_data = data[0].vector.length;
	}

	public PCA compute()
	{
		centerData();
		computeCovarianceMatrix();
		updatePCAMatrix();
		return this;
	}

	public void centerData()
	{

		// compute mean
		float[] mean_tmp = new float[dim_data];
		for (int i = 0; i < num_data; i++)
		{
			for (int j = 0; j < dim_data; j++)
			{
				mean_tmp[j] += data[i].vector[j];
			}
		}

		for (int j = 0; j < dim_data; j++)
		{
			mean_tmp[j] /= (float) num_data;
		}

		// center data (subtract mean) -> mean is at origin now
		for (int i = 0; i < num_data; i++)
		{
			for (int j = 0; j < dim_data; j++)
			{
				data[i].vector[j] -= mean_tmp[j];
			}
		}
		mean = new DwVector(mean_tmp);
	}

	public void computeCovarianceMatrix()
	{
		// NxN-matrix
		// . symetric, positive definite or positive semi-definite
		// every square symmetric matrix is orthogonally (orthonormally)
		// diagonalisable.
		// --> S = E D E-transpose
		// . diagonal -> variances
		// . off-diagonal -> co-variances (... how well correlated two variables
		// are)
		// 1. Maximise the signal, measured by variance (maximise the diagonal
		// entries)
		// 2. Minimise the covariance between variables (minimise the
		// off-diagonal entries)
		final double[][] mat = new double[dim_data][dim_data];
		for (int r = 0; r < dim_data; r++)
		{ // rows
			for (int c = r; c < dim_data; c++)
			{ // cols

				double sum = 0;
				for (int i = 0; i < num_data; i++)
				{
					sum += data[i].vector[r] * data[i].vector[c];
				}
				mat[r][c] = mat[c][r] = sum / (num_data - 1);
			}
		}
		cvmat = new Matrix(mat);
		// System.out.println("covariance matrix");
		// cvmat.print(8,8);
	}

	public void updatePCAMatrix()
	{
		edec = cvmat.eig(); // get eigenvalue decomposition
		emat = edec.getV(); // get eigenvector matrix

		// System.out.println(" eigenvector matrix (before reordering)");
		// emat.print(8, 8);

		// transpose it, to get eigenvectors from columns
		emat = emat.transpose();

		double[][] emat_dd = emat.getArray();
		double[] eval = edec.getRealEigenvalues();

		// create objects for sorting
		// columns are eigenvectors ... principal components
		evec = new DwEigenVector[eval.length];
		for (int i = 0; i < evec.length; i++)
		{
			evec[i] = new DwEigenVector(emat_dd[i], eval[i]);
		}

		// sort eigenvectors by eigenvalues (decreasing)
		// System.out.println("unsorted eigenvectors");
		// for(int i = 0; i < evec.length; i++) evec[i].print();
		Arrays.sort(evec);
		// System.out.println("sorted eigenvectors: "+evec.length);
		// for(int i = 0; i < evec.length; i++) evec[i].print();

		// put the sorted vectors back into the matrix ... use all eigenvectors
		setTransformDimension(dim_data);
	}

	void printEigenValuesSorted()
	{
		System.out.println("sorted eigenvalues: " + evec.length);
		for (int i = 0; i < evec.length; i++)
			System.out.println("[" + i + "] " + evec[i].eval);
	}

	void printEigenVectorsSorted()
	{
		System.out.println("sorted eigenvectors: " + evec.length);
		for (int i = 0; i < evec.length; i++)
			evec[i].print();
	}

	// can be used to reduce dimensions.
	// e.g. dimensions with very low eigenvalues can be removed
	// = same, as setting the dimension of the vector to 0, after the
	// transformation
	public void setTransformDimension(int dim_new)
	{

		// compose new transform matrix of sorted eigenvectors
		// if dim_new is smaller than the original size, than dimensions are
		// reduced!
		double[][] emat_dd = new double[dim_new][dim_data];
		for (int i = 0; i < dim_new; i++)
		{
			emat_dd[i] = evec[i].evec;
		}
		emat = new Matrix(emat_dd); // to keep edec object untouched
		
		// System.out.println(" new matrix");
		// emat.print(8, 8);

		// not necessary, because already transposed while inserting the sorted
		// data
		// emat = emat.transpose();
		// System.out.println(" new matrix transposed");
		// emat.print(8, 8);
	}

	public DwVector transformVector(DwVector vec)
	{
		int cols = emat.getColumnDimension();
		int rows = emat.getRowDimension();

		if (cols != vec.vector.length)
		{
			System.out.println("error, cant transform vector");
		}
		
		double[][] emat_dd = emat.getArray();
		float[] vec_new = new float[rows];

		for (int r = 0; r < rows; r++)
		{
			float val = 0;
			for (int c = 0; c < cols; c++)
			{
				val += emat.get(r, c) * vec.vector[c];
			}
			vec_new[r] = val;
		}
		
		return new DwVector(vec_new);
	}

	public DwVector[] transformData(DwVector[] data, boolean transpose)
	{
		Matrix mat = emat;
		if (transpose)
		{
			mat = emat.transpose();
		}

		final int cols = mat.getColumnDimension();
		final int rows = mat.getRowDimension();

		final double[][] emat_dd = mat.getArray();
		final int num_data = data.length;

		DwVector[] data_new = new DwVector[num_data];
		for (int i = 0; i < num_data; i++)
		{
			DwVector vec = data[i];

			float[] vec_new = new float[rows];
			for (int r = 0; r < rows; r++)
			{
				float val = 0;
				for (int c = 0; c < cols; c++)
				{
					val += emat_dd[r][c] * vec.vector[c];
				}
				vec_new[r] = val;
			}
			data_new[i] = new DwVector(vec_new);
		}

		if (transpose)
		{
			mat = emat.transpose();
		}

		return data_new;
	}
	
	
	public double[][] getTrasformMatrix()
	{
		return emat.getArray();
	}
	
	public void setTransformMatrix(double[][] matrix)
	{
		this.emat = new Matrix(matrix);
	}
	
}
