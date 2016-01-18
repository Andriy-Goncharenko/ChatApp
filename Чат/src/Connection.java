import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Connection  {


    private Socket socket;
    private volatile boolean isActual;

    public Connection(Socket socket) {
        this.socket = socket;
        isActual = true;
    }

    public void sendNickHello(String nick) throws IOException {
        socket.getOutputStream().write(("ChatApp 2015" + " user " + nick + "\n").getBytes("UTF-8"));
        socket.getOutputStream().flush();
    }

    public void sendNickBusy(String nick) throws IOException {
        socket.getOutputStream().write(("ChatApp 2015" + " user " + nick + " busy\n").getBytes("UTF-8"));
        socket.getOutputStream().flush();
    }

    public void accept() throws IOException {
        waitStartAccepting(30);

        socket.getOutputStream().write("Accepted\n".getBytes("UTF-8"));
        socket.getOutputStream().flush();
    }

    public void reject() throws IOException {
        waitStartAccepting(30);

        socket.getOutputStream().write("Rejected\n".getBytes("UTF-8"));
        socket.getOutputStream().flush();
    }

    public void sendMessage(String message) throws IOException {
        socket.getOutputStream().write("Message\n".getBytes("UTF-8"));
        socket.getOutputStream().write((message + "\n").getBytes("UTF-8"));
        socket.getOutputStream().flush();
    }

    public void disconnect() throws IOException {
        socket.getOutputStream().write("Disconnect\n".getBytes("UTF-8"));
        socket.getOutputStream().flush();

        isActual = false;
        socket.close();
    }

    private void waitStartAccepting(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored){}
    }

    public boolean isActual(){
        return isActual;
    }

    public Command receive() throws IOException {
        @SuppressWarnings("resource")
        Scanner in = new Scanner(new BufferedInputStream(socket.getInputStream()));
        String line = in.nextLine();

        if(line.contains(" ") && line.startsWith("ChatApp 2015")){

            Boolean isBusy = line.toLowerCase().endsWith(" busy");
            String nick;

            if (isBusy)
                nick = line.substring(line.indexOf(" user ") + 1, line.indexOf(" busy"));
            else
                nick = line.substring(line.lastIndexOf(" ") + 1, line.length());

            return new NickCommand("ChatApp 2015", nick, isBusy);

        } else if(COMMAND_HASH_MAP.containsKey(line.toLowerCase())) {

            if(line.toUpperCase().equals(Command.CommandType.MESSAGE.toString())) {

                line = in.nextLine();
                return new MessageCommand(line);

            } else
                return COMMAND_HASH_MAP.get(line.toLowerCase());

        } else return null;
    }

    @SuppressWarnings("serial")
    static final HashMap<String, Command> COMMAND_HASH_MAP = new HashMap<String, Command>(){{
        put("accepted", new Command(Command.CommandType.ACCEPT));
        put("disconnect", new Command(Command.CommandType.DISCONNECT));
        put("message", new Command(Command.CommandType.MESSAGE));
        put("ChatApp 2015", new Command(Command.CommandType.NICK));
        put("rejected", new Command(Command.CommandType.REJECT));
    }};
}