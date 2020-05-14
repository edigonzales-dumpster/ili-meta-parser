package ch.so.agi;

import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.interlis.ili2c.Ili2c;
import ch.interlis.ili2c.Ili2cException;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.metamodel.AbstractClassDef;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.PredefinedModel;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.TypeModel;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.ilirepository.IliManager;

public class MetaParser {
    Logger log = LoggerFactory.getLogger(MetaParser.class);

    private TransferDescription td = null;

    public void doIt(String filename) throws Ili2cException {
        td = getTransferDescriptionFromFileName(filename);
        
        log.info(td.getLastModel().getDocumentation());
        log.info(td.getLastModel().getMetaValues().toString());
        
        
        // https://github.com/edigonzales/itf2avdpoolng_sogis/blob/master/src/main/java/org/catais/utils/ModelUtility.java
        // TODO Vorsicht lastModel
        // Kann noch sehr viel anderes als Topic sein, oder?
        Iterator modeli = td.getLastModel().iterator();
        while (modeli.hasNext()) {
            Object tObj = modeli.next();
            
            if (tObj instanceof Topic) {
                Topic topic = (Topic) tObj;
                Iterator iter = topic.getViewables().iterator();
                while (iter.hasNext()) {
                    Object obj = iter.next();
                    log.info(obj.toString());
                    
                    if (obj instanceof Viewable) {
                        Viewable v = (Viewable) obj;
                        
//                        if(isPureRefAssoc(v)){
//                            continue;
//                        }

                        log.info(v.getDocumentation());
                        log.info(v.getMetaValues().toString());

                        String className = v.getScopedName(null);
                        Iterator attri = v.getAttributes();
//                        ViewableWrapper viewableWrapper = new ViewableWrapper(className, v);
//                        java.util.List attrv = ch.interlis.iom_j.itf.ModelUtilities.getIli1AttrList((AbstractClassDef) v);
                        while (attri.hasNext()) {
                            Object aObj = attri.next();
                            log.info("aObj: " + aObj);
                            if (aObj instanceof AttributeDef) {
                                AttributeDef attr = (AttributeDef) aObj;
                                log.info(attr.getDocumentation());
                            }
                            
                        }

                        
                        
                    }
                }
                
            }
            
            
            
//            if (mObj instanceof Model) {
//                Model model = (Model) mObj;
//                if (model instanceof TypeModel) {
//                    continue;                               
//                }
//                if (model instanceof PredefinedModel) {
//                    continue;
//                }
//
//                Iterator topici = model.iterator();
//                while (topici.hasNext()) {
//                    Object tObj = topici.next();
//                    log.info(tObj.toString());
//                }
//            }

        }

    }
    
    
    
    
    
    private TransferDescription getTransferDescriptionFromFileName(String fileName) throws Ili2cException {
        IliManager manager = new IliManager();
        String repositories[] = new String[] { "http://models.interlis.ch/", "http://models.kkgeo.ch/", "http://models.geo.admin.ch/", "." };
        manager.setRepositories(repositories);
        
        ArrayList<String> ilifiles = new ArrayList<String>();
        ilifiles.add(fileName);
        Configuration config = manager.getConfigWithFiles(ilifiles);
        ch.interlis.ili2c.metamodel.TransferDescription iliTd = Ili2c.runCompiler(config);
                
        if (iliTd == null) {
            throw new IllegalArgumentException("INTERLIS compiler failed");
        }
        
        return iliTd;
    }
}
