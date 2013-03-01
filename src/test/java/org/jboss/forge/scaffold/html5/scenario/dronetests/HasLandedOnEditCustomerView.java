package org.jboss.forge.scaffold.html5.scenario.dronetests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class HasLandedOnEditCustomerView implements ExpectedCondition<Boolean> {

    @Override
    public Boolean apply(WebDriver driver) {
        return driver.getCurrentUrl().contains("/Customers/edit") && !driver.findElement(By.id("saveCustomer")).isEnabled();
    }
}