package example;

import com.valdisdot.sqlexecutor.ui.Application;

import java.io.File;

public class GUIExample {
    public static void main(String[] args) {
        new Application().run(Application.Mode.GUI, new File("example/config"));
    }
}
