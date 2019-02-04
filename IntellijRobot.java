package IntellijRobots;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedMap;

public class IntellijRobot extends AdvancedRobot {

    private EnemyList enemies = new EnemyList(this);


    public void run() {

        Color radar = new Color(255, 205, 56);
        Color body = new Color(255, 169, 43);
        Color gun = new Color(136, 136, 136);

        setColors(body, gun, radar);

        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        turnRadarRight(Double.POSITIVE_INFINITY);
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if(!enemies.containsRobot(e.getName())) enemies.add(e);
        enemies.get(e).updateData(e);
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        super.onRobotDeath(event);
        enemies.remove(event);
    }

    @Override
    public void onPaint(Graphics2D g) {
        super.onPaint(g);
        g.setColor(Color.GREEN);
        g.drawString(Integer.toString(enemies.getEnemyList().size()), 50, 50);
        for(Enemy e : enemies.getEnemyList()) {
            g.drawLine((int)e.getX(), (int)e.getY(), (int) getX(), (int) getY());
            g.fillRect((int)e.getX() - 20, (int)e.getY() - 20, 40, 40);
        }
    }


    class Enemy{
        AdvancedRobot player;

        ProbabilityMap probabilities = new ProbabilityMap();

        String name;
        double x, y;
        double heading;
        double speed;
        double energy;
        double distance;
        double bearing;
        double angle;

        Enemy(ScannedRobotEvent e, AdvancedRobot player){
            this.player = player;
            name = e.getName();
            updateData(e);
        }

        private void updateData(ScannedRobotEvent e){
            energy = e.getEnergy();
            heading = e.getHeading();
            speed = e.getVelocity();
            distance = e.getDistance();
            bearing = e.getBearing();

            angle = Math.toRadians((player.getHeading() + bearing) % 360);

            x = player.getX() + Math.sin(angle) * distance;
            y = player.getY() + Math.cos(angle) * distance;
        }

        String getName(){return name;}

        double getX(){return x;}
        double getY(){return y;}
    }

    class EnemyList{
        AdvancedRobot player;

        EnemyList(AdvancedRobot player){
            this.player = player;
        }

        ArrayList<Enemy> enemyList = new ArrayList<>();

        ArrayList<Enemy> getEnemyList(){return enemyList;}

        void add(ScannedRobotEvent e){
            enemyList.add(new Enemy(e, player));
        }

        void remove(RobotDeathEvent d){
            enemyList.remove(getRobotByName(d.getName()));
        }

        Enemy get(ScannedRobotEvent s){
            return getRobotByName(s.getName());
        }

        private Enemy getRobotByName(String n){
            int i = 0;
            for(Enemy e : enemyList)
                if(enemyList.get(i++).getName().equals(n)) return e;
            return null;
        }

        boolean containsRobot(String id){
            for(Enemy e : enemyList) if(e.getName().equals(id)) return true;
            return false;
        }
    }

    class ProbabilityMap{
        ArrayList<Action> possibleActions = new ArrayList<>();
        public ProbabilityMap(){
            possibleActions.add(new AccelerateForwards());
            possibleActions.add(new AccelerateBackwards());
            possibleActions.add(new TurnRight());
            possibleActions.add(new TurnLeft());
        }

        public void updateProbabilities(ScannedRobotEvent newData, Enemy oldData){
            //TODO
            //Calculate disparity between data and apply it to the actions' sortedmap
            //with duration being time since last change of action
        }
    }

    abstract class Action{

        SortedMap<Integer, Double> durationToProbability;
        public double getProbabilityAtDuration(int duration){
            if(!isPossible()) return 0;
            Integer[] keys = durationToProbability.keySet().toArray(new Integer[durationToProbability.size()]);
            for(int i = 0; i < keys.length; i++){
                if(keys[i] > duration){
                    double dY = durationToProbability.get(keys[i]) - durationToProbability.get(keys[i-1]);
                    double dX = keys[i] - keys[i-1];
                    double estimatedProbability = dY/dX * (duration - keys[i-1]);
                    return estimatedProbability;
                }
            }
            return 0;
        }

        abstract boolean isPossible();
    }

    private class AccelerateForwards extends Action{
        @Override
        boolean isPossible() {
            return false;
        }
        //TODO
        //link probability map to generating and applying a move;
    }
    private class AccelerateBackwards extends Action{
        @Override
        boolean isPossible() {
            return false;
        }
        //TODO
        //link probability map to generating and applying a move;
    }
    private class TurnRight extends Action{
        @Override
        boolean isPossible() {
            return true;
        }
        //TODO
        //link probability map to generating and applying a move;
    }
    private class TurnLeft extends Action{
        @Override
        boolean isPossible() {
            return true;
        }
        //TODO
        //link probability map to generating and applying a move;
    }
}
