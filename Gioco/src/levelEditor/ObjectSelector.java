package levelEditor;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class ObjectSelector {
	Image[] items;
	int currentIndex = -1;
	public ObjectSelector(Image[] items) {
		this.items = items;
		currentIndex = 0;
	}
	
	public Image nextItem() {
		currentIndex = (currentIndex + 1) % items.length;
		return items[currentIndex];
	}
	
	public Image previousItem() {
		currentIndex = currentIndex - 1 + (currentIndex > 0 ? 0 : items.length);
		return items[currentIndex];
	}
}
