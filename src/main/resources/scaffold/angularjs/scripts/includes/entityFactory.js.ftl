<#assign
    entityModule = "${entityName?lower_case}Module"
    entityResource = "${entityName}Resource"
    entityId = "${entityName}Id"
    entityResourceUrlFragment = "${entityName?lower_case}s"
>
${entityModule}.factory('${entityResource}', function($resource
<#list properties as property>
<#if (property["many-to-one"]!"false") == "true" || (property["one-to-one"]!"false") == "true">
, ${property.simpleType}Resource
</#if>
</#list>
){
    var ${entityName?lower_case} = $resource('rest/${entityResourceUrlFragment}/:${entityId}',{${entityId}:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    
    <#list properties as property>
    <#if (property["many-to-one"]!"false") == "true" || (property["one-to-one"]!"false") == "true">
    ${entityName?lower_case}.prototype.get${property.name?cap_first} = function(cb) {
        var resource = ${property.simpleType}Resource.query({${property.name}Id:this.${property.name}.id},cb);
        return resource;
    };
    </#if>
    </#list>
    
    return ${entityName?lower_case};
});