package IntellijRobots;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

import java.awt.*;

class Enemy {
    private IntellijRobot player;

    private ProbabilityMap probabilities;
    private Predictor predictor;

    private String name;
    private double x,y;
    private double heading = -1;
    private double speed;
    private double energy;
    private double distance;
    private double bearing;
    private double angle;
    private double turnRate = 0;
    private int ticksWithoutChange = 0;

    private long lastUpdateTime = 0;
    private double lastKnownSpeed = 0;

    Enemy(ScannedRobotEvent e, IntellijRobot player){
        this.player = player;
        probabilities = new ProbabilityMap((IntellijRobot)player);
        predictor = new Predictor(this, player);

        name = e.getName();
        updateData(e);
    }

    void updateData(ScannedRobotEvent e){
        updateProbabilities(e);

        lastUpdateTime = e.getTime();

        energy = e.getEnergy();
        heading = e.getHeading();
        speed = e.getVelocity();
        distance = e.getDistance();
        bearing = e.getBearing();

        angle = Math.toRadians((player.getHeading() + bearing) % 360);

        x = player.getX() + Math.sin(angle) * distance;
        y = player.getY() + Math.cos(angle) * distance;
    }

    long getTime(){
        return lastUpdateTime;
    }

    String getName(){return name;}

    double getX(){return x;}
    double getY(){return y;}

    double getSpeed(){return speed;}

    double getHeading(){return heading;}

    double getLastKnownTurnRate(){return turnRate;}
    void setLastKnownTurnRate(double tr){turnRate = tr;}

    int getTicksWithoutChange(){return ticksWithoutChange;}
    void setTicksWithoutChange(int c){ticksWithoutChange = c;}

    private void updateProbabilities(ScannedRobotEvent e){
        probabilities.updateProbabilities(e, this);
    }
    public ProbabilityMap getProbabilities(){return probabilities;}

    public DoublePoint mostLikelyLocation(int time){
        return predictor.mostLikelyPosition(time);
    }
}
