package com.changenode;

import com.changenode.plugin.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxApplication extends Application implements Log {

    public static JavaFxApplication javaFxApplication;

    public ConfigurableApplicationContext applicationContext;

    static public void quit() {
        if (javaFxApplication != null) {
            javaFxApplication.applicationContext.close();
            javaFxApplication = null;
        }
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void stop() {
        quit();
    }

    public void launchSpringBoot() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        try {
            this.applicationContext = new SpringApplicationBuilder()
                    .sources(SpringApplication.class)
                    .run(args);
        } catch (Exception e) {
            log("Unable to start Spring Boot");
            log(e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * This is the very simple "registry" for the various demonstration features of this application.
     */
    private final Plugin[] plugins = new Plugin[]{new StandardMenus(), new HelloWorld(), new FileDrop(),
            new DesktopIntegration(), new LogFile(), new DarkMode(), new SpringContextExplorer()};

    private TextArea textArea;
    private Label statusLabel;

    private StringBuilder logCache = new StringBuilder();

    public void log(String s) {
        if (textArea == null) {
            logCache.append(s);
            return;
        }

        if (logCache.length() > 0) {
            textArea.appendText(logCache.toString());
            logCache = new StringBuilder();
        }

        textArea.appendText(s);
        textArea.appendText(System.lineSeparator());
        statusLabel.setText(s);
    }

    @Override
    public void start(Stage stage) {
        javaFxApplication = this;

        BorderPane borderPane = new BorderPane();

        VBox topElements = new VBox();

        MenuBar menuBar = new MenuBar();
        topElements.getChildren().add(menuBar);

        ToolBar toolbar = new ToolBar();
        topElements.getChildren().add(toolbar);

        textArea = new TextArea();
        textArea.setWrapText(true);

        statusLabel = new Label();
        statusLabel.setPadding(new Insets(5.0f, 5.0f, 5.0f, 5.0f));
        statusLabel.setMaxWidth(Double.MAX_VALUE);

        borderPane.setTop(topElements);
        borderPane.setBottom(statusLabel);
        borderPane.setCenter(textArea);

        Scene scene = new Scene(borderPane, 800, 600);

        stage.setTitle("Hello World");
        stage.setScene(scene);

        for (Plugin plugin : plugins) {
            try {
                plugin.setup(stage, textArea, toolbar, this, menuBar);
            } catch (Exception e) {
                System.err.println("Unable to start plugin");
                System.err.println(plugin.getClass().getName());
                e.printStackTrace();
                log("Unable to start plugin");
                log(plugin.getClass().getName());
                log(e.getMessage());
            }
        }

        statusLabel.setText("Ready.");

        stage.show();

        launchSpringBoot();
    }
}
