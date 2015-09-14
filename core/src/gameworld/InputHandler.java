package gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import configuration.Configuration;
import gameobjects.Square;

import static configuration.Settings.NUM_OF_SQUARES;

public class InputHandler implements InputProcessor {

    private GameWorld world;
    private float scaleFactorX;
    private float scaleFactorY;
    int activeTouch = 0;
    private Vector2 touchDown, touchUp;
    private Square touchedSquare;
    private Square[][] squares;
    private int angle;

    public InputHandler(GameWorld world, float scaleFactorX, float scaleFactorY) {
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
        this.world = world;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.R) {
        } else if (keycode == Input.Keys.F) {
        } else if (keycode == Input.Keys.D) {
            if (Configuration.DEBUG) Configuration.DEBUG = false;
            else Configuration.DEBUG = true;
        } else if (keycode == Input.Keys.C) {
            Gdx.app.log("Matches", world.board.check().toString());
        } else if (keycode == Input.Keys.S) {
        } else if (keycode == Input.Keys.L) {
            world.goToGameScreen();
        } else if (keycode == Input.Keys.SPACE) {

        } else if (keycode == Input.Keys.LEFT) {
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.RIGHT) {
        } else if (keycode == Input.Keys.LEFT) {
        }
        return false;
    }


    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        touchDown = new Vector2(screenX, screenY);
        touchedSquare = null;
        squares = world.board.squares;
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES; j++) {
                if (squares[i][j].isTouchDown(screenX, screenY)) {
                    touchedSquare = squares[i][j];
                    //Gdx.app.log("TouchedSquare: ", i + " " + j);
                }
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        activeTouch--;

        touchUp = new Vector2(screenX, screenY);
        angle = angleBetweenTwoPoints(touchDown, touchUp);

        if (touchedSquare != null) {
            if (angle > (360 - 45) || (angle < 45 && angle >= 0)) {
                touchedSquare.slideRight();
            } else if (angle >= 45 && angle < 135) {
                touchedSquare.slideUp();
            } else if (angle <= (360 - 135) && angle >= 135) {
                touchedSquare.slideLeft();
            } else if (angle >= (360 - 135) && angle < (360 - 45)) {
                touchedSquare.slideDown();
            } else {
                //world.setDebutText("No Slide");
            }
        }

        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES; j++) {
                squares[i][j].isTouchUp(screenX, screenY);
            }
        }
        return false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (world.gameHeight - screenY / scaleFactorY);
    }

    public static int angleBetweenTwoPoints(Vector2 one, Vector2 two) {
        float deltaY = one.y - two.y;
        float deltaX = two.x - one.x;
        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
        if (angle < 0) {
            angle = 360 + angle;
        }
        if (new Vector2(deltaX, deltaY).len() < 20) {
            return -1;
        }
        return (int) angle;
    }
}
