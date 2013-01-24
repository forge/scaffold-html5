<h2>Search for Companys</h2>
<form id="companySearch" class="form-horizontal">
    <div class="control-group">
        <label for="name" class="control-label">Name</label>
        <div class="controls">
            <input id="name" name="name" type="text" ng-model="search.name" placeholder="Enter the company name"></input>
        </div>
    </div>
    <div class="control-group">
        <label for="ticker" class="control-label">Ticker</label>
        <div class="controls">
            <input id="ticker" name="ticker" type="text" ng-model="search.ticker" placeholder="Enter the company ticker"></input>
        </div>
    </div>
    <div class="control-group">
        <div class="controls">
            <a id="Search" name="Search" class="btn btn-primary" ng-click="searchCompany()"><i class="icon-search icon-white"></i> Search</a>
            <a id="Create" name="Create" class="btn" href="#/companys/new"><i class="icon-plus-sign"></i> Create New</a>
        </div>
    </div>
</form>
<div id="search-results">
    <table class="table table-bordered">
        <thead>
            <tr>
                <th>Name</th>
                <th>Ticker</th>
            </tr>
        </thead>
        <tbody id="search-results-body">
            <!-- <tr ng-repeat="result in searchResults | filter:search| startFrom:currentPage*pageSize | limitTo:pageSize"> -->
            <tr ng-repeat="result in searchResults | startFrom:currentPage*pageSize | limitTo:pageSize">
                <td><a href="#/companys/edit/{{result.id}}">{{result.name}}</a></td>
                <td><a href="#/companys/edit/{{result.id}}">{{result.ticker}}</a></td>
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