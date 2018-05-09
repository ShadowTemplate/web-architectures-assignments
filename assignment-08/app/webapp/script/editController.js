app.controller('editController', function ($scope, noteService) {
    var lastSelected = noteService.myData[noteService.lastSelected];
    $scope.noteText = lastSelected.content;

    $scope.saveChange = function () {
        noteService.update(lastSelected.id, $scope.noteText).success(function (data) {
            window.location.href = "#/list";
        });
    };

    $scope.delete = function () {
        noteService.delete(lastSelected.id).success(function (data) {
            window.location.href = "#/list";
        });
    };
});
