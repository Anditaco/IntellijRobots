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
        possibleActions.add(new IntellijRobots.Turn());
    }

    void updateProbabilities(ScannedRobotEvent newData, Enemy oldData) {
        long dT = newData.getTime() - oldData.getTime();
        if (dT > 8) return;
        //intellijRobot.out.println(dT + " ticks happened since " + oldData.getName() + " was last updated");

        double accelerationTickCount = 0;
        double speed = oldData.getSpeed();
        if(oldData.getSpeed() < newData.getVelocity()){
            while(speed < newData.getVelocity()){
                accelerationTickCount++;
                speed = speed < 0 ? speed + 2 : speed + 1;
            }
        }
        else{
            while(speed > newData.getVelocity()){
                accelerationTickCount++;
                speed = speed > 0 ? speed - 2 : speed - 1;
            }
        }

        double averageTurnRate;
        if(oldData.getHeading() != -1)
            averageTurnRate = ((newData.getHeading()+360-oldData.getHeading())%360)/dT;
        else averageTurnRate = 0;

        double averageAcceleration = (newData.getVelocity() - oldData.getSpeed())/dT;

        double averageTurnSpeed = Rules.getTurnRate((oldData.getSpeed() + newData.getVelocity())/2.0);

        int startingTick;
        //intellijRobot.out.println("last known heading: " + oldData.getHeading());
        //intellijRobot.out.println("new heading: " + newData.getHeading());
        if(oldData.getHeading() == newData.getHeading() && averageAcceleration == 0 && oldData.getSpeed() != 0){
           startingTick = oldData.getTicksWithoutChange();
           oldData.setLastKnownTurnRate(averageTurnRate);
           oldData.setTicksWithoutChange(startingTick + (int)dT);
        }
        else{
            startingTick = 0;
            oldData.setTicksWithoutChange(0);
        }
        //intellijRobot.out.println("starting tick: " + startingTick);

        //probability for each tick to turn that gives an expected value of the yielded amount turned
        double turnProbability = (averageTurnRate == 0 || dT == 0 || averageTurnSpeed == 0) ? 0 : Math.pow(averageTurnRate/averageTurnSpeed,1/dT);
        //intellijRobot.out.println("robot turned " + ((newData.getHeading()+360-oldData.getHeading())%360) + " degrees");
        //intellijRobot.out.println("average turn speed: " + averageTurnSpeed);
        //intellijRobot.out.println("turn probability is " + turnProbability);

        //probability for each tick to change speed that gives an expected value of the number of ticks where speed changed
        double accelerateProbability = Math.pow(Math.abs(accelerationTickCount), 1.0/dT);


        int tSign = averageTurnRate < 0 ? -1 : 1;
        int aSign = averageAcceleration < 0 ? -1 : 1;
        for(int i = startingTick; i < startingTick + dT; i++){
            possibleActions.get(1).weigh(i, turnProbability*tSign);
            possibleActions.get(0).weigh(i, accelerateProbability*aSign);
        }
    }

    public Action getAccelerateAction(){
        return possibleActions.get(0);
    }

    public Action getTurnAction(){
        return possibleActions.get(1);
    }
}
