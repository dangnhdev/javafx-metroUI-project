package org.controller;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.animations.FadeInLeftTransition;
import org.animations.FadeInRightTransition;
import org.controlsfx.control.Notifications;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.math.BigInteger;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author 404NotFound
 */
public class ForgotPasswordController implements Initializable {

    @FXML
    private AnchorPane paneData;
    @FXML
    private TextField tfEmail;
    @FXML
    private Button btSendCode;
    @FXML
    private TextField tfCode;
    @FXML
    private Button btOk;
    @FXML
    private Button btCancel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label lbProgress;
    @FXML
    private PasswordField tfPassword1;
    @FXML
    private PasswordField tfPassword2;
    private Properties mailServerProperties;
    private Session getMailSession;
    private MimeMessage generateMailMessage;
    private String code;
    private SecureRandom random = new SecureRandom();
    private String mail;
    private Service<Void> mailService;
    @FXML
    private AnchorPane paneHeader;
    @FXML
    private AnchorPane paneBody;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            new FadeInLeftTransition(paneHeader).play();
            new FadeInRightTransition(paneBody).play();
        });

        code = new BigInteger(130, random).toString(32);
        mailService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        updateMessage("Setting up server...");
                        updateProgress(0, 100);
                        mailServerProperties = System.getProperties();
                        mailServerProperties.put("mail.smtp.port", "587");
                        updateProgress(10, 100);
                        mailServerProperties.put("mail.smtp.auth", "true");
                        updateProgress(20, 100);
                        mailServerProperties.put("mail.smtp.starttls.enable", "true");
                        updateProgress(30, 100);

                        updateMessage("Get Mail Session...");
                        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
                        updateProgress(40, 100);
                        generateMailMessage = new MimeMessage(getMailSession);
                        updateProgress(50, 100);
                        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
                        generateMailMessage.setSubject("Reset password");
                        String emailBody = "Code của bạn là: " + code + "<br><br> Regards, <br>Nguyen Hai Dang";
                        generateMailMessage.setContent(emailBody, "text/html");

                        updateMessage("Sending code...");
                        Transport transport = getMailSession.getTransport("smtp");
                        updateProgress(60, 100);
                        transport.connect("smtp.gmail.com", "saophaicomailnhi", "dangxunb@1234");
                        updateProgress(80, 100);
                        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
                        Platform.runLater(() -> {
                            Notifications.create().title("")
                                    .text("Code has been sent to your email")
                                    .showInformation();
                        });

                        transport.close();
                        updateProgress(100, 100);
                        updateMessage("Code sent.");
                        return null;
                    }
                };
            }
        };
        setProgress();
    }

    @FXML
    private void actionSendCode(ActionEvent event) {
        if (tfEmail.getText().equals("")) {
            Notifications.create().title("Error").text("Please Enter your email").showError();
        } else {
            mail = tfEmail.getText();
            mailService.start();
        }
    }

    @FXML
    private void actionOk(ActionEvent event) {
        if (tfCode.getText().equals("")) {
            Notifications.create().title("Error").text("Please Enter your code").showError();
        } else if (tfPassword1.getText().equals("") || tfPassword2.getText().equals("")) {
            Notifications.create().title("Error").text("All TextField must be filled").showError();
        } else if (!tfCode.getText().equals(code)) {
            Notifications.create().title("Error").text("Your code doesn't match").showError();
        } else {
            Notifications.create().title("").text("Password Changed").showInformation();
            actionCancel(event);
        }
    }

    @FXML
    private void actionCancel(ActionEvent event) {
        ((Stage) lbProgress.getScene().getWindow()).close();
    }

    private void setProgress() {
        lbProgress.textProperty().bind(mailService.messageProperty());
        btSendCode.disableProperty().bind(mailService.runningProperty());
        btOk.disableProperty().bind(mailService.runningProperty());
        btCancel.disableProperty().bind(mailService.runningProperty());
        progressBar.progressProperty().bind(mailService.progressProperty());
    }
}
