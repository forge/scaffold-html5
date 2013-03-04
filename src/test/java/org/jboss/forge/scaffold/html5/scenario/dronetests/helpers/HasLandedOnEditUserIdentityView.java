package org.jboss.forge.scaffold.html5.scenario.dronetests.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class HasLandedOnEditUserIdentityView implements ExpectedCondition<Boolean> {

    @Override
    public Boolean apply(WebDriver driver) {
        return driver.getCurrentUrl().contains("/UserIdentitys/edit") && !driver.findElement(By.id("saveUserIdentity")).isEnabled();
    }
}