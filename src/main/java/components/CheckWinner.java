package components;

import engine.GameObject;
import engine.Window;
import scenes.LevelSceneInitializer;

public class CheckWinner extends Component {
    transient public int playerCheck;
    transient public int asteroidCheck;
    @Override
    public void update(float dt) {
        playerCheck=0;
        asteroidCheck = 0;
        for (GameObject g : Window.getScene().getGameObjects()) {
            if (g.getComponent(PlayerController.class) != null) {
                playerCheck++;
            }
            if(g.getComponent(Asteroid.class) != null){
                asteroidCheck++;
            }
        }
        if(playerCheck==0){
            Window.changeScene(new LevelSceneInitializer());
        }
        if(asteroidCheck==0){
            Window.changeScene(new LevelSceneInitializer());
        }
    }
}
