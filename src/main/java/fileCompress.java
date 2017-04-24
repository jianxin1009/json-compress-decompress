import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Sukumaar on 18-04-2017.
 */
public class fileCompress {

    public static void main(String[] args) throws IOException {
        if ((args.length == 3)) {
            String sourcePath = args[1].replace("\\", "/");
            String destinationPath = args[2].replace("\\", "/");

            if ((args[0].equalsIgnoreCase("-c"))) {
                minify(sourcePath, destinationPath);
            } else if ((args[0].equalsIgnoreCase("-d"))) {
                maxify(sourcePath, destinationPath);
            }
        } else {
            usage();
        }


    }

    private static void usage() {
        System.err.println("Usage: ");
        System.err.println("Minify: java -jar compress.jar -c sourcePath destPath");
        System.err.println("Maxify: java -jar compress.jar -d sourcePath destPath");
        System.exit(100);
    }

    public static void minify(String srcPath, String destPath) throws IOException {
        
        String content = Files.toString(new File(srcPath), Charsets.UTF_8);


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readValue(content, JsonNode.class);
        
        Files.write(jsonNode.toString(), new File(destPath), Charsets.UTF_8);

    }

    public static void maxify(String srcPath, String destPath) throws IOException {
        String source = Files.toString(new File(srcPath), Charsets.UTF_8);
        StringWriter out = new StringWriter();

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(source);
        try (JsonGenerator gen = factory.createGenerator(out)) {
            gen.setPrettyPrinter(new DefaultPrettyPrinter());
            while (parser.nextToken() != null) {
                gen.copyCurrentEvent(parser);
            }
        }
        Files.write(out.getBuffer().toString(), new File(destPath), Charsets.UTF_8);

    }

}

