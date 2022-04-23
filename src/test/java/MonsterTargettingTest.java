import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class MonsterTargettingTest {

    @Test
    void target_base_if_no_targets() {

        Player.MY_BASE = new Player.Entity(123456, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        List<Player.Entity> monsters = emptyList();

        Player.Entity target = Player.getTarget(monsters);

        assertThat(target.id).isEqualTo(123456);
    }

    @Test
    void dont_target_monsters_targetting_opponent() {

        Player.MY_BASE = new Player.Entity(123456, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        List<Player.Entity> monsters = List.of(
                new Player.Entity(1, 0, 10000, 10000, 0, 0, 10, 0, 0, 0, 2));

        Player.Entity target = Player.getTarget(monsters);

        assertThat(target.id).isEqualTo(123456);
    }

    @Test
    void prefere_threat_for_base_over_no_threat() {

        List<Player.Entity> monsters = List.of(
                new Player.Entity(1, 0, 10000, 10000, 0, 0, 10, 0, 0, 0, 0),
                new Player.Entity(2, 0, 10000, 10000, 0, 0, 10, 0, 0, 0, 1));

        Player.Entity target = Player.getTarget(monsters);

        assertThat(target.id).isEqualTo(2);
    }

    @Test
    void prefere_near_base_over_far_away() {

        List<Player.Entity> monsters = List.of(
                new Player.Entity(1, 0, 10000, 10000, 0, 0, 10, 0, 0, 0, 1),
                new Player.Entity(2, 0, 10000, 10000, 0, 0, 10, 0, 0, 1, 1));

        Player.Entity target = Player.getTarget(monsters);

        assertThat(target.id).isEqualTo(2);
    }

    @Test
    void prefere_nearest() {
        Player.MY_BASE = new Player.Entity(123456, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        List<Player.Entity> monsters = List.of(
                new Player.Entity(1, 0, 2000, 2000, 0, 0, 10, 0, 0, 1, 1),
                new Player.Entity(2, 0, 1500, 1500, 0, 0, 10, 0, 0, 1, 1));

        Player.Entity target = Player.getTarget(monsters);

        assertThat(target.id).isEqualTo(2);
    }

}
