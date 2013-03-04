package org.jboss.forge.scaffold.html5.scenario.dronetests.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class HasLandedOnNewUserIdentityView implements ExpectedCondition<Boolean> {

    @Override
    public Boolean apply(WebDriver driver) {
        return driver.getCurrentUrl().endsWith("/UserIdentitys/new") && driver.findElement(By.id("saveUserIdentity")).isEnabled();
    }

}
