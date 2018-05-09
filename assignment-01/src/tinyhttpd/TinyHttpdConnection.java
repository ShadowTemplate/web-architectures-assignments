package tinyhttpd;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TinyHttpdConnection extends Thread {

    /*
    * The RegExp checks if the request header is well formatted, according to the HTTP RFC (details here:
    * https://tools.ietf.org/html/rfc2616#section-5.1) and to the application requirements.
    * */
    private static final String INPUT_HEADER_REGEX = "^GET /process/.* HTTP/[0-9].[0-9]$";

    private static final String OUTPUT_CONTENT_HEADER = "\r\nContent-Type: text/html\r\nContent-Length: ";
    private static final String OUTPUT_SUCCESS_HEADER = "HTTP/1.1 200 OK" + OUTPUT_CONTENT_HEADER;
    private static final String OUTPUT_ERROR_HEADER = "HTTP/1.1 400 Bad Request" + OUTPUT_CONTENT_HEADER;
    private static final String OUTPUT_HEADER_ENDING = "\r\n\r\n";
    private static final String PAGE_TEMPLATE = "<html><head><title>Server</title></head><body><p>%s</p></body></html>";

    private enum ResponseType {INVALID_REQUEST_ERROR, PROCESS_ERROR, PROCESS_SUCCESS}

    private Socket socket;

    TinyHttpdConnection(Socket socket) {
        this.socket = socket;
        setPriority(NORM_PRIORITY - 1);
        start();
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                String request = in.readLine();
                if (request == null) {
                    return;
                }
                System.out.println(request);
                if (!request.matches(INPUT_HEADER_REGEX)) {
                    sendResponse(out, ResponseType.INVALID_REQUEST_ERROR, null);
                    return;
                }
                String[] commandTokens = parseCommand(request);
                String command = String.join(" ", commandTokens);
                System.out.println("Executing command: " + command);
                try {
                    List<String> result = executeCommand(commandTokens);
                    result.add(0, command);
                    sendResponse(out, ResponseType.PROCESS_SUCCESS, result);
                } catch (IOException | InterruptedException e) {
                    System.err.println("An error occurred while running the external process.\n" + e.getMessage());
                    sendResponse(out, ResponseType.PROCESS_ERROR, Arrays.asList(command, e.getMessage()));
                }
            } catch (IOException e) {
                System.err.println("Unable to write response into socket.\n" + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Unable to read request from socket.\n" + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error while closing socket.\n" + e.getMessage());
            }
        }
    }

    private void sendResponse(BufferedWriter out, ResponseType response, List<String> data) throws IOException {
        String output, header;
        switch (response) {
            case INVALID_REQUEST_ERROR:
                output = String.format(PAGE_TEMPLATE, "ERROR: invalid request.");
                header = OUTPUT_ERROR_HEADER;
                break;
            case PROCESS_ERROR:
                output = String.format(PAGE_TEMPLATE, "ERROR: process invocation failure.<br><br>Command: <b>" +
                        data.get(0) + "</b><br><br><u>Details:</u><br>" + data.get(1));
                header = OUTPUT_ERROR_HEADER;
                break;
            case PROCESS_SUCCESS:
                StringBuilder log = new StringBuilder();
                log.append("SUCCESS: process invocation completed.<br><br>Command: <b>").append(data.get(0))
                        .append("</b><br><br><u>Output:</u><br>");
                for (int i = 1; i < data.size(); i++) {
                    log.append(data.get(i)).append("<br>");
                }
                output = String.format(PAGE_TEMPLATE, log.toString());
                header = OUTPUT_SUCCESS_HEADER;
                break;
            default:
                output = "";
                header = OUTPUT_ERROR_HEADER;
        }
        out.write(header + output.length() + OUTPUT_HEADER_ENDING + output);
        out.flush();
    }

    private static String[] parseCommand(String req) {
        String url = req.substring(req.indexOf("/process/") + "/process/".length(), req.lastIndexOf("HTTP") - 1);
        String[] splits = url.split("\\?");  // splits[0] = command, splits[1] = options
        String[] options = splits.length > 1 ? splits[1].split("&") : new String[0];  // there may not be options
        String[] command = new String[options.length + 1];
        command[0] = splits[0];
        for (int i = 0; i < options.length; i++) {
            command[i + 1] = options[i].substring(options[i].lastIndexOf("=") + 1);  // extract options values
        }
        return command;
    }

    private static List<String> executeCommand(String[] command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectError();
        pb.redirectOutput();
        Process p = pb.start();
        BufferedReader outputStream = new BufferedReader(new InputStreamReader(p.getInputStream())),
                errorStream = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        p.waitFor();  // wait for the external process to complete

        String buffer;
        List<String> lines = new ArrayList<>();
        for (BufferedReader stream : Arrays.asList(outputStream, errorStream)) {  // read the output of both streams
            while (stream.ready() && (buffer = stream.readLine()) != null) {
                System.out.println(buffer);
                lines.add(buffer);
            }
            stream.close();
        }
        return lines;
    }
}
