package math;

/*
 * Part of the Java Image Processing Cookbook, please see
 * http://www.lac.inpe.br/JIPCookbook/index.jsp
 * for information on usage and distribution.
 * Rafael Santos (rafael.santos@lac.inpe.br)
 */
 
/**
 * This abstract class represents a image processing task that is executed
 * on its own thread.
 * This class uses the concept of processing task size and position - if
 * one can estimate the size (in steps, for example) of the task, an
 * application could get the position of the processing, i.e. how far we
 * are on the task.
 */
public abstract class ImageProcessingTask extends Thread
  {
 /**
  * This is the method (inherited from Thread) which will do the bulk
  * image processing. It is declared as abstract as a reminder to the
  * programmer, which must implement it.
  */
  public abstract void run();
 
 /**
  * This method returns the size of the image processing task. The size can
  * be any estimated value measured in any unit (e.g. number of loops or
  * processed pixels).
  * This method must be implemented on classes which inherit from this one and its
  * result should be constant, i.e. not rely on variables or counters that may
  * change during the execution of the algorithm.
  * @return the size of the image processing size.
  */
  public abstract long getSize();
 
 /**
  * This method returns the position on the image processing task, i.e. how many
  * processing steps were already done. The classes that inherits from this one
  * must implement this method.
  * @return the position of the task processing.
  */
  public abstract long getPosition();
 
 /**
  * This method returns true if the task is finished. Please notice that
  * it is not implemented as (position == size) since there may be cases
  * where the task size is estimated and the position may not be ever
  * equal to the exact size.
  * This method must be implemented on classes which inherit from this one.
  * @return true if the processing task has finished.
  */
  public abstract boolean isFinished();
  }