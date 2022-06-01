package scenes;

import components.SpriteRenderer;
import components.Spritesheet;
import components.StateMachine;
import jade.GameObject;
import util.AssetPool;

public class LevelSceneInitializer extends SceneInitializer {
    public LevelSceneInitializer() {

    }
    @Override
    public void init(Scene scene) {
        Spritesheet backSprites = AssetPool.getSpritesheet("src/main/resources/images/BackObjects.png");
    }

    @Override
    public void loadResources(Scene scene) {
        AssetPool.getShader("src/main/resources/shaders/default.glsl");
        AssetPool.addSpritesheet("src/main/resources/images/spriteSheet1.png",
                new Spritesheet(AssetPool.getTexture("src/main/resources/images/spriteSheet1.png"), 72, 72, 34, 0));

        AssetPool.addSpritesheet("src/main/resources/images/staticObject1.png",
                new Spritesheet(AssetPool.getTexture("src/main/resources/images/staticObject1.png"), 259, 259, 1, 0));

        AssetPool.addSpritesheet("src/main/resources/images/gizmos.png",
                new Spritesheet(AssetPool.getTexture("src/main/resources/images/gizmos.png"), 24, 48, 3, 0));

        AssetPool.addSpritesheet("src/main/resources/images/BackObjects.png",
                new Spritesheet(AssetPool.getTexture("src/main/resources/images/BackObjects.png"), 158, 158, 2, 0));









        AssetPool.addSound("src/main/resources/sounds/vestron-vulture-new-wave-hookers.ogg", true);


        for (GameObject g : scene.getGameObjects()) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }



            if (g.getComponent(StateMachine.class) != null) {
                StateMachine stateMachine = g.getComponent(StateMachine.class);
                stateMachine.refreshTextures();
            }
        }
    }

    @Override
    public void imgui() {

    }

}
