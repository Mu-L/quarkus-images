package io.quarkus.images.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface ContainerToolCommands {

    String getExecutable();

    List<String> build(String dockerFile, String arch, String imageName);

    default List<String> push(String imageName) {
        return List.of(getExecutable(), "push", imageName);
    }

    default List<String> createManifest(String imageName, Map<String, String> archToImage) {
        List<String> command = new ArrayList<>(Arrays.asList(getExecutable(), "manifest", "create", imageName));
        for (Map.Entry<String, String> entry : archToImage.entrySet()) {
            command.addAll(List.of("--amend", entry.getValue()));
        }
        return command;
    }

    default List<String> pushManifest(String imageName) {
        return List.of(getExecutable(), "manifest", "push", imageName);
    }

    default List<String> tag(String imageName, String tagName) {
        return List.of(getExecutable(), "tag", imageName, tagName);
    }

    class DockerCommands implements ContainerToolCommands {

        static final DockerCommands INSTANCE = new DockerCommands();

        @Override
        public String getExecutable() {
            return "docker";
        }

        @Override
        public List<String> build(String dockerFile, String arch, String imageName) {
            List<String> command = new ArrayList<>(
                    Arrays.asList(getExecutable(), "buildx", "build", "--load", "-f", dockerFile));
            if (arch != null) {
                command.add("--platform=linux/" + arch);
            }
            command.add("--tag");
            command.add(imageName);
            command.add(".");
            return command;
        }
    }

    class PodmanCommands implements ContainerToolCommands {

        static final PodmanCommands INSTANCE = new PodmanCommands();

        @Override
        public String getExecutable() {
            return "podman";
        }

        @Override
        public List<String> build(String dockerFile, String arch, String imageName) {
            List<String> command = new ArrayList<>(Arrays.asList(getExecutable(), "build", "-f", dockerFile));
            if (arch != null) {
                command.add("--platform=linux/" + arch);
            }
            command.add("--tag");
            command.add(imageName);
            command.add(".");
            return command;
        }
    }
}
