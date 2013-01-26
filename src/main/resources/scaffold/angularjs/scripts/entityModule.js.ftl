'use strict';

angular.module('${entity.name}',['ngResource']).
    factory('${entity.name}Resource', function($resource){
        var ${entity.name?lower_case} = $resource('rest/${entity.name?lower_case}s/:${entity.name}Id',{${entity.name}Id:'@id'},{'query':{method:'GET',isArray:true},'update':{method:'PUT'}});
        return ${entity.name?lower_case};
    });