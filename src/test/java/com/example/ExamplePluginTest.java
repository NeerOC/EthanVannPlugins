package com.example;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;
import org.reflections.Reflections;
import net.runelite.client.plugins.Plugin;

import java.util.Set;

public class ExamplePluginTest {
    public static void main(String[] args) throws Exception {
        // Scan all classes in the com.example package and its subpackages
        Reflections reflections = new Reflections("com.example");
        Set<Class<? extends Plugin>> plugins = reflections.getSubTypesOf(Plugin.class);
        
        // Load all found plugins
        ExternalPluginManager.loadBuiltin(plugins.toArray(new Class[0]));
        RuneLite.main(args);
    }
}
