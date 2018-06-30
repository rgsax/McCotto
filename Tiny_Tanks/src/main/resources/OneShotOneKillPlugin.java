//Compila con javac -cp "./plugins.jar" RandomPlugin.java

import plugins.*;

public class OneShotOneKillPlugin extends Plugin {
	public OneShotOneKillPlugin(PluginWorld mondo) {
		super(mondo);
	}
	
	@Override
	public void applyPlugin() {
		for(CharacterEntity player : mondo.getPlayerEntities()) {
			player.setHealth(100);
			player.setDamage(1);
			player.setBUllets(1);
		}

		for(CharacterEntity enemy : mondo.getEnemyEntities()) {
			enemy.setHealth(1);
			enemy.setDamage(1);
			enemy.setBUllets(1);
		}
	}
}

