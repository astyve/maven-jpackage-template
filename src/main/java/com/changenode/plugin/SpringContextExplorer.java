package com.changenode.plugin;

import com.changenode.JavaFxApplication;
import com.changenode.Log;
import com.changenode.Plugin;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.Iterator;

public class SpringContextExplorer implements Plugin {
    private Log log;

    @Override
    public void setup(Stage stage, TextArea textArea, ToolBar toolBar, Log log, MenuBar menuBar) {

        Button springButton = new Button();
        springButton.setText("Spring");
        springButton.setFocusTraversable(false);
        springButton.setOnAction(e -> echoContext());
        toolBar.getItems().add(springButton);

        this.log = log;
    }

    private void echoContext() {

        log.log("Spring Bean names:");
        Iterator<String> beanNamesIterator = JavaFxApplication.javaFxApplication
                .applicationContext.getBeanFactory().getBeanNamesIterator();

        while (beanNamesIterator.hasNext()) {
            log.log(beanNamesIterator.next());
        }

        log.log("Spring Bean Autowire Capable Beans:");
        AutowireCapableBeanFactory autowireCapableBeanFactory = JavaFxApplication.javaFxApplication.
                applicationContext.getAutowireCapableBeanFactory();
        if (autowireCapableBeanFactory instanceof SingletonBeanRegistry) {
            String[] singletonNames = ((SingletonBeanRegistry) autowireCapableBeanFactory).getSingletonNames();
            for (String singleton : singletonNames) {
                log.log(singleton);
            }
        }

        Object helloWorld = JavaFxApplication.javaFxApplication.applicationContext.getBean("helloWorldService");

        if (helloWorld == null) {
            log.log("Unable to find helloWorldService in context");
            return;
        }

        HelloWorldService helloWorldService = (HelloWorldService) helloWorld;
        log.log(helloWorldService.now());
    }
}
