package IntellijRobots;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

import java.util.ArrayList;

class EnemyList {
    private AdvancedRobot player;
    EnemyList(AdvancedRobot player){
        this.player = player;
    }

    private ArrayList<Enemy> enemyList = new ArrayList<>();
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
