<#assign
    entityModule = "${entityName?lower_case}Module"
    entityResource = "${entityName}Resource"
    entityId = "${entityName}Id"
    entityResourceUrlFragment = "${entityName?lower_case}s"
>
${entityModule}.factory('${entityResource}', function($resource){
    var resource = $resource('rest/${entityResourceUrlFragment}/:${entityId}',{${entityId}:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});