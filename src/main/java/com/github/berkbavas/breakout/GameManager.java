package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.shapes.Ball;
import com.github.berkbavas.breakout.shapes.Brick;
import com.github.berkbavas.breakout.shapes.Paddle;
import com.github.berkbavas.breakout.shapes.base.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class GameManager {
    private static final double GAME_BOARD_WIDTH = 800;
    private static final double GAME_BOARD_HEIGHT = 800;

    private static final double TOP_BAR_WIDTH = GAME_BOARD_WIDTH;
    private static final double TOP_BAR_HEIGHT = 50;

    private static final double PADDLE_WIDTH = 120;
    private static final double PADDLE_HEIGHT = 15;
    private static final double PADDLE_X = (GAME_BOARD_WIDTH - PADDLE_WIDTH) * 0.5;
    private static final double PADDLE_Y = GAME_BOARD_HEIGHT - 100;

    private static final double BRICK_WIDTH = 60;
    private static final double BRICK_HEIGHT = 25;
    private static final double BRICK_GAP_X = 2;
    private static final double BRICK_GAP_Y = 2;

    private static final double BRICKS_TOP_MARGIN = 75;
    private static final double BRICKS_LEFT_MARGIN = (GAME_BOARD_WIDTH - 10 * (BRICK_WIDTH + BRICK_GAP_X)) * 0.5;

    private static final double BALL_DEFAULT_X = GAME_BOARD_WIDTH * 0.5;
    private static final double BALL_DEFAULT_Y = GAME_BOARD_HEIGHT * 0.75;
    private static final double BALL_RADIUS = 8;
    private static final double BALL_SPEED = 5;

    private final Group container = new Group();
    private final Canvas gameBoard = new Canvas(GAME_BOARD_WIDTH, GAME_BOARD_HEIGHT);
    private final Canvas topBar = new Canvas(TOP_BAR_WIDTH, TOP_BAR_HEIGHT);

    private final Paddle paddle = new Paddle(PADDLE_X, PADDLE_Y, PADDLE_WIDTH, PADDLE_HEIGHT, Color.rgb(15, 15, 30));
    private final Ball ball = new Ball(BALL_DEFAULT_X, BALL_DEFAULT_Y, BALL_RADIUS, Color.WHITE);
    private final Brick[][] bricks = new Brick[10][10];
    private final Stopwatch stopwatch = new Stopwatch();
    private GameStatus gameStatus;
    private double dx = 0;
    private double dy = 0;
    private int score = 0;
    private int lives = 3;

    public GameManager() {
        container.getChildren().add(topBar);
        topBar.setLayoutX(0);
        topBar.setLayoutY(0);
        container.getChildren().add(gameBoard);
        gameBoard.setLayoutX(0);
        gameBoard.setLayoutY(topBar.getHeight());

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(96, 245, 145));
        shadow.setSpread(0.5);
        paddle.setRadius(15);
        paddle.setEffect(shadow);

        Color color = Color.rgb(90, 40, 250);

        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                double x = BRICKS_LEFT_MARGIN + (BRICK_WIDTH + BRICK_GAP_X) * j;
                double y = BRICKS_TOP_MARGIN + (BRICK_HEIGHT + BRICK_GAP_Y) * i;
                bricks[i][j] = new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, color.interpolate(Color.rgb(96, 245, 145), i / 10d));
                bricks[i][j].setRadius(5);
                bricks[i][j].setScore(25 * (bricks.length - i));
            }
        }

        resetBall();
        gameStatus = GameStatus.COUNT_DOWN;
        stopwatch.reset();
        paint();
    }

    public void update() {
        switch (gameStatus) {
            case RUNNING:
                ball.setX(ball.getX() + dx);
                ball.setY(ball.getY() + dy);

                Point2D right = ball.right();
                Point2D top = ball.top();
                Point2D left = ball.left();
                Point2D bottom = ball.bottom();

                // Collision test with bricks
                for (int i = 0; i < bricks.length; i++) {
                    for (int j = 0; j < bricks[i].length; j++) {
                        Brick brick = bricks[i][j];

                        if (brick.isHit()) {
                            continue;
                        }

                        if (brick.contains(right)) {
                            dx = -BALL_SPEED;
                            brick.setHit(true);
                            score += brick.getScore();
                        } else if (brick.contains(left)) {
                            dx = BALL_SPEED;
                            brick.setHit(true);
                            score += brick.getScore();
                        } else if (brick.contains(top)) {
                            dy = BALL_SPEED;
                            brick.setHit(true);
                            score += brick.getScore();
                        } else if (brick.contains(bottom)) {
                            dy = -BALL_SPEED;
                            brick.setHit(true);
                            score += brick.getScore();
                        }
                    }

                }

                // Collision test with walls
                if (top.getY() <= 0) {
                    dy = BALL_SPEED;
                } else if (left.getX() <= 0) {
                    dx = BALL_SPEED;
                } else if (right.getX() >= gameBoard.getWidth()) {
                    dx = -BALL_SPEED;
                } else if (bottom.getY() >= gameBoard.getHeight()) {
                    dy = -BALL_SPEED;
                    lives--;
                    resetBall();
                    stopwatch.reset();

                    if (lives < 0) {
                        gameStatus = GameStatus.GAME_OVER;
                    } else {
                        gameStatus = GameStatus.COUNT_DOWN;
                    }

                }

                // Collision test with paddle
                if (paddle.contains(right)) {
                    dx = -BALL_SPEED;
                } else if (paddle.contains(left)) {
                    dx = BALL_SPEED;
                } else if (paddle.contains(top)) {
                    dy = BALL_SPEED;
                } else if (paddle.contains(bottom)) {
                    dy = -BALL_SPEED;
                }

                if (isFinished()) {
                    gameStatus = GameStatus.WIN;
                }

                break;
            case COUNT_DOWN:
                if (stopwatch.elapsed() > 4.0) {
                    gameStatus = GameStatus.RUNNING;
                }
                break;
            case GAME_OVER:
            case WIN:
            case PAUSED:
                break;
        }

        paint();
    }


    public Canvas getGameBoard() {
        return gameBoard;
    }

    public Group getContainer() {
        return container;
    }

    private void paint() {
        clear(gameBoard);
        paintBackground(gameBoard);
        clear(topBar);
        paintBackground(topBar);

        switch (gameStatus) {
            case RUNNING:
                paintTopBar();
                paintBricks();
                paintBall();
                paintPaddle();
                break;
            case PAUSED:
                break;
            case COUNT_DOWN:
                paintTopBar();
                paintBricks();
                paintBall();
                paintPaddle();
                writeCountDown((int) (4 - stopwatch.elapsed()));
                break;
            case GAME_OVER:
                paintTopBar();
                writeText("Game over!");
                break;
            case WIN:
                paintTopBar();
                writeText("You win!");
                break;
        }
    }

    private void resetDeltas() {
        dx = Math.random() < 0.5 ? BALL_SPEED : -BALL_SPEED;
        dy = -BALL_SPEED;
    }

    public void movePaddle(double delta) {
        double x = paddle.getX() + delta * 0.5;
        x = Math.max(5, Math.min(x, GAME_BOARD_WIDTH - paddle.getWidth() - 5));
        paddle.setX(x);
    }

    public void resetBall() {
        resetDeltas();
        ball.setX(BALL_DEFAULT_X);
        ball.setY(BALL_DEFAULT_Y);
    }

    public boolean isFinished() {
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                if (!bricks[i][j].isHit()) {
                    return false;
                }
            }
        }

        return true;
    }

    public void clear(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void paintBackground(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(15, 15, 30));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void paintBricks() {
        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                if (!bricks[i][j].isHit()) {
                    bricks[i][j].paint(gc);
                }
            }
        }
    }

    public void paintPaddle() {
        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        paddle.paint(gc);
    }

    public void paintBall() {
        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        ball.paint(gc);
    }

    public void writeCountDown(int n) {
        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        Font font = Font.font("Black Ops One", FontWeight.BOLD, 120);
        gc.setFont(font);
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(String.valueOf(n), gameBoard.getWidth() / 2, 50 + gameBoard.getHeight() / 2);
    }

    public void writeText(String text) {
        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        Font font = Font.font("Black Ops One", FontWeight.NORMAL, 60);
        gc.setFont(font);
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(text, gameBoard.getWidth() / 2, gameBoard.getHeight() / 2);
    }

    public void paintTopBar() {
        GraphicsContext gc = topBar.getGraphicsContext2D();
        Font font = Font.font("Black Ops One", FontWeight.NORMAL, 22);
        gc.setFont(font);
        gc.setFill(Color.WHITE);

        switch (gameStatus) {
            case RUNNING:
            case PAUSED:
            case COUNT_DOWN:
                gc.setTextBaseline(VPos.CENTER);
                gc.setTextAlign(TextAlignment.LEFT);
                gc.fillText(String.format("Score: %d", score), BRICKS_LEFT_MARGIN, topBar.getHeight() * 0.5);
                gc.setTextAlign(TextAlignment.RIGHT);
                gc.fillText(String.format("Lives: %d / 3", lives), topBar.getWidth() - BRICKS_LEFT_MARGIN, topBar.getHeight() * 0.5);
                break;
            case GAME_OVER:
            case WIN:
                gc.setTextBaseline(VPos.CENTER);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.fillText(String.format("Score is %d", score), topBar.getWidth() * 0.5, topBar.getHeight() * 0.5);
                break;
        }
    }
}
