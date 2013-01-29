'use strict';

angular.module('${entityName}',['ngResource']).
    factory('${entityName}Resource', function($resource){
        var ${entityName?lower_case} = $resource('rest/${entityName?lower_case}s/:${entityName}Id',{${entityName}Id:'@id'},{'query':{method:'GET',isArray:true},'update':{method:'PUT'}});
        return ${entityName?lower_case};
    });