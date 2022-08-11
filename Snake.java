package application;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Snake extends Application {
	//declare variable
	static int Size = 10;
	static int Speed = 3;
	static int FoodColor = 0;
	static int Width = 20;
	static int Height = 20;
	static int CoordinateX = 0;
	static int CoordinateY = 0;
	static int BodyX = 45;
	static int BodyY= 35;
	static List<Body> snake = new ArrayList<>();
	static Dir direction = Dir.right;
	static boolean GameOver = false;
	static Random rand = new Random();

	//Declare enumeration for the directions
	public enum Dir {
		left, right, up, down
	}

	//Declare new class Body
 static class Body {
		int x;
		int y;

		public Body(int x, int y) {
			this.x = x;
			this.y = y;
		}
		

	}
	
		
	public void start(Stage primaryStage) {
		try {
			food();

			VBox root = new VBox();
			Canvas c = new Canvas(Width * BodyX, Height * BodyY);
			GraphicsContext gc = c.getGraphicsContext2D();
			root.getChildren().add(c);

			new AnimationTimer() {
				long lastTick = 0;

				public void handle(long now) {
					if (lastTick == 0) {
						lastTick = now;
						game(gc);
						return;
					}

					if (now - lastTick > 1000000000 / Speed) {
						lastTick = now;
						game(gc);
					}
				}

			}.start();

			Scene scene = new Scene(root, Width * BodyX, Height * BodyY);

			// control
			scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
				if (key.getCode() == KeyCode.W) {
					direction = Dir.up;
				}
				if (key.getCode() == KeyCode.A) {
					direction = Dir.left;
				}
				if (key.getCode() == KeyCode.S) {
					direction = Dir.down;
				}
				if (key.getCode() == KeyCode.D) {
					direction = Dir.right;
				}

			});

			
			snake.add(new Body(3, 3));
			snake.add(new Body(3, 3));
			primaryStage.setScene(scene);
			primaryStage.setTitle("Snake");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// game
	public static void game(GraphicsContext gc) {
		if (GameOver) {
			gc.setFill(Color.RED);
			gc.setFont(new Font("", 55));
			gc.fillText("GAME OVER", 300, 150);
			gc.setFill(Color.BLACK);
			gc.setFont(new Font("Verdan", 25));
			gc.fillText("Total Score: " + (Speed - 4), 355, 270);
			gc.setFill(Color.RED);
			gc.setFont(new Font("", 50));
			gc.fillText("Try Again", 339, 390);
			
			
			return;
		}

		//Get the location for every part of the snake
		for (int i = snake.size() - 1; i >= 1; i--) {
			snake.get(i).x = snake.get(i - 1).x;
			snake.get(i).y = snake.get(i - 1).y;
		}

		
		switch (direction) {
		case up:
			snake.get(0).y--;
			if (snake.get(0).y < 0) {
				GameOver = true;
			}
			break;
		case down:
			snake.get(0).y++;
			if (snake.get(0).y > Height) {
				GameOver = true;
			}
			break;
		case left:
			snake.get(0).x--;
			if (snake.get(0).x < 0) {
				GameOver = true;
			}
			break;
		case right:
			snake.get(0).x++;
			if (snake.get(0).x > Width) {
				GameOver = true;
			}
			break;

		}

		if (CoordinateX == snake.get(0).x && CoordinateY == snake.get(0).y) {
			snake.add(new Body(-1, -1));
			food();
		}

		for (int i = 1; i < snake.size(); i++) {
			if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
				GameOver = true;
			}
		}

		
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, Width * BodyX, Height * BodyY);
		
		for (int i=1; i<21; i++) {
			gc.setLineWidth(1);
                gc.strokeLine(45*i, 50, 45*i, 650);
		}
		for (int j=1; j<15; j++) {
			gc.setLineWidth(1);
                gc.strokeLine(0, 47*j, 900, 47*j);
		}
            	

		gc.setFill(Color.BLACK);
		gc.setFont(new Font("", 30));
		gc.fillText("Total Score: " + (Speed - 4), 700, 675);
		
		gc.setFill(Color.BLACK);
		gc.setFont(new Font("", 25));
		gc.fillText("Keys:   A-Right    W-Up     S-Down     D-Left", 10, 30);

		Color cc = Color.WHITE;

		switch (FoodColor) {
		case 0:
			cc = Color.PURPLE;
			break;
		case 1:
			cc = Color.RED;
			break;
		case 2:
			cc = Color.YELLOW;
			break;
		case 3:
			cc = Color.BLUE;
			break;
		case 4:
			cc = Color.ORANGE;
			break;
		}
		gc.setFill(cc);
		gc.fillOval(CoordinateX * BodyX, CoordinateY * BodyY, BodyX, BodyY);
		
		for (Body c : snake) {
			gc.setFill(Color.GREEN);
			gc.fillOval(c.x * BodyX, c.y * BodyY, BodyX - 1, BodyY - 1);

		}

	}

	public static void food() {
		start: while (true) {
			CoordinateX = rand.nextInt(Width);
			CoordinateY = rand.nextInt(Height);

			for (Body c : snake) {
				if (c.x == CoordinateX && c.y == CoordinateY) {
					continue start;
				}
			}
			FoodColor = rand.nextInt(5);
			Speed++;
			break;

		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
// A class representing shapes that can be displayed
  class Shape {
    
Color color = Color.WHITE;  // Color of this shape.

void setColor(Color color) {
        // Set the color of this shape
    this.color = color;
}
}
 
 // This class represents oval shapes
  class Oval extends Shape {
 void draw(GraphicsContext g) {
     g.setFill(color);
     g.setStroke(Color.BLACK);
 }
 }
