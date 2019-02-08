package IntellijRobots;

import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.ScannedRobotEvent;

class Enemy {
    private AdvancedRobot player;

    private ProbabilityMap probabilities;
    private double[][] projections = new double[4][2];

    private String name;
    private double x,y;
    private double heading;
    private double speed;
    private double energy;
    private double distance;
    private double bearing;
    private double angle;

    private long lastUpdateTime = 0;

    Enemy(ScannedRobotEvent e, AdvancedRobot player){
        this.player = player;
        probabilities = new ProbabilityMap((IntellijRobot)player);

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

    double getHeadingR(){return heading * Math.PI / 180;}

    private void updateProbabilities(ScannedRobotEvent e){
        probabilities.updateProbabilities(e, this);
    }

    public double[][] getProjectionsAtTime(int dT){

        /* predicted locations for no change in speed */
        double turnRate = Rules.getTurnRateRadians(this.getSpeed());

        double xNoTurnNoDV = dT * this.getSpeed() * Math.cos(Math.PI/2 - this.getHeadingR()) + this.getX();
        double yNoTurnNoDV = dT * this.getSpeed() * Math.sin(Math.PI/2 - this.getHeadingR()) + this.getY();

        double xTurningRightNoDV = -1 * (this.getSpeed()/turnRate) * Math.cos(Math.PI - this.getHeadingR() + turnRate*dT) + this.getX();
        double yTurningRightNoDV = -1 * (this.getSpeed()/turnRate) * Math.sin(Math.PI - this.getHeadingR() + turnRate*dT) + this.getY();

        double xTurningLeftNoDV = -1 * (this.getSpeed()/(turnRate*-1)) * Math.cos(Math.PI - this.getHeadingR() + turnRate*dT*-1) + this.getX();
        double yTurningLeftNoDV = -1 * (this.getSpeed()/(turnRate*-1)) * Math.sin(Math.PI - this.getHeadingR() + turnRate*dT*-1) + this.getY();


        /* predicted locations for only negative change in speed */

        double slowRate = Rules.DECELERATION;
        double speedRate = Rules.ACCELERATION;

        double dNTFSD = 0;
        for(int i = 0; i < dT; i++){
            if(this.getSpeed() - slowRate*i >= 0){
                dNTFSD += this.getSpeed() - slowRate*i;
            }
            else if(i-this.getSpeed()/slowRate * speedRate <= Rules.MAX_VELOCITY){
                dNTFSD -= i-this.getSpeed()/slowRate * speedRate;
            }
            else{
                dNTFSD -= Rules.MAX_VELOCITY;
            }
        }
        double avgSpeedDNTFSD = dNTFSD/dT;
        double xNoTurnFullSlow = avgSpeedDNTFSD * Math.cos(Math.PI/2 + this.getHeadingR()) + this.getX();
        double yNoTurnFullSlow = avgSpeedDNTFSD * Math.sin(Math.PI/2 + this.getHeadingR()) + this.getY();


        /* predicted locations for only positive change in speed */

        double aNTFSD = 0;
        for(int i = 0; i < dT; i++){
            if(this.getSpeed() + slowRate*i <= 0){
                aNTFSD -= this.getSpeed() + slowRate*i;
            }
            else if(i-this.getSpeed()/slowRate * speedRate <= Rules.MAX_VELOCITY){
                aNTFSD += i-this.getSpeed()/slowRate * speedRate;
            }
            else{
                aNTFSD += Rules.MAX_VELOCITY;
            }
        }
        double avgSpeedSNTFSD = aNTFSD/dT;
        double xNoTurnFullSpeed = avgSpeedSNTFSD * Math.cos(Math.PI/2 + this.getHeadingR()) + this.getX();
        double yNoTurnFullSpeed = avgSpeedSNTFSD * Math.sin(Math.PI/2 + this.getHeadingR()) + this.getY();


        return new double[][] {{xNoTurnFullSlow, yNoTurnFullSlow},
                                {xTurningRightNoDV, yTurningRightNoDV},
                                {xNoTurnNoDV, yNoTurnNoDV},
                                {xTurningLeftNoDV, yTurningLeftNoDV},
                                {xNoTurnFullSpeed, yNoTurnFullSpeed}};
    }

    public void setProjections(double d1, double d2, double d3, double d4, double d5, double d6, double d7, double d8){
        projections[0][0] = d1;
        projections[0][1] = d2;
        projections[1][0] = d3;
        projections[1][1] = d4;
        projections[2][0] = d5;
        projections[2][1] = d6;
        projections[3][0] = d7;
        projections[3][1] = d8;
    }

}
