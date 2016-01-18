
public class Command {

    public CommandType type;

    public Command() {

    }

    static enum CommandType {
        ACCEPT, DISCONNECT, MESSAGE, NICK, REJECT

    }

    public Command(CommandType t) {
        type = t;
    }

    public String toString() {
        return type.toString();
    }



}