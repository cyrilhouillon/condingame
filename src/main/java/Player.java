import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class Player {

    static final List<Entity> waitingPointsPlayer1 = new ArrayList<Entity>();
    static final List<Entity> waitingPointsPlayer2 = new ArrayList<Entity>();

    static {
        waitingPointsPlayer1.add(new Player.Entity(123, 0, 5200, 700, 0, 0, 0, 0, 0, 0, 0));
        waitingPointsPlayer1.add(new Player.Entity(456, 0, 4500, 3200, 0, 0, 0, 0, 0, 0, 0));
        waitingPointsPlayer1.add(new Player.Entity(789, 0, 2200, 4900, 0, 0, 0, 0, 0, 0, 0));

        waitingPointsPlayer2.add(new Player.Entity(123, 0, 12000, 7600, 0, 0, 0, 0, 0, 0, 0));
        waitingPointsPlayer2.add(new Player.Entity(456, 0, 13500, 5000, 0, 0, 0, 0, 0, 0, 0));
        waitingPointsPlayer2.add(new Player.Entity(789, 0, 15500, 3500, 0, 0, 0, 0, 0, 0, 0));
    }

    static final int TYPE_MONSTER = 0;
    static final int TYPE_MY_HERO = 1;
    static final int TYPE_OP_HERO = 2;
    static List<Entity> waitingPoints;
    static Entity MY_BASE;

    static class Entity {
        int id;
        int type;
        int x, y;
        int shieldLife;
        int isControlled;
        int health;
        int vx, vy;
        int nearBase;
        int threatFor;

        Entity(int id, int type, int x, int y, int shieldLife, int isControlled, int health, int vx, int vy, int nearBase, int threatFor) {
            this.id = id;
            this.type = type;
            this.x = x;
            this.y = y;
            this.shieldLife = shieldLife;
            this.isControlled = isControlled;
            this.health = health;
            this.vx = vx;
            this.vy = vy;
            this.nearBase = nearBase;
            this.threatFor = threatFor;
        }

    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        // base_x,base_y: The corner of the map representing your base
        int baseX = in.nextInt();
        int baseY = in.nextInt();

        MY_BASE = new Entity(0, 0, baseX, baseY, 0, 0, 0, 0, 0, 0, 0);

        if (baseX == 0) {
            waitingPoints = waitingPointsPlayer1;
        } else {
            waitingPoints = waitingPointsPlayer2;
        }
        // heroesPerPlayer: Always 3
        int heroesPerPlayer = in.nextInt();


        // game loop
        while (true) {
            int myHealth = in.nextInt(); // Your base health
            int myMana = in.nextInt(); // Ignore in the first league; Spend ten mana to cast a spell
            int oppHealth = in.nextInt();
            int oppMana = in.nextInt();
            int entityCount = in.nextInt(); // Amount of heros and monsters you can see

            List<Entity> myHeroes = new ArrayList<>(entityCount);
            List<Entity> oppHeroes = new ArrayList<>(entityCount);
            List<Entity> monsters = new ArrayList<>(entityCount);
            for (int i = 0; i < entityCount; i++) {
                int id = in.nextInt();              // Unique identifier      
                int type = in.nextInt();            // 0=monster, 1=your hero, 2=opponent hero        
                int x = in.nextInt();               // Position of this entity       
                int y = in.nextInt();
                int shieldLife = in.nextInt();      // Ignore for this league; Count down until shield spell fades      
                int isControlled = in.nextInt();    // Ignore for this league; Equals 1 when this entity is under a control spell        
                int health = in.nextInt();          // Remaining health of this monster      
                int vx = in.nextInt();              // Trajectory of this monster      
                int vy = in.nextInt();
                int nearBase = in.nextInt();        // 0=monster with no target yet, 1=monster targeting a base        
                int threatFor = in.nextInt();       // Given this monster's trajectory, is it a threat to 1=your base, 2=your opponent's base, 0=neither       

                Entity entity = new Entity(
                        id, type, x, y, shieldLife, isControlled, health, vx, vy, nearBase, threatFor
                );
                switch (type) {
                    case TYPE_MONSTER:
                        monsters.add(entity);
                        break;
                    case TYPE_MY_HERO:
                        myHeroes.add(entity);
                        break;
                    case TYPE_OP_HERO:
                        oppHeroes.add(entity);
                        break;
                }
            }

            moveToBestTarget(monsters, 0);
            moveToBestTarget(monsters, 1);
            pushMonstersToOpponent(monsters, 2);
        }
    }

    private static void pushMonstersToOpponent(List<Entity> monsters, int indexHero) {
        moveToBestTarget(monsters, indexHero);
    }

    private static void moveToBestTarget(List<Entity> monsters, int indexHero) {
        Entity target = getTarget(monsters, indexHero);

        if (target != null) {
            System.out.println(String.format("MOVE %d %d", target.x + 2 * target.vx, target.y + 2 * target.vy));
        } else {
            System.out.println("WAIT");
        }
    }

    public static Entity getTarget(List<Entity> monsters, int indexHero) {
        return monsters.stream()
                .filter(e -> e.threatFor != 2)
                .sorted(comparator()).findFirst()
                .orElse(waitingPoints.get(indexHero));
    }

    public static Comparator<? super Entity> comparator() {
        Comparator<Entity> threatComparator = Comparator.comparing(e -> e.threatFor, Comparator.reverseOrder());
        Comparator<Entity> nearBaseComparator = Comparator.comparing(e -> e.nearBase, Comparator.reverseOrder());
        Comparator<Entity> nearestComparator = Comparator.comparing(e -> squaredDistanceFromBase(e), Comparator.naturalOrder());
        return threatComparator.thenComparing(nearBaseComparator).thenComparing(nearestComparator);
    }

    public static int squaredDistanceFromBase(Entity entity) {
        int x_dist = entity.x - MY_BASE.x;
        int y_dist = entity.y - MY_BASE.y;
        return (x_dist * x_dist) + (y_dist * y_dist);
    }
}