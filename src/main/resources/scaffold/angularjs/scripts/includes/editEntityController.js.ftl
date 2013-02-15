<#assign
    angularModule = "${entityName?lower_case}Module"
    angularController = "Edit${entityName}Controller"
    angularResource = "${entityName}Resource"
    entityId = "${entityName}Id"
    model = "$scope.${entityName?lower_case}"
>
${angularModule}.controller('${angularController}', function($scope,$routeParams,$location,${angularResource}
<#list properties as property>
<#if (property["many-to-one"]!"false") == "true" || (property["one-to-one"]!"false") == "true" || (property["n-to-many"]!"false") == "true">
, ${property.simpleType}Resource
</#if>
</#list>
) {
    var self = this;
    $scope.disabled = false;

    $scope.get = function() {
        ${angularResource}.get({${entityId}:$routeParams.${entityId}}, function(data){
            self.original = data;
            ${model} = new ${angularResource}(self.original);
            <#list properties as property>
            <#if (property["many-to-one"]!"false") == "true" || (property["one-to-one"]!"false") == "true">
            ${property.simpleType}Resource.queryAll(function(data) {
                $scope.${property.name}List = data;
                angular.forEach($scope.${property.name}List, function(datum){
                    if(angular.equals(datum,${model}.${property.name})) {
                        ${model}.${property.name} = datum;
                        self.original.${property.name} = datum;
                    }
                });
            });
            <#elseif (property["n-to-many"]!"false") == "true">
            ${property.simpleType}Resource.queryAll(function(data) {
                $scope.${property.name}List = data;
                angular.forEach($scope.${property.name}List, function(datum){
                    angular.forEach(${model}.${property.name}, function(nestedDatum,index){
                        if(angular.equals(datum,nestedDatum)) {
                            ${model}.${property.name}[index] = datum;
                            self.original.${property.name}[index] = datum;
                        }
                    });
                });
            });
            </#if>
            </#list>
        }, function() {
            $location.path("/${entityName}s");
        });
    };

    $scope.isClean = function() {
        return angular.equals(self.original, ${model});
    };

    $scope.save = function() {
        ${model}.$update(function(){
            $scope.get();
            $scope.displayError = false;
        }, function() {
            $scope.displayError=true;
        });
    };

    $scope.cancel = function() {
        $location.path("/${entityName}s");
    };

    $scope.remove = function() {
        ${model}.$remove(function() {
            $location.path("/${entityName}s");
        });
    };
    
    <#list properties as property>
    <#if (property["n-to-many"]!"false") == "true">
    $scope.remove${property.name} = function(${property.name} , index) {
        console.log("Removing element at {0} from {1}", index, ${property.name} );
        ${property.name}.splice(index, 1);
    };
    
    $scope.add${property.name} = function(${property.name}Element) {
        if(!${model}.${property.name}) {
            ${model}.${property.name} = [];
        }
        ${model}.${property.name}.push(new ${property.simpleType}Resource());
        console.log("Adding {0} to {1}", ${property.name}Element, ${model}.${property.name} ); 
    };
    </#if>
    </#list>
    
    $scope.get();
});