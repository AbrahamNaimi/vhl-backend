package io.github.AbrahamNaimi.vhl_backend;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModuleStructureTest {

    @Test
    void modulesRespectTheirBoundaries() {
        ApplicationModules.of(VhlBackendApplication.class).verify();
    }
}
