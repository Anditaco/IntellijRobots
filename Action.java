package IntellijRobots;

import java.util.Map;
import java.util.TreeMap;

abstract class Action {


    //TODO
    //FIX PROBABILITIES SO THEY ARE NOT 0





    private Map<Integer, Integer> dataCounts = new TreeMap<Integer, Integer>();
    private Map<Integer, Double> durationToProbability = new TreeMap<Integer, Double>();
    public double getProbabilityAtDuration(int duration){
        Integer[] keys = durationToProbability.keySet().toArray(new Integer[durationToProbability.size()]);
        for(int i = 0; i < keys.length; i++){
            if(keys[i] > duration){
                double dY = durationToProbability.get(keys[i]) - durationToProbability.get(keys[i-1]);
                double dX = keys[i] - keys[i-1];
                double estimatedProbability = dY/dX * (duration - keys[i-1]);
                return estimatedProbability;
            }
        }
        return 0;
    }

    public void weigh(int d, double probability){
        if(!dataCounts.containsKey(d)) dataCounts.put(d,1);
        else{dataCounts.replace(d, dataCounts.get(d) + 1);}

        if(!durationToProbability.containsKey(d)) durationToProbability.put(d,probability);
        else{durationToProbability.replace(d, (probability - durationToProbability.get(d))/dataCounts.get(d) + durationToProbability.get(d));}
    }

    public int getScopeOfDuration(){
        Integer[] keys = durationToProbability.keySet().toArray(new Integer[durationToProbability.size()]);
        return keys[keys.length-1];
    }

}

class Accelerate extends Action{ }
class Turn extends Action{ }
