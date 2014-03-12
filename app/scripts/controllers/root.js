'use strict';

/**
 * The common parent for all controllers. It is used to re-use common code
 * by adding the relevant methods under the scope. 
 *
 * As Angular supports hierarchical scopes, references to those methods from 
 * the child controllers will eventually be resolved in the scope of this root controller.
 */
(function () {

    var RootCtrl = function ($scope, $location, Constants, userService, ActiveAccount) {

        /** Sets the location to the given hash */
        $scope.go = function (hash) {
            $location.path(hash);
        };

        /**
         * Signs out the user and redirects to the home page or any logout specific
         * landing page.
         */
        $scope.signOut = function () {
            userService.signOut().then(function () {
                    ActiveAccount.signedOut();
                    $location.path("/");
                }
            );
        }

        // If the next view requires authentication and the user has not logged in yet,
        // then forward to the login page
        var preventUnauthorizedAccess = function (newUrl) {            
            if (!ActiveAccount.isAuthenticated() && Constants.OPEN_URLS.indexOf(newUrl) < 0) {
                $scope.go("/login");
            }
        };

        $scope.$on("$routeChangeStart", function(event, next, current) {            
            preventUnauthorizedAccess(next.$$route.originalPath);
        });

        $scope.$on("$locationChangeStart", function(event, newUrl, oldUrl) {                        
            var hashIdx = newUrl.indexOf('#') + 1;
            var urlToCheck = '/';
            if (hashIdx > 0) {
                urlToCheck = newUrl.substr(hashIdx);
            } 
            preventUnauthorizedAccess(urlToCheck);
        });
    };

    RootCtrl.$inject = ['$scope', '$location', 'Constants', 'userService', 'ActiveAccount'];

    angular.module('BackendApis.App').controller('RootCtrl', RootCtrl);
}());