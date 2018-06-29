import plugins.*;

public class HardModePlugin extends Plugin {
	public HardModePlugin(PluginWorld mondo) {
		super(mondo);
	}
	
	@Override
	public void applyPlugin() {
		for(CharacterEntity player : mondo.getPlayerEntities()) {
			player.setHealth(1);
		}

		for(CharacterEntity enemy : mondo.getEnemyEntities())
			enemy.setDamage(20);
	}
}

