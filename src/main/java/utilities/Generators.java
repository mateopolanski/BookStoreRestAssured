package utilities;

import org.apache.commons.lang3.RandomStringUtils;

public class Generators {

    public static String genName(){
        String generatedName = RandomStringUtils.randomAlphanumeric(2);
        return (generatedName+"Admin");
    }

    public static String genEmail(){
        String generatedName = RandomStringUtils.randomAlphabetic(2);
        return (generatedName+"@gmail.com");
    }

}
