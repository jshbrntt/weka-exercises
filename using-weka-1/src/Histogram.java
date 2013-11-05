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
	public int getNumOfBins()
	{
		return numOfBins;
	}

	public void setNumOfBins(int numOfBins)
	{
		this.numOfBins = numOfBins;
	}

	public int getAttPos()
	{
		return attPos;
	}

	public void setAttPos(int attPos)
	{
		this.attPos = attPos;
	}

	protected int attPos;
	protected int numOfBins;
	protected double[] binIntervals;
	protected int[][] histograms;
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
		//	WORKING OUT THE GLOBAL MAXIMUM AND MINIMUM:
		for (int i = 0; i < instances.numInstances(); ++i)
		{
			System.out.println("Value "+i+":\t"+instances.instance(i).value(attPos));
			max = Math.max(max, instances.instance(i).value(attPos));
			min = Math.min(min, instances.instance(i).value(attPos));
		}

		//	THE DIFFERENCE BETWEEN THE GLOBAL MAXIMUM AND MINIMUM:
		double diff		= max - min;
		System.out.println("Maximum:\t" + max);
		System.out.println("Minimum:\t" + min);
		System.out.println("Difference:\t" + diff);
		System.out.println("Bins:\t"+numOfBins);

		//	WORKING OUT THE RANGE OF EACH BIN, INITIALISING ARRAY TO STORE THE BIN INTERVALS:
		double range	= diff / numOfBins;
		binIntervals	= new double[numOfBins + 1];

		//	GENERATING BIN INTERVALS USING THE RANGE AND GLOBAL MINIMUM:
		for (int i = 0; i < binIntervals.length; ++i)
		{
			binIntervals[i]	= min + range * i;
			System.out.println("Bin "+i+":\t"+(min + range * i));
		}

		//	INITIALISING HISTOGRAMS (ONE FOR EACH CLASS):
		System.out.println("Initialising Histograms:\t"+instances.numClasses()+" by "+numOfBins);
		histograms	= new int[instances.numClasses()][numOfBins];
		for (int i = 0; i < histograms.length; i++)
		{
			String string	= "";
			if(i == 0) string += "Male:\t";
			if(i == 1) string += "Female:\t";
			for (int j = 0; j < histograms[0].length; j++)
			{
				string += histograms[i][j] + ",";
			}
			System.out.println(string);
		}

		//	COUNTING ALL INSTANCES USING THEIR RELATIVE CLASS HISTOGRAM:
		for (int k = 0; k < instances.numInstances(); ++k)
		{
			double value	= instances.instance(k).value(attPos);
			int classIndex	= (int) instances.instance(k).classValue();
			int binIndex	= 0;
			for (int i = 0; i < (binIntervals.length - 1); ++i)
			{
				if(value >= binIntervals[i] && value < binIntervals[i+1])
				{
					System.out.println("Falls into Bin "+i+":\t"+binIntervals[i]+" >= "+value+" < "+ binIntervals[i+1]+": "+(classIndex == 0 ? "Male" : "Female"));
					histograms[classIndex][i]++;
				}
			}
		}

		for (int i = 0; i < histograms.length; i++)
		{
			String string	= "";
			if(i == 0) string += "Male:\t";
			if(i == 1) string += "Female:\t";
			for (int j = 0; j < histograms[0].length; j++)
			{
				string += histograms[i][j] + ",";
			}
			System.out.println(string);
		}
	}

	@Override
	public double[] distributionForInstance(Instance instance) throws Exception
	{
		//	WHICH BIN DOES THE INSTANCE FALL INTO:
		int binIndex = 0;
		for (int i = 0; i < (binIntervals.length - 1); ++i)
		{
			if(instance.value(attPos) >= binIntervals[i] && instance.value(attPos) < binIntervals[i+1])
			{
				System.out.println("Instance: "+instance.value(attPos)+" is in Bin: "+i+" ("+binIntervals[i]+">=x<"+binIntervals[i+1]+")");
			    binIndex = i;
				break;
			}
		}

		//	WORKING OUT FREQUENCY SUM OF HISTOGRAMS:
		int freqSum[] = new int[histograms.length];
		for (int i = 0; i < histograms.length; ++i)
		{
			for (int j = 0; j < histograms[0].length; ++j)
			{
				freqSum[i]	+= histograms[i][j];
			}
		}

		//	CALCULATING INDEPENDENT PROBABILITIES, AND THERE SUM:
		double binProbs[]	= new double[histograms.length];
		double probSum = 0;
		for (int i = 0; i < histograms.length; ++i)
		{
			double binProb	= (double)histograms[i][binIndex] / freqSum[i];
			binProbs[i]	= binProb;
			probSum		+= binProb;
			System.out.println(
					(i == 0 ? "Male" : "Female")+" Probability:\t"
					+ histograms[i][binIndex] +"/"+ freqSum[i]+" = "+binProb);
		}

		//	NORMALISING PROBABILITIES USING THE SUM:
		for (int i = 0; i < binProbs.length; ++i)
		{
			binProbs[i] /= probSum;
			System.out.println(
					(i == 0 ? "Male" : "Female") + " Normalised Probability:\t"
					+ binProbs[i]);
		}

		//	RETURNING PROBABILITY OF INSTANCE FALLING INTO EACH CLASS:
		return binProbs;
	}
}
