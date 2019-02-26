package IntellijRobots;

import java.util.Map;
import java.util.TreeMap;

abstract class Action {

    private Map<Integer, Integer> dataCounts = new TreeMap<Integer, Integer>();
    private Map<Integer, Double> durationToProbability = new TreeMap<Integer, Double>();
    public double getProbabilityAtDuration(int duration){
        Integer[] keys = durationToProbability.keySet().toArray(new Integer[durationToProbability.size()]);
        for(int i = 0; i < keys.length; i++){
            if(keys[i] == duration){
                return durationToProbability.get(keys[i]);
            }
        }
        return 0;
    }

    public void weigh(int d, double probability){
        if(Math.abs(probability) > 1) return;

        if(!dataCounts.containsKey(d)) dataCounts.put(d,1);
        else{dataCounts.replace(d, dataCounts.get(d) + 1);}

        if(!durationToProbability.containsKey(d)) durationToProbability.put(d,probability);
        else{durationToProbability.replace(d, (probability - durationToProbability.get(d))/((double)dataCounts.get(d)) + durationToProbability.get(d));}
    }

    public int getScopeOfDuration(){
        Integer[] keys = durationToProbability.keySet().toArray(new Integer[durationToProbability.size()]);
        return keys[keys.length-1];
    }

}

class Accelerate extends Action{ }
class Turn extends Action{ }
