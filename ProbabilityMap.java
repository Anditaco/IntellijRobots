package IntellijRobots;

import robocode.Rules;
import robocode.ScannedRobotEvent;

import java.util.ArrayList;

class ProbabilityMap {
    private IntellijRobot intellijRobot;
    private ArrayList<Action> possibleActions = new ArrayList<>();
    ProbabilityMap(IntellijRobot intellijRobot){
        this.intellijRobot = intellijRobot;
        possibleActions.add(new AccelerateForwards());
        possibleActions.add(new AccelerateBackwards());
        possibleActions.add(new TurnRight());
        possibleActions.add(new TurnLeft());
    }

    void updateProbabilities(ScannedRobotEvent newData, Enemy oldData){
        long dT = newData.getTime() - oldData.getTime();
        intellijRobot.out.println(dT + " ticks happened since " + oldData.getName() + " was last updated");

        //Calculate their estimated data if everything stayed the same
        double turnRate = Rules.getTurnRateRadians(oldData.getSpeed());


        //check against current data


        //if they have to have turned, update probability for all durations lower than dT

        //if they have to have changed speed, update probability for all durations lower than dT

    }
}
