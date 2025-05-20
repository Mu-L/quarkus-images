package io.quarkus.images;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.quarkus.images.modules.MandrelModule.parseJDKVersion;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class SimpleTest {

    @BeforeAll
    static void init() {
        JDock.setDockerFileDir(new File("target/test"));
    }

    @Test
    public void testMandrelAmd64() {
        String arch = "amd64";
        String mandrel_version = "22.3.0.1-Final";
        String sha = "72ba94f4ca8e48eaa905433a6d0cfff5e7a657fb4f5419e86d7b8f5332ed0345";
        String java_version = "17";

        String filename = "mandrel-java%s-%s-%s.Dockerfile".formatted(
                java_version, mandrel_version, arch);
        Dockerfile dockerFile = Builders.getMandrelDockerFile(mandrel_version, java_version, arch, sha);
        dockerFile.build(new File("target/test/" + filename));

    }

    @Test
    public void testMandrelArm64() {
        String arch = "arm64";
        String mandrel_version = "22.3.0.1-Final";
        String sha = "729ad2496191d4e0bc0dea3d19ac3ede0f4561e0ba4c3468d5824ca5a160d81b";
        String java_version = "17";

        String filename = "mandrel-java%s-%s-%s.Dockerfile".formatted(
                java_version, mandrel_version, arch);

        Dockerfile dockerFile = Builders.getMandrelDockerFile(mandrel_version, java_version, arch, sha);
        dockerFile.build(new File("target/test/" + filename));
    }

    @Test
    public void testGraalvmAmd64() {
        String arch = "amd64";
        String graalvm_version = "22.1.0";
        String sha = "f11d46098efbf78465a875c502028767e3de410a31e45d92a9c5cf5046f42aa2";
        String java_version = "17";

        String filename = "graalvm-ce-java%s-%s-%s.Dockerfile".formatted(
                java_version, graalvm_version, arch);

        Dockerfile dockerFile = Builders.getGraalVmDockerFile(graalvm_version, java_version, arch, sha);
        dockerFile.build(new File("target/test/" + filename));
    }

    @Test
    public void testRunWithExecForm() {
        Dockerfile df = Dockerfile.from("registry.access.redhat.com/ubi8/ubi-minimal:8.10");
        df.exec("/bin/bash", "-c", "echo hello");
        assertThat(df.build()).contains("RUN [ \"/bin/bash\", \"-c\", \"echo hello\" ]\n");
    }

    @Test
    public void testRunWithShellForm() {
        Dockerfile df = Dockerfile.from("registry.access.redhat.com/ubi8/ubi-minimal:8.10");
        df.run("source $HOME/.bashrc", "echo $HOME");
        assertThat(df.build()).contains("RUN source $HOME/.bashrc \\\n && echo $HOME\n");
    }

    @Test
    void testJDKVersionParsing() {
        assertArrayEquals(new int[] { 20, 0, 1, 9 }, parseJDKVersion("20.0.1+9"));
        assertArrayEquals(new int[] { 21, 1, 3, 9 }, parseJDKVersion("21.1.3+9-LTS"));
        assertArrayEquals(new int[] { 21, 0, 4, 7 }, parseJDKVersion("21.0.4+7-LTS"));
        assertArrayEquals(new int[] { 21, 0, 5, 4 }, parseJDKVersion("21.0.5-beta+4-ea"));
        assertArrayEquals(new int[] { 22, 0, 0, 36 }, parseJDKVersion("22+36"));
        assertArrayEquals(new int[] { 25, 0, 0, 20 }, parseJDKVersion("25-beta+20-ea"));
    }
}
