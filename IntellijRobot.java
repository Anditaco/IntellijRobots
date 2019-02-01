package IntellijRobots;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;
import java.util.ArrayList;

public class IntellijRobot extends AdvancedRobot {

    public EnemyList enemies = new EnemyList(this);

    public void run() {

        Color radar = new Color(255, 198, 69);
        Color gun = new Color(255, 169, 43);
        Color body = new Color(136, 136, 136);

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

        String name;
        double x, y;
        double heading;
        double speed;
        double energy;
        double distance;
        double bearing;
        double angle;



        public Enemy(ScannedRobotEvent e, AdvancedRobot player){
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

        public String getName(){return name;}

        public double getX(){return x;}
        public double getY(){return y;}
        public double getDistance(){return distance;}
        public double getBearing(){return bearing;}
    }

    class EnemyList{
        AdvancedRobot player;

        public EnemyList(AdvancedRobot player){
            this.player = player;
        }

        ArrayList<Enemy> enemyList = new ArrayList<>();

        public ArrayList<Enemy> getEnemyList(){return enemyList;}

        public void add(ScannedRobotEvent e){
            enemyList.add(new Enemy(e, player));
        }

        public void remove(RobotDeathEvent d){
            enemyList.remove(getRobotByName(d.getName()));
        }

        public Enemy get(ScannedRobotEvent s){
            return getRobotByName(s.getName());
        }

        private Enemy getRobotByName(String n){
            int i = 0;
            for(Enemy e : enemyList)
                if(enemyList.get(i++).getName().equals(n)) return e;
            return null;
        }

        public boolean containsRobot(String id){
            for(Enemy e : enemyList) if(e.getName().equals(id)) return true;
            return false;
        }
    }

}
