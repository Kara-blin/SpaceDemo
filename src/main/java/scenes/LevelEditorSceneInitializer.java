package scenes;

import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import jade.*;
import org.joml.Vector2f;
import physics2d.components.Box2DCollider;
import physics2d.components.Rigidbody2D;
import physics2d.enums.BodyType;
import util.AssetPool;

import java.io.File;
import java.util.Collection;

public class LevelEditorSceneInitializer extends SceneInitializer {

    private Spritesheet backSprites;
    private GameObject levelEditorStuff;

    public LevelEditorSceneInitializer() {

    }

    @Override
    public void init(Scene scene) {
        Spritesheet gizmos = AssetPool.getSpritesheet("src/main/resources/images/gizmos.png");
        backSprites = AssetPool.getSpritesheet("src/main/resources/images/BackObjects.png");

        levelEditorStuff = scene.createGameObject("LevelEditor");
        levelEditorStuff.setNoSerialize();
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new KeyControls());
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new EditorCamera(scene.camera()));
        levelEditorStuff.addComponent(new GizmoSystem(gizmos));
        scene.addGameObjectToScene(levelEditorStuff);
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
        ImGui.begin("Level Editor Stuff");
        levelEditorStuff.imgui();
        ImGui.end();

        ImGui.begin("Test window");

        if (ImGui.beginTabBar("WindowTabBar")) {
            if (ImGui.beginTabItem("Walls")) {

                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;

                Spritesheet spritesheet = AssetPool.getSpritesheet("src/main/resources/images/staticObject1.png");
                Sprite sprite = spritesheet.getSprite(0);
                float spriteWidth = sprite.getWidth() /5;
                float spriteHeight = sprite.getHeight() /5;
                int id = sprite.getTexId();
                Vector2f[] texCoords = sprite.getTexCoords();

                ImGui.pushID(0);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f, 0);
                    Rigidbody2D rb = new Rigidbody2D();
                    rb.setBodyType(BodyType.Static);
                    object.addComponent(rb);
                    Box2DCollider b2d = new Box2DCollider();
                    b2d.setHalfSize(new Vector2f(0.25f, 0.25f));
                    object.addComponent(b2d);
                    object.addComponent(new Ground());
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();

                ImVec2 lastButtonPos = new ImVec2();
                ImGui.getItemRectMax(lastButtonPos);
                float lastButtonX2 = lastButtonPos.x;
                float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                ImGui.endTabItem();
        }

            if (ImGui.beginTabItem("BackGrounds items")) {
                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;
                for (int i = 0; i < 2; i++) {

                    Sprite sprite = backSprites.getSprite(i);
                    float spriteWidth = sprite.getWidth() ;
                    float spriteHeight = sprite.getHeight() ;
                    int id = sprite.getTexId();
                    Vector2f[] texCoords = sprite.getTexCoords();

                    ImGui.pushID(i);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                        GameObject object = Prefabs.generateSpriteObject(sprite, 1f, 1f,0f);
                        object.transform.zIndex = -1;
                        object.addComponent(new CheckWinner());
                        levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                    }
                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);
                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                    if (i + 1 < backSprites.size() && nextButtonX2 < windowX2) {
                        ImGui.sameLine();
                    }
                }

                ImGui.endTabItem();
            }


            if (ImGui.beginTabItem("Prefabs")) {
            int uid = 0;
            Spritesheet playerSprites = AssetPool.getSpritesheet("src/main/resources/images/spriteSheet1.png");
            Sprite sprite = playerSprites.getSprite(0);
            float spriteWidth = sprite.getWidth();
            float spriteHeight = sprite.getHeight();
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(uid++);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                GameObject object = Prefabs.generateShip1();
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();
            ImGui.sameLine();

            sprite = playerSprites.getSprite(19);
            id = sprite.getTexId();
            texCoords = sprite.getTexCoords();
            ImGui.pushID(uid++);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                GameObject object = Prefabs.generateAsteroid1();
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();

            ImGui.endTabItem();
        }


        if (ImGui.beginTabItem("Sounds")) {
            Collection<Sound> sounds = AssetPool.getAllSounds();
            for (Sound sound : sounds) {
                File tmp = new File(sound.getFilepath());
                if (ImGui.button(tmp.getName())) {
                    if (!sound.isPlaying()) {
                        sound.play();
                    } else {
                        sound.stop();
                    }
                }

                if (ImGui.getContentRegionAvailX() > 100) {
                    ImGui.sameLine();
                }
            }

            ImGui.endTabItem();
        }
        ImGui.endTabBar();
    }

        ImGui.end();
    }
}