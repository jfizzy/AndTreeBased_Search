package Input;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author 
 */
public class FileExaminer {

    private final String fp;
    private File f;
    private BufferedReader br;

    private final Pattern sec;
    private final Pattern brk;
    private final Pattern dpt;
    private final Pattern slt;
    private final Pattern crs;
    private final Pattern ncp;
    private final Pattern uwt;

    public FileExaminer(String fp) {
        this.ncp = Pattern.compile("^([A-Z][A-Z][A-Z][A-Z] [0-9][0-9][0-9] (LEC [0-9][0-9] TUT [0-9][0-9]|LEC [0-9][0-9]|TUT [0-9][0-9]), [A-Z][A-Z][A-Z][A-Z] [0-9][0-9][0-9] (LEC [0-9][0-9] TUT [0-9][0-9]|LEC [0-9][0-9]|TUT [0-9][0-9]))$");
        this.crs = Pattern.compile("^([A-Z][A-Z][A-Z][A-Z] [0-9][0-9][0-9] (LEC [0-9][0-9] TUT [0-9][0-9]|LEC [0-9][0-9]|TUT [0-9][0-9]))$");
        this.slt = Pattern.compile("^(MO|TU|WE|TR|FR), [( |1-2)][0-9]:[0-5][0-9], [0-9]*, [0-9]*$");
        this.dpt = Pattern.compile("^[a-zA-Z0-9]+$");
        this.brk = Pattern.compile("^\\s*$");
        this.sec = Pattern.compile("^([A-Za-z]+( [A-Za-z]+)*:)$");
        this.uwt = Pattern.compile("^([A-Z][A-Z][A-Z][A-Z] [0-9][0-9][0-9] (LEC [0-9][0-9]|LEC [0-9][0-9] TUT [0-9][0-9]), (MO|TU|WE|TR|FR), [ 12]?[0-9]:[0-9][0-9])$");
        this.fp = fp;
        this.f = null;
        this.br = null;
    }

    public void init() {
        try {
            f = new File(fp);
            br = new BufferedReader(new FileReader(f));

        } catch (Exception e) {
            System.err.println("Problem when initializing input file reader");
        }
    }

    public void parse() {
        try {
            if (!br.ready()) {
                return;
            }
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                Matcher msec = sec.matcher(line); // section format regex
                Matcher mbrk = brk.matcher(line); // break format regex
                Matcher mdpt = dpt.matcher(line); // department format regex
                Matcher mslt = slt.matcher(line); // slot format regex
                Matcher mcrs = crs.matcher(line); // course format regex
                Matcher mncp = ncp.matcher(line); // not-compatible format regex
                Matcher muwt = uwt.matcher(line); // unwanted format regex

                if (msec.find()) { // section
                    // check which section is being read next
                    System.out.println("SEC LINE | " + line);

                    line = line.toLowerCase();
                    switch (line) {
                        case "course slots":
                            break;
                        case "lab slots":
                            break;
                        default:
                            break;
                    }
                } else if (mdpt.find()) { // department
                    System.out.println("DPT LINE | " + line);
                } else if (mslt.find()) { // slot
                    System.out.println("SLT LINE | " + line);
                } else if (mcrs.find()) { // course
                    System.out.println("CRS LINE | " + line);
                } else if (mncp.find()) { // 
                    System.out.println("NCP LINE | " + line);
                } else if (muwt.find()) {
                    System.out.println("UWT LINE | " + line);
                } else if (mbrk.find()) { // break
                    System.out.println("BRK LINE | " + line);
                    // skip and prepare for new section
                } else { //not one of our line formats
                    System.out.println("BAD LINE | " + line);
                }

            }
        } catch (IOException io) {
            System.err.println("There was a problem reading in the input file contents");
        }
    }
}
