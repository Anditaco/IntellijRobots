package IntellijRobots;

import java.util.SortedMap;

abstract class Action {
    private SortedMap<Integer, Double> durationToProbability;
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
        //TODO
        //Implement weigh so as to factor in its previous state
        durationToProbability.put(d, probability);
    }
}

class Accelerate extends Action{

}
class Turn extends Action{

}
