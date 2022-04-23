import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class MonsterTargettingTest {

    public static final List<Player.Entity> heroes = List.of(
            new Player.Entity(91, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            new Player.Entity(92, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            new Player.Entity(93, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0));

    @BeforeEach
    void initBase(){
        Player.waitingPoints = List.of(
                new Player.Entity(123, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
                new Player.Entity(456, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
                new Player.Entity(789, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        Player.MY_BASE = new Player.Entity(123456, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    @Test
    void hero_target_his_waiting_point_if_no_targets() {

        Player.waitingPoints = List.of(
                new Player.Entity(123, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
                new Player.Entity(456, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
                new Player.Entity(789, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        List<Player.Entity> monsters = emptyList();

        Player.Entity target = Player.getTarget(monsters, heroes, 1);

        assertThat(target.id).isEqualTo(456);
    }

    @Test
    void dont_target_monsters_targetting_opponent() {

        List<Player.Entity> monsters = List.of(
                new Player.Entity(1, 0, 10000, 10000, 0, 0, 10, 0, 0, 0, 2));

        Player.Entity target = Player.getTarget(monsters, heroes, 0);

        assertThat(target.id).isEqualTo(123);
    }

    @Test
    void prefere_threat_for_base_over_no_threat() {

        List<Player.Entity> monsters = List.of(
                new Player.Entity(1, 0, 10000, 10000, 0, 0, 10, 0, 0, 0, 0),
                new Player.Entity(2, 0, 10000, 10000, 0, 0, 10, 0, 0, 0, 1));

        Player.Entity target = Player.getTarget(monsters, heroes, 0);

        assertThat(target.id).isEqualTo(2);
    }

    @Test
    void prefere_near_base_over_far_away() {

        List<Player.Entity> monsters = List.of(
                new Player.Entity(1, 0, 10000, 10000, 0, 0, 10, 0, 0, 0, 1),
                new Player.Entity(2, 0, 10000, 10000, 0, 0, 10, 0, 0, 1, 1));

        Player.Entity target = Player.getTarget(monsters, heroes, 0);

        assertThat(target.id).isEqualTo(2);
    }

    @Test
    void prefere_nearest_from_base() {

        List<Player.Entity> monsters = List.of(
                new Player.Entity(1, 0, 2000, 2000, 0, 0, 10, 0, 0, 1, 1),
                new Player.Entity(2, 0, 1500, 1500, 0, 0, 10, 0, 0, 1, 1));

        Player.Entity target = Player.getTarget(monsters, heroes, 0);

        assertThat(target.id).isEqualTo(2);
    }

    @Test
    void prefere_nearest_from_base_but_when_close_prefere_nearest_from_hero() {

        List<Player.Entity> heroes = List.of(
                new Player.Entity(91, 1, 1900, 800, 0, 0, 0, 0, 0, 0, 0));

        List<Player.Entity> monsters = List.of(
                new Player.Entity(1, 0, 1000, 2000, 0, 0, 10, 0, 0, 1, 1),
                new Player.Entity(2, 0, 2020, 1020, 0, 0, 10, 0, 0, 1, 1));

        Player.Entity target = Player.getTarget(monsters, heroes, 0);

        assertThat(target.id).isEqualTo(2);
    }

}
