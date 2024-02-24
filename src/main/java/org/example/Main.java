package org.example;

import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //Spark.staticFileLocation("public_html");
        testDataEntity testDataRequest= new testDataEntity();
        Spark.get("/",(request, response) -> {
            HashMap<String, Object> model= new HashMap<>();
            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"index"));
        });

        Spark.get("/chat",(request, response) -> {
            HashMap<String, Object> model= new HashMap<>();
            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"chat-window"));
        });
        Spark.post("/chat",(request, response) -> {
            var choice = request.queryParams("choice");
            HashMap<String, Object> model= new HashMap<>();
            if("testData".equals(choice))
            {
                //return  "<div>Okay i get it, you want help with test data</div>" ;
                return new ThymeleafTemplateEngine().render(new ModelAndView(model,"testdata"));
            } else if ("jira".equals(choice)) {
                return  "<div>Okay i get it, you want help with jira</div>" ;
            } else if (choice.contains("td.scenario")) {
                testDataRequest.setScenarioId( choice.substring(choice.lastIndexOf('.') + 1).trim());
                return new ThymeleafTemplateEngine().render(new ModelAndView(model,"scenario"));
            }else if (choice.contains("td.pmt")){
                testDataRequest.setPaymentType( choice.substring(choice.lastIndexOf('.') + 1).trim());
                return new ThymeleafTemplateEngine().render(new ModelAndView(model,"paymentType"));
            }
            else if (choice.contains("td.count")){
                testDataRequest.setNumnerOfCases(Integer.parseInt( choice.substring(choice.lastIndexOf('.') + 1).trim()));
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(testDataRequest);
                System.out.println(json);
                return "<div>request submitted</div>";
                //execute sql query to process
            }
            return "<div>Okay i get it";
        });
    }
}