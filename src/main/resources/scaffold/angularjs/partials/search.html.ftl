<h2>Search for ${entityName}s</h2>
<form id="${entityName}Search" class="form-horizontal">
    <#list properties as property>
    <div class="control-group">
        <#if (property.hidden!"false") != "true">
        <label for="${property.name}" class="control-label">${property.name}</label>
        <div class="controls">
            <input id="${property.name}" name="${property.name}" type="text" ng-model="search.${property.name}" placeholder="Enter the ${entityName} ${property.name}"></input>
        </div>
        </#if>
    </div>
    </#list>
    <div class="control-group">
        <div class="controls">
            <a id="Search" name="Search" class="btn btn-primary" ng-click="performSearch()"><i class="icon-search icon-white"></i> Search</a>
            <a id="Create" name="Create" class="btn" href="#/${entityName}s/new"><i class="icon-plus-sign"></i> Create New</a>
        </div>
    </div>
</form>
<div id="search-results">
    <table class="table table-bordered">
        <thead>
            <tr>
            <#list properties as property>
                <th>${property.name}</th>
            </#list>
            </tr>
        </thead>
        <tbody id="search-results-body">
            <tr ng-repeat="result in searchResults | filter:search | startFrom:currentPage*pageSize | limitTo:pageSize">
            <#list properties as property>
                <td><a href="#/${entityName}s/edit/{{result.id}}">{{result.${property.name}}}</a></td>
            </#list>
            </tr>
        </tbody>
    </table>
    <div class="pagination pagination-centered">
        <ul>
            <li ng-class="{disabled:currentPage == 0}">
                <a id="prev" href ng-click="previous()">«</a>
            </li>
            <li ng-repeat="n in pageRange" ng-class="{active:currentPage == n}" ng-click="setPage(n)">
                <a href ng-bind="n + 1">1</a>
            </li>
            <li ng-class="{disabled: currentPage == (numberOfPages() - 1)}">
                <a id="next" href ng-click="next()">»</a>
            </li>
        </ul>
    </div>
</div>