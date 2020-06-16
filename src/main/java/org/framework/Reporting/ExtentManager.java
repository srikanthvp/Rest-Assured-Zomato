package org.framework.Reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static ExtentReports extent;
    static String filepath = System.getProperty("user.dir")+"/reports/"+ new SimpleDateFormat("yyyy-MM-dd-HH-mm_")
                            .format(new Date())+System.getProperty("Environment")+".html";
//    static String filepath =System.getProperty("user.dir")+"/reports/Result.html";

    static ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(filepath);

    public synchronized static ExtentReports getInstance() {
        if (extent == null) {
            htmlReporter.config().setDocumentTitle("API Automation Report");
            htmlReporter.config().setReportName("Zomato API");
            htmlReporter.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
           extent.attachReporter(htmlReporter);
        }
        return extent;
    }
}
