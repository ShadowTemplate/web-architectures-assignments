app.controller('listController', function ($scope, noteService) {
    $scope.myData = [];
    $scope.gridOptions = {
        multiSelect: false,
        data: 'myData',
        columnDefs: [{field:'id', displayName:'Id'},
            {field:'created', displayName:'Creation date'},
            {field:'lastModified', displayName:'Last edit'},
            {field:'content', displayName:'Content preview'}],
        afterSelectionChange: function (rowItem, event) {
            noteService.lastSelected = rowItem.rowIndex;
            window.location.href = "#/edit";
        }
    };

    noteService.list().success(function (data) {
        $scope.myData = data;
        noteService.myData = data;
    });

    $scope.createNew = function () {
        window.location.href = "#/new";
    };
});