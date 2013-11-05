import sun.nio.cs.HistoricallyNamedCharset;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IB1;
import weka.core.Instance;
import weka.core.Instances;

import java.io.FileReader;
import java.io.IOException;

public class Main {
	public static Instances loadData(String fullPath) {
		Instances d = null;
		FileReader r;
		try {
			r = new FileReader(fullPath);
			d = new Instances(r);
			d.setClassIndex(d.numAttributes() - 1);
		} catch (IOException e) {
			System.out.println("Unable to load data on path " + fullPath + " Exception thrown =" + e);
			System.exit(0);
		}
		return d;
	}


	public static void main(String[] args) {
		Instances instances = loadData("HeightSex.arff");

		instances.setClassIndex(instances.instance(0).numAttributes() - 1);
		Histogram histogram = new Histogram();
		try
		{
			histogram.buildClassifier(instances);
			histogram.distributionForInstance(instances.instance(3));
		}
		catch(Exception e)
		{

		}

		/*
		train.setClassIndex(train.instance(0).numAttributes() - 1);

		// Naive Bayes Classifier
		NaiveBayes classifier1	= new NaiveBayes();
		IB1 classifier2			= new IB1();
		try
		{
			classifier1.buildClassifier(train);
			classifier2.buildClassifier(train);

			for (int i = 0; i < train.numInstances(); ++i)
			{
				System.out.println("Prediction " + i +	":\t" + classifier1.classifyInstance(train.instance(i))
														+ "\t" + classifier2.classifyInstance(train.instance(i)));
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		*/
	}
}