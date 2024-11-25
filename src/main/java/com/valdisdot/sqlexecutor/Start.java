package com.valdisdot.sqlexecutor;

import com.valdisdot.sqlexecutor.ui.Application;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
/*
* full params example
* "mode"="cli" "configFolder"="path/to/configFolder"
* */
public class Start {
    public static void main(String[] args) {
        Map<String, String> params = new HashMap<>();
        for(String arg : args) {
            String[] param = arg.split("=");
            if(param.length > 1) {
                params.put(
                        param[0].replaceAll("\"", "").trim(),
                        param[1].replaceAll("\"", "").trim()
                );
            }
        }
        Application.Mode mode = "cli".equalsIgnoreCase(params.get("mode")) ? Application.Mode.CLI : Application.Mode.GUI;
        File configFolder = params.containsKey("configFolder") ? new File(params.get("configFolder")) : null;
        new Application().run(mode, configFolder);
    }
}