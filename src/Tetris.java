import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Tetris extends Application{

// Variables (변수 지정)
    public static final int MOVE = 25; // 이동
    public static final int SIZE = 25;
    public static int XMAX = SIZE * 12; // 가로
    public static int YMAX = SIZE * 24; // 세로
    public static int [][] MESH = new int [XMAX][YMAX]; // 총 게임판 크기
    private static Pane groupe = new Pane();
    private static Form object;
    private static Scene scene = new Scene(groupe,XMAX+150, YMAX); // 게임하는 곳 실제 크기 (가로로 +150 => 점수판)
    public static int score = 0; // 점수
    public static int top = 0; // top이 0이면 맨 위를 의미
    private static boolean game = true;
    private static Form nextObj = Controller.makeRect();
    private static int linesNo = 0; // 총 블록이 쌓인 수

    //creating scene and start the game
    public void main (String [] args){
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception{
        for(int [] a : MESH){
           Arrays.fill(a,0);
        }

        //Create score ans level area
        Line line = new Line(XMAX,0,XMAX,YMAX);
        Text scoretext = new Text("Score : ");
        scoretext.setStyle("-fx-font: 20 arials");
        scoretext.setY(50);
        scoretext.setX(XMAX + 5);

        Text level = new Text("Lines : ");
        level.setStyle("-fx-font: 20 arials");
        level.setY(100);
        level.setX(XMAX+5);
        level.setFill(Color.GREEN);
        groupe.getChildren().addAll(scoretext,line,level);

        //creating first block and the stage
        Form a = nextObj;
        groupe.getChildren().addAll(a.a,a.b,a.c,a.d);
        moveOnKeyPress(a);
        object = a;
        nextObj = Controller.makeRect();
        stage.setScene(scene);
        stage.setTitle("T E T R I S");
        stage.show();

        //Timer
        Timer fall = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(object.a.getY()==0 || object.b.getY()==0 || object.c.getY()==0 || object.d.getY()==0){
                            top++;
                        }else{
                            top = 0;
                        }

                        if(top == 2){
                            // GAME OVER
                            Text over = new Text("GAME OVER");
                            over.setFill(Color.RED);
                            over.setStyle("-fx-font: 70 arial;");
                            over.setY(250);
                            over.setX(10);
                            groupe.getChildren().add(over);
                            game = false;
                        }

                        if(top == 15){
                            // Exit
                            System.exit(0);
                        }

                        if(game){
                            MoveDown(object);
                            scoretext.setText("Score : "+Integer.toString(score));
                            level.setText("Line : "+Integer.toString(linesNo));
                        }
                    }
                });
            }
        };
        fall.schedule(task,0,300);
    }
    private void moveOnKeyPress(Form form){
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case RIGHT:
                        Controller.MoveRight(form);
                        break;
                    case DOWN:
                        MoveDown(form);
                        score++;
                        break;
                    case LEFT:
                        Controller.MoveLeft(form);
                        break;
                    case UP:
                        MoveTurn(form);
                        break;
                }
            }
        });
    }

    private void MoveTurn(Form form){
        int f = form.form; // 360도 4방향으로 돌린 것, 1~4로 표현함
        Rectangle a = form.a;
        Rectangle b = form.b;
        Rectangle c = form.c;
        Rectangle d = form.d;
        switch (form.getName()){
            case "j":
                if(f==1 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==2 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,2,-2)){
                    MoveDown(form.a);
                    MoveLeft(form.a);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    MoveLeft(form.a);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    MoveUp(form.c);
                    form.changeForm();
                    break;

                }
                if(f==3 && cB(a,-1,-1) && cB(c,-1,-1) && cB(d,2,-2)){
                    MoveLeft(form.a);
                    MoveUp(form.a);
                    MoveUp(form.c);
                    MoveRight(form.c);
                    MoveUp(form.d);
                    MoveUp(form.d);
                    MoveRight(form.d);
                    MoveRight(form.d);
                    form.changeForm();
                    break;
                }
                if(f==4 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,2,-2)){
                    MoveUp(form.a);
                    MoveRight(form.a);
                    MoveRight(form.c);
                    MoveDown(form.c);
                    MoveRight(form.d);
                    MoveRight(form.d);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    form.changeForm();
                    break;
                }
            case "l":
                if(f==1 && cB(a,1,-1) && cB(c,1,1) && cB(b,2,2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveRight(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==2 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==3 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==4 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
            case "o":
                if(f==1 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==2 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==3 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==4 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
            case "s":
                if(f==1 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==2 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==3 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==4 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
            case "t":
                if(f==1 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==2 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==3 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==4 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
            case "z":
                if(f==1 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==2 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==3 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==4 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
            case "i":
                if(f==1 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==2 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==3 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if(f==4 && cB(a,1,-1) && cB(c,-1,-1) && cB(d,-2,-2)){
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
        }
    }

    private boolean cB(Rectangle rect, int x, int y){
        boolean yb = false;
        boolean xb = false;
        if(x >= 0){
            xb = rect.getX() + x*MOVE <= XMAX - SIZE;
        }
        if(x < 0){
            xb = rect.getX() + x*MOVE >= 0;
        }
        if(y >= 0){
            yb = rect.getY() + y*MOVE >= 0;
        }
        if(y < 0){
            yb = rect.getY() + y*MOVE < YMAX;
        }
        return xb && yb && MESH[((int)rect.getX()/SIZE) + x][((int)rect.getY()/SIZE) - y] == 0;
    }


}
