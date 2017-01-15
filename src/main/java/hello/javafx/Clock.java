package hello.javafx;

import java.time.LocalDateTime;
import java.util.TimeZone;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

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
		final Circle circle = new Circle(120, 120, 100);
		circle.setFill(Color.AZURE);
		circle.setStroke(Color.BLACK);

		final Group root = (Group) scene.getRoot();
		root.getChildren().add(circle);
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
		secLine.setStroke(Color.YELLOWGREEN);

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

