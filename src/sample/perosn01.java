package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class perosn01 extends Application {
    public static final TextArea showMessage = new TextArea();
    public static final TextField inputMsg = new TextField();
    public static final Button sendBtn = new Button();
    private static final Label Name = new Label();
    private static final Circle image=new Circle();
    private static Socket socket = new Socket();
    private static Font font = new Font("Roboto", 15);
    private static BufferedReader br;
    private static PrintWriter out;


    public static void main(String[] args) throws IOException {
        networkConnection();
        launch(args);


    }


    private static void networkConnection() throws IOException {

        ServerSocket serverSocket = new ServerSocket(7777);
        System.out.println("Ready to Accepting Connection");
        System.out.println("Waiting...");
        socket = serverSocket.accept();

        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());


        handleEvents();
        startReading();
       // startWriting();


    }

    private static void handleEvents() {

        inputMsg.setOnAction(event -> {
            String msg = inputMsg.getText();
            showMessage.appendText("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + msg + "\n");
            out.println(msg);
            out.flush();
            inputMsg.setText("");
            inputMsg.requestFocus();
        });

        sendBtn.setOnAction(event -> {
            String msg = inputMsg.getText();
            showMessage.appendText("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + msg + "\n");
            out.println(msg);
            out.flush();
            inputMsg.setText("");
            inputMsg.requestFocus();
        });
    }

    private static void startReading() {

        Runnable r1 = () -> {
            System.out.println("reader Started..");
            try {
                while (!socket.isClosed()) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {

                        socket.close();
                        break;
                    }
                    showMessage.appendText("\t"+msg + "\n");


                }
            } catch (IOException e) {
                System.out.println("Socket closed");
            }
        };
        new Thread(r1).start();
    }
/*
    private static void startWriting() {

        Runnable r2=()->{
            System.out.println("Writer Started..");
            try{

                while (!socket.isClosed()){

                   // BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    //String content=br.readLine();
                    //out.println(content);
                    //out.flush();

                    //if (content.equals("exit")){
                     //   socket.close();
                     //   break;
                    //}
                }
            }catch (Exception e){

            }
        };
        new Thread(r2).start();

    }
*/

    Parent create(){
        HBox topBar=new HBox(10,image,Name);
        topBar.setPrefHeight(50);
        topBar.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        topBar.getStyleClass().add("bg");
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.paddingProperty().set(new Insets(5,10,5,20));


        image.setSmooth(true);
        image.setFill(Color.WHITE);
        image.setRadius(26.6);

        showMessage.setPrefHeight(490);
        showMessage.setFont(font);
        showMessage.setEditable(false);


        Name.setText("Tareq Adnan");
        Name.setFont(Font.font("OPEN SANS",FontWeight.BOLD,20));
        Name.setTextFill(Paint.valueOf("WHITE"));
        Name.alignmentProperty().set(Pos.CENTER_LEFT);




        HBox bottomBar=new HBox(20,inputMsg,sendBtn);
        bottomBar.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        bottomBar.getStyleClass().add("bg");
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setPrefHeight(55);

        inputMsg.setPrefHeight(40);
        inputMsg.setPrefWidth(520);
        inputMsg.getStyleClass().add("textfield");

        sendBtn.setText("SEND");
        sendBtn.setFont(font);
        sendBtn.setTextFill(Paint.valueOf("WHITE"));
        sendBtn.setPrefHeight(40);
        sendBtn.setPrefWidth(160);
        sendBtn.getStyleClass().add("btn");

        VBox root=new VBox(0,topBar,showMessage,bottomBar);

        root.setPrefSize(700,600);
        return root;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(create()));
        primaryStage.setTitle("Messenger");
        primaryStage.show();

    }
}
