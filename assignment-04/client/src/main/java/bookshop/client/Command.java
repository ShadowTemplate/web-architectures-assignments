package bookshop.client;

import java.util.List;

class Command {
    private OperationI op;
    private List<String> args;

    public Command(OperationI op, List<String> args) {
        this.op = op;
        this.args = args;
    }


    public OperationI getOp() {
        return op;
    }

    public List<String> getArgs() {
        return args;
    }
}
