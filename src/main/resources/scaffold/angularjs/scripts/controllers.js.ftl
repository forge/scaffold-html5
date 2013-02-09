'use strict';

function Search${entityName}Controller($scope,$filter,$http,${entityName}Resource
<#list properties as property>
<#if (property["many-to-one"]!"false") == "true" || (property["one-to-one"]!"false") == "true">
,${property.simpleType}Resource
</#if>
</#list>
) {
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
	<#if (property["many-to-one"]!"false") == "true" || (property["one-to-one"]!"false") == "true">
    $scope.${property.name}List = ${property.simpleType}Resource.queryAll();
    </#if>
    </#list>

	$scope.performSearch = function() {
		$scope.searchResults = ${entityName}Resource.queryAll(function(){
            var max = $scope.numberOfPages();
            $scope.pageRange = [];
            for(var ctr=0;ctr<max;ctr++) {
                $scope.pageRange.push(ctr);
            }
		});
		/*$http.post('rest/${entityName}s/search',$scope.search).success(function(data,status){
			$scope.searchResults = data;
		});*/
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
};

function New${entityName}Controller($scope,$location,${entityName}Resource
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
    
    $scope.remove${property.name} = function(${property.name} , ${property.name}Element) {
        console.log("Removing {0} from {1}", ${property.name}Element, ${property.name} );
    };
    
    $scope.add${property.name} = function(${property.name}Element) {
        if(!$scope.${entityName?lower_case}.${property.name}) {
            $scope.${entityName?lower_case}.${property.name} = [];
        }
        $scope.${entityName?lower_case}.${property.name}.push(new ${property.simpleType}Resource());
        console.log("Adding {0} to {1}", ${property.name}Element, $scope.${entityName?lower_case}.${property.name} );
    };
    </#if>
    </#list>

	$scope.save = function() {
		${entityName}Resource.save($scope.${entityName?lower_case}, function(${entityName?lower_case},responseHeaders){
			// Get the Location header and parse it.
			var locationHeader = responseHeaders('Location');
			var fragments = locationHeader.split('/');
			var id = fragments[fragments.length -1];
			$location.path('/${entityName}s/edit/' + id);
			$scope.displayError = false;
		}, function() {
		    $scope.displayError = true;
		});
	};
	
    $scope.cancel = function() {
        $location.path("/${entityName}s");
    };
}

function Edit${entityName}Controller($scope,$routeParams,$location,${entityName}Resource
<#list properties as property>
<#if (property["many-to-one"]!"false") == "true" || (property["one-to-one"]!"false") == "true" || (property["n-to-many"]!"false") == "true">
, ${property.simpleType}Resource
</#if>
</#list>
) {
	var self = this;
	$scope.disabled = false;

	$scope.get = function() {
	    ${entityName}Resource.get({${entityName}Id:$routeParams.${entityName}Id}, function(data){
            self.original = data;
            $scope.${entityName?lower_case} = new ${entityName}Resource(self.original);
            <#list properties as property>
            <#if (property["many-to-one"]!"false") == "true" || (property["one-to-one"]!"false") == "true">
            ${property.simpleType}Resource.queryAll(function(data) {
                $scope.${property.name}List = data;
                angular.forEach($scope.${property.name}List, function(datum){
                    if(angular.equals(datum,$scope.${entityName?lower_case}.${property.name})) {
                        $scope.${entityName?lower_case}.${property.name} = datum;
                        self.original.${property.name} = datum;
                    }
                });
            });
            <#elseif (property["n-to-many"]!"false") == "true">
            ${property.simpleType}Resource.queryAll(function(data) {
                $scope.${property.name}List = data;
                angular.forEach($scope.${property.name}List, function(datum){
                    angular.forEach($scope.${entityName?lower_case}.${property.name}, function(nestedDatum,index){
                        if(angular.equals(datum,nestedDatum)) {
                            $scope.${entityName?lower_case}.${property.name}[index] = datum;
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
		return angular.equals(self.original, $scope.${entityName?lower_case});
	};

	$scope.save = function() {
		$scope.${entityName?lower_case}.$update(function(){
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
		$scope.${entityName?lower_case}.$remove(function() {
			$location.path("/${entityName}s");
		});
	};
	
    <#list properties as property>
    <#if (property["n-to-many"]!"false") == "true">
    $scope.remove${property.name} = function(${property.name} , ${property.name}Element) {
        console.log("Removing {0} from {1}", ${property.name}Element, ${property.name} );
    };
    
    $scope.add${property.name} = function(${property.name}Element) {
        if(!$scope.${entityName?lower_case}.${property.name}) {
            $scope.${entityName?lower_case}.${property.name} = [];
        }
        $scope.${entityName?lower_case}.${property.name}.push(new ${property.simpleType}Resource());
        console.log("Adding {0} to {1}", ${property.name}Element, $scope.${entityName?lower_case}.${property.name} ); 
    };
    </#if>
    </#list>
	
	$scope.get();
};