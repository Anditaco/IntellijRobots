package IntellijRobots;


import robocode.Rules;

import java.awt.*;

//should generate the locations an enemy could be based on its movement probabilities
public class Predictor {
    Enemy enemy;
    IntellijRobot player;

    public Predictor(Enemy e, IntellijRobot p){
        enemy = e;
        player = p;
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
            double theta1 = (5*Math.PI/2 - heading*Math.PI/180)%(2*Math.PI);
            double dTheta = dHeading*Math.PI/180.0;

            double xPrime = location.getX() + 2*speed/dTheta*Math.sin(dTheta/2)*Math.cos(Math.PI/2-dTheta/2-theta1);
            double yPrime = location.getY() + 2*speed/dTheta*Math.sin(dTheta/2)*Math.sin(Math.PI/2-dTheta/2-theta1);

            location.setLocation(xPrime,yPrime);

            player.out.println("Starting info: x = " + location.getX() + ", y = " + location.getY() + ", heading = " + heading + ", speed = " + speed);

            heading += dHeading;
            speed += dSpeed;

            player.out.println("Calculated info: x = " + xPrime + ", y = " + yPrime + " heading " + heading + ", speed = " + speed);
        }
        return location;
    }
}
