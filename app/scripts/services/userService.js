'user strict';

/**
 * Defines the user service, part of the BackendApis.Services module.
 * It provides methods for signing in & out a user.
 *
 * The api is based on promises. To sign in a user, you would do:
 *
 *  userService.authenticate('foo', 'bar').then(function(user) {
 *      $scope.currentUser = user;
 *  });
 */
(function () {

    var userService = function ($http, $rootScope, $q, Constants) {

        return {
            /**
             * Authenticates a user based on the given credentials.
             *
             * @param user the username
             * @param password the password
             * @return a promise that is resolved to an Account object upon success or an error 
             * response code upon failure
             */
            signIn: function (user, password) {
                var p = $q.defer();
                var res = $http.post(Constants.API_URL + '/signIn',
                    "username=" + user + "&password=" + password,
                    {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    });

                // Upon success, return the account object as the result of the promise
                // Otherwise, reject the promise with the response status code
                res.then(function (response) {
                        var user = response.data;
                        p.resolve(user);
                    }, function (response) {
                        p.reject(response.status);
                    }

                )

                return p.promise;
            },

            /**
             * Clears the authentication context, after this call the user is not associated with
             * any account defined at the backend.
             *
             * @return a promise that is resolved to the response status code upon failure
             */
            signOut: function () {
                var p = $q.defer();
                $http.post(Constants.API_URL + '/signOut').then(function () {
                        p.resolve(null);
                    }, function (response) {
                        p.reject(response.status);
                    }
                )
                return p.promise;
            }

        }
    };

    userService.$inject = ['$http', '$rootScope', '$q', 'Constants'];

    angular.module('BackendApis.Services', []).factory('userService', userService);

}());
