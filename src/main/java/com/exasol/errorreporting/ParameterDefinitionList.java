package com.exasol.errorreporting;

import java.util.ArrayList;
import java.util.List;

/**
 * List of {@link ParameterDefinition}s.
 * <p>
 * This class exists to make the definitions more fault tolerant in case of missing or duplicate definitions.
 * </p>
 */
public class ParameterDefinitionList {
    private final List<ParameterDefinition> parameterDefinitions = new ArrayList<>();

    /**
     * Add a parameter definition to the list.
     *
     * @param parameterDefinition definition to be added
     */
    public void add(final ParameterDefinition parameterDefinition) {
        this.parameterDefinitions.add(parameterDefinition);
    }

    /**
     * Get a parameter definition by the name of the parameter.
     *
     * @param name parameter name to search for
     * @return first parameter definition where the name matches, or {@link ParameterDefinition#UNDEFINED_PARAMETER} if
     * no parameter matches
     */
    public ParameterDefinition get(final String name) {
        if(name != null) {
            for(ParameterDefinition definition : this.parameterDefinitions) {
                if (name.equals(definition.getName())) {
                    return definition;
                }
            }
        }
        return ParameterDefinition.UNDEFINED_PARAMETER;
    }

    /**
     * Check if a parameter of this name exists.
     *
     * @param name the name of the parameter
     * @return {@code true} if a parameter that name exists in the parameter definition list, {@code false} if the name
     * searched for is {@code null} or does not exist
     */
    public boolean containsKey(final String name) {
        if(name != null) {
            for(ParameterDefinition definition : this.parameterDefinitions) {
                if(name.equals(definition.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
