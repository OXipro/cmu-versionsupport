package com.oxipro.cmu.versionsupport;

import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VersionMappingTest {

    @Test
    public void packageTokenWinsOverTheMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("1.21.11", "v1_21_R1");
        assertEquals("v1_21_R7", VersionMapping.resolve(
                "org.bukkit.craftbukkit.v1_21_R7",
                "1.21.11-R0.1-SNAPSHOT",
                map
        ));
    }

    @Test
    public void legacyVersionWithoutAPackageUsesTheMap() {
        assertEquals("v1_21_R5", VersionMapping.resolve(
                "org.bukkit.craftbukkit",
                "1.21.8-R0.1-SNAPSHOT",
                VersionMappingTest.map()
        ));
    }

    @Test
    public void minecraft26Point1IsTheBaseToken() {
        assertEquals("v26", VersionMapping.resolve(
                "org.bukkit.craftbukkit",
                "26.1-R0.1-SNAPSHOT",
                Collections.<String, String>emptyMap()
        ));
        assertEquals("v26", VersionMapping.resolve(
                "org.bukkit.craftbukkit",
                "26.1.0-R0.1-SNAPSHOT",
                Collections.<String, String>emptyMap()
        ));
    }

    @Test
    public void later26ReleasesKeepTheirOwnToken() {
        assertEquals("v26_1_2", VersionMapping.resolve(
                "org.bukkit.craftbukkit",
                "26.1.2-R0.1-SNAPSHOT",
                Collections.<String, String>emptyMap()
        ));
        assertEquals("v26_3", VersionMapping.resolve(
                "org.bukkit.craftbukkit",
                "26.3-R0.1-SNAPSHOT",
                Collections.<String, String>emptyMap()
        ));
    }

    @Test
    public void mapEntryOverridesTheYearToken() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("26.3", "v26");
        assertEquals("v26", VersionMapping.resolve(
                "org.bukkit.craftbukkit",
                "26.3-R0.1-SNAPSHOT",
                map
        ));
    }

    @Test
    public void unknownLegacyVersionStaysUnresolved() {
        assertNull(VersionMapping.resolve(
                "org.bukkit.craftbukkit",
                "1.22-R0.1-SNAPSHOT",
                Collections.<String, String>emptyMap()
        ));
    }

    @Test
    public void yearClassesFallBackToThe26Base() {
        assertEquals(
                list("com.example.Block_v26"),
                VersionMapping.versionClassNames("com.example.Block_", "v26")
        );
        assertEquals(
                list("com.example.Block_v26_1_2", "com.example.Block_v26_1", "com.example.Block_v26"),
                VersionMapping.versionClassNames("com.example.Block_", "v26_1_2")
        );
        assertEquals(
                list("com.example.Block_v26_3", "com.example.Block_v26"),
                VersionMapping.versionClassNames("com.example.Block_", "v26_3")
        );
    }

    @Test
    public void legacyRevisionStillDropsOnlyTheRSuffix() {
        assertEquals(
                list("com.example.Block_v1_21_R7", "com.example.Block_v1_21"),
                VersionMapping.versionClassNames("com.example.Block_", "v1_21_R7")
        );
    }

    private static Map<String, String> map() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("1.21.8", "v1_21_R5");
        return map;
    }

    private static List<String> list(String... values) {
        List<String> names = new java.util.ArrayList<String>();
        Collections.addAll(names, values);
        return names;
    }
}
