package utils;

import org.apache.commons.lang3.RandomStringUtils;

public class Generators {

    public static String genName(){
        String generatedName = RandomStringUtils.randomAlphanumeric(3);
        return ("MatiTest"+ generatedName);
    }

    public static String genEmail(){
        String generatedName = RandomStringUtils.randomAlphabetic(3);
        return ("MatiTest"+ generatedName+"@gmail.com");
    }

}
