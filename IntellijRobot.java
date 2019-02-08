package IntellijRobots;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;

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
            g.setColor(new Color(255,0,0,128));
            double[][] projections = e.getProjectionsAtTime(8);
            for(int i = 0; i < projections.length; i++){
                g.fillRect((int)projections[i][0] - 5, (int)projections[i][1] - 5, 10, 10);
                //g.drawLine((int)projections[i][0], (int)projections[i][1],(int)projections[(i+1)%projections.length][0],(int)projections[(i+1)%projections.length][1]);
            }
            g.setColor(new Color(0,0,255,128));
            g.drawLine((int)e.getX(), (int)e.getY(), (int) getX(), (int) getY());
            g.fillRect((int)e.getX() - 20, (int)e.getY() - 20, 40, 40);
        }
    }
}
