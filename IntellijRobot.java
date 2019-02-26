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
            g.setColor(new Color(0,0,255,128));
            g.drawLine((int)e.getX(), (int)e.getY(), (int) getX(), (int) getY());
            g.fillRect((int)e.getX() - 20, (int)e.getY() - 20, 40, 40);
        }

        for(int i = 0; i < enemies.getEnemyList().size(); i++){
            Enemy e = enemies.getEnemyList().get(i);

            int scopeOfSpeedDuration = e.getProbabilities().getAccelerateAction().getScopeOfDuration();
            //out.println("scope of speed: " + scopeOfSpeedDuration);
            int scopeOfTurnDuration = e.getProbabilities().getTurnAction().getScopeOfDuration();
            //out.println("scope of turn: " + scopeOfTurnDuration);
            int sizeDeterminingScope = Integer.max(scopeOfSpeedDuration, scopeOfTurnDuration);
            //out.println("size determining scope: " + sizeDeterminingScope);


            double barScaleFactor = .8;
            double tileWidth = getBattleFieldWidth() * barScaleFactor / sizeDeterminingScope;
            double startingX = getBattleFieldWidth() * (1-barScaleFactor)/2;
            for(int col = 0; col < sizeDeterminingScope; col++){
                double probabilityAtDuration = e.getProbabilities().getAccelerateAction().getProbabilityAtDuration(col);

                //out.println("Probability of change in speed at " + col + " is " + probabilityAtDuration);

                g.setColor(new Color(128 + (int)(100*probabilityAtDuration), 0 ,128 - (int)(100*probabilityAtDuration)));
                g.fillRect((int)(startingX + tileWidth*col), 20, (int)(tileWidth), 20);
                g.setColor(Color.BLACK);
                g.drawRect((int)(startingX + tileWidth*col), 20, (int)(tileWidth), 20);
                g.drawString(Double.toString(Math.floor(probabilityAtDuration*100.0)/100.0), (int)(startingX + tileWidth*col), 25);
            }

            for(int col = 0; col < sizeDeterminingScope; col++){
                double probabilityAtDuration = e.getProbabilities().getTurnAction().getProbabilityAtDuration(col);

                //out.println("Probability of change in turn at " + col + " is " + probabilityAtDuration);

                int green = Math.abs(probabilityAtDuration)<=1 ? 128 + (int)(127*probabilityAtDuration) : 255;
                int blue = Math.abs(probabilityAtDuration)<=1 ? 128 - (int)(127*probabilityAtDuration) : 255;
                g.setColor(new Color(0, green ,blue));
                g.fillRect((int)(startingX + tileWidth*col), 40, (int)(tileWidth), 20);
                g.setColor(Color.BLACK);
                g.drawRect((int)(startingX + tileWidth*col), 40, (int)(tileWidth), 20);
                g.drawString(Double.toString(Math.floor(probabilityAtDuration*100.0)/100.0), (int)(startingX + tileWidth*col), 45);
            }
        }
    }
}
