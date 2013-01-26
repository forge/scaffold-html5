'use strict';

<#list entityNames as entityName>
function Search${entityName}Controller($scope,$filter,$http,${entityName}Resource) {
	$scope.search={};
	$scope.currentPage = 0;
	$scope.pageSize= 10;
	$scope.searchResults = [];
	$scope.numberOfPages = function() {
		var result = Math.ceil($scope.searchResults.length/$scope.pageSize);
		return (result == 0) ? 1 : result;
	};

	$scope.search${entityName} = function() {
		$scope.searchResults = ${entityName}Resource.query();
		/*$http.post('rest/${entityName}s/search',$scope.search).success(function(data,status){
			$scope.searchResults = data;
		});*/
	};

	$scope.search${entityName}();
};

function New${entityName}Controller($scope,$location,${entityName}Resource) {
	$scope.disabled = false;

	$scope.save = function() {
		${entityName}Resource.save($scope.${entityName?lower_case}, function(${entityName?lower_case},responseHeaders){
			// Get the Location header and parse it.
			var locationHeader = responseHeaders('Location');
			var fragments = locationHeader.split('/');
			var id = fragments[fragments.length -1];
			$location.path('/${entityName}s/edit/' + id);
		});
	};
	
    $scope.cancel = function() {
        $location.path("/${entityName}s");
    };
}

function Edit${entityName}Controller($scope,$routeParams,$location,${entityName}Resource) {
	var self = this;
	$scope.disabled = false;

	$scope.get = function() {
	    ${entityName}Resource.get({${entityName}Id:$routeParams.${entityName}Id}, function(data){
            self.original = data;
            $scope.${entityName?lower_case} = new ${entityName}Resource(self.original);
        });
	};

	$scope.isClean = function() {
		return angular.equals(self.original, $scope.${entityName?lower_case});
	};

	$scope.save = function() {
		$scope.${entityName?lower_case}.$update(function(){
            $scope.get();
		});
	};

	$scope.cancel = function() {
		$location.path("/${entityName}s");
	};

	$scope.delete = function() {
		$scope.${entityName?lower_case}.$remove(function() {
			$location.path("/${entityName}s");
		});
	};
	
	$scope.get();
};
</#list>