<#assign
    angularModule = "${entityName?lower_case}Module" 
    angularController = "Search${entityName}Controller" 
    angularResource = "${entityName}Resource" 
    entityId = "${entityName}Id" 
    model = "$scope.${entityName?lower_case}" 
    entityRoute = "/${entityName}s" 
>

<#assign relatedResources>
<#list properties as property>
<#if (property["many-to-one"]!) == "true" || (property["one-to-one"]!) == "true" || (property["n-to-many"]!) == "true">
, ${property.simpleType}Resource<#t>
</#if>
</#list>
</#assign>

${angularModule}.controller('${angularController}', function($scope, $filter, $http, ${angularResource} ${relatedResources}) {
    $scope.filter = $filter;
    $scope.search={};
    $scope.currentPage = 0;
    $scope.pageSize= 10;
    $scope.searchResults = [];
    $scope.pageRange = [];
    $scope.numberOfPages = function() {
        var result = Math.ceil($scope.searchResults.length/$scope.pageSize);
        return (result == 0) ? 1 : result;
    };
    <#list properties as property>
    <#if (property["many-to-one"]!) == "true" || (property["one-to-one"]!) == "true">
    <#assign
        relatedCollection = "$scope.${property.name}List"
        relatedResource = "${property.simpleType}Resource">
    ${relatedCollection} = ${relatedResource}.queryAll();
    </#if>
    </#list>

    $scope.performSearch = function() {
        $scope.searchResults = ${angularResource}.queryAll(function(){
            var max = $scope.numberOfPages();
            $scope.pageRange = [];
            for(var ctr=0;ctr<max;ctr++) {
                $scope.pageRange.push(ctr);
            }
        });
    };
    
    $scope.previous = function() {
       if($scope.currentPage > 0) {
           $scope.currentPage--;
       }
    };
    
    $scope.next = function() {
       if($scope.currentPage < ($scope.numberOfPages() - 1) ) {
           $scope.currentPage++;
       }
    };
    
    $scope.setPage = function(n) {
       $scope.currentPage = n;
    };

    $scope.filterSearchResults = function(result) {
        var flag = true;
        for(var key in $scope.search){
            if($scope.search.hasOwnProperty(key)) {
                var expected = $scope.search[key];
                if(expected == null || expected === "") {
                    continue;
                }
                var actual = result[key];
                if(angular.isObject(expected)) {
                    flag = flag && angular.equals(expected,actual);
                }
                else {
                    flag = flag && (actual.toString().indexOf(expected.toString()) != -1);
                }
                if(flag === false) {
                    return false;
                }
            }
        }
        return true;
    };

    $scope.performSearch();
});