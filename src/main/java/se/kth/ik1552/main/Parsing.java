package se.kth.ik1552.main;

import java.io.*;
import java.util.Objects;

public class Parsing {

    static String separator = "\t";
    static String emptyTabDesc = "        ";
    static String emptySpaceDesc = " ";
    static boolean filterOn = false;

    private static void ParseFile(String fileName) throws IOException {



        File input = createFile("input", fileName);

        File output = createFile("output", fileName);

        BufferedReader br = new BufferedReader(new FileReader(input));
        PrintWriter pw = new PrintWriter(new FileWriter(output));

        // Declaring a string variable
        String st;
        writeHeaders(pw);

        // Condition holds true till
        // there is character in a string
        // while ((st = br.readLine()) != null) {
        //    writeLines(st, pw);
        //}

        pw.close();

    }

    private static File createFile(String type, String fileName) {

        return new File(
                "C:\\Parsing\\" + type + "\\" + fileName);
    }

    private static void writeLines(String st, PrintWriter pw, boolean checkConditions) {

        boolean shoulddWrite = true;
        boolean capture = true;

        if (st.length() < 35) return;

        String endOfString = st.substring(36, st.length());

        String date = st.substring(0, 5);
        String time = st.substring(6,21);
        String key = st.substring(34, 35);
        String desc = endOfString.split("\\:")[0];
        String comment = endOfString.split("\\:")[1];

        if (Objects.equals(comment, "")) comment = " na";

        if (checkConditions) shoulddWrite = controlForConditions(key, desc, comment);

        if (filterOn) capture = checkFilterConditions(key, desc, comment);

        // Print the string
        if (shoulddWrite && capture) pw.write( date
                + separator + time
                + separator + st.substring(23, 27) // ??
                + separator + st.substring(29, 33) // ??
                + separator + key
                + separator + desc
                + separator + comment.substring(1) // remove blank space
                + "\n");
    }

    private static boolean checkFilterConditions(String key, String desc, String comment) {
        if (Objects.equals(desc, "SmartLoc.LocationUploadManager")) return true;

        return false;
    }

    private static boolean controlForConditions(String key, String desc, String comment) {


        if (desc.length() > 9)
            if (Objects.equals(desc.substring(0,10),"System.err")) return false; // Error handling

        if (desc.length() > 5)
            if (Objects.equals(desc.substring(0,6),"ACodec")) return false; // Error handling

        if (desc.length() > 4)
            if (Objects.equals(desc.substring(0,5),"aaaaa")) return false; // Control functions color/size

        if (Objects.equals(desc,emptySpaceDesc)) return false; // seems to be maintenance activity
        if (Objects.equals(desc,emptyTabDesc)) return false; // seems to be maintenance activity
        if (Objects.equals(desc,"AccelerometerListener")) return false; // Checking for user motions
        if (Objects.equals(desc,"ContextImpl")) return false;

        if (comment.length() > 9)
            if (Objects.equals(comment.substring(0,10),"CTRL-DEBUG")) return false; // Control functions color/size

        if (Objects.equals(comment, " na")) return false;
        if (Objects.equals(comment,"msm_cpu_pm_enter_sleep mode")) return false;
        if (Objects.equals(comment, " na")) return false;

        return true;
    }

    private static void writeHeaders(PrintWriter pw) {

        pw.write("date"
                + separator + "time"
                + separator + "?"
                + separator + "?"
                + separator + "key"
                + separator + "desc"
                + separator + "comment"
                + "\n");
    }


    public static void main(String[] args) throws IOException {

        /*

        ParseFile("logcat");

        for (int i = 1; i <= 20; i ++ ) {
            if (i < 10) ParseFile("logcat.0" + i);
            else ParseFile("logcat." + i);
        }

         */

        ReadAndParseAllFiles();

    }

    private static void ReadAndParseAllFiles() throws IOException {

        String outputFile = filterOn ? "filter.txt" : "all.txt";

        File output = new File("C:\\Parsing\\output\\" + outputFile);
        PrintWriter pw = new PrintWriter(new FileWriter(output));

        writeHeaders(pw);

        ReadAndParseFile("logcat", output, pw);

        for (int i = 1; i <= 20; i ++ ) {
            if (i < 10) ReadAndParseFile("logcat.0" + i, output, pw);
            else ReadAndParseFile("logcat." + i, output, pw);
        }

        pw.close();

    }

    private static void ReadAndParseFile(String fileName, File output, PrintWriter pw) throws IOException {

        File input = createFile("input", fileName);
        BufferedReader br = new BufferedReader(new FileReader(input));

        String st;
        while ((st = br.readLine()) != null) {
            writeLines(st, pw, true);
        }

    }
}
