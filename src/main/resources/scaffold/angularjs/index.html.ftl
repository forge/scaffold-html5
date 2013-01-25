<!doctype html>
<html lang="en" ng-app="root">
<head>
	<meta charset="UTF-8">
	<title>Forge Prototype</title>
    <link href="styles/bootstrap.css" rel="stylesheet" media="screen">
    <link href="styles/main.css" rel="stylesheet" media="screen">
    <link href="styles/bootstrap-responsive.css" rel="stylesheet" media="screen">
	<script src="scripts/vendor/angular.js"></script>
	<script src="scripts/vendor/angular-resource.js"></script>
</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<a class="brand" href="#">Angular Prototype</a>
			</div>
		</div>
	</div>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span3">
                <img src="images/forge-logo.png" alt="JBoss Forge"></img>
                <nav class="well sidebar-nav">
                    <ul id="sidebar-entries" class="nav nav-list">
                        <#list entityNames as entityName>
                    	<li><a href="#${entityName}">${entityName}</a></li>
                    	</#list>
                    </ul>
                </nav>
            </div>
            <div class="span9">
                <div id="main" class="hero-unit" ng-view>
                </div>
            </div>
        </div>
    </div>
</body>
</html>