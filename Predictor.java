package IntellijRobots;


import robocode.Rules;

import java.awt.*;
import java.util.ArrayList;

//should generate the locations an enemy could be based on its movement probabilities
public class Predictor {
    Enemy enemy;

    public Predictor(Enemy e){
        enemy = e;
    }


    public Point mostLikelyPosition(int time){
        Point location = new Point((int)enemy.getX(), (int)enemy.getY());
        double heading = enemy.getHeading();
        double speed = enemy.getSpeed();

        for(int i = 0; i < time; i++){
            //update location based on expected behavior at time
            double speedProbability = enemy.getProbabilities().getAccelerateAction().getProbabilityAtDuration(i);
            double speedFactor = (speedProbability != 0 && speed != 0) ? ((speedProbability/Math.abs(speedProbability) == speed/Math.abs(speed)) ? 1 : 2) : 1;

            double dSpeed = speedFactor*speedProbability;
            double dHeading = Rules.getTurnRate(speed)*enemy.getProbabilities().getTurnAction().getProbabilityAtDuration(i);
            double dTheta = dHeading*Math.PI/180.0;

            //TODO Update the prime calculator to account for initial heading

            //x' = x + cos(dTheta)*2*dTheta/speed*tan(dTheta/2)
            //y' = y + sin(dTheta)*2*dTheta/speed*tan(dTheta/2)
            double xPrime = location.getX() + 2*Math.cos(dTheta)*dTheta*Math.tan(dTheta/2)/speed;
            double yPrime = location.getY() + 2*Math.sin(dTheta)*dTheta*Math.tan(dTheta/2)/speed;

            heading += dHeading;
            speed += dSpeed;
        }
    }
}
