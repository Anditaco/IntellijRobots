package IntellijRobots;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

class Enemy {
    AdvancedRobot player;

    ProbabilityMap probabilities;

    String name;
    double x, y;
    double heading;
    double speed;
    double energy;
    double distance;
    double bearing;
    double angle;

    long lastUpdateTime = 0;
    long currentUpdateTime = 0;

    Enemy(ScannedRobotEvent e, AdvancedRobot player){
        this.player = player;
        probabilities = new ProbabilityMap((IntellijRobot)player);

        name = e.getName();
        updateData(e);
    }

    public void updateData(ScannedRobotEvent e){
        lastUpdateTime = currentUpdateTime;
        currentUpdateTime = e.getTime();

        energy = e.getEnergy();
        heading = e.getHeading();
        speed = e.getVelocity();
        distance = e.getDistance();
        bearing = e.getBearing();

        angle = Math.toRadians((player.getHeading() + bearing) % 360);

        x = player.getX() + Math.sin(angle) * distance;
        y = player.getY() + Math.cos(angle) * distance;

        updateProbabilities(e);
    }

    long getTime(){
        return lastUpdateTime;
    }

    String getName(){return name;}

    double getX(){return x;}
    double getY(){return y;}

    private void updateProbabilities(ScannedRobotEvent e){
        probabilities.updateProbabilities(e, this);
    }
}
