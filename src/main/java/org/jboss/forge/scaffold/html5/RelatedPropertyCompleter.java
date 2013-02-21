package org.jboss.forge.scaffold.html5;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.forge.parser.java.Field;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.shell.completer.SimpleTokenCompleter;

public class RelatedPropertyCompleter extends SimpleTokenCompleter {
    private String relatedJavaType;
    private Project project;
    private JavaClass javaClass;

    @Inject
    public RelatedPropertyCompleter(Project project, @RelatedType String relatedType) {
        this.project = project;
        this.relatedJavaType = relatedType;
        JavaSourceFacet java = this.project.getFacet(JavaSourceFacet.class);
        try {
            JavaResource relatedResource = java.getJavaResource(this.relatedJavaType);
            this.javaClass = (JavaClass) relatedResource.getJavaSource();
        } catch (FileNotFoundException fileEx) {
            // This is not supposed to happen, since the JPA entity class/file is supposed to be present by now.
            throw new RuntimeException(fileEx);
        }
    }

    @Override
    public List<String> getCompletionTokens() {
        final List<String> tokens = new ArrayList<String>();
        for (Field<JavaClass> field : javaClass.getFields()) {
            tokens.add(field.getName());
        }
        return tokens;
    }
}