import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class yamlTest {
    public static void main(String[] args) throws FileNotFoundException {
        Map<String, Object> propMap = new Yaml().load(new FileReader("/Users/iminseo/Desktop/JAVA/logger_test/src/main/resources/my-logger.yml"));
//        System.out.println(propMap);
        System.out.println(propMap.get("loggers"));
    }
}
