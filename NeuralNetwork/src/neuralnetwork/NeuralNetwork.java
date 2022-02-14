package neuralnetwork;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class NeuralNetwork {

    public static void say(Object s) {
        System.out.println(s);
    }

    public static final String home = "C:\\Users\\jeann\\Desktop\\Cube Solver";
    public static File cubeDump = new File(home+"\\CubePieces.txt");
    public static File matrices = new File(home+"\\matrices.txt");
    public static File extrData = new File(home+"\\Extra Data.txt");
    
    //multithreading
    public static int numberOfThreads = 6;
    public static ExecutorService threadPool;
    public static boolean multiThread = false;
    public static double THREAD_START = 2.34;
    

    //holding variables
    public static double lastCorrectNess = 0;
    public static double secondToLastCorrectNess = 0;
    public static double lastHighestError = 100;
    public static ArrayList<double[]> reallyWrongs = new ArrayList<>();
    public static ArrayList<float[]>reallyWrongFloats = new ArrayList<>();

    //learning types
    public static final int REINFORCED_LEARNING = 1;
    public static final int ADJUSTED_LEARNING = 3;

    
    //settings
    public static int act = FeedforwardNetwork.SIG;
    public static int BASE = 5;
    public static int display = 100;
    public static int minorCube = 5;
    public static int majorCube = 20;
    public static double learningRate = 0.2;
    public static double learningMomentum = 0.0;
    public static double lowestError = 0.01;
    public static double maxRandom = 1.0;//The highest possible random number for the weight matrices
    public static double minRandom = -0.95;//The lowest possible random number for the weight matrices
    public static int learningType = ADJUSTED_LEARNING;
    public static double adjustedLearningError = 0.05;
    public static double reinforceGap = 0.01;//reinforce learning
    public static int testCubes = 10;//number of cubes to test the network on
    public static boolean randomStart = true;//if true, the network will start with random values, otherwise it will load in the matrix
    public static double correctNess = 0.5;//the value that the answer from the network must be greater than or equal to for it to be correct
    public static double highestIndividualError = 0.1;
    public static boolean compareTimes = false;//whether or not to test the actual method of comparing CubePieces to see the difference in time
    public static boolean displayErrors = false;//show all of the really wrong answers
    public static boolean shuffleData = false;//Shuffle the data around after each epoch
    public static boolean adaptiveLearningRate = false;//after any wrong answer, the learning rate is decreased, after a right answer, it is increased
    public static boolean focusOnReallyWrongs = false;//DOES NOT WORK...after at least 5 epochs, and the error rate is less than 10%, the training will focus on only the really wrong answers
    public static boolean higherRateOnLargeErrors = false;
    public static boolean injectRandom = false;
    public static double injectMax = 0.2;
    
    public static void main(String[] args) throws Exception {
        
        say(home);
        //Util.generateCubeData();System.exit(0);
        FeedforwardNetwork net = setupNetwork();
        //FloatingFeedforwardNetwork net = setupFloatingNetwork();
        net.saveMatrices(extrData);
        //testCubes=50000;test(net);System.exit(0);
        double[][] data = Util.getCubes();
        float[][] dataf = new float[data.length][data[0].length];
        for(int i = 0; i<data.length; i++){
            for(int j = 0; j<data[0].length; j++){
                dataf[i][j] = (float)data[i][j];
            }
        }
        

        for (int i = 0; i < 3; i++) {
            lastCorrectNess = 0;
            secondToLastCorrectNess = 0;
            testCubes *= 10;
            int index = 0;
            double c = 0.99;
            if(i>=2)c = 0.999;
            while (lastCorrectNess < c || secondToLastCorrectNess < c) {
                say("Universe " + index + ":");
                net.highestError = 100;
                net.lastError = 100;
                
                train(net, data);
                
                //double[][] wrong = test(net);
                //say("Wrongs: " + wrong.length + "\n\n\n\n");
                //double[][] newData = new double[data.length + wrong.length][data[0].length];
                //System.arraycopy(data, 0, newData, 0, data.length);
                //System.arraycopy(wrong, 0, newData, data.length, wrong.length);
                //data = newData;

                //Save the CubePieces
                try {
                    cubeDump.delete();
                    cubeDump.createNewFile();
                    try (BufferedWriter write = new BufferedWriter(new FileWriter(cubeDump))) {
                        for (double[] da : data) {
                            String s = "";
                            for (double d : da) {
                                s = s + (int) d;
                            }
                            s = s.substring(0, s.length());
                            write.write(s);
                            write.newLine();
                        }
                        write.flush();
                    }
                } catch (IOException e) {
                }
                index++;
            }
        }

    }

    public static double[][] test(FeedforwardNetwork net) {
        say("Generating Test CubePieces.");
        int falseCorrect = 0;
        int trueCorrect = 0;
        ArrayList<String> trues = CubePiece.generateCubeData(testCubes / 2, BASE * BASE, false, true);
        ArrayList<String> falses = CubePiece.generateCubeData(testCubes / 2, BASE * BASE, false, false);
        ArrayList<String> wrong = new ArrayList<>();
        double time1 = System.currentTimeMillis();
        double time2 = 0;
        say("Testing trues.");
        for (String s : trues) {
            //say("Testing Cube Data: " + s);
            boolean ans = Util.doubleToBoolean(Double.parseDouble(s.charAt(s.length() - 1) + ""));
            double ret = net.present(Util.stringToDoubles(s.substring(0, s.length() - 1)));
            boolean cor = (ret >= correctNess && ans == true) || (ret < correctNess && ans == false);
            //say("Output:" + ret + "  Correct Answer:" + ans);
            if (cor) {
                trueCorrect++;
            } else {
                wrong.add(s);
            }
            //say("");
        }
        say("Testing falses.");
        for (String s : falses) {
            //say("Testing Cube Data: " + s);
            boolean ans = Util.doubleToBoolean(Double.parseDouble(s.charAt(s.length() - 1) + ""));
            double ret = net.present(Util.stringToDoubles(s.substring(0, s.length() - 1)));
            boolean cor = (ret >= correctNess && ans == true) || (ret < correctNess && ans == false);
            //say("Output:" + ret + "  Correct Answer:" + ans);
            if (cor) {
                falseCorrect++;
            } else {
                wrong.add(s);
            }
            //say("");
        }
        time1 = System.currentTimeMillis() - time1;

        if (compareTimes) {
            int fake = 0;
            say("converting pieces");
            ArrayList<CubePiece> c11 = new ArrayList<>();
            ArrayList<CubePiece> c12 = new ArrayList<>();
            ArrayList<CubePiece> c21 = new ArrayList<>();
            ArrayList<CubePiece> c22 = new ArrayList<>();
            for (String s : trues) {
                CubePiece[] ca = Util.bipointsToCubePieces(s);
                c11.add(ca[0]);
                c12.add(ca[1]);
            }
            for (String s : falses) {
                CubePiece[] ca = Util.bipointsToCubePieces(s);
                c21.add(ca[0]);
                c22.add(ca[1]);
            }

            say("Starting normal tests.");
            time2 = System.currentTimeMillis();
            for (int i = 0; i < c11.size(); i++) {
                boolean ans = Util.doubleToBoolean(Double.parseDouble(trues.get(i).charAt(trues.get(i).length() - 1) + ""));
                boolean ret = c11.get(i).comparePiece(c12.get(i));
                if (true) {
                    fake++;
                }
            }
            for (int i = 0; i < c21.size(); i++) {
                boolean ans = Util.doubleToBoolean(Double.parseDouble(falses.get(i).charAt(falses.get(i).length() - 1) + ""));
                boolean ret = c21.get(i).comparePiece(c22.get(i));
                if (true) {
                    fake++;
                }
            }
            time2 = System.currentTimeMillis() - time2;
        }

        say("\n");
        say("The number of correct responces: " + (trueCorrect + falseCorrect) + "/" + testCubes);
        say("True correctness: " + (trueCorrect) + "/" + (testCubes / 2) + "  =  " + (trueCorrect * 1000 / (testCubes / 2)) / 10.0 + "%");
        say("False correctness: " + (falseCorrect) + "/" + (testCubes / 2) + "  =  " + (falseCorrect * 1000 / (testCubes / 2)) / 10.0 + "%");
        say("The overall correctness: " + ((trueCorrect + falseCorrect) * 1000 / testCubes) / 10.0 + "%");
        say("Time for Network: " + time1 / 1000 + "seconds");
        if (compareTimes) {
            say("Time for normal: " + time2 / 1000 + " seconds");
        }

        secondToLastCorrectNess = lastCorrectNess;
        lastCorrectNess = (double) (trueCorrect + falseCorrect) / testCubes;

        //returns the wrong answers
        double[][] ret = new double[wrong.size()][trues.get(0).length()];
        for (int i = 0; i < wrong.size(); i++) {
            ret[i] = Util.stringToDoubles(wrong.get(i));
        }
        return ret;
    }

    public static FeedforwardNetwork setupNetwork() {
        int first = BASE * BASE * BASE * 2;
        FeedforwardNetwork net = new FeedforwardNetwork(first);
        net.setLearningRate(learningRate);
        net.setActivationFunction(FeedforwardNetwork.SIG);
        net.addLayer((int) first / 2);//hidden
        net.addLayer(1);//output

        /*initialize the matrices*/
        if (randomStart) {
            say("Randomizing weight Matrices.");
            net.initialize();
        } else {
            say("Loading in weight matrix.");
            net.loadMatrices(Util.loadMatrices(new File(System.getProperty("user.home") + "\\Desktop\\BASE" + BASE + " MATRICES.txt")));
            for(int i = 0; i<net.getSize(); i++){
                net.getLayer(i).resetDeltaMatrix();
            }
        }
        return net;
    }
    
    
    public static FloatingFeedforwardNetwork setupFloatingNetwork() {
        int first = BASE * BASE * BASE * 2;
        FloatingFeedforwardNetwork net = new FloatingFeedforwardNetwork(first);
        net.setLearningRate((float)learningRate);
        net.setActivationFunction(FeedforwardNetwork.SIG);
        net.addLayer((int) first / 2);//hidden
        net.addLayer(1);//output

        /*initialize the matrices*/
        say("Randomizing weight Matrices.");
        net.initialize();
        return net;
    }

    public static void train(FeedforwardNetwork net, double[][] data) {
        double saveError = 100;
        boolean focusedTraining = false;
        for (int i = 0; i < 20000; i++) {
            if (net.lastError < saveError) {
                net.saveMatrices(matrices);
                saveError = net.lastError;
            }
            if (net.lastError <= lowestError && net.highestError < highestIndividualError) {
                break;
            }
            if(shuffleData)data = Util.shuffleData(data);
            
            net.setLearningRate(learningRate);
            say("Epoch " + i + ":");
            if(net.numberReallyWrong<=0.2*data.length&&net.lastError<=0.1&&net.highestError>0.999){
                if(focusOnReallyWrongs){
                    focusedTraining = true;
                    net.focusedTraining = true;
                }
                if(higherRateOnLargeErrors){
                    net.higherRate = true;
                    say("Higher Rate");
                }
                if(injectRandom){
                    say("Injecting Randomness");
                    for(int l = 0; l<net.getSize(); l++){
                        Matrix m = net.getLayer(l).getWeightMatrix();
                        for(int i1 = 0; i1<m.getRows(); i1++){
                            for(int j1 = 0; j1<m.getCols(); j1++){
                                m.set(i1, j1, injectMax*(1.0-2*Math.random()));
                            }
                        }
                    }
                }
            }
            if(reallyWrongs.isEmpty()){
                focusedTraining = false;
            }
            if(focusedTraining){
                say("Focused Epoch");
                double[][] data2 = new double[reallyWrongs.size()][reallyWrongs.get(0).length];
                for(int j = 0; j<reallyWrongs.size(); j++){
                    data2[j] = reallyWrongs.remove(j);
                }
                net.train(data2);
            }else{
                net.train(data);
            }
            
            if(adaptiveLearningRate){
                if(net.highestError<=lastHighestError&&net.highestError!=1.0){
                    learningRate *= 1.1;
                }else{
                    learningRate *= 0.9;
                }
            }
            if(learningRate>0.6)learningRate = 0.6;
            if(learningRate<0.1)learningRate = 0.1;
            if(net.highestError<0.4)learningMomentum = 0.3;
            lastHighestError = net.highestError;
            say("Current Learning Rate: "+learningRate);
            say("Really Wrong answers: "+net.numberReallyWrong);
            say("\n");
        }
    }
    
    
    
    
    public static void train(FloatingFeedforwardNetwork net, float[][] data) {
        float saveError = 100;
        boolean focusedTraining = false;
        for (int i = 0; i < 20000; i++) {
            if (net.lastError < saveError) {
                net.saveMatrices(matrices);
                saveError = net.lastError;
            }
            if (net.lastError <= lowestError && net.highestError < highestIndividualError) {
                break;
            }
            
            net.setLearningRate((float)learningRate);
            say("Epoch " + i + ":");
            if(net.numberReallyWrong<=0.2*data.length&&net.lastError<=0.1&&net.highestError>0.999){
                if(focusOnReallyWrongs){
                    focusedTraining = true;
                    net.focusedTraining = true;
                }
                if(higherRateOnLargeErrors){
                    net.higherRate = true;
                    say("Higher Rate");
                }
                if(injectRandom){
                    say("Injecting Randomness");
                    for(int l = 0; l<net.getSize(); l++){
                        FloatMatrix m = net.getLayer(l).getWeightFloatMatrix();
                        for(int i1 = 0; i1<m.getRows(); i1++){
                            for(int j1 = 0; j1<m.getCols(); j1++){
                                m.set(i1, j1, (float)(injectMax*(1.0-2*Math.random())));
                            }
                        }
                    }
                }
            }
            if(reallyWrongs.isEmpty()){
                focusedTraining = false;
            }
            if(focusedTraining){
                say("Focused Epoch");
                float[][] data2 = new float[reallyWrongs.size()][reallyWrongs.get(0).length];
                for(int j = 0; j<reallyWrongs.size(); j++){
                    data2[j] = reallyWrongFloats.remove(j);
                }
                net.train(data2);
            }else{
                net.train(data);
            }
            
            if(adaptiveLearningRate){
                if(net.highestError<=lastHighestError&&net.highestError!=1.0){
                    learningRate *= 1.1;
                }else{
                    learningRate *= 0.9;
                }
            }
            if(learningRate>0.6)learningRate = 0.6;
            if(learningRate<0.1)learningRate = 0.1;
            if(net.highestError<0.4)learningMomentum = 0.3;
            lastHighestError = net.highestError;
            say("Current Learning Rate: "+learningRate);
            say("Really Wrong answers: "+net.numberReallyWrong);
            say("\n");
        }
    }

}
