import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Created with IntelliJ IDEA.
 * User: Josh
 * Date: 29/10/13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
public class Histogram extends Classifier
{
	public int getNumOfBins() {
		return numOfBins;
	}

	public void setNumOfBins(int numOfBins) {
		this.numOfBins = numOfBins;
	}

	public int getAttPos() {
		return attPos;
	}

	public void setAttPos(int attPos) {
		this.attPos = attPos;
	}

	protected int attPos;
	protected int numOfBins;
	protected double[] binIntervals;
	protected double max;
	protected double min;

	public Histogram()
	{
		max			= Float.MIN_VALUE;
		min			= Float.MAX_VALUE;
		attPos		= 0;
		numOfBins	= 10;
	}

	@Override
	public void buildClassifier(Instances instances) throws Exception
	{
		for (int i = 0; i < instances.numInstances(); ++i)
		{
			System.out.println("Value "+i+":\t"+instances.instance(i).value(attPos));
			max = Math.max(max, instances.instance(i).value(attPos));
			min = Math.min(min, instances.instance(i).value(attPos));
		}

		double diff		= max - min;
		System.out.println("Maximum:\t" + max);
		System.out.println("Minimum:\t" + min);
		System.out.println("Difference:\t" + diff);

		double interval	= diff / numOfBins;
		binIntervals	= new double[numOfBins + 1];

		for (int j = 0; j < binIntervals.length; ++j)
		{
			binIntervals[j]	= min + interval * j;
			System.out.println("Bin "+j+":\t"+(min + interval * j));
		}
	}

	@Override
	public double[] distributionForInstance(Instance instance) throws Exception
	{
		for (int i = 0; i < (binIntervals.length - 1); ++i)
		{
			if(instance.value(attPos) >= binIntervals[i] && instance.value(attPos) < binIntervals[i+1])
			{
				System.out.println(binIntervals[i]+" "+instance.value(attPos)+" "+ binIntervals[i+1]);
			}
		}
		return null;
	}
}
