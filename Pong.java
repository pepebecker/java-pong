import org.lwjgl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

public class Pong {

	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	private boolean isRunning = true;
	private Ball ball;
	private Bat bat;

	public Pong() {
		setUpDisplay();
		setUpOpenGL();
		setUpEntities();
		setUpTimer();
		start();
		while (isRunning) {
			render();
			logic(getDelta());
			input();
			Display.update();
			Display.sync(60);
			if (Display.isCloseRequested()) {
				isRunning = false;
			}
		}
		Display.destroy();
	}

	private void input() {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			bat.setDY(-.3);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			bat.setDY(0.3);
		} else {
			bat.setDY(0);
		}
	}

	private long lastFrame;

	private long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	private int getDelta() {
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		lastFrame = getTime();
		return delta;
	}
	
	private void start() {
		ball.setX(WIDTH / 2 - 5);
		ball.setY(HEIGHT / 2 - 5);
		ball.setDX(.1);
		ball.setDY(Math.random() * .1f);
	}

	private void logic(int delta) {
		ball.update(delta);
		bat.update(delta);
		if (ball.getX() <= bat.getX() + bat.getWidth() && ball.getX() >= bat.getX() && ball.getY()  >= bat.getY() && ball.getY() <= bat.getY() + bat.getHeight()) {
			ball.setDX(0.3 );
		}
		
		if (ball.getX() + ball.getWidth() >= WIDTH) {
			ball.setDX(-.3);
		}
		
		if (ball.getY() <= 0) {
			ball.setDY(0.3);
		}
		
		if (ball.getY() + ball.getHeight() >= HEIGHT) {
			ball.setDY(-.3);
		}
		
		if (ball.getX() <= 0) {
			start();
		}
		
		if (bat.getY() < 0) {
			bat.setY(0);
		}
		
		if (bat.getY() > HEIGHT - bat.getHeight()) {
			bat.setY(HEIGHT - bat.getHeight());
		}
	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		ball.draw();
		bat.draw();
	}

	private void setUpTimer() {
		lastFrame = getTime();
	}

	private void setUpEntities() {
		bat = new Bat(10, HEIGHT / 2 - 40, 10, 80);
		ball = new Ball(WIDTH / 2 - 5, HEIGHT / 2 - 5, 10, 10);
	}

	private void setUpOpenGL() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	private void setUpDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("Pong");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	private static class Bat extends AbstractMovableEntity {

		public Bat(double x, double y, double width, double height) {
			super(x, y, width, height);
		}

		@Override
		public void draw() {
			glRectd(x, y, x + width, y + height);
		}
		
	}
	
	private static class Ball extends AbstractMovableEntity {

		public Ball(double x, double y, double width, double height) {
			super(x, y, width, height);
		}

		@Override
		public void draw() {
			glRectd(x, y, x + width, y + height);
		}
		
	} 

	public static void main(String[] args) {
		new Pong();
	}

}
