<form id="new-${entity.name}" name="new${entity.name}" class="form-horizontal">
    <#list entity.fields as field>
    <div class="control-group">
        <label for="${field.name}" class="control-label">${field.name}</label>
        <div class="controls">
            <input id="${field.name}" name="${field.name}" type="text" ng-model="${entity.name}.${field.name}"></input>
        </div>
    </div>
    </#list>
    <div class="control-group">
        <div class="controls">
            <button id="save${entity.name}" name="save${entity.name}" class="btn btn-primary" ng-disabled="isClean() || new${entity.name}.$invalid" ng-click="save()"><i class="icon-ok-sign icon-white"></i> Save</button>
            <button id="cancel" name="cancel" class="btn" ng-click="cancel()"><i class="icon-remove-sign"></i> Cancel</button>
            <button id="delete${entity.name}" name="delete${entity.name}" class="btn" ng-show="${entity.name}.id" ng-click="delete()"><i class="icon-trash"></i> Delete</button>
        </div>
    <div>
</form>