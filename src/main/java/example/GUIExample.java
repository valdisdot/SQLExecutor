package example;

import com.valdisdot.sqlexecutor.ui.Application;

import java.io.File;
import java.util.Locale;

public class GUIExample {
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new Application().run(Application.Mode.GUI, new File("example/config"));
    }
}
