app.controller('newController', function ($scope, noteService) {
    $scope.saveNote = function () {
        noteService.create($scope.newNoteText).success(function (data) {
            window.location.href = "#/list";
        });
    };
});
