package io.quarkus.images.commands;

import io.quarkus.images.BuildContext;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EntryPointCommand implements Command {

    private final String[] cmd;

    public EntryPointCommand(String[] cmd) {
        this.cmd = cmd;
    }

    @Override
    public String execute(BuildContext context) {
        return "ENTRYPOINT [" + Arrays.stream(cmd).collect(Collectors.joining("\", \"", "\"", "\"")) + "]";
    }
}
