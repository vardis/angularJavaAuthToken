'use strict';

/**
 * Manages the user interaction with the main page.
 */
(function () {

    var MainCtrl = function ($scope, ActiveAccount) {

        $scope.isLoggedIn = function () {
            return ActiveAccount.isAuthenticated();
        };

    };

    MainCtrl.$inject = ['$scope', 'ActiveAccount'];

    angular.module('BackendApis.App').controller('MainCtrl', MainCtrl);
}());