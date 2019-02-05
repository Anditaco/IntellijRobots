package IntellijRobots;

import robocode.ScannedRobotEvent;

import java.util.ArrayList;

class ProbabilityMap {
    private IntellijRobot intellijRobot;
    ArrayList<Action> possibleActions = new ArrayList<>();
    public ProbabilityMap(IntellijRobot intellijRobot){
        this.intellijRobot = intellijRobot;
        possibleActions.add(new AccelerateForwards());
        possibleActions.add(new AccelerateBackwards());
        possibleActions.add(new TurnRight());
        possibleActions.add(new TurnLeft());
    }

    public void updateProbabilities(ScannedRobotEvent newData, Enemy oldData){
        long dT = newData.getTime() - oldData.getTime();
        intellijRobot.out.println(dT + " ticks happened since " + oldData.getName() + " was last updated");

        //TODO
        //Calculate disparity between data and apply it to the actions' sortedmap
        //with duration being time since last change of action
    }
}
