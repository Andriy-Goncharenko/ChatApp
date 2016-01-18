public class NickCommand extends Command {
    private boolean busy;
    private String nick, version;

    public NickCommand(String version, String nick, boolean busy) {
        super(CommandType.NICK);
        this.version = version;
        this.busy = busy;
        this.nick = nick;
    }

    public boolean isBusy() {
        return busy;
    }

    public String getNick() {
        return nick;
    }

    public String toString() {
        if (busy)
            return version + " user " + nick + " busy";
        else
            return version + " user " + nick;
    }
}