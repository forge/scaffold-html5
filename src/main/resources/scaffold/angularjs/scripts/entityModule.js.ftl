'use strict';

var ${entityName?lower_case}Module = angular.module('${entityName}',['ngResource']);
    
<#include "includes/entityFactory.js.ftl">

<#include "includes/searchEntityController.js.ftl">

<#include "includes/newEntityController.js.ftl">

<#include "includes/editEntityController.js.ftl">