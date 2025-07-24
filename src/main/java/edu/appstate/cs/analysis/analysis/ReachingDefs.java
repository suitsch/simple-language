package edu.appstate.cs.analysis.analysis;

import edu.appstate.cs.analysis.cfg.CFG;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ReachingDefs {
    private CFG cfg = null;

    public ReachingDefs(CFG cfg) {
        this.cfg = cfg;
    }

    public static class Def {
        private String name;
        private String loc;
        public Def(String name, String loc) {
            this.name = name;
            this.loc = loc;
        }

        @Override
        public String toString() {
            return String.format("[name: %s, loc: %s]", name, loc);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Def def = (Def) o;
            return Objects.equals(name, def.name) && Objects.equals(loc, def.loc);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, loc);
        }
    }

    public Map<String, Set<Def>> computeDefs() {
        Map<String, Set<Def>> defs = new HashMap<>();

        // TODO: We will add the analysis here!

        return defs;
    }
}
