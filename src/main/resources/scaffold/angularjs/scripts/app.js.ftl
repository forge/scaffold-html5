<#assign angularApp = "${project.projectName}">
'use strict';

var ${angularApp} = angular.module('${angularApp}', ['${angularApp}.filters'
<#list entityNames as entityName>
,'${entityName}'
</#list>
])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      <#list entityNames as entityName>
      <#assign
                searchEntityController = "Search${entityName}Controller"
                newEntityController = "New${entityName}Controller"
                editEntityController = "Edit${entityName}Controller"
                entityId = "${entityName}Id"
                entityRoute = "/${entityName}s"
                entityPartialsLocation = "partials/${entityName}">
      .when('${entityRoute}',{templateUrl:'${entityPartialsLocation}/search.html',controller:'${searchEntityController}'})
      .when('${entityRoute}/new',{templateUrl:'${entityPartialsLocation}/detail.html',controller:'${newEntityController}'})
      .when('${entityRoute}/edit/:${entityId}',{templateUrl:'${entityPartialsLocation}/detail.html',controller:'${editEntityController}'})
      </#list>
      .otherwise({
        redirectTo: '/'
      });
  }])
  .controller('NavController', function NavController($scope, $location) {
    $scope.matchesRoute = function(route) {
        return ($location.path().indexOf(route) != -1);
    }
  });
