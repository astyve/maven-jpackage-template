package com.changenode;

import com.changenode.plugin.HelloWorldService;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

import static javax.swing.filechooser.FileSystemView.getFileSystemView;

@SpringBootApplication
public class SpringApplication {

    public static File outputFile;

    public static void main(String[] args) {
        try {
            outputFile = File.createTempFile("debug", ".log", getFileSystemView().getDefaultDirectory());
            PrintStream output = new PrintStream(new BufferedOutputStream(new FileOutputStream(outputFile)), true);
            System.setOut(output);
            System.setErr(output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // This is how normal Spring Boot app would be launched
        // SpringApplication.run(SpringBootExampleApplication.class, args);

        // JavaFxApplication doesn't exist yet,
        // we'll create it in the next step
        Application.launch(JavaFxApplication.class, args);
    }
}
