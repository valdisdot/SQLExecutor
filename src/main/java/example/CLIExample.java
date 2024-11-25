package example;

import com.valdisdot.sqlexecutor.ui.Application;

import java.io.File;

public class CLIExample {
    public static void main(String[] args) {
        new Application().run(Application.Mode.CLI, new File("example/config"));
    }
}
