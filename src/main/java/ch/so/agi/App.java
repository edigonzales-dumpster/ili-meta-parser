package ch.so.agi;

import ch.interlis.ili2c.Ili2cException;

public class App {
    public static void main(String[] args) throws Ili2cException {
        
        MetaDataParser parser = new MetaDataParser();
        parser.doIt("src/test/data/SO_AGI_Modell_Eins_202000513.ili");        
        
        System.out.println("Hallo Welt.");
    }
}
