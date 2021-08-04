package com.haptik.ftl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
public class AppController {
    final
    Configuration cfg;

    private final String currentDirectory;
    private final File dataFile;

    @Autowired
    public AppController(Configuration cfg, @Value("${template.path}") String templatePath, @Value("${data.path}") String dataPath) throws IOException {
        this.cfg = cfg;
        this.currentDirectory = System.getProperty("user.dir");
        if (dataPath == null || dataPath.equals("")) {
            dataFile = new File(currentDirectory, "data.json");
        } else {
            dataFile = new File(dataPath);
        }
        if (templatePath == null || templatePath.equals("")) {
            this.cfg.setDirectoryForTemplateLoading(new File(currentDirectory, "templates"));
        } else {
            this.cfg.setDirectoryForTemplateLoading(new File(templatePath));
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    @GetMapping("/ftl/{name}")
    public String ftl(@PathVariable String name) throws IOException, TemplateException, ParseException {
        JSONObject data = (JSONObject) new JSONParser().parse(new FileReader(this.dataFile));
        JSONObject currentFileData = (JSONObject) data.get(name);
        Template t = this.cfg.getTemplate(name);
        Writer sw = new StringWriter();
        t.process(currentFileData, sw);
        return sw.toString();
    }
}
