package hello.javafx;

import java.time.LocalDateTime;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * A JavaFX-based Clock
 * 
 * 
 * @author pango
 *
 */
public class Clock extends Application {
	private int tick = 0;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("Clock");
		Group root = new Group();
		Scene scene = new Scene(root, 240, 240, Color.WHITE);

		stage.setScene(scene);
		setInitialTime();

		addCircle(scene);
		addHands(scene);

		stage.show();
	}

	private void movePivot(Node node, double x, double y) {
		node.getTransforms().add(new Translate(-x, -y));
		node.setTranslateX(x);
		node.setTranslateY(y);
	}

	private void addCircle(final Scene scene) {
		final Circle circle = new Circle(120, 120, 118);
		circle.setFill(Color.SLATEGRAY);
		circle.setStroke(Color.BLACK);

		final Circle innerCircle = new Circle(120, 120, 100);
		innerCircle.setFill(Color.BEIGE);

		final Group root = (Group) scene.getRoot();
		root.getChildren().add(circle);
		root.getChildren().add(innerCircle);

		addIndicators(root);
	}

	private void addIndicators(final Group root){
		for(int i=0; i<60; i++){
			final Line line;
			int len = 10;
			int wid = 1;

			if(i%5 == 0){
				len = 15;
				wid = 3;
			}

			line= new Line(120, 20+len, 120, 20);
			line.setStroke(Color.LIGHTSLATEGRAY);
			line.setStrokeWidth(wid);

			movePivot(line, 0, 95);
			line.setRotate(i*6);

			root.getChildren().add(line);
		}
	}

	private void addHands(final Scene scene) {
		// Hands
		final Line hrLine = new Line(120, 120, 120, 60);
		final Line minLine = new Line(120, 120, 120, 40);
		final Line secLine = new Line(120, 120, 120, 40);
		hrLine.setStrokeWidth(5);
		minLine.setStrokeWidth(3);
		secLine.setStrokeWidth(1);
		hrLine.setStroke(Color.GREEN);
		minLine.setStroke(Color.BLUE);
		secLine.setStroke(Color.BROWN);

		movePivot(hrLine, 0, 30);
		movePivot(minLine, 0, 40);
		movePivot(secLine, 0, 40);

		rotateHands(hrLine, minLine, secLine, tick);

		final Group root = (Group) scene.getRoot();
		root.getChildren().add(hrLine);
		root.getChildren().add(minLine);
		root.getChildren().add(secLine);

		Timeline tl = new Timeline();
		tl.setCycleCount(Animation.INDEFINITE);
		KeyFrame moveHands = new KeyFrame(Duration.seconds(1.0), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				tick = (tick + 1) % (60*60*24);
				rotateHands(hrLine, minLine, secLine, tick);
			}
		});
		tl.getKeyFrames().add(moveHands);
		tl.play();
	}

	private void rotateHands(Line hr, Line min, Line sec, int tick){
		sec.setRotate((tick%60)*6);
		min.setRotate(((tick/60)%60)*6);
		hr.setRotate(((tick/60/60)%60)*6);
	}

	private void setInitialTime(){
		LocalDateTime dt = LocalDateTime.now();
		tick = ( dt.getHour()*60 + dt.getMinute() )*60 + dt.getSecond();
	}
}

