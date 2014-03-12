'use strict';

/**
 * Manages the user interaction with the login page.
 */
(function () {

    var LoginCtrl = function ($scope, userService, ActiveAccount) {

        $scope.username = '';
        $scope.password = '';
        $scope.loginFailed = false;

        /**
         * Submits the credentials entered by the user for authentication.
         */
        $scope.login = function () {
            $scope.loginFailed = false;

            userService.signIn($scope.username, $scope.password)
                .then(function (user) {
                    ActiveAccount.signedIn(user);

                    $scope.go('/');

                }, function () {
                    $scope.loginFailed = true;
                });
        }
    };

    LoginCtrl.$inject = ['$scope', 'userService', 'ActiveAccount'];

    angular.module('BackendApis.App').controller('LoginCtrl', LoginCtrl);

}());