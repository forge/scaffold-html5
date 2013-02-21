package org.jboss.forge.scaffold.html5;

import javax.enterprise.inject.Produces;

import org.jboss.forge.shell.project.ProjectScoped;

@ProjectScoped
public class RelatedTypeHolder {

    private String relatedType;

    @Produces
    @RelatedType
    public String producesRelatedClass() {
        return this.relatedType;
    }
    
    public void setRelatedType(String relatedType) {
        this.relatedType = relatedType;
    }
}
