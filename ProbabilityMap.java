package IntellijRobots;

import robocode.Rules;
import robocode.ScannedRobotEvent;

import java.util.ArrayList;

class ProbabilityMap {
    private IntellijRobot intellijRobot;
    private ArrayList<Action> possibleActions = new ArrayList<>();
    ProbabilityMap(IntellijRobot intellijRobot){
        this.intellijRobot = intellijRobot;
        possibleActions.add(new Accelerate());
        possibleActions.add(new Turn());
    }

    void updateProbabilities(ScannedRobotEvent newData, Enemy oldData) {
        long dT = newData.getTime() - oldData.getTime();
        if (dT > 8) return;
        intellijRobot.out.println(dT + " ticks happened since " + oldData.getName() + " was last updated");

        double averageTurnRate = (newData.getHeading() - oldData.getHeading())/dT;
        double averageAcceleration = (newData.getVelocity() - oldData.getSpeed())/dT;

        int startingTick;
        if(averageTurnRate == oldData.getLastKnownTurnRate() && averageAcceleration == 0){
           startingTick = oldData.getTicksWithoutChange();
           oldData.setLastKnownTurnRate(averageTurnRate);
           oldData.setTicksWithoutChange(startingTick + (int)dT);
        }
        else{
            startingTick = 0;
        }

        double averageTurnSpeed = Rules.getTurnRate((oldData.getSpeed() + newData.getVelocity())/2.0);
        //probability for each tick to turn that gives an expected value of the yielded amount turned
        double turnProbability = Math.pow(Math.abs((averageTurnRate*dT))/(Math.pow(averageTurnSpeed, dT)), 1.0/dT);

        //TODO
        //tallies number of ticks used to accelerate based on different braking and speeding up rates
        double accelerationTickCount;
        if((oldData.getSpeed() < 0 && newData.getVelocity() < 0)){
            accelerationTickCount = Math.abs(averageAcceleration);
        }
        //TODO
        //Factor in the different rates of acceleration based on direction to the probability of accelerating
        double accelerateProbability = Math.pow(Math.abs((averageAcceleration*dT))/(Math.pow(averageAcceleration, dT)), 1.0/dT);


        int tSign = averageTurnRate < 0 ? -1 : 1;
        int aSign = averageAcceleration < 0 ? -1 : 1;
        for(int i = startingTick; i < startingTick + dT; i++){
            possibleActions.get(1).weigh(i, turnProbability*tSign);
            possibleActions.get(0).weigh(i, accelerateProbability*aSign);
        }

    }
}
