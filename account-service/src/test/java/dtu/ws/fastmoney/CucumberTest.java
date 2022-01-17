package dtu.ws.fastmoney;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(plugin="summary"
        , publish= false
        , features = "src/test/java/dtu/ws/fastmoney/payment.feature"  // directory of the feature files
)

public class CucumberTest {

}
