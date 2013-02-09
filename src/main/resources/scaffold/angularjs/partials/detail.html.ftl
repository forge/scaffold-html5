<form id="new-${entityName}" name="new${entityName}" class="form-horizontal">
    <div ng-show="displayError" class="alert alert-error">
        <strong>Error!</strong> Something broke. Retry, or cancel and start afresh.
    </div>
    <#list properties as property>
    <div class="control-group" ng-class="{error: new${entityName}.${property.name}.$invalid}">
        <#if (property.hidden!"false") == "false">
        <label for="${property.name}" class="control-label">${property.name?cap_first}</label>
        <div class="controls">
            <#if (property["many-to-one"]!"false") == "true" || (property["one-to-one"]!"false") == "true">
            <select id="${property.name}" name="${property.name}" ng-model="${entityName?lower_case}.${property.name}" ng-options="${property.name?substring(0, 1)} as ${property.name?substring(0, 1)}.id for ${property.name?substring(0, 1)} in ${property.name}List" <#if (property.required!"false") == "true">required</#if> >
                <option value="">Choose a ${property.name?cap_first}</option>
            </select>
                <#if (property.required!"false") == "true">
                <span class="help-inline" ng-show="new${entityName}.${property.name}.$error.required">required</span> 
                </#if>
            <#elseif (property["n-to-many"]!"false") == "true">
            <div ng-repeat="${property.name}Element in ${entityName?lower_case}.${property.name}">
                <select id="${property.name}{{$index}}" name="${property.name}{{$index}}" ng-model="${entityName?lower_case}.${property.name}[$index]" ng-options="${property.name?substring(0, 1)} as ${property.name?substring(0, 1)}.id for ${property.name?substring(0, 1)} in ${property.name}List">
                    <option value="">Choose a ${property.name?cap_first}</option>
                </select> 
                <button ng-click="remove${property.name}(${entityName?lower_case}.${property.name} , $index)">Delete</button>
            </div>
            <button ng-click="add${property.name}( ${entityName?lower_case}.${property.name} )">Add a ${property.name?cap_first}</button>
            <#else>
            <input id="${property.name}" name="${property.name}" <#if property.type == "number">type="number" <#if property["minimum-value"]??> min="${property["minimum-value"]}" </#if><#if property["maximum-value"]??> max="${property["maximum-value"]}" </#if></#if> <#if (property["datetime-type"]!"") == "date">type="date"<#elseif (property["datetime-type"]!"") == "time">type="time"<#elseif (property["datetime-type"]!"") == "both">type="datetime"<#else>type="text"</#if> <#if (property.required!"false") == "true">required</#if> <#if property["maximum-length"]??> maxlength="${property["maximum-length"]}"</#if> ng-model="${entityName?lower_case}.${property.name}" placeholder="Enter the ${entityName} ${property.name}"></input>
                <#if (property.required!"false") == "true">
                <span class="help-inline" ng-show="new${entityName}.${property.name}.$error.required">required</span> 
                </#if>
                <#if property.type == "number">
                <span class="help-inline" ng-show="new${entityName}.${property.name}.$error.number">not a number</span>
                    <#if property["minimum-value"]??>
                    <span class="help-inline" ng-show="new${entityName}.${property.name}.$error.min">minimum allowed is ${property["minimum-value"]}</span>
                    </#if>
                    <#if property["maximum-value"]??>
                    <span class="help-inline" ng-show="new${entityName}.${property.name}.$error.max">maximum allowed is ${property["maximum-value"]}</span>
                    </#if>
                </#if>
            </#if>
        </div>
        </#if>
    </div>
    </#list>
    <div class="control-group">
        <div class="controls">
            <button id="save${entityName}" name="save${entityName}" class="btn btn-primary" ng-disabled="isClean() || new${entityName}.$invalid" ng-click="save()"><i class="icon-ok-sign icon-white"></i> Save</button>
            <button id="cancel" name="cancel" class="btn" ng-click="cancel()"><i class="icon-remove-sign"></i> Cancel</button>
            <button id="delete${entityName}" name="delete${entityName}" class="btn" ng-show="${entityName?lower_case}.id" ng-click="remove()"><i class="icon-trash"></i> Delete</button>
        </div>
    <div>
</form>