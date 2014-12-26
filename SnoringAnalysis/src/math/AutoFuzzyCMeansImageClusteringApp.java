package math;

/*
 * Part of the Java Image Processing Cookbook, please see
 * http://www.lac.inpe.br/JIPCookbook/index.jsp
 * for information on usage and distribution.
 * Rafael Santos (rafael.santos@lac.inpe.br)
 */
 
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
 
/**
 * This application demonstrates the usage of the cluster validity measures that
 * can be calculated by the FuzzyCMeansImageClustering algorithm. It attempts to
 * find an optimal number of clusters for a particular image.
 */
public class AutoFuzzyCMeansImageClusteringApp
  {
 /**
  * The application entry point.
  */
  public static void main(String[] args)
    {
    if (args.length != 3) // Check command line arguments.
      {
      System.err.println("Usage: java algorithms.fuzzycmeans.AutoFuzzyCMeansImageClusteringApp "+
                         "inputImage minC maxC");
      System.exit(0);
      }
    // Load the input image.
    PlanarImage inputImage = JAI.create("fileload", args[0]);
    int minC = Integer.parseInt(args[1]);
    int maxC = Integer.parseInt(args[2]);
    // Error messages due to non-valid values are left to the reader.
    // Create several tasks, each with a different number of clusters.
    double partitionCoefficient,partitionEntropy,compactnessAndSeparation;
    double bestPartitionCoefficient = Double.MIN_VALUE;
    double bestPartitionEntropy = Double.MAX_VALUE;
    double bestCompactnessAndSeparation = Double.MAX_VALUE;
    int bestByPartitionCoefficient=1,bestByPartitionEntropy=1,bestByCompactnessAndSeparation=1;
    System.out.println("+----------+-----------------------+-------------------+----------------------------+");
    System.out.println("| Clusters | Partition Coefficient | Partition Entropy | Compactness and Separation |");
    System.out.println("+----------+-----------------------+-------------------+----------------------------+");
    for(int c=minC;c<=maxC;c++)
      {
      // Create the task.
      FuzzyCMeansImageClustering task = new FuzzyCMeansImageClustering(inputImage,c,100,2,0.005);
      task.run(); // Run it (without threading).
      // Get the resulting validity measures.
      partitionCoefficient = task.getPartitionCoefficient();
      partitionEntropy = task.getPartitionEntropy();
      compactnessAndSeparation = task.getCompactnessAndSeparation();
      // See which is the best so far.
      if (partitionCoefficient > bestPartitionCoefficient)
        {
        bestPartitionCoefficient = partitionCoefficient;
        bestByPartitionCoefficient = c;
        }
      if (partitionEntropy < bestPartitionEntropy)
        {
        bestPartitionEntropy = partitionEntropy;
        bestByPartitionEntropy = c;
        }
      if (compactnessAndSeparation < bestCompactnessAndSeparation)
        {
        bestCompactnessAndSeparation = compactnessAndSeparation;
        bestByCompactnessAndSeparation = c;
        }
      // Print a simple report.
      System.out.println("|    "+String.format("%2d",new Object[]{c})+
                         "    |"+String.format("%23.6f|%19.6f|%28.6f|",
                                 new Object[]{partitionCoefficient,partitionEntropy,compactnessAndSeparation}));
      }
    System.out.println("+----------+-----------------------+-------------------+----------------------------+");
    System.out.println("Best number of clusters:");
    System.out.println("  according to Partition Coefficient:"+bestByPartitionCoefficient);
    System.out.println("  according to Partition Entropy:"+bestByPartitionEntropy);
    System.out.println("  according to Compactness and Separation:"+bestByCompactnessAndSeparation);
    }
  }