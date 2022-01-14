package tokenTest;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(plugin="summary"
        , publish= false
        , features = "src/test/tokenTest/token.feature"  // directory of the feature files
)

public class CucumberTest {

}
