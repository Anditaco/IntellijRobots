package IntellijRobots;

import java.util.SortedMap;

abstract class Action {
    private SortedMap<Integer, Double> durationToProbability;
    public double getProbabilityAtDuration(int duration){
        if(!isPossible()) return 0;
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

    abstract boolean isPossible();
}

class AccelerateForwards extends Action{
    @Override
    boolean isPossible() {
        return false;
    }
    //TODO
    //link probability map to generating and applying a move;
}
class AccelerateBackwards extends Action{
    @Override
    boolean isPossible() {
        return false;
    }
    //TODO
    //link probability map to generating and applying a move;
}
class TurnRight extends Action{
    @Override
    boolean isPossible() {
        return true;
    }
    //TODO
    //link probability map to generating and applying a move;
}
class TurnLeft extends Action{
    @Override
    boolean isPossible() {
        return true;
    }
    //TODO
    //link probability map to generating and applying a move;
}
