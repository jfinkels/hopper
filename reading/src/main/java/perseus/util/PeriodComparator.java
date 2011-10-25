package perseus.util;

import java.util.*;

/**
 * A class used for comparing periods.
*/
public class PeriodComparator {

    static HashMap periodMap = new HashMap();

    static{
	periodMap.put("Middle Bronze Age", "MBA");
	periodMap.put("Late Bronze Age", "LBA");
	periodMap.put("Dark Age", "Dark Age");
	periodMap.put("Geometric", "Geom");
	periodMap.put("Geom./Arch./Clas.", "Geom/Arch/Clas");
	periodMap.put("Early Archaic", "Early Archaic");
	periodMap.put("Archaic", "Archaic");
	periodMap.put("Early Archaic", "Archaic");
	periodMap.put("High Archaic", "Archaic");
	periodMap.put("Late Archaic", "Archaic");
	periodMap.put("Early Classical", "Late Archaic");
	periodMap.put("Late Archaic", "Late Archaic");
	periodMap.put("Late Archaic/Early Classical", "Late Archaic");
	periodMap.put("Late Archaic/Early Classical", "L. Arch/Early Clas.");
	periodMap.put("Late Arch./Early Clas.", "L. Arch/Early Clas.");
	periodMap.put("Late Archaic/Early Classical", "Arch/Clas");
	periodMap.put("Archaic/Classical", "Arch/Clas");
	periodMap.put("Archaic - Hellenistic", "Arch. - Hell.");
	periodMap.put("Early Classical", "Early Clas.");
	periodMap.put("High Classical", "Early Clas.");
	periodMap.put("Late Archaic/Early Classical", "Early Clas.");
	periodMap.put("Early Classical", "Early Clas.");
	periodMap.put("Classical", "Classical");
	periodMap.put("Late Archaic/Early Classical", "Classical");
	periodMap.put("Late Classical", "Classical");
	periodMap.put("Classical/Late Class.", "Clas/Late Clas.");
	periodMap.put("High Classical", "High Classical");
	periodMap.put("Late Classical", "Late Classical");
	periodMap.put("Late Classical/Early Hellenistic", "Late Classical");
	periodMap.put("Late Classical/Hellenistic", "Late Clas./Hell.");
	periodMap.put("Late Clas./Hell.", "Late Clas/Hell.");
	periodMap.put("Late Clas./Hell.", "Late Clas/Hell.");
	periodMap.put("Late Clas./Hell.", "Late Clas/Hell.");
	periodMap.put("Early Hellenistic", "Early Hellenistic");
	periodMap.put("Hellenistic", "Hell.");
	periodMap.put("High Hellenistic", "Hell.");
	periodMap.put("Late Hellenistic", "Late Hell.");
	periodMap.put("Graeco-Roman", "Hell./Roman");
	periodMap.put("Late Republican", "Hell./Roman");
	periodMap.put("Hellenistic/Roman", "Hell./Roman");
	periodMap.put("Roman", "Roman");
	periodMap.put("Roman?", "Roman");
	periodMap.put("Late Etruscan/Republican", "Etruscan/Republican");
	periodMap.put("Late Republican", "Etruscan/Republican");
	periodMap.put("Republican", "Etruscan/Republican");
	periodMap.put("Roman Imperial", "Roman Imperial");
	periodMap.put("Julio-Claudian", "Roman Imperial");
	periodMap.put("Augustan", "Augustan");
	periodMap.put("Augustan/Julio-Claudian", "Augustan");
	periodMap.put("Augustan", "Augustan");
	periodMap.put("Julio-Claudian", "Julio-Claudian");
	periodMap.put("Julio-Claudian or Hadrianic", "Julio-Claudian");
	periodMap.put("Julio-Claudian/Flavian", "Julio-Claudian");
	periodMap.put("Flavian", "Flavian");
	periodMap.put("Late Flavian/Trajanic", "Flavian");
	periodMap.put("Flavian-Antonine?", "Nerva");
	periodMap.put("Nerva", "Nerva");
	periodMap.put("Trajanic", "Trajanic");
	periodMap.put("Trajanic/Hadrianic", "Trajanic");
	periodMap.put("Trajanic?", "Trajanic");
	periodMap.put("Hadrianic", "Hadrianic");
	periodMap.put("Antonine", "Antonine");
	periodMap.put("Late Antonine/Severan", "Antonine");
	periodMap.put("Severan", "Severan");
	periodMap.put("Severan?", "Severan");
	periodMap.put("Third Century A.C.", "Third C. A.C.");
	periodMap.put("Third Century A.C.", "Third Century A.C.");
	periodMap.put("Late Roman Imperial", "Late Roman");
	periodMap.put("Late Roman Imperial/Early Byzantine", "Late Roman");
	periodMap.put("Tetrarchic", "Tetrarchic");
	periodMap.put("Constantinian", "Constantinian");
	periodMap.put("Early Byzantine", "Early Byzantine");
	periodMap.put("Chrisitan", "Early Byzantine");
	periodMap.put("Modern", "Modern");
	periodMap.put("-500", "Archaic");	
    }

    /**
     * Retrieve the sortable period given the display period
     *
     * @param key The display period
     * @return the sortable period
    */
    public static String getSortablePeriod(String key){
	return (String) periodMap.get(key);
    }

}
