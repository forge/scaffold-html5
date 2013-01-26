<h2>Search for ${entity.name}s</h2>
<form id="${entity.name}Search" class="form-horizontal">
    <#list entity.fields as field>
    <div class="control-group">
        <label for="${field.name}" class="control-label">${field.name}</label>
        <div class="controls">
            <input id="${field.name}" name="${field.name}" type="text" ng-model="search.${field.name}" placeholder="Enter the ${entity.name} ${field.name}"></input>
        </div>
    </div>
    </#list>
    <div class="control-group">
        <div class="controls">
            <a id="Search" name="Search" class="btn btn-primary" ng-click="search${entity.name}()"><i class="icon-search icon-white"></i> Search</a>
            <a id="Create" name="Create" class="btn" href="#/${entity.name}s/new"><i class="icon-plus-sign"></i> Create New</a>
        </div>
    </div>
</form>
<div id="search-results">
    <table class="table table-bordered">
        <thead>
            <tr>
            <#list entity.fields as field>
                <th>${field.name}</th>
            </#list>
            </tr>
        </thead>
        <tbody id="search-results-body">
            <!-- <tr ng-repeat="result in searchResults | filter:search| startFrom:currentPage*pageSize | limitTo:pageSize"> -->
            <tr ng-repeat="result in searchResults | filter:search | startFrom:currentPage*pageSize | limitTo:pageSize">
            <#list entity.fields as field>
                <td><a href="#/${entity.name}s/edit/{{result.id}}">{{result.${field.name}}}</a></td>
            </#list>
            </tr>
        </tbody>
    </table>
    <button ng-disabled="currentPage == 0" ng-click="currentPage=currentPage-1">
        Previous
    </button>
    {{currentPage+1}}/{{numberOfPages()}}
    <button ng-disabled="currentPage >= numberOfPages() - 1" ng-click="currentPage=currentPage+1">
        Next
    </button>
</div>
<div id="pagination"></div>