package task;

import org.testng.TestNG;
import org.testng.reporters.EmailableReporter;

//This is a runner for TestNG, run this to generate the report
public class TestNGRunner {
    public static void main(String[] args) {
        TestNG testng = new TestNG();
        testng.addListener(new EmailableReporter());
        testng.setOutputDirectory("./src/main/resources");
        System.setProperty("org.uncommons.reportng.skip.html", "true");
        System.setProperty("org.uncommons.reportng.skip.xml", "true");
        System.setProperty("org.uncommons.reportng.skip.suite", "true");
        System.setProperty("org.uncommons.reportng.skip.feedback", "true");
        testng.setTestClasses(new Class[] {GmailScenarioTest.class});
        testng.setUseDefaultListeners(false);
        testng.run();
    }
}
