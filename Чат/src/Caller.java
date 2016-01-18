import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Caller {
    private String localNick;
    private InetSocketAddress remoteAddress;
    private String remoteNick;
    private CallStatus callStatus;

    public Caller(String localNick) {
        this.localNick = localNick;
    }

    public Caller(String localNick, InetSocketAddress remoteAddress) {
        this(localNick);
        this.remoteAddress = remoteAddress;
    }

    public Caller(String localNick, String ip) {
        this(localNick);
        remoteAddress = new InetSocketAddress(ip, 28411);
    }

    public Connection call() {
        Connection connection = null;

        try {
            connection = new Connection(new Socket(remoteAddress.getAddress(),
                    28411));

            connection.sendNickHello(localNick);

            callStatus = receiveCallStatus(connection);
        } catch (IOException | NullPointerException ex) {
            callStatus = CallStatus.NOT_ACCESSIBLE;
        }

        if (callStatus.toString().equals("OK")) {
            runCommandListenerThreadAndAddObserver(connection);
            return connection;
        }

        return null;
    }

    private CallStatus receiveCallStatus(Connection connection) {
        try {
            Command lastCommand = connection.receive();

            if (isNickCommand(lastCommand)) {
                NickCommand nickCommand = (NickCommand) lastCommand;

                if (nickCommand.isBusy())
                    return CallStatus.BUSY;

                lastCommand = connection.receive();

                if (isAccept(lastCommand))
                    return CallStatus.OK;

                return CallStatus.REJECTED;
            }

            return CallStatus.NO_SERVICE;

        } catch (IOException ex) {
            return CallStatus.NOT_ACCESSIBLE;
        }
    }

    private boolean isNickCommand(Command lastCommand) {
        if (lastCommand instanceof NickCommand) {
            setRemoteNick(((NickCommand) lastCommand).getNick());
            return true;
        }

        return false;
    }

    private boolean isAccept(Command lastCommand) {
        return lastCommand.toString().equals("ACCEPT");
    }

    private void runCommandListenerThreadAndAddObserver(Connection connection) {
        CommandListenerThread commandListenerThread = new CommandListenerThread(
                connection);
        commandListenerThread.addObserver(MainForm.window);
    }

    public void setLocalNick(String localNick) {
        this.localNick = localNick;
    }

    public void setRemoteAddress(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public void setRemoteNick(String remoteNick) {
        this.remoteNick = remoteNick;
    }

    public CallStatus getCallStatus() {
        return callStatus;
    }

    public String getRemoteNick() {
        return remoteNick;
    }

    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public String getLocalNick() {
        return localNick;
    }

    public static enum CallStatus {
        BUSY, NO_SERVICE, NOT_ACCESSIBLE, OK, REJECTED
    }

}