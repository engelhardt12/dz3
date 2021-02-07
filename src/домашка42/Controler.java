package домашка42;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import server.AuthService;


public class Controler
{
    @FXML
    TextArea textArea;
    @FXML
    TextField textField;
    @FXML
    Button btn1;
    @FXML
    HBox bottomPanel;
    @FXML
    HBox upperPanel;
    @FXML
    HBox registredPanel;
    @FXML
    TextField loginField;
    @FXML
    TextField passwordField;
    @FXML
    TextField regLogin;
    @FXML
    PasswordField regPassword;
    @FXML
    TextField regNick;
    @FXML
    ListView<String> clientList;

    private boolean isAuthorized;
    private void setAuthorized(boolean isAuthorized){
        this.isAuthorized=isAuthorized;
        if(!isAuthorized){
            upperPanel.setVisible(true);
            upperPanel.setManaged(true);
            registredPanel.setVisible(true);
            registredPanel.setManaged(true);
            bottomPanel.setVisible(false);
            bottomPanel.setManaged(false);
            clientList.setVisible(false);
            clientList.setManaged(false);

        }
        else{
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            registredPanel.setVisible(false);
            registredPanel.setManaged(false);
            bottomPanel.setVisible(true);
            bottomPanel.setManaged(true);
            clientList.setVisible(true);
            clientList.setManaged(true);


        }
    }


    private Socket socket;
    private DataInputStream pony;
    private DataOutputStream yu;
    private final String IP_adress = "Lockal";
    private final int Port = 8;
    private void conect () {

        try {
            socket = new Socket(IP_adress,Port);
            pony=new DataInputStream(socket.getInputStream());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String str = pony.readUTF();
                        if (str.startsWith("/authok")) {
                            setAuthorized(true);
                            loadHistory();
                            break;
                        }else{
                            textArea.appendText(str+"");


                        }
                    }
                    while (true) {
                        String str=pony.readUTF();
                        if(str.equals("/serverclosed")){
                            break;

                        }
                        if (str.startsWith("/clientlist")){
                            String [] tokens = str.split("");
                            Platform.runLater (new Runnable(){
                                @Override
                                public void run() {
                                    clientList.getItems().clear();
                                    for (int i = 1; i < tokens.length; i++) {
                                        clientList.getItems().add(tokens[i]);
                                    }
                                }
                            });
                        }else{
                            textArea.appendText(str+"");
                            SaveHistory();
                        }
                    }
                }catch(IOException s){
                    s.printStackTrace;
                }finally{
                    try{
                        socket.close();
                    }catch (IOException c){
                        c.printStackTrace();
                    }
                    setAuthorized(false);
                }

            }).start();

        }catch(IOException n) {
            n.printStackTrace();
        }


    }

    void Dispose (){
        System.out.println("Sending message about end ");
        try{
            if (yu!=null){
                yu.writeUTF("/exit");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sendMsg(){
        try{
            yu.writeUTF(textField.getText());
            textField.clear();
            textField.requestFocus();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void tryToAuth() {
        if (socket == null || socket.isClosed()) {
            connect();
        }
        try {
            int hash = passwordField.getText().hashCode();
            out.writeUTF("/auth " + loginField.getText() + " " + hash);
            loginField.clear();
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registration() {
        if (socket == null || socket.isClosed()) {
            conect();
        }
        AuthService.setNewUsers(regLogin.getText(), regPassword.getText(), regNick.getText());
        System.out.println(" ");
        regLogin.clear();
        regPassword.clear();
        regNick.clear();
    }

    public void selectClient(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            System.out.println("");
        }
    }

    private void SaveHistory() throws IOException {
        try {
            File history = new File("history.txt");
            if (!history.exists()) {
                System.out.println("");
                history.createNewFile();
            }
            PrintWriter fileWriter = new PrintWriter(new FileWriter(history, false));

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(textArea.getText());
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadHistory() throws IOException {
        int posHistory = 100;
        File history = new File("history.txt");
        List<String> historyList = new ArrayList<>();
        FileInputStream in = new FileInputStream(history);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
            historyList.add(temp);
        }

        if (historyList.size() > posHistory) {
            for (int i = historyList.size() - posHistory; i <= (historyList.size() - 1); i++) {
                textArea.appendText(historyList.get(i) + "\n");
            }
        } else {
            for (int i = 0; i < posHistory; i++) {
                System.out.println(historyList.get(i));
            }
        }
    }

}

