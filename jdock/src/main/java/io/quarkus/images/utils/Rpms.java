package io.quarkus.images.utils;

/**
 * We could have made this transparent in the {@code MicrodnfCommand} but it seems better to have full understanding and control
 * over what we install.
 */
public class Rpms {

    public static String zlibDevel(String base) {
        if (base.contains("/ubi8/") || base.contains("/ubi9/") || base.contains("/ubi8-") || base.contains("/ubi9-")) {
            return "zlib-devel";
        }

        // starting with UBI10, the package is named zlib-ng-compat-devel
        return "zlib-ng-compat-devel";
    }
}
