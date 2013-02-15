<#assign
    angularModule = "${entityName?lower_case}Module"
    angularController = "New${entityName}Controller"
    angularResource = "${entityName}Resource"
    entityId = "${entityName}Id"
    model = "$scope.${entityName?lower_case}"
    entityRoute = "/${entityName}s"
>
${angularModule}.controller('${angularController}', function ($scope,$location,${angularResource}
<#list properties as property>
<#if (property["many-to-one"]!"false") == "true" || (property["one-to-one"]!"false") == "true" || (property["n-to-many"]!"false") == "true">
, ${property.simpleType}Resource
</#if>
</#list>
) {
    $scope.disabled = false;
    
    <#list properties as property>
    <#if (property["many-to-one"]!"false") == "true" || (property["one-to-one"]!"false") == "true">
    ${property.simpleType}Resource.queryAll(function(data){
        $scope.${property.name}List = angular.fromJson(JSON.stringify(data));
    });
    </#if>
    </#list>
    
    <#list properties as property>
    <#if (property["n-to-many"]!"false") == "true">
    ${property.simpleType}Resource.queryAll(function(data){
        $scope.${property.name}List = angular.fromJson(JSON.stringify(data));
    });
    
    $scope.remove${property.name} = function(${property.name} , index) {
        console.log("Removing element at {0} from {1}", index, ${property.name} );
        ${property.name}.splice(index, 1);
    };
    
    $scope.add${property.name} = function() {
        if(!${model}.${property.name}) {
            ${model}.${property.name} = [];
        }
        ${model}.${property.name}.push(new ${property.simpleType}Resource());
    };
    </#if>
    </#list>

    $scope.save = function() {
        ${angularResource}.save(${model}, function(${entityName?lower_case},responseHeaders){
            // Get the Location header and parse it.
            var locationHeader = responseHeaders('Location');
            var fragments = locationHeader.split('/');
            var id = fragments[fragments.length -1];
            $location.path('${entityRoute}/edit/' + id);
            $scope.displayError = false;
        }, function() {
            $scope.displayError = true;
        });
    };
    
    $scope.cancel = function() {
        $location.path("${entityRoute}");
    };
});