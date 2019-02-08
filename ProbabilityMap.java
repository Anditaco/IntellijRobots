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
        if(dT > 8) return;
        intellijRobot.out.println(dT + " ticks happened since " + oldData.getName() + " was last updated");

        //Calculate their estimated data if everything stayed the same
        double turnRate = Rules.getTurnRateRadians(oldData.getSpeed());
        double slowRate = Rules.DECELERATION;
        double speedRate = Rules.ACCELERATION;


        double sNTFSD = 0;
        for(int i = 0; i < dT; i++){
            if(oldData.getSpeed() - slowRate*i >= 0){
                sNTFSD += oldData.getSpeed() - slowRate*i;
            }
            else{
                sNTFSD -= i-oldData.getSpeed()/slowRate * speedRate;
            }
        }
        double avgSpeedNTFSD = sNTFSD/dT;
        double xNoTurnFullSlow = avgSpeedNTFSD * Math.cos(Math.PI/2 + oldData.getHeadingR()) + oldData.getX();
        double yNoTurnFullSlow = oldData.getSpeed() * Math.sin(Math.PI/2 + oldData.getHeadingR()) + oldData.getY();

        double xNoTurnNoSlow = oldData.getSpeed() * Math.cos(Math.PI/2 + oldData.getHeadingR()) + oldData.getX();
        double yNoTurnNoSlow = oldData.getSpeed() * Math.sin(Math.PI/2 + oldData.getHeadingR()) + oldData.getY();

        double xTurningRightNoSlow = (oldData.getSpeed()/turnRate) * Math.cos(Math.PI - oldData.getHeadingR() + turnRate*dT) + oldData.getX();
        double yTurningRightNoSlow = (oldData.getSpeed()/turnRate) * Math.sin(Math.PI - oldData.getHeadingR() + turnRate*dT) + oldData.getY();

        double xTurningLeftNoSlow = (oldData.getSpeed()/(turnRate*-1)) * Math.cos(Math.PI - oldData.getHeadingR() + turnRate*dT*-1) + oldData.getX();
        double yTurningLeftNoSlow = (oldData.getSpeed()/(turnRate*-1)) * Math.sin(Math.PI - oldData.getHeadingR() + turnRate*dT*-1) + oldData.getY();


        oldData.setProjections(xNoTurnFullSlow, yNoTurnFullSlow, xNoTurnNoSlow, yNoTurnNoSlow, xTurningRightNoSlow, yTurningRightNoSlow, xTurningLeftNoSlow, yTurningLeftNoSlow);

        //TODO
        //MAKE THE PROJECTIONS DRAW oN THE SCREEN


        //check against current data


        //if they have to have turned, update probability for all durations lower than dT

        //if they have to have changed speed, update probability for all durations lower than dT

    }
}
