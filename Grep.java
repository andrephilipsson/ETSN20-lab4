import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Grep {

    private static boolean ignoreCase = false;
    private static boolean showLineNumbers = false;
    private static boolean multipleFiles = false;
    private static boolean fixedStrings = false;
    private static boolean alwaysShowFilename = false;
    private static boolean neverShowFilename = false;

    public static void main(String[] args) {
        // Parse arguments and get files and pattern
        Arguments parsedArgs = parseArgs(args);
        if (parsedArgs.files.isEmpty()) {
            printUsage();
        }

        // Create pattern with proper flags
        Pattern pattern = createPattern(parsedArgs.pattern);
        boolean foundInAnyFile = false;
        multipleFiles = parsedArgs.files.size() > 1;

        // Process each file
        for (String filename : parsedArgs.files) {
            try {
                foundInAnyFile |= searchFile(pattern, filename);
            } catch (FileNotFoundException e) {
                System.err.println(
                    "jgrep: " + filename + ": No such file or directory"
                );
            } catch (IOException e) {
                System.err.println(
                    "jgrep: " + filename + ": Error reading file"
                );
            }
        }

        // Exit with status 1 if no matches were found
        if (!foundInAnyFile) {
            System.exit(1);
        }
    }

    private static Pattern createPattern(String patternStr) {
        int flags = 0;
        if (ignoreCase) {
            flags |= Pattern.CASE_INSENSITIVE;
        }
        if (fixedStrings) {
            flags |= Pattern.LITERAL;
        }
        return Pattern.compile(patternStr, flags);
    }

    private static boolean searchFile(Pattern pattern, String filename) throws IOException {
        boolean found = false;
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;
            while ((line = in.readLine()) != null) {
                lineNumber++;
                if (pattern.matcher(line).find()) {
                    printMatch(filename, lineNumber, line);
                    found = true;
                }
            }
        }
        return found;
    }

    private static void printMatch(
        String filename,
        int lineNumber,
        String line
    ) {
        StringBuilder output = new StringBuilder();

        // Show filename if -H is set or multiple files (unless -h is set)
        if (!neverShowFilename && (alwaysShowFilename || multipleFiles)) {
            output.append(filename).append(":");
        }
        if (showLineNumbers) {
            output.append(lineNumber).append(":");
        }

        output.append(line);
        System.out.println(output);
    }

    private static class Arguments {

        String pattern;
        List<String> files = new ArrayList<>();
    }

    private static Arguments parseArgs(String[] args) {
        Arguments arguments = new Arguments();
        boolean patternNext = true;

        for (String arg : args) {
            if (arg.startsWith("-")) {
                switch (arg) {
                    case "--help":
                        printUsage();
                        break;
                    case "-i":
                    case "--ignore-case":
                        ignoreCase = true;
                        break;
                    case "-n":
                    case "--line-number":
                        showLineNumbers = true;
                        break;
                    case "-F":
                    case "--fixed-strings":
                        fixedStrings = true;
                        break;
                    case "-H":
                        alwaysShowFilename = true;
                        break;
                    case "-h":
                        neverShowFilename = true;
                        break;
                    default:
                        System.err.println("jgrep: unknown option -- '" + arg + "'");
                        printUsage();
                }
            } else {
                if (patternNext) {
                    arguments.pattern = arg;
                    patternNext = false;
                } else {
                    arguments.files.add(arg);
                }
            }
        }

        return arguments;
    }

    private static void printUsage() {
        System.err.println("Usage: jgrep [OPTIONS] [PATTERN] [FILE...]");
        System.err.println();
        System.err.println("Options:");
        System.err.println("  -H                   Print the filename for each match");
        System.err.println("  -h                   Suppress the prefixing filename on output");
        System.err.println("  -F, --fixed-strings  Interpret pattern as a list of fixed strings");
        System.err.println("  -i, --ignore-case    Ignore case distinctions in pattern");
        System.err.println("  -n, --line-number    Print line number with output lines");
        System.err.println("  --help               Print help message");
        System.err.println();
        System.exit(1);
    }
}
