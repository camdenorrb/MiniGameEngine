package dev.twelveoclock.plugintemplate.module;

/**
 * The base for something that can be enabled or disabled
 */
public interface Module {

    /**
     * Enables the module
     */
    void enable();


    /**
     * Disabled the module
     */
    void disable();


    /**
     * @return Whether the module is enabled
     */
    boolean isEnabled();

}
