import sun.nio.cs.HistoricallyNamedCharset;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IB1;
import weka.core.Instance;
import weka.core.Instances;

import java.io.FileReader;
import java.io.IOException;

public class Main
{
	public static Instances loadData(String fullPath)
	{
		Instances d = null;
		FileReader r;
		try {
			r = new FileReader(fullPath);
			d = new Instances(r);
			d.setClassIndex(d.numAttributes() - 1);
		}
		catch (IOException e)
		{
			System.out.println("Unable to load data on path " + fullPath + " Exception thrown =" + e);
			System.exit(0);
		}
		return d;
	}

	public static void main(String[] args)
	{
		startQuestionFour();
		startQuestionFive();
	}

	private static void startQuestionFour()
	{
		//	IMPORTING TEXT FILE:
		Instances instances	= loadData("ArsenalTrain.arff");

		//	SETTING CLASS INDEX TO THE LAST ATTRIBUTE (Loss,Win,Draw):
		instances.setClassIndex(instances.instance(0).numAttributes() - 1);

		//	CONSTRUCTING CLASSIFIER OBJECTS:
		NaiveBayes nativeBayes	= new NaiveBayes();
		IB1 ib1					= new IB1();

		try
		{
			//	BUILDING CLASSIFIERS ON TRAINING DATA:
			nativeBayes.buildClassifier(instances);
			ib1.buildClassifier(instances);

			//	GETTING PREDICTION OF RESULT FROM BOTH CLASSIFIERS (Loss,Win,Draw):
			for (int i = 0; i < instances.numInstances(); ++i)
			{
				double prob1	= nativeBayes.classifyInstance(instances.instance(i));
				double prob2	= nativeBayes.classifyInstance(instances.instance(i));
				System.out.println("Bayes:\t"+prob1+"\tIB1:\t"+prob2);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	private static void startQuestionFive()
	{
		//	IMPORTING TEXT FILE:
		Instances instances	= loadData("HeightSex.arff");

		//	SETTING CLASS INDEX TO THE LAST ATTRIBUTE ([M]ale, [F]emale):
		instances.setClassIndex(instances.instance(0).numAttributes() - 1);
		Histogram histogram = new Histogram();

		try
		{
			//	BUILDING CLASSIFIERS ON TRAINING DATA:
			histogram.buildClassifier(instances);

			//	GETTING THE PROBABILITY OF IT FALLING INTO EITHER CLASS ([M]ale, [F]emale):
			double[] probs = histogram.distributionForInstance(instances.instance(8));
			System.out.println("Probability Instance is Male:\t"+probs[0]);
			System.out.println("Probability Instance is Female:\t"+probs[1]);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}