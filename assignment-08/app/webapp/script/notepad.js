var app = angular.module('notepad', ['ngGrid', 'ui.bootstrap', 'ngRoute']);

app.config(function ($routeProvider) {
    $routeProvider.when('/', {templateUrl: 'list.html', controller: 'listController'});
    $routeProvider.when('/edit', {templateUrl: 'edit.html', controller: 'editController'});
    $routeProvider.when('/new', {templateUrl: 'new.html', controller: 'newController'});
    $routeProvider.otherwise({redirectTo: '/'});
});

app.service('noteService', function ($http) {
    this.lastSelected = -1;
    this.myData = [];
    var noteEndpoint = "/rest/note";

    this.create = function (content) {
        return $http({url: noteEndpoint, method: "POST", params: {content: content}});
    };

    this.list = function () {
        return $http({url: noteEndpoint, method: "GET"});
    };

    this.update = function (id, content) {
        return $http({url: noteEndpoint, method: "PUT", params: {id: id, content: content}});
    };

    this.delete = function (id) {
        return $http({url: noteEndpoint, method: "DELETE", params: {id: id}});
    };
});
