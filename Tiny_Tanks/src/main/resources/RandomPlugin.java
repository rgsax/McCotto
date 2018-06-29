import plugins.*;
import java.util.Random;

public class RandomPlugin extends Plugin  {
    public RandomPlugin(PluginWorld mondo) {
        super(mondo);
    }

    @Override
    public void applyPlugin() {
        Random r = new Random();

        for(CharacterEntity player : mondo.getPlayerEntities()) {
            player.setHealth(r.nextInt(101) + 50);
            player.setDamage(r.nextInt(20) + 1);
            player.setBUllets(r.nextInt(8) + 3);
        }

        for(CharacterEntity enemy : mondo.getEnemyEntities()) {
            enemy.setHealth(r.nextInt(101) + 50);
            enemy.setDamage(r.nextInt(20) + 1);
            enemy.setBUllets(r.nextInt(8) + 3);
        }
    }
}