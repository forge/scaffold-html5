<#assign formName="${entityName}Form"
        formProperty = "${formName}.${property.name}"
        modelProperty = "${entityName?uncap_first}.${property.name}"
        propertyLabel = "${property.name?cap_first}" />
<#if (property.hidden!"false") == "false">
    <div class="control-group" ng-class="{error: ${formProperty}.$invalid}">
        <label for="${property.name}" class="control-label">${propertyLabel}</label>
        <div class="controls">
            <input id="${property.name}" name="${property.name}"<#t/>
                <#if property.type == "number"> type="number"<#t/>
                    <#if property["minimum-value"]??> min="${property["minimum-value"]}"</#if><#t/>
                    <#if property["maximum-value"]??> max="${property["maximum-value"]}"</#if><#t/>
                <#elseif (property["datetime-type"]!"") == "date"> type="date"<#t/>
                <#elseif (property["datetime-type"]!"") == "time"> type="time"<#t/>
                <#elseif (property["datetime-type"]!"") == "both"> type="datetime"<#t/>
                <#elseif property.type == "boolean"> type="checkbox"<#t/>
                <#else> type="text"</#if><#t/>
                <#if (property.required!"false") == "true"> required</#if><#t/>
                <#if property["maximum-length"]??> ng-maxlength="${property["maximum-length"]}"</#if><#if property["minimum-length"]??> ng-minlength="${property["minimum-length"]}"</#if><#lt/> ng-model="${modelProperty}" placeholder="Enter the ${entityName} ${property.name}"></input>
            <#if (property.required!) == "true">
            <span class="help-inline" ng-show="${formProperty}.$error.required">required</span> 
            </#if>
            <#if property.type == "number">
            <span class="help-inline" ng-show="${formProperty}.$error.number">not a number</span>
            <#if property["minimum-value"]??>
            <span class="help-inline" ng-show="${formProperty}.$error.min">minimum allowed is ${property["minimum-value"]}</span>
            </#if>
            <#if property["maximum-value"]??>
            <span class="help-inline" ng-show="${formProperty}.$error.max">maximum allowed is ${property["maximum-value"]}</span>
            </#if>
            </#if>
            <#if property["minimum-length"]??>
            <span class="help-inline" ng-show="${formProperty}.$error.minlength">minimum length is ${property["minimum-length"]}</span>
            </#if>
            <#if property["maximum-length"]??>
            <span class="help-inline" ng-show="${formProperty}.$error.maxlength">maximum length is ${property["maximum-length"]}</span>
            </#if>
        </div>
    </div>
</#if>