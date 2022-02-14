package neuralnetwork;

public class DotThread implements Runnable{

    FeedforwardLayer layer;
    double[] data;

    public DotThread(FeedforwardLayer layer, double[] data) {
        this.layer = layer;
        this.data = data;
    }

    @Override
    public void run() {
        double ret = NeuralNetwork.THREAD_START;
        DotLayer in = new DotLayer(new double[0], 0);
        while ((in = layer.getNextThreadCalculation(ret, in.getIndex())) != null) {
            ret = dotProduct(in);
            if(this.layer.getNetwork().activationFunction==FeedforwardNetwork.TANH){
                ret = Math.tanh(ret);
            }else if(this.layer.getNetwork().activationFunction==FeedforwardNetwork.SIG){
                ret = 1/(1+Math.exp(-ret));
            }
        }
    }

    public void setMainData(double[] data) {
        this.data = data;
    }
    
    public void setLayer(FeedforwardLayer layer){
        this.layer = layer;
    }

    double dotProduct(DotLayer in) {
        if(data.length!=in.getData().length)NeuralNetwork.say("DotProduct: a!=b");
        double ret = 0;
        for (int i = 0; i < data.length; i++) {
            ret += data[i] * in.getData()[i];
        }
        return ret;
    }
}
